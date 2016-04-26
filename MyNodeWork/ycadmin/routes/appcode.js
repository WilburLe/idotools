var express = require('express');
var router = express.Router();
var scale = require("../configer/StatScaleEnum").scale;
var appcode = require("../model/AppcodeModel").appcode;

router.get('/', function (req, res, next) {
    appcode.find().exec(function (err, doc) {
        res.render("appcode", {data: doc, scale: JSON.stringify(scale)});
    });
});

router.get('/save', function (req, res, next) {
    var app = req.query.app;
    var name = req.query.name;
    var query = {app: app, name: name};
    appcode.create(query);
    res.redirect("/appcode");
});

module.exports = router;