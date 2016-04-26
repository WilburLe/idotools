var express = require('express');

var redis = require("redis");
var client = redis.createClient(6379, '127.0.0.1', {});
client.auth('123456');
client.select(10, function () { /* … */
});
client.on("error", function (err) {
    console.log("Redis Error " + err);
});

// 设置键值
//client.set("Testing", "string val", redis.print);

// 取值
//client.get("Testing", function(err, replies) {
//    console.log("replies>> "+replies);
//});


module.exports.redis = client;
