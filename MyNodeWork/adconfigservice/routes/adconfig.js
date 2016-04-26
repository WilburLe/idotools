var express = require('express');
var router = express.Router();
var fs = require('fs');
var redis = require('../model/redis');
var basepath = require('../common').basepath;

var replaceAll = function (find, replace, str) {
    var find = find.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
    return str.replace(new RegExp(find, 'g'), replace);
}
router.post('/openad', function (req, res, next) {
    var state = req.body.state;

    var buf = new Buffer(state);
    var configpath = basepath + "/config/adopen_config.txt";
    fs.open(configpath, "w", function (err, fd) {
        fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) {
            if (!err) {
                redis.redis.set("openad", state + "");
            }
            res.end();
        });
    })
});



module.exports = router;
