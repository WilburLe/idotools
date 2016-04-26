var express = require('express');
var router = express.Router();
var fs = require('fs');
var redis = require('../model/redis');


router.get('/dadata', function (req, res, next) {
    var packageName = req.query.packageName;
    var channel = req.query.channel;
    var version = req.query.version;
    var rk = packageName + "@" + channel + "@" + version;
    redis.redis.get("openad", function (err, reply) {
        res.writeHead(200, {'Content-Type': 'application/json'});
        var result = {};
        if (reply == 1) {
            result.open = true;
            redis.redis.get(rk, function (err, replies) {
                if (replies) {
                    result.data = JSON.parse(replies);
                } else {
                    result.data = [];
                }
                res.end(JSON.stringify(result));
            });
        } else {
            result.open = false;
            result.data = [];
            res.end(JSON.stringify(result));
        }

    });

});

module.exports = router;