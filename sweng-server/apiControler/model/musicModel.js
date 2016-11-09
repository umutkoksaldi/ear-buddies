// Récupération du Modèle
var unirest = require('unirest');
var util = require('util');
var setting = require('../../setting/error.js');
var databaseConfig = require('../../setting/database.js');
var globalConfig = require('../../setting/global.js');
var musicManipulation = require ('../object/music.js')
var utils = require('../utils/Utils.js');


// database
var databasePostgres = require('../database/postgres.js');
var User = require('../database/SequelizeORM.js').User;
var Music = require('../database/SequelizeORM.js').Music;

//---------------------------------- DEFINE CONSTANT ------------------------------------
var URL_LASTFM = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo"

//---------------------------------- DEFINE CONSTANT ------------------------------------

// Définition de l'objet controllerUtilisateur
function controlerMusic(){

  // Implementation of the REST GET service.
  this.getMusic = function(idMusic,callback){

      utils.logInfo("getUserByIdConnection(), get the user"+ idMusic);

        // select query.
         Music.sync().then(function () {

             // select query.
             var getMusic =  Music.findOne({
                  where: {
                    id : idMusic
                  }

                }).then(function(getMusic) {

                    var response = musicManipulation.transformResponseClient(getMusic.dataValues); 
                    callback(response,setting.htmlCode.succes_request);

                }).catch(function(error) {
                    utils.logError("error getting user : "+error)
                    callback(null,setting.htmlCode.unavailable_ressources);
              });
          });
    }


    this.updateMusic = function(idApi,MusicObject,callback)    {     

        // GET the user description by doing a post on facebook API.
        unirest.get(buildRequestLastFm(MusicObject.artistName,MusicObject.musicName)).end(function(res){

          // We can't reach the facebook graph.
          if (res.error) {
           callback(null,setting.htmlCode.unavailable_ressources);
          }
          else {
                var body = {'artist':null,'track':null}

                // get the value from the response.
                res.raw_body  = JSON.parse(res.raw_body, (key, value) => {
                  // the case where we can't find any informations about the music.
                  if(key == 'error'){
                    body = {
                              'artist': MusicObject.artistName ,
                              'track':{'url': null , 'name':MusicObject.musicName}
                            }
                  }

                  // we get the result.
                  if(key =='artist'){
                      body.artist = value
                  }

                 if(key =='track'){
                      body.track = value
                  }
                  return value;                
              });


               // We synchronize with the databse in order to change the name and the 
               Music.sync({}).then(function () {

                    // add the music.
                    var CreateMusic =  Music.create({
                            artist        : body.artist,
                            name          : body.track.name,
                            url           : body.track.url,
                            tag           : 'yolo'

                  // callback if the user srequest succeed.
                  }).then(function(CreateMusic) {
                      utils.logError("Sucess"); 

                      User.sync().then(function () {
                      // select query.
                       var getUser =  User.findOne({
                            where: {
                              idApiConnection: idApi
                            }

                          }).then(function(getUser) {

                              // We force the link.
                              getUser.setCurrentMusic(CreateMusic.id);
                              getUser.addMusic(CreateMusic.id);
                              var response = musicManipulation.transformResponseClient(CreateMusic.dataValues); 
                              callback(response,setting.htmlCode.succes_request);

                          }).catch(function(error) {
                              utils.logError("error getting user : "+error)
                              callback(null,setting.htmlCode.unavailable_ressources);
                          });
                      });

                  // if there is any error in computing value for user.
                  }).catch(function(error) {
                      utils.logInfo("controllerUtilisateur(), the request fail"+error);
                      callback(null,setting.htmlCode.unavailable_ressources);
                  })

              });
            }

        })
    };


    var buildRequestLastFm = function(nameArtist, nameMusic){
      artistePart  = nameArtist == undefined ? '' : '&artist='+ nameArtist;
      musicPart    = nameMusic == undefined  ? '' : '&track='  + nameMusic;
      formatPart   = '&format=json'; 

      utils.logInfo(artistePart);
      utils.logInfo(musicPart);
      utils.logInfo(formatPart);

      return  URL_LASTFM +"&api_key="+globalConfig.ApiKey.lastFm+ artistePart + musicPart + formatPart;
    }
}

module.exports = new controlerMusic();