var me = {};
var mongodb = require('../configer/mongodb');
var Schema = mongodb.mongoose.Schema;

var scaledata = new Schema({
    statistics: {
        scaladata: {
            app: String,
            scale: String,
            date: String,
            survive: Boolean,
            count: Number
        }
    },
    timestamp: Number
});

exports.scaledata = mongodb.mongoose.model("scaledata", scaledata, "scaledata");

