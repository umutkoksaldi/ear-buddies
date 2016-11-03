var pg = require('pg');
var databaseConfig = require('../../setting/database.js');
var utils = require('../utils/Utils.js');

function BaseDeDonnee()
{

    // first we select on wich database we are going to work.
    var urlDatabase = null
    if(process.env.NODE_ENV !== 'test') {
        utils.logInfo("Postgres.Database(), run in normal configuration");
        urlDatabase = databaseConfig.PostGre.url
    }
    else {
        utils.logInfo("Postgres.Database(), run for test configuration");
        urlDatabase = databaseConfig.PostGreTest.url
    }

    // Then we try to connect to the database.
    this.postgres = require('pg');
    this.ConnexionPostgres  = function ()
    {
        // Get a Postgres client from the connection pool
        this.postgres.defaults.ssl = true;
        this.postgres.connect(urlDatabase, function(err, client, done) 
        {
            // Handle connection errors
            if(err) 
            {
              utils.logError("Postgres.Database(), Connect to postgres database fail ");
            }
            else
            {
              utils.logInfo("Postgres.Database(), Connect to the postgres database success");
              utils.logInfo(urlDatabase);
            }
        });
    }; 
}

var BaseDeDonnee = new BaseDeDonnee();
// export object
module.exports = BaseDeDonnee;
