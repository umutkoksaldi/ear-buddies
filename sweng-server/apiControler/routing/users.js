var express 		 = require('express');
var router 		     = express.Router();
var path             = require('path');
var userController    = require('../model/userModel.js');
var userControllerTest= require('../model/userModelTest.js')
var error            = require('../../setting/error.js');
var utils            = require('../utils/Utils.js');

// Match on the route
router.route("/")

    // Create Users.
    .post(function(req,res)
    {
        utils.logInfo(" routing.user(), create a user ");
        utils.logDebug(" routing.user()" + JSON.stringify(req.body));

        // if the body of the request is defined we can retry to add user
	    if(req.body != undefined){
            // We call user Control with the callback method json to send a Json
    		userController.updateGetInformationUser(req.body,function(reponse, htmlCode){
  				res.status(htmlCode).json(reponse);
  			});
    	}
        else {
           res.status(error.bad_request).json(null);
        }
    })

// get a user by his id
router.route("/:id")

    // Get the user.
    .get(function(req,res)
    {
        utils.logInfo("routing.user(), get user");
        if(typeof(req.params.id) != 'undefined'){
            userController.getUser(req.params.id,function(reponse, htmlCode){
                res.status(htmlCode).json(reponse);
            });
        }
        else{
           res.status(error.bad_request).json(null);
        }
    })

    // Delete a user.
    .delete(function(req,res)
    {
        utils.logInfo("routing.user(), delete user");
        if(typeof(req.params.id) != 'undefined'){
            userController.removeUser(req.params.id,function(reponse, htmlCode){
                res.status(htmlCode).json(reponse);
            });
        }
        else{
           res.status(error.bad_request).json(null);
        }
    })

    // Update user information.
    .put(function(req,res)
    {
        utils.logInfo("routing.user(), put to update user");
        utils.logDebug("routing.user()"+JSON.stringify(req.body));
        if(req.body != undefined && typeof(req.params.id) != 'undefined'){
            userController.updateUser(req.params.id,req.body,function(reponse, htmlCode){
                res.status(htmlCode).json(reponse);
            });
        }
        else{
           var response = null
           res.status(error.bad_request).json(response);
        }
    });


// get a user by his id
router.route("/Setting/:id")

    // Create Users.
    .put(function(req,res)
    {
        utils.logInfo(" routing.user(), yolo");
        utils.logDebug(" routing.user()" + JSON.stringify(req.body));

        // if the body of the request is defined we can retry to add user
        if(typeof(req.params.id) != 'undefined' && req.body != undefined){
            // We call user Control with the callback method json to send a Json
            utils.logInfo(" routing.user(), updateSetting");
            userController.updateSetting(req.params.id,req.body,function(reponse, htmlCode){
                res.status(htmlCode).json(reponse);
            });
        }
        else {
           res.status(error.bad_request).json(null);
        }
    });

// get a user by his id
router.route("/getUsersAround")
    // Create Users.
    .post(function(req,res)
    {
        utils.logInfo(" routing.user(), create a user ");
        utils.logDebug(" routing.user()" + JSON.stringify(req.body));

        // if the body of the request is defined we can retry to add user
        if(req.body != undefined){
            // We call user Control with the callback method json to send a Json
            userController.getUsersAround(req.body,function(reponse, htmlCode){
                res.status(htmlCode).json(reponse);
            });
        }
        else {
           res.status(error.bad_request).json(null);
        }
    });

    // get a user by his id
router.route("/getUsersAroundTest")
    // Create Users.
    .post(function(req,res)
    {
        utils.logInfo(" routing.user(), create a user ");
        utils.logDebug(" routing.user()" + JSON.stringify(req.body));

        // if the body of the request is defined we can retry to add user
        if(req.body != undefined){
            // We call user Control with the callback method json to send a Json
            userControllerTest.getUsersAround(req.body,function(reponse, htmlCode){
                res.status(htmlCode).json(reponse);
            });
        }
        else {
           res.status(error.bad_request).json(null);
        }
    });


module.exports = router;
