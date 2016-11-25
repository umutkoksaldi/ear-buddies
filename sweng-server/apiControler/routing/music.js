var express 		 = require('express');
var router 		     = express.Router();
var path             = require('path');
var musicControler    = require('../model/musicModel.js');
var error            = require('../../setting/error.js');
var utils            = require('../utils/Utils.js');

// Mach on the route
router.route("/:id")

    // Get the user.
    .get(function(req,res)
    {
        utils.logInfo("routing.user(), get user");
        if(typeof(req.params.id) != 'undefined'){
            musicControler.getMusic(req.params.id,function(reponse, htmlCode){
                res.status(htmlCode).json(reponse);
            });
        }
        else{
           res.status(error.bad_request).json(null);
        }
    })

    .post(function(req,res)
    {
        utils.logInfo(" routing.user(), create a user ");
        utils.logDebug(" routing.user()" + JSON.stringify(req.body));

        // if the body of the request is defined we can rtry to add user
        if(req.body != undefined & typeof(req.params.id) != 'undefined'){
            // We call user Control with the callback method json to send a Json
            musicControler.updateMusic(req.params.id,req.body,function(reponse, htmlCode){
                res.status(htmlCode).json(reponse);
            });
        }
        else {
           res.status(error.bad_request).json(null);
        }
    });


// Mach on the route
router.route("/history/:id")
    // Get the user.
    .get(function(req,res)
    {
        utils.logInfo("routing.user(), get user");
        if(typeof(req.params.id) != 'undefined'){
            musicControler.getHistory(req.params.id,function(reponse, htmlCode){
                res.status(htmlCode).json(reponse);
            });
        }
        else{
           res.status(error.bad_request).json(null);
        }
    })

module.exports = router;
