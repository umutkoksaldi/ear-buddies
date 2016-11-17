// Récupération du Modèle
var unirest = require('unirest');
var util = require('util');
var setting = require('../../setting/error.js');
var databaseConfig = require('../../setting/database.js');
var globalConfig = require('../../setting/global.js');
var userManipulation = require ('../object/user.js')
var utils = require('../utils/Utils.js');
var Constant = require ('../utils/Constant.js')
var utilTest = require ('util') 

// database
var databasePostgres = require('../database/postgres.js');
var User = require('../database/SequelizeORM.js').User;
var Music = require('../database/SequelizeORM.js').Music;
var Setting = require('../database/SequelizeORM.js').Setting;
var sequelize = require('../database/SequelizeORM.js').sequelize ;

//---------------------------------- DEFINE CONSTANT ------------------------------------

var URL_FACEBOOK = "https://graph.facebook.com/";
var FIELDS_FACEBOOK = 'email,cover,birthday,first_name,last_name,name,picture';
var LATTITUDE = 46.526848
var LONGITUDE = 6.601919

//---------------------------------- DEFINE CONSTANT ------------------------------------

// Définition de l'objet controllerUtilisateur
function controllerUtilisateur(){

  // private method, allow to create a specific user by the object given by facebook.
  var createUser = function(res,backgroundPict,callback){

          // build the url to get the picture
          var urlPictureFacebook = "https://graph.facebook.com/"+res.body.id+"/picture?height=500&width=500"; 
          utils.logInfo("createUser(), insertion or geetin a user");
          var UserAge = userManipulation.computeAge(res.body.birthday);

          // default location.
          var point = { type: 'Point', coordinates: [LATTITUDE,LONGITUDE]};

          // Insert the new user in the database .
          User.sync({force: false}).then(function () {

                var createUser =  User.create({
                        idApiConnection : res.body.id, 
                        lastname : res.body.last_name,
                        firstname: res.body.first_name,
                        email : res.body.email,
                        age : UserAge,
                        profilePicture : urlPictureFacebook,
                        backgroundPicture : backgroundPict,
                        location : point

                // callback if the user srequest succeed.
                }).then(function(createUser) {

                  utils.logInfo("createUser(), the request succeed");

                  // We associate with the default value.
                  createUser.setSetting(Constant.GeneralModel.idDefaultSetting);

   
                  createUser.getSetting().then(function(associatedTasks) {
                      userManipulation.changeSetting(createUser.dataValues,associatedTasks.dataValues)
                      
                      // We kepp only the id of facebook.
                      delete createUser.dataValues['id']
                      var response = userManipulation.transformResponseClient(createUser.dataValues);
                      callback(response,setting.htmlCode.succes_request);
                  }).catch(function(error) {
                      utils.logError("This User does not have any SettingValue"+error)
                      callback(null,setting.htmlCode.unavailable_ressources);
                  });


                // return a 500 code if the request is null.
                }).catch(function(error) {
                     utils.logInfo("createUser(), the request fail" +error);
                     callback(null,setting.htmlCode.unavailable_ressources);
                })

           });
    }

 
  // private method, allow to get a specific user designed by api connection.
  var getUserByIdConnection = function(idApi,callback){

      var urlPictureFacebook = "https://graph.facebook.com/"+idApi+"/picture?height=500&width=500"; 
      utils.logInfo("getUserByIdConnection(), get the user"+ idApi);
      User.sync().then(function () {
        // select query.
         var getUser =  User.findOne({
              where: {
                idApiConnection: idApi
              }
            }).then(function(getUser) {

                utils.logInfo("request succeed"+idApi)
                getUser.getSetting().then(function(associatedTasks) {
                    userManipulation.changeSetting(getUser.dataValues,associatedTasks.dataValues)
                    delete getUser.dataValues['id']
                    var response = userManipulation.transformResponseClient(getUser.dataValues);
                    callback(response,setting.htmlCode.succes_request);
                }).catch(function(error) {
                    utils.logError("This User does not have any SettingValue"+error)
                    callback(null,setting.htmlCode.unavailable_ressources);
                });

            }).catch(function(error) {
                utils.logError("error getting user : "+error)
                callback(null,setting.htmlCode.unavailable_ressources);
            });
        });
    }


   var getUserAroundBySetting = function(idApi,callback){

      utils.logInfo("controllerUtilisateur(), insertion or geetin a user, adduser()");

      // I wanted to use the ORM but it does not work properly with geospatial request.
      var queryRequest = 'SELECT *, ST_Distance_Sphere(ST_MakePoint(:lattitude,:longitude), "location") AS user_distance '+
                          'FROM "Users" '+
                          'WHERE ST_Distance_Sphere(ST_MakePoint(:lattitude, :longitude), "location") <= ( :radius*1000 ) ' +
                          'AND age >= :ageMin '+
                          'AND age < :ageMax '+
                          'AND id != \':idUser\''+
                          'ORDER by user_distance;'

      User.sync().then(function () {
        // select query.
         var getUser =  User.findOne({
              where: {
                idApiConnection: idApi
              }
            }).then(function(getUser) {

                utils.logInfo("request succeed"+idApi)
                getUser.getSetting().then(function(settingUser) {

                    // execute the query by replacing values in query.
                    sequelize.query(queryRequest,
                    { 
                        replacements: 
                                      { 
                                        lattitude: getUser.location.coordinates[0],
                                        longitude: getUser.location.coordinates[1],
                                        radius: settingUser.radius,
                                        ageMin: settingUser.ageMin,
                                        ageMax: settingUser.ageMax,
                                        idUser : getUser.id
                                      }, type: sequelize.QueryTypes.SELECT 

                      }).then(function(usersAround) {
                            usersAround.forEach(function(value){
                              userManipulation.transformResponseClient(value);
                            });   

                            callback(usersAround,setting.htmlCode.succes_request);

                      }).catch(function(error) {
                            utils.logError('Query impossible '+error)
                            callback(null,setting.htmlCode.unavailable_ressources);
                      });

                }).catch(function(error) {
                  utils.logError("This User does not have any SettingValue"+error)
                  callback(null,setting.htmlCode.unavailable_ressources);
                });

            }).catch(function(error) {

                utils.logError("error getting user : "+idApi)
                callback(null,setting.htmlCode.unavailable_ressources);
            });
        });
  }


    this.updateGetInformationUser = function (body,callback)
    {     
        utils.logDebug("adduser()"+JSON.stringify(buildRequestFacebook(body.id,body.accesToken)));
        console.log("token"+body.accesToken)
        console.log("id   "+body.id)

        // GET the user description by doing a post on facebook API
        unirest.get(buildRequestFacebook(body.id,body.accesToken)).end(function(res){

          // We can't reach the facebook graph.
          if (res.error) {
           callback(null,setting.htmlCode.unavailable_ressources);
          }
          else {
                // make an JsonObject.
                res.body  = JSON.parse(res.body); 
                // build the url to get the picture
                var urlPictureFacebook = "https://graph.facebook.com/"+res.body.id+"/picture?height=500&width=500"; 

                utils.logInfo("updateGetInformationUser(), insertion or geetin a user, adduser()");

                var backgroundPict
                if (res.body.cover ==  undefined){
                  backgroundPict = "https://graph.facebook.com/"+121620614972695+"/picture?height=500&width=500"
                }
                else{
                  backgroundPict =  res.body.cover.source
                }

               // We synchronize with the databse in order to change the name and the 
               User.sync({}).then(function () {

                     var CreateUser =  User.update({
                          email : res.body.email,
                          profilePicture : urlPictureFacebook,
                          backgroundPicture : backgroundPict
                      }, 
                      {
                      where: {
                                idApiConnection: res.body.id
                             }
                  // callback if the user srequest succeed.
                  }).then(function(CreateUser) {
                      utils.logInfo("updateGetInformationUser(), the request succeed");

                      // We don't find any users with the id, we need to add the new user
                      // to the database.
                      if(CreateUser[0] == 0){
                        utils.logInfo("creation d'un nouveau utilisateur");
                        createUser(res,backgroundPict,callback);
                      }
                      // We get the user in the database and send to the client. 
                      else{
                          utils.logInfo("get the user freshly updated");
                          getUserByIdConnection(res.body.id,callback);
                      }

                  // if there is any error in computing value for user.
                  }).catch(function(error) {
                       utils.logInfo("updateGetInformationUser(), the request fail"+error);
                       callback(null,setting.htmlCode.unavailable_ressources);
                  })

              });
            }

        })
    };

  this.getUsersAround = function(UserObject,callback){
      utils.logInfo("getUsersAround()");
      utils.logInfo(UserObject);

      
      // We synchronize with the databse in order to change the name and the 
       User.sync({force: false}).then(function () {

            var point = { type: 'Point', coordinates: [UserObject.lattitude,UserObject.longitude]};
            var updateUser =  User.update({
                location : point
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
                  getUserAroundBySetting(UserObject.idApiConnection,callback);
              }
          }).catch(function(error) {
               utils.logInfo("controllerUtilisateur(), the request fail"+error);
               callback(null,setting.htmlCode.unavailable_ressources);
          })

      });


    }  

    this.getUser = function(idApi,callback){
      utils.logInfo("controllerUtilisateur(), get user "+idApi+", getUser()");
      getUserByIdConnection(idApi,callback)
    }  

    this.removeUser = function(idApi,callback){

       User.sync().then(function () {

           // delete the user.
           var deleteUser =  User.destroy({
                  where: {
                    idApiConnection: idApi
                  }

          // callback if the user srequest succeed.
          }).then(function(deleteUser) {
                  utils.logInfo("controllerUtilisateur(), the request succeed");
                  callback(null,setting.htmlCode.succes_request);

          // return a 500 code if the request is null.
          }).catch(function(error) {
               utils.logInfo("controllerUtilisateur(), the request fail"+error);
              callback(null,setting.htmlCode.unavailable_ressources);
          })

      });
    }    

    
    this.updateUser = function (idApi,UserObject,callback){

      // We synchronize with the databse in order to change the name and the 
       User.sync({force: false}).then(function () {

            var updateUser =  User.update({
                lastname: UserObject.lastname,
                firstname : UserObject.firstname,
                description: UserObject.description,
              }, 
              {
              where: {
                        idApiConnection: idApi
                     }
          // callback if the user srequest succeed.
          }).then(function(updateUser) {
                  utils.logInfo("controllerUtilisateur(), the request succeed");
                  callback(null,setting.htmlCode.succes_request);

          // return a 500 code if the request is null.
          }).catch(function(error) {
               utils.logInfo("controllerUtilisateur(), the request fail"+error);
              callback(null,setting.htmlCode.unavailable_ressources);
          })

      });
    } 


    this.updateSetting = function (idApi,SettingObject,callback){

      utils.logInfo("updateSetting()");
         
         User.sync().then(function () {
           // select query.
           var getUser =  User.findOne({
                where: {
                  idApiConnection: idApi
                }
            }).then(function(getUser) {
                utils.logInfo("updateSetting(), update setting of:"+getUser.idApiConnection);
                getUser.getSetting().then(function(associatedTasks) {

                    utils.logInfo("updateSetting(), update setting of:"+getUser.idApiConnection);
                    // the Setting of the user is the default one.
                    if (associatedTasks.dataValues.id == 1){
                          // Insert the default setting for everyone in the database.
                          Setting.sync({force: false}).then(function () {

                                // we create a new setting associated to the user.
                                var createSetting =  Setting.create({
                                        ageMin        : SettingObject.ageMin,
                                        ageMax        : SettingObject.ageMax,
                                        radius        : SettingObject.radius,
                                }).then(function(createSetting) {
                                        // create new settign
                                        utils.logInfo("initiateValue(), Added Setting default");
                                        // update the current value of the setting.
                                        getUser.setSetting(createSetting.id);                        
                                        callback(createSetting.id,setting.htmlCode.succes_request);

                                }).catch(function(error) {
                                     utils.logInfo("initiateValue(), We cannot add the new Setting" +error);
                                     callback(null,setting.htmlCode.unavailable_ressources);
                                })
                          });
                    }

                    // We update the current Setting of the User, already differents 
                    // from the default one.
                    else { 
                         // Insert the default setting for everyone in the database.
                         Setting.sync({force: false}).then(function () {

                            // we create a new setting associated to the user.
                            var createSetting =  Setting.update({
                                    ageMin        : SettingObject.ageMin,
                                    ageMax        : SettingObject.ageMax,
                                    radius        : SettingObject.radius,
                             },
                             {
                                where: {
                                          id: associatedTasks.dataValues.id
                                       }  
                                }).then(function(createSetting) {

                                        utils.logInfo("initiateValue(), Added Setting default");
                                        callback(null,setting.htmlCode.succes_request);

                                }).catch(function(error) {
                                     utils.logInfo("initiateValue(), We cannot add the new Setting" +error);
                                     callback(null,setting.htmlCode.unavailable_ressources);
                                })
                          });
                    }
                    
                }).catch(function(error) {
                    utils.logError("This User does not have any SettingValue"+error)
                    callback(null,setting.htmlCode.unavailable_ressources);
                });

            }).catch(function(error) {
                utils.logError("error getting user : "+error)
                callback(null,setting.htmlCode.unavailable_ressources);
            });
        });
    }


    var buildRequestFacebook = function(id , accessToken){
        return URL_FACEBOOK + id +"?fields="+ FIELDS_FACEBOOK +"&access_token="+ accessToken +"&height=500&width=500"; 
    }
}

module.exports = new controllerUtilisateur();