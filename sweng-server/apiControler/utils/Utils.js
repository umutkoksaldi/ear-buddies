var winston = require('winston'); 



// if We test we desactivate the output in the console.
// in order to see only the response.
if (process.env.NODE_ENV !== 'test') {
    // log for developpement.
    logger = new (winston.Logger)({
        transports: [
            new (winston.transports.Console)({'timestamp':true}), 
            new (winston.transports.File)({ filename: 'foo.log' })
        ]
    });
} else {
    // log for testing.
    logger = new (winston.Logger)({
        transports: [
            new (winston.transports.File)({ filename: 'foo.log','timestamp':true }),
        ]
    });
}


function Utils()
{

    this.logInfo = function (Messagetolog){
        this.log("info",Messagetolog);
    }
    
    this.logError = function (Messagetolog){
        this.log("error",Messagetolog);
    }

    this.logWarn = function (Messagetolog){
        this.log("warn",Messagetolog);
    }

    this.logDebug = function(Messagetolog){
        this.log("debug",Messagetolog);
    }

    this.log = function (type,Messagetolog){
        logger.log(type,Messagetolog);

    }

    this.dateUtilsToString = function (mydateMili){
        var myDate = new Date(mydateMili)
        //return myDate.getUTCFullYear()+"-"+myDate.getUTCMonth()+"-"+myDate.getUTCDate()
        return myDate.toDateString();
    }

    this.dateUtilsToMili = function (mydateUTC){
        var myDate = new Date(mydateUTC)
        return myDate.getTime();
    }
}


// export object
module.exports = new Utils ();