//During the test the env variable is set to test
process.env.NODE_ENV = 'test';

//Require the dev-dependencies
var chai = require('chai');
var chaiHttp = require('chai-http');
var server = require('../app');
var utils = require('../apiControler/utils/Utils.js');
var should = chai.should();
var setting = require('../setting/error.js');
var musicManipulation = require ('../apiControler/object/music.js')

chai.use(chaiHttp);
var User = require('../apiControler/database/SequelizeORM.js').User;


// ----------------------------------------------   DEFINE USEFUL CONSTANT  -----------------------------------------

var  MusicTest ={
    "id": 11,
    "artist": "chere",
    "name": "Beliver",
    "url": "",
    "tag": "pop",
    "updatedAt": "2016-10-27T09:16:35.972Z",
    "createdAt": "2016-10-27T09:16:35.972Z",
    "UserId": null
  }

  var  MusicTestUpdate = {
    "id": 11,
    "artist": "chere",
    "name": "Beliver",
    "url": "",
    "tag": "pop",
  }
  var FAKE_MUSIC_ID = 100;

// ----------------------------------------------   TEST  -----------------------------------------

//Our parent block
describe('Test music uniatry method', () => {

  // ----------------------------------------------   COMPUTE Object  -----------------------------------------
    describe('Compute new Music object', () => {
        it('should compute the client response', (done) => {
                    var result = musicManipulation.transformResponseClient(MusicTest)
                    chai.assert.equal(result.id,MusicTestUpdate.id,'ids are not be equals');
                    chai.assert.equal(result.artist,MusicTestUpdate.artist,'ids are not be equals');
                    chai.assert.equal(result.name,MusicTestUpdate.name,'ids are not be equals');
                    chai.assert.equal(result.url,MusicTestUpdate.url,'ids are not be equals');
                    chai.assert.equal(result.updatedAt,undefined,'remove updatedAt');
                    chai.assert.equal(result.createdAt,undefined,'remove updatedAt');
                    chai.assert.equal(result.UserId,undefined,'remove updatedAt');
                    done();

              });
        });


       describe('test fail get Music', () => {
              it('should not get a music by his id', (done) => {

                url = '/api/Musics/'+ FAKE_MUSIC_ID;
                chai.request(server)
                    .get(url)
                      .end((err, res) => {
                            res.should.have.status(setting.htmlCode.unavailable_ressources);
                            done();
                  });

        });
    });

});