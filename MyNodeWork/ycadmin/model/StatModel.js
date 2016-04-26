var me = {};
var mongodb = require('../configer/mongodb');
var Schema = mongodb.mongoose.Schema;

var statSchema = new Schema({
    app: {type: String},
    date: {type: String},
    count: {type: Number, default: 0},
    scale: {type: String},
    survive: {type: Boolean}
});

exports.statdata = mongodb.mongoose.model("statdata", statSchema, "statdata");

