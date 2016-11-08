//During the test the env variable is set to test
process.env.NODE_ENV = 'test';

//Require the dev-dependencies
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../app');
var utils = require('../apiControler/utils/Utils.js');
var should = chai.should();
var setting = require('../setting/error.js');
var userManipulation = require ('../apiControler/object/user.js')

chai.use(chaiHttp);
var User = require('../apiControler/database/SequelizeORM.js').User;


// ----------------------------------------------   DEFINE USEFUL CONSTANT  -----------------------------------------
var UserTest = {

        "idApiConnection": "121620614972695",
        "lastname": "Tests",
        "firstname": "Sweng",
        "email": "etienne.husler@epfl.ch",
        "age": 0,
        "profilePicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
        "backgroundPicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
        "description": null,

        "SettingId": 1,

        "currentMusicId": null,
        "location": {
          "lattitude": null,
          "longitude": null
        }
}

var setting = {
          "id": 1,
          "ageMin": 0,
          "ageMax": 100,
          "radius": 20
        }
// ----------------------------------------------   TEST  -----------------------------------------

//Our parent block
describe('Test setting unitary method', () => {

  // ----------------------------------------------   Transform client response  -----------------------------------------
  // 
      describe('change object return ', () => {
        it('modify the object return', (done) => {
                    var objectResponse = userManipulation.changeSetting(UserTest,setting)
                    chai.assert.equal(objectResponse.setting.id, setting.id, 'Settings should be equals');
                    chai.assert.equal(objectResponse.setting.ageMin, setting.ageMin, 'Settings should be equals');
                    chai.assert.equal(objectResponse.setting.ageMax, setting.ageMax, 'Settings should be equals');
                    chai.assert.equal(objectResponse.setting.radius, setting.radius, 'Settings should be equals');
                    chai.assert.equal(objectResponse.SettingId, undefined, 'This attribute shoul not exist anymore');
                    done();
              });
        });


});