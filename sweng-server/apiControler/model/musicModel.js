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


var TAG_MUSIC = ['pop','rock','rap','metal','hiphop'];
var TAG_MUSIC_UNKNOWN = 'unknown';
var LIMIT_HISTORY = 10; 

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

    this.getHistory = function(idUser,callback){

        User.sync().then(function () {
          // select query.
           var getUser =  User.findOne({
                where: {
                  idApiConnection: idUser
                }
              }).then(function(getUser) {

                  utils.logInfo("request succeed"+idUser)

                  // We get the most recent, we limit to ten.
                  getUser.getMusic({
                    order:'"createdAt" DESC',
                    limit : LIMIT_HISTORY
                  }).then(function(MusicsofUsers) {

                      MusicsofUsers.forEach(function(value){
                            musicManipulation.transformResponseClient(value.dataValues)
                      });   
                      callback(MusicsofUsers,setting.htmlCode.succes_request);
                  }).catch(function(error) {
                    utils.logError("Cannot get the Musics of the user "+ error)
                    callback(null,setting.htmlCode.unavailable_ressources);
                });

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
                var resultFMRequest = {'artist':null,'track':null,'tag' : TAG_MUSIC_UNKNOWN}

                // get the value from the response.
                res.raw_body  = JSON.parse(res.raw_body, (key, value) => {
                    // the case where we can't find any informations about the music.
                    if(key == 'error'){
                      resultFMRequest = {
                                'artist': MusicObject.artistName ,
                                'track':{'url': null , 'name':MusicObject.musicName},
                                'tag' : TAG_MUSIC_UNKNOWN
                              }
                    }

                    // we get the result.
                    if(key == 'artist'){
                      
                        if (value.name != null){
                          resultFMRequest.artist = value.name
                        }
                    }

                   // get the name of the music.
                   if(key =='track'){
                        resultFMRequest.track = value
                    }

                    // get the tags of the music
                    if(key == 'toptags'){
                      value.tag.forEach(function(tag) {

                         for (indexTagPossible in TAG_MUSIC){  
                            // if the tag contain one possible/defined tags we add it as a tag
                            if(TAG_MUSIC[indexTagPossible].indexOf(tag.name) > -1) { 
                              resultFMRequest.tag = TAG_MUSIC[indexTagPossible];
                              break;
                            }
                         }

                      });
                  }

                  return value;  
                            
              });


               // We synchronize with the databse in order to change the name and the 
               Music.sync({}).then(function () {

                    // add the music.
                    var CreateMusic =  Music.create({
                            artist        : resultFMRequest.artist,
                            name          : resultFMRequest.track.name,
                            url           : resultFMRequest.track.url,
                            tag           : resultFMRequest.tag

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