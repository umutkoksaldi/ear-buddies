var utils = require('../utils/Utils.js');

function MusicDto()
{

    this.transformResponseClient = function (musicObject) {

        utils.logInfo("transformResponseClient(), Modify the Json client")

        delete musicObject['updatedAt']
        delete musicObject['createdAt']
        delete musicObject['UserId']
        return musicObject;
    }

}


// export object
module.exports = new MusicDto();