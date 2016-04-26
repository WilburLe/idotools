var express = require('express');
var redis = require('./redis');
var fs = require('fs');
var basepath = require('../common').basepath;

var adconfig = function () {
    fs.readdir(basepath + "/config/ad/", function (err, files) {
        if (err) {
            console.log('read dir error');
        } else {
            var rdata = {};
            files.forEach(function (item) {
                var adpath = basepath + "/config/ad/" + item;
                var adconfig = JSON.parse(fs.readFileSync(adpath));
                var app = adconfig.app;
                var channel = adconfig.channel;
                var ad = adconfig.ad;
                var version = adconfig.version;
                for (var m in version) {
                    var rkey = app + "@" + channel + "@" + version[m];
                    var data = rdata[rkey];
                    if (!data) {
                        data = [];
                    }
                    data.push(ad);
                    rdata[rkey] = data;
                }
            });

            redis.redis.keys('*', function (err, keys) {
                if (err) {
                    return console.log(err);
                }
                for (var i = 0, len = keys.length; i < len; i++) {
                    var key = keys[i];
                    if(key != 'openad') {
                        redis.redis.expire(key, 3);
                    }
                }
                for (var rk in rdata) {
                    var rv = rdata[rk];
                    redis.redis.set(rk, JSON.stringify(rv));
                    console.log("redis set k=" + rk + ", v=" + rv + " success~")
                }
            });

        }
    });
}

module.exports.adconfig = adconfig;
