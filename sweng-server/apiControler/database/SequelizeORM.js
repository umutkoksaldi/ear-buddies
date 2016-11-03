var pg = require('pg');
var databaseConfig = require('../../setting/database.js');
var utils = require('../utils/Utils.js');
var Sequelize = require('sequelize');

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

    this.makeLinkBetweenTable = function(){
      this.User.hasMany(this.Music)
      this.User.belongsTo(this.Music, { as: 'CurrentMusic', constraints: false })
    }
}

var BaseDeDonnee = new BaseDeDonnee();
BaseDeDonnee.makeLinkBetweenTable();

// export object
module.exports = BaseDeDonnee;
