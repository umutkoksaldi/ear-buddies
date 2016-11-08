var pg = require('pg');
var databaseConfig = require('../../setting/database.js');
var utils = require('../utils/Utils.js');
var Sequelize = require('sequelize');
var Constant = require('../utils/Constant.js');

function BaseDeDonnee()
{
          // first we select on wich database we are going to work.
      var urlDatabase = null;
      var logging_output = null;

      if(process.env.NODE_ENV !== 'test') {
          utils.logInfo("Postgres.Database(), run in normal configuration");
          urlDatabase = databaseConfig.PostGre.url
          this.sequelize = new Sequelize(urlDatabase,{
            dialect: 'postgres', 
             dialectOptions: {
                ssl: true
              }
          });
          
      }
      else {
          utils.logInfo("Postgres.Database(), run for test configuration");
          urlDatabase = databaseConfig.PostGreTest.url
          this.sequelize = new Sequelize(urlDatabase,{
            logging : false,
            dialect: 'postgres', 
              dialectOptions: {
                ssl: true
              }
          });
      }
      utils.logInfo("Sequelize database connected to: "+urlDatabase);

    // create the schema of the user.
    this.User = this.sequelize.define('Users', {
          idApiConnection: {
            type: Sequelize.BIGINT,
            //unique: 'unique_api_connection'
          },
          firstname           : Sequelize.STRING,
          email               : Sequelize.STRING,
          age                 : Sequelize.INTEGER,
          backgroundPicture   : Sequelize.STRING,
          profilePicture      : Sequelize.STRING,
          description         : Sequelize.STRING,
          lastname            : Sequelize.STRING,
          lattitude           : Sequelize.REAL,
          longitude           : Sequelize.REAL,

      },{
          timestamps: false
    })

    
    // create the schema of the user.
    this.Music = this.sequelize.define('Music', {
          artist        : Sequelize.STRING,
          name          : Sequelize.STRING,
          url           : Sequelize.STRING,
          tag           : Sequelize.STRING,

      },{
          timestamps: true
    })

    // create the schema of the user.
    this.Setting = this.sequelize.define('Setting', {
          ageMin        : {type: Sequelize.INTEGER,defaultValue: 0},
          ageMax        : {type: Sequelize.INTEGER,defaultValue: 100},
          radius        : {type: Sequelize.INTEGER,defaultValue: 20},

      },{
          timestamps: false
    })

    var setting = this.Setting

    this.makeLinkBetweenTable = function(){
      this.User.hasMany(this.Music)
      this.User.belongsTo(this.Music, {as: 'CurrentMusic', constraints: false})
      this.User.belongsTo(this.Setting, {constraints: false})
    }

    this.initiateValue = function(){

          // Insert the default setting for everyone in the database.
          this.Setting.sync({force: false}).then(function () {

                var createSetting =  setting.create({
                    id : Constant.GeneralModel.idDefaultSetting
                }).then(function(createSetting) {
                        utils.logInfo("initiateValue(), Added Setting default");
                // return a 500 code if the request is null.
                }).catch(function(error) {
                     utils.logInfo("initiateValue(), We cannot add the new Setting" +error);
                })
          });
    }

}

var BaseDeDonnee = new BaseDeDonnee();
BaseDeDonnee.makeLinkBetweenTable();
BaseDeDonnee.initiateValue();

// export object
module.exports = BaseDeDonnee;
