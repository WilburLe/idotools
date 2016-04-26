var me = {};
var mongodb = require('../configer/mongodb');
var Schema = mongodb.mongoose.Schema;

var apaceSchema = new Schema({
    app: {type: String},
    name: {type: String}
});

exports.appcode = mongodb.mongoose.model("appcodes", apaceSchema, "appcodes");

