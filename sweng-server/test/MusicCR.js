//During the test the env variable is set to test
process.env.NODE_ENV = 'test';

var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../app');
var utils = require('../apiControler/utils/Utils.js');
var should = chai.should();
var chaiAsPromised = require ("chai-as-promised")
chai.use(chaiAsPromised);
var setting = require('../setting/error.js');
var expect = chai.expect;

// import model form the ORM
var Music = require('../apiControler/database/SequelizeORM.js').Music;
var User = require('../apiControler/database/SequelizeORM.js').User;
chai.use(chaiHttp);

 // ----------------------------------------------   DEFINE USEFUL CONSTANT  -----------------------------------------

var ID_USER = 1;
var MUSIC_ID = 1;

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

var GoodMusicName = {
      'artistName' : 'rihanna',
      'musicName' : 'umbrella'
}

var responseGood = {
      'artist' : 'Rihanna',
      'name' : 'Umbrella',
      'url' : 'https://www.last.fm/music/Rihanna/_/Umbrella',
      'tag' : 'pop',
      "urlPicture": "https://lastfm-img2.akamaized.net/i/u/174s/a60d9d9ae55226699420b52ab28d3ad0.png"
}

var BadMusicName = {
      'musicName' : 'umbreleto'
}



// ----------------------------------------------   TEST  -----------------------------------------

//We create the user.
describe('Test Music API', function(){

    this.timeout(4000);
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
                    Music.sync({force: true}).then(function () {});
                    expect(result).to.equal(true);
                    done();
              } catch(err) {
                  done(err);
              }
          }, done);    
  });

 // ----------------------------------------------   Create User good parameters -----------------------------------------
         describe('create Music 1', () => {
                it('should create music good parameters --> LastFM connection', (done) => {

                  url = '/api/Musics/'+ userTest.id;
                  chai.request(server)
                      .post(url)
                      .send(GoodMusicName)
                        .end((err, res) => {

                          // the promise allow to test some asynchronous method.
                          var testPromise = new Promise(function(resolve, reject) {
                          setTimeout(function() {

                              res.should.have.status(setting.htmlCode.succes_request);
                                 // select query.
                                 var getMusic =  Music.findOne({
                                      where: {

                                      }
                                    }).then(function(getMusic) {
                                          resolve(getMusic.dataValues);
                                    });
                            
                        }, 200);
                    });

                    testPromise.then(function(result){
                        try {
                              expect(result.artist).to.equal(responseGood.artist);
                              expect(result.name).to.equal(responseGood.name);
                              expect(result.url).to.equal(responseGood.url);
                              expect(result.tag).to.equal(responseGood.tag);
                              expect(result.urlPicture).to.equal(responseGood.urlPicture);
                              // We check in the after if the user has the ID_USER.
                              expect(result.UserId).to.equal(ID_USER);
                              expect(result.id).to.equal(MUSIC_ID);
                              
                              done();
                        } catch(err) {
                            done(err);
                        }
                      }, done);
              });
          });
    });

       describe('test the get Music', () => {
              it('should the get a music by his id', (done) => {

                url = '/api/Musics/'+ userTest.id;
                chai.request(server)
                    .post(url)
                    .send(GoodMusicName)
                      .end((err, res) => {

                        // the promise allow to test some asynchronous method.
                        var testPromise = new Promise(function(resolve, reject) {
                        setTimeout(function() {

                            res.should.have.status(setting.htmlCode.succes_request);
                               // select query.
                               var getMusic =  Music.findOne({
                                    where: {

                                    }
                                  }).then(function(getMusic) {
                                        resolve(getMusic.dataValues);
                                  });
                          
                      }, 200);
                  });

                  testPromise.then(function(result){
                      try {

                         // Test the get Method.
                         chai.request(server)
                          .get('/api/Musics/'+ result.id)
                          .end((err, res) => {
                              expect(result.artist).to.equal(responseGood.artist);
                              expect(result.name).to.equal(responseGood.name);
                              expect(result.url).to.equal(responseGood.url);
                              expect(result.tag).to.equal(responseGood.tag);
                              expect(result.UserId).to.equal(ID_USER);
                              expect(result.id).to.equal(MUSIC_ID);
                              expect(result.urlPicture).to.equal(responseGood.urlPicture);
                              done();

                        });
                      } catch(err) {
                          done(err);
                      }
                    }, done);
            });
        });
    });


     describe('create Music 2', () => {
            it('should create music bad parameters --> LastFM connection', (done) => {

              url = '/api/Musics/'+ userTest.id;
              chai.request(server)
                  .post(url)
                  .send(BadMusicName)
                    .end((err, res) => {

                      // the promise allow to test some asynchronous method.
                      var testPromise = new Promise(function(resolve, reject) {
                      setTimeout(function() {

                          res.should.have.status(setting.htmlCode.succes_request);
                             // select query.
                             var getMusic =  Music.findOne({
                                  where: {

                                  }
                                }).then(function(getMusic) {
                                      resolve(getMusic.dataValues);
                                }); 
                    }, 200);
                });

                testPromise.then(function(result){
                    try {
                          expect(result.artist).to.equal(null);
                          expect(result.name).to.equal(BadMusicName.musicName);
                          expect(result.url).to.equal(null);
                          // We check in the after if the user has the ID_USER.
                          expect(result.UserId).to.equal(ID_USER);
                          expect(result.id).to.equal(MUSIC_ID);
                          done();
                    } catch(err) {
                        done(err);
                    }
                  }, done);
          });
      });
    });


    // After each test we verify that the user is updated properly. 
    afterEach((done) => {
              // the promise allow to test some asynchronous method.
              var testPromise = new Promise(function(resolve, reject) {
                setTimeout(function() {

                           var getUser =  User.findOne({
                                where: {
                                  idApiConnection: userTest.id
                                }
                              }).then(function(getUser) {
                                    resolve(getUser.dataValues);
                              });
                    }, 300);
                });

          testPromise.then(function(result){
              try {
                    expect(result.CurrentMusicId).to.equal(ID_USER);
                    expect(result.id).to.equal(MUSIC_ID);
                    console.log ("After :" + true);
                    done();
              } catch(err) {
                  console.log ("After :" + false);
                  done(err);
              }
          }, done);    
    });
});






