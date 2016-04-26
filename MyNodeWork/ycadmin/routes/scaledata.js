var express = require('express');
var router = express.Router();

var moment = require('moment');
var scaledata = require("../model/ScaledataModel").scaledata;
var stat = require("../model/StatModel").statdata;
var scale = require("../configer/StatScaleEnum").scale;


router.get('/', function (req, res, next) {
    var days = req.query.days;
    var apps = req.query.apps;
    var scale = req.query.scale;
    res.render("scaledata", {days: days, apps: apps, scale: scale});
});

router.get('/chart', function (req, res, next) {
    var days = req.query.days;
    var appsStr = req.query.apps;
    var scale = req.query.scale;
    var apps = appsStr.split(',');
    var dateMax = moment().format("YYYY-MM-DD");
    var dateMin = moment().add(0 - days, 'days').format("YYYY-MM-DD");
    var query = {
        "statistics.scaladata.date": {"$gte": dateMin, "$lte": dateMax},
        "statistics.scaladata.scale": scale,
        "statistics.scaladata.app": {"$in": apps}
    };
    var labels = [];
    for (var i = 0; i < days; i++) {
        labels.push(moment().add(0 - i, 'days').format("YYYY-MM-DD"));
    }
    var colors = ["red", "green", "blue", "yellow", "black", "gray", "orange", "pink", "brown", "purple"];
    scaledata.find(query)
        .sort({"statistics.scaladata.date": -1})
        .exec(function (err, doc) {
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
                var labelview = {};
                labelview.label = apps[i];
                labelview.color = color;
                labelviews.push(labelview);
                for (var j in labels) {
                    var label = labels[j];
                    var data = 0;
                    for (var m in doc) {
                        var stat = doc[m].statistics.scaladata;
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
                datasets.push(dataset);
            }
            result.datasets = datasets;
            result.labels = labels;
            result.labelviews = labelviews;
            res.writeHead(200, {'Content-Type': 'application/json'});
            res.end(JSON.stringify(result));
        });
})
;

router.get('/save', function (req, res, next) {
    for (var i = 0; i < 100; i++) {
        var date = moment().add(0 - i, 'days').format("YYYY-MM-DD");
        var count = Math.ceil(Math.random() * 10 * (i + 1));
        var query = {
            statistics: {
                scaladata: {
                    app: "com.idotools.test1",
                    scale: "diu",
                    date: date,
                    survive: false,
                    count: count
                }
            }, timestamp: 1451577600
        };
        scaledata.create(query);
    }
    for (var i = 0; i < 100; i++) {
        var date = moment().add(0 - i, 'days').format("YYYY-MM-DD");
        var count = Math.ceil(Math.random() * 10 * (i + 1));
        var query = {
            statistics: {
                scaladata: {
                    app: "com.idotools.test1",
                    scale: "dau",
                    date: date,
                    survive: false,
                    count: count
                }
            }, timestamp: 1451577600
        };
        scaledata.create(query);
    }
    for (var i = 0; i < 100; i++) {
        var date = moment().add(0 - i, 'days').format("YYYY-MM-DD");
        var count = Math.ceil(Math.random() * 10 * (i + 1));
        var query = {
            statistics: {
                scaladata: {
                    app: "com.idotools.test1",
                    scale: "wau",
                    date: date,
                    survive: false,
                    count: count
                }
            }, timestamp: 1451577600
        };
        scaledata.create(query);
    }
    for (var i = 0; i < 100; i++) {
        var date = moment().add(0 - i, 'days').format("YYYY-MM-DD");
        var count = Math.ceil(Math.random() * 10 * (i + 1));
        var query = {
            statistics: {
                scaladata: {
                    app: "com.idotools.test1",
                    scale: "mau",
                    date: date,
                    survive: false,
                    count: count
                }
            }, timestamp: 1451577600
        };
        scaledata.create(query);
    }
    for (var i = 0; i < 100; i++) {
        var date = moment().add(0 - i, 'days').format("YYYY-MM-DD");
        var count = Math.ceil(Math.random() * 10 * (i + 1));
        var query = {
            statistics: {
                scaladata: {
                    app: "com.idotools.test1",
                    scale: "dru",
                    date: date,
                    survive: false,
                    count: count
                }
            }, timestamp: 1451577600
        };
        scaledata.create(query);
    }
    for (var i = 0; i < 100; i++) {
        var date = moment().add(0 - i, 'days').format("YYYY-MM-DD");
        var count = Math.ceil(Math.random() * 10 * (i + 1));
        var query = {
            statistics: {
                scaladata: {
                    app: "com.idotools.test1",
                    scale: "wru",
                    date: date,
                    survive: false,
                    count: count
                }
            }, timestamp: 1451577600
        };
        scaledata.create(query);
    }
    for (var i = 0; i < 100; i++) {
        var date = moment().add(0 - i, 'days').format("YYYY-MM-DD");
        var count = Math.ceil(Math.random() * 10 * (i + 1));
        var query = {
            statistics: {
                scaladata: {
                    app: "com.idotools.test1",
                    scale: "mru",
                    date: date,
                    survive: false,
                    count: count
                }
            }, timestamp: 1451577600
        };
        scaledata.create(query);
    }
    res.redirect("/scaledata");

});

module.exports = router;
