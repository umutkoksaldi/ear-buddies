//During the test the env variable is set to test
process.env.NODE_ENV = 'test';

//Require the dev-dependencies
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../app');
var utils = require('../apiControler/utils/Utils.js');
var Constant = require('../apiControler/utils/Constant.js');
var should = chai.should();
var chaiAsPromised = require ("chai-as-promised")
chai.use(chaiAsPromised);
var setting = require('../setting/error.js');
var expect = chai.expect;

chai.use(chaiHttp);
var User = require('../apiControler/database/SequelizeORM.js').User;

// ----------------------------------------------   DEFINE USEFUL CONSTANT  -----------------------------------------
// define constante
var LONGITUDE = 6.601919;
var LATTITUDE = 46.526848;
var ID = 121620614972695;

var point = { type: 'Point', coordinates: [LATTITUDE, LONGITUDE]};

var user1 = {
  "email": "testU1@test.test",
  "id" : ID,
  "age": 12,
  "firstname": "Antoine",
  "lastname": "Dupond",
  "name": "Antoine Dupond",
  "profilePicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
  "backgroundPicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
  "point" : point
}

//Right outside the radius
point = { type: 'Point', coordinates: [46.706911, 6.576562]}
var user2 = {
  "email": "testU2@test.test",
  "id" : "221620614972695",
  "age": 12,
  "firstname": "Arnaud",
  "lastname": "Hennig",
  "name": "Arnaud Hennig",
  "profilePicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
  "backgroundPicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
  "point" : point
}

//Right inside the radius
point = { type: 'Point', coordinates: [46.704615, 6.568923]}
var user3 = {
  "email": "testU3@test.test",
  "id" : "321620614972695",
  "age": 12,
  "firstname": "Jean",
  "lastname": "Despré",
  "name": "Jean Despré",
  "profilePicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
  "backgroundPicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
  "point" : point
}

//inside the radius too old
point = { type: 'Point', coordinates: [46.518474, 6.601183]}
var user4 = {
  "email": "testU4@test.test",
  "id" : "321620614972695",
  "age": 102,
  "firstname": "Florian",
  "lastname": "DeLaRue",
  "name": "Florian DeLeRue",
  "profilePicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
  "backgroundPicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
  "point" : point
}

var PARAM_USER = {
  'idApiConnection' : ID,
  'lattitude'       : LATTITUDE,
  'longitude'       : LONGITUDE,
}

// ----------------------------------------------   TEST  -----------------------------------------

//Our parent block
describe('Test User API getUsersAround.', () => {

  // Before each test we insert 3 user.
  beforeEach((done) => {

    // the promise allow to test some asynchronous method.
    var testPromise = new Promise(function(resolve, reject) {
      setTimeout(function() {
        User.sync({force: true}).then(function () {

          var createUser1 =  User.create({
            idApiConnection :user1.id,
            lastname : user1.lastname,
            firstname: user1.firstname,
            email : user1.email,
            age : user1.age,
            profilePicture : user1.profilePicture,
            backgroundPicture : user1.backgroundPicture,
            location : user1.point

            // callback if the user srequest succeed.
          }).then(function(createUser1) {
            createUser1.setSetting(Constant.GeneralModel.idDefaultSetting);

            var createUser2 =  User.create({
              idApiConnection :user2.id,
              lastname : user2.lastname,
              firstname: user2.firstname,
              email : user2.email,
              age : user2.age,
              profilePicture : user2.profilePicture,
              backgroundPicture : user2.backgroundPicture,
              location : user2.point

              // callback if the user srequest succeed.
            }).then(function(createUser2) {
              createUser2.setSetting(Constant.GeneralModel.idDefaultSetting);

              var createUser3 =  User.create({
                idApiConnection :user3.id,
                lastname : user3.lastname,
                firstname: user3.firstname,
                email : user3.email,
                age : user3.age,
                profilePicture : user3.profilePicture,
                backgroundPicture : user3.backgroundPicture,
                location : user3.point

                // callback if the user srequest succeed.
              }).then(function(createUser3) {
                createUser3.setSetting(Constant.GeneralModel.idDefaultSetting);

                var createUser4 =  User.create({
                  idApiConnection :user4.id,
                  lastname : user4.lastname,
                  firstname: user4.firstname,
                  email : user4.email,
                  age : user4.age,
                  profilePicture : user4.profilePicture,
                  backgroundPicture : user4.backgroundPicture,
                  location : user4.point

                  // callback if the user srequest succeed.
                }).then(function(createUser4) {
                  createUser4.setSetting(Constant.GeneralModel.idDefaultSetting);
                  resolve(true);
                  // return a 500 code if the request is null.
                }).catch(function(error) {
                  resolve(false);
                })
                // return a 500 code if the request is null.
              }).catch(function(error) {
                resolve(false);
              })

              // return a 500 code if the request is null.
            }).catch(function(error) {
              resolve(false);
            })

            // return a 500 code if the request is null.
          }).catch(function(error) {
            resolve(false);
          })
        })
      }, 200);
    });

    testPromise.then(function(result){
      try {
        console.log ("Before :" +result);
        expect(result).to.equal(true);
        done();
      } catch(err) {
        done(err);
      }
    }, done);
  });

  // ----------------------------------------------   TEST GET USER AROUND-----------------------------------------

  describe('get user around', () => {
    it ('should get only one other user', (done) =>{
      chai.request(server)
      .post('/api/Users/getUsersAround/')
      .send(PARAM_USER)
      .end((err, res) => {
        console.log("Get UserAround");
        //test server response
        res.should.have.status(setting.htmlCode.succes_request);
        var body = res.body;
        chai.assert.equal(body.length, 1, 'There should be one user');
        chai.assert.equal(body[0].firstname, user3.firstname);
        done();
      });
    });
  });
});
