// Récupération du Modèle
var unirest = require('unirest');
var util = require('util');
var setting = require('../../setting/error.js');
var databaseConfig = require('../../setting/database.js');
var globalConfig = require('../../setting/global.js');
var userManipulation = require ('../object/user.js')
var utils = require('../utils/Utils.js');
var Constant = require ('../utils/Constant.js')

// database
var databasePostgres = require('../database/postgres.js');
var User = require('../database/SequelizeORM.js').User;
var Music = require('../database/SequelizeORM.js').Music;
var Setting = require('../database/SequelizeORM.js').Setting;
var sequelize = require('../database/SequelizeORM.js').sequelize ;

//---------------------------------- DEFINE CONSTANT ------------------------------------

var URL_FACEBOOK = "https://graph.facebook.com/";
var FIELDS_FACEBOOK = 'email,cover,birthday,first_name,last_name,name,picture';

//---------------------------------- DEFINE CONSTANT ------------------------------------

// Définition de l'objet controllerUtilisateur
function controllerUtilisateurTest(){



  // private method, allow to get a specific user designed by api connection.
  var getUserAroundTest = function(idApi,callback){

      utils.logInfo("controllerUtilisateur(), insertion or geetin a user, adduser()");

      User.sync().then(function () {

         var getUser =  User.findOne({
              where: {
                idApiConnection: idApi
              }

            }).then(function(getUser) {

                utils.logError("request succeed"+idApi)
                delete getUser.dataValues['id']
                var user1 = userManipulation.transformResponseClient(getUser.dataValues);
                
                // in oder to test the application, we return 3 same users.
                testUser = [user1,user1,user1]
                

                callback(testUser,setting.htmlCode.succes_request);

            }).catch(function(error) {

                utils.logError("error getting user : "+idApi)
                callback(null,setting.htmlCode.unavailable_ressources);
            });
        });

    }


  this.getUsersAround = function(UserObject,callback){
      utils.logInfo("getUsersAround()");
      utils.logInfo(UserObject);

      // We synchronize with the databse in order to change the name and the 
       User.sync({force: false}).then(function () {

            var updateUser =  User.update({
                lattitude : UserObject.lattitude,
                longitude : UserObject.longitude,
              }, 
              {
              where: {
                        idApiConnection: UserObject.idApiConnection
                     }
          // callback if the user srequest succeed.
          }).then(function(updateUser) {

              if(updateUser[0] == 0){
                utils.logInfo("The User does not exist !");
                callback(null,setting.htmlCode.unavailable_ressources);
              }
              // We get the user in the database and send to the client. 
              else{
                  utils.logInfo("I'am update the user");
                  getUserAroundTest(UserObject.idApiConnection,callback);
              }
          }).catch(function(error) {
               utils.logInfo("controllerUtilisateur(), the request fail"+error);
               callback(null,setting.htmlCode.unavailable_ressources);
          })

      });

    }  
}

module.exports = new controllerUtilisateurTest();