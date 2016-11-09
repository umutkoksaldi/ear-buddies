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
var userTest = {

          "email": "test@test.test",
          "id" : "121620614972695",
          "age": 12,
          "firstname": "Antoine",
          "lastname": "Dupond",
          "name": "Antoine Dupond",
          "profilePicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500",
          "backgroundPicture": "https://graph.facebook.com/121620614972695/picture?height=500&width=500"
  }


var userUpdated = {

        "id" : "121620614972695",
        "firstname": "updateName",
        "lastname": "carlos",
        "description" :  "Description Updated"
}

var FAKE_ID = 1

 // ----------------------------------------------   TEST  -----------------------------------------

//Our parent block
describe('Test User API update/delete/get.', () => {

    // Before each test we insert a user. 
    beforeEach((done) => { 

        // the promise allow to test some asynchronous method.
        var testPromise = new Promise(function(resolve, reject) {
          setTimeout(function() {
                  User.sync({force: true}).then(function () {

                      var createUser =  User.create({
                              idApiConnection :userTest.id, 
                              lastname : userTest.lastname,
                              firstname: userTest.firstname,
                              email : userTest.email,
                              age : userTest.age,
                              profilePicture : userTest.profilePicture,
                              backgroundPicture : userTest.backgroundPicture

                      // callback if the user srequest succeed.
                      }).then(function(createUser) {
                          createUser.setSetting(Constant.GeneralModel.idDefaultSetting);
                          resolve(true);

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

    // ----------------------------------------------   TEST GET USER -----------------------------------------
    /*
    * We try to get the User 
    */
    describe('get user', () => {
        it('should get a user', (done) => {
          chai.request(server)
              .get('/api/Users/'+userTest.id)
              .end((err, res) => {
                  console.log("Get a User")
                  res.text  = JSON.parse(res.text);
                  // test the server Reponse.
                  res.should.have.status(setting.htmlCode.succes_request);

                  // test the receive value.
                  chai.assert.equal(res.text.idApiConnection, userTest.id, 'id should be equals');
                  chai.assert.equal(res.text.firstname, userTest.firstname, 'firstName should be equals');
                  chai.assert.equal(res.text.email, userTest.email, 'email should be equals');
                  chai.assert.equal(res.text.age, userTest.age, 'age should be equals');
                  chai.assert.equal(res.text.backgroundPicture, userTest.backgroundPicture, 'backkground picture should be equals');
                  chai.assert.equal(res.text.profilePicture, userTest.profilePicture, 'profile picture should be equals');
                  chai.assert.equal(res.text.lastname, userTest.lastname, 'lastname should be equals');
                  done();
              });
        });
    });

    describe('get user', () => {
        it('should fail to get a uer', (done) => {
          chai.request(server)
              .get('/api/Users/'+FAKE_ID)
              .end((err, res) => {
                  console.log("Get a User")
                  res.text  = JSON.parse(res.text);
                  // test the server Reponse.
                  res.should.have.status(setting.htmlCode.unavailable_ressources);
                  done();
              });
        });
    });

    // ----------------------------------------------   TEST DELETE USER -----------------------------------------
    /*
    * Should test if we delete the user.
    */
    describe('delete user', () => {
        it('should delete a user', (done) => {
          chai.request(server)
              .delete('/api/Users/'+userTest.id)
              .end((err, res) => {

                  // Check the User 
                  res.should.have.status(setting.htmlCode.succes_request);
                   // the promise allow to test some asynchronous method.
                      var testPromise = new Promise(function(resolve, reject) {
                      setTimeout(function() {

                          res.should.have.status(setting.htmlCode.succes_request);
                            User.sync().then(function () {
                              // select query.
                               var getUser =  User.findOne({
                                    where: {
                                      idApiConnection: userTest.id
                                    }
                                  }).then(function(getUser) {
                                      resolve(getUser);
                                  })
                              });
                    }, 200);
                });

                testPromise.then(function(result){
                    try {
                          expect(result).to.equal(null);
                          done();
                    } catch(err) {
                        done(err);
                    }
                  }, done);
            });
        });
    });


    describe('delete user', () => {
        it('should not a user', (done) => {
          chai.request(server)
              .delete('/api/Users/'+FAKE_ID)
              .end((err, res) => {
                  // Check the User 
                  res.should.have.status(setting.htmlCode.succes_request);
                   // We check if the user is actually remove.
                   User.sync().then(function () {
                      // select query.
                       var getUser =  User.findOne({
                            where: {
                              idApiConnection: userTest.id
                            }
                          }).then(function(getUser) {
                              // check if the user is null.
                              chai.assert.isNotNull(getUser, 'The user in the database is not deleted');
                              done();

                          }).catch(function(error) {
                             utils.logInfo("deleteUser(), the request fail" +error);
                             chai.assert.isOk(false, 'impossible to add user');
                             done(error);
                          });

                  });
            });
        });
    });


    // ----------------------------------------------   TEST UPDATE USER -----------------------------------------
    /*
    * Should test if we update the user.
    */
    describe('update user', () => {
        it('should update a user', (done) => {
          chai.request(server)
              .put('/api/Users/'+ userUpdated.id)
              .send({"description":userUpdated.description,
                    "firstname":userUpdated.firstname,
                    "lastname":userUpdated.lastname
                  })
              .end((err, res) => {
                // Check the User 
                  res.should.have.status(setting.htmlCode.succes_request);
                   // the promise allow to test some asynchronous method.
                      var testPromise = new Promise(function(resolve, reject) {
                      setTimeout(function() {

                          res.should.have.status(setting.htmlCode.succes_request);
                            User.sync().then(function () {
                              // select query.
                               var getUser =  User.findOne({
                                    where: {
                                      idApiConnection: userTest.id
                                    }
                                  }).then(function(getUser) {
                                      resolve(getUser.dataValues);
                                  })
                              });
                    }, 200);
                });

                testPromise.then(function(result){
                    try {
                            expect(result.idApiConnection).to.equal(userUpdated.id);
                            expect(result.firstname).to.equal(userUpdated.firstname);
                            expect(result.description).to.equal(userUpdated.description);
                            expect(result.lastname).to.equal(userUpdated.lastname);
                            done();
                    } catch(err) {
                        done(err);
                    }
                  }, done);
              });
        });
    });

});