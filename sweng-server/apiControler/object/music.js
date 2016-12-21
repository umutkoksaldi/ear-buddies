var utils = require('../utils/Utils.js');

function MusicDto()
{

	/**
	 * tranform a music object, remove time parameters and userid.
	 * @param  {[json]} musicObject 
	 * @return {[Json]} return the modified music object.
	 */
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