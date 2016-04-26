var express = require('express');
var router = express.Router();

var moment = require('moment');
var stat = require("../model/StatModel").statdata;


router.get('/', function (req, res, next) {
    var days = req.query.days;
    var apps = req.query.apps;
    var scale = req.query.scale;
    res.render("stat", {days: days, apps: apps, scale: scale});
});

router.get('/chart', function (req, res, next) {
    var days = req.query.days;
    var appsStr = req.query.apps;
    var scale = req.query.scale;
    var apps = appsStr.split(',');
    //var apps = ["com.finance.download", "com.toolbox.download", "12345", "2122", "2233", "999", "222221", "dsd", "d211", "231", "32d2", "123dds", "3123"];
    var dateMax = moment().format("YYYY-MM-DD");
    var dateMin = moment().add(0 - days, 'days').format("YYYY-MM-DD");
    var query = {"date": {"$gte": dateMin, "$lte": dateMax}, "app": {"$in": apps}};
    var labels = [];
    for (var i = 0; i < days; i++) {
        labels.push(moment().add(0 - i, 'days').format("YYYY-MM-DD"));
    }
    var colors = ["red", "green", "blue", "yellow", "black", "gray", "orange", "pink", "brown", "purple"];
    stat.find(query).sort({date: 1}).exec(function (err, doc) {
        var result = {};
        var labelviews = [];

        var datasets = [];
        for (var i = 0; i < apps.length; i++) {
            var color = colors[i % colors.length];
            var dataset = {};
            dataset.label = apps[i];
            dataset.strokeColor = color;
            dataset.pointColor = color;
            dataset.fillColor = color;
            dataset.data = [];
            datasets.push(dataset);

            var labelview = {};
            labelview.label = apps[i];
            labelview.color = color;
            labelviews.push(labelview);

            for (var j in labels) {
                var label = labels[j];
                var data = 0;
                for (var m in doc) {
                    var stat = doc[m];
                    var app = stat.app;
                    var date = stat.date;
                    var count = stat.count;
                    if (label == date && app == apps[i]) {
                        data = count;
                        break;
                    }
                }
                dataset.data.push(data);
            }
        }
        result.datasets = datasets;
        result.labels = labels;
        result.labelviews = labelviews;
        res.writeHead(200, {'Content-Type': 'application/json'});
        res.end(JSON.stringify(result));
    });
});

router.get('/save', function (req, res, next) {
    for (var i = 0; i < 100; i++) {
        var date = moment().add(0 - i, 'days').format("YYYY-MM-DD");
        var count = Math.ceil(Math.random() * 10 * (i + 1));
        var query = {app: "com.toolbox.download", date: date, count: count, scale: "456"};
        stat.create(query);
    }
    res.redirect("/stat");

});

module.exports = router;
