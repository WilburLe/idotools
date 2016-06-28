var express = require('express');
var router = express.Router();
var fs = require('fs');
var async = require('async');
var FileQueue = require('filequeue');
var fq = new FileQueue(200);
var adconfig2redis = require('../model/adconfig2redis');
var log = require("../model/log").logger("appconfig"); // logger中的参数随便起
var basepath = require('../common').basepath;

var replaceAll = function (find, replace, str) {
    var find = find.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
    return str.replace(new RegExp(find, 'g'), replace);
}

router.get('/', function (req, res, next) {
    var checkapp = req.query.checkapp;
    var checkchannel = req.query.checkchannel;
    async.waterfall([
        getAppsFunction,
        getChannelsFunction,
        getAdsFunction,
        getAdopenFunction,
        adsFunction
    ], function (err, result) {
        res.render('appconfig', result);
        res.end();
    });


    function getAppsFunction(callback) {
        fq.readFile(basepath + "/config/app_config.txt", {encoding: 'utf8'}, function (err, data) {
            var apps = "[" + data + "]";
            callback(null, err, apps);
        });
    }

    function getChannelsFunction(err, apps, callback) {
        fq.readFile(basepath + "/config/channel_config.txt", {encoding: 'utf8'}, function (err, data) {
            var channels = "[" + data + "]";
            callback(null, err, apps, channels);
        });
    }

    function getAdsFunction(err, apps, channels, callback) {
        fq.readFile(basepath + "/config/ad_config.txt", {encoding: 'utf8'}, function (err, data) {
            var ads = "[" + data + "]";
            callback(null, err, apps, channels, ads);
        });
    }

    function getAdopenFunction(err, apps, channels, ads, callback) {
        fq.readFile(basepath + "/config/adopen_config.txt", {encoding: 'utf8'}, function (err, data) {
            callback(null, err, apps, channels, ads, data);
        });
    }

    function adsFunction(err, apps, channels, ads, adopen, callback) {
        var apphaves = [];
        var channelhaves = [];
        var adhaves = [];
        fq.readdir(basepath + "/config/ad/", function (err, files) {
            if (err) {
                callback('read dir error', null);
            } else {
                files.forEach(function (item) {
                    var configgroup = item.replace('_config.txt', '').split('@');
                    apphaves.push(configgroup[0]);
                    channelhaves.push({app: configgroup[0], channel: configgroup[1]});
                    if (checkapp == configgroup[0] && checkchannel == configgroup[1]) {
                        adhaves.push(configgroup[2]);
                    }

                });
                callback(null,
                    {
                        apps: apps,
                        channels: channels,
                        ads: ads,
                        apphaves: JSON.stringify(apphaves),
                        channelhaves: JSON.stringify(channelhaves),
                        checkapp: checkapp,
                        checkchannel: checkchannel,
                        adhaves: adhaves,
                        adopen: adopen
                    }
                );
            }
        });
    }

});

router.post('/saveapp', function (req, res, next) {
    var appname = req.body.appname;
    var appcode = req.body.appcode;
    //包名唯一
    fq.readFile(basepath + "/config/app_config.txt", {encoding: 'utf8'}, function (err, srcData) {
        if (srcData.toString().indexOf(appcode) >= 0) {
            console.log(appcode + " is not one")
            res.writeHead(302, {
                'Location': '/appconfig?errormsg=1'
            });
            res.end();
            return;
        }
        var app = {};
        app.code = appcode;
        app.name = appname;
        app.version = [];
        var buf;
        if (srcData.length > 0) {
            buf = new Buffer("," + JSON.stringify(app));
        } else {
            buf = new Buffer(JSON.stringify(app));
        }
        fs.open(basepath + "/config/app_config.txt", "a+", function (err, fd) {
            fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) {

            });
        })
        res.writeHead(302, {
            'Location': '/appconfig'
        });
        res.end();
    });

});

router.post('/savechannel', function (req, res, next) {
    var channelname = req.body.channelname;
    var channelcode = req.body.channelcode;
    //渠道唯一
    fq.readFile(basepath + "/config/channel_config.txt", {encoding: 'utf8'}, function (err, srcData) {
        if (srcData.toString().indexOf(channelcode) >= 0) {
            console.log(channelcode + " is not one")
            res.writeHead(302, {
                'Location': '/appconfig?errormsg=2'
            });
            res.end();
            return;
        }

        var channel = {};
        channel.name = channelname;
        channel.code = channelcode;
        var srcLength = srcData.length;
        var buf;
        if (srcLength > 0) {
            buf = new Buffer("," + JSON.stringify(channel));
        } else {
            buf = new Buffer(JSON.stringify(channel));
        }
        fs.open(basepath + "/config/channel_config.txt", "a+", function (err, fd) {
            fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) {

            });
        })
        res.writeHead(302, {
            'Location': '/appconfig'
        });
        res.end();
    });

});

router.post('/savead', function (req, res, next) {
    var adname = req.body.adname;
    var adcode = req.body.adcode;
    //广告位唯一
    fq.readFile(basepath + "/config/channel_config.txt", {encoding: 'utf8'}, function (err, srcData) {
        if (srcData.toString().indexOf(adcode) >= 0) {
            console.log(adcode + " is not one")
            res.writeHead(302, {
                'Location': '/appconfig?errormsg=3'
            });
            res.end();
            return;
        }

        var ad = {};
        ad.name = adname;
        ad.code = adcode;
        var srcLength = srcData.length;
        var buf;
        if (srcLength > 0) {
            buf = new Buffer("," + JSON.stringify(ad));
        } else {
            buf = new Buffer(JSON.stringify(ad));
        }
        fs.open(basepath + "/config/ad_config.txt", "a+", function (err, fd) {
            fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) {

            });
        })
        res.writeHead(302, {
            'Location': '/appconfig'
        });
        res.end();
    });

});
router.post('/saveversion', function (req, res, next) {
    var appcode = req.body.app;
    var name = req.body.name;
    var code = req.body.code;
    var version = {name: name, code: code};

    var appconfigpath = basepath + "/config/app_config.txt";
    fq.readFile(basepath + "/config/app_config.txt", {encoding: 'utf8'}, function (err, data) {
        var apps = "[" + data + "]";
        var apparr = JSON.parse(apps);
        for (var m in apparr) {
            var app = apparr[m];
            if (appcode == app.code) {
                if (app.version) {
                    app.version.push(version);
                } else {
                    app.version = [version];
                }
                break;
            }
        }
        var buf = new Buffer(JSON.stringify(apparr).substr(1, JSON.stringify(apparr).length - 2));
        fs.open(appconfigpath, "w", function (err, fd) {
            fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) {

                //处理广告配置中的version
                fq.readdir(basepath + "/config/ad/", function (err, files) {
                    if (err) {
                        console.log('read dir error');
                    } else {
                        files.forEach(function (item) {
                            var adpath = basepath + "/config/ad/" + item;
                            var configgroup = item.replace('_config.txt', '').split('@');
                            if (appcode == configgroup[0]) {
                                try {
                                    fq.readFile(adpath, {encoding: 'utf8'}, function (err, data) {
                                        var adconfig = JSON.parse(data);
                                        adconfig.version.push(code);
                                        var adbuf = new Buffer(JSON.stringify(adconfig));
                                        fs.open(adpath, "w", function (err, fd) {
                                            fs.write(fd, adbuf, 0, adbuf.length, 0, function (err, written, buffer) {

                                            });
                                        });
                                    });
                                } catch (e) {
                                    console.log(">>>> " + e);
                                }
                            }
                        });
                        //redis
                        adconfig2redis.adconfig()
                        res.json({status: 1});
                    }
                });
            });
        });

    });

});
router.post('/delversion', function (req, res, next) {
    var appcode = req.body.app;
    var vercode = req.body.code;

    //处理app配置中的version
    var appconfigpath = basepath + "/config/app_config.txt";
    fq.readFile(appconfigpath, {encoding: 'utf8'}, function (err, data) {
        var apps = "[" + data + "]";
        var apparr = JSON.parse(apps);
        for (var m in apparr) {
            var app = apparr[m];
            if (appcode == app.code) {
                var vers = app.version;
                var vers_ = []
                for (var k in vers) {
                    var ver = vers[k];
                    if (ver.code == vercode) {
                        continue;
                    }
                    vers_.push(ver);
                }
                app.version = vers_;
                break;
            }
        }
        var buf = new Buffer(JSON.stringify(apparr).substr(1, JSON.stringify(apparr).length - 2));
        fs.open(appconfigpath, "w", function (err, fd) {
            fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) {
                //处理广告配置中的version
                fq.readdir(basepath + "/config/ad/", function (err, files) {
                    if (err) {
                        console.log('read dir error');
                    } else {
                        files.forEach(function (item) {
                            var adpath = basepath + "/config/ad/" + item;
                            var configgroup = item.replace('_config.txt', '').split('@');
                            if (appcode == configgroup[0]) {
                                try {
                                    fq.readFile(adpath, {encoding: 'utf8'}, function (err, data) {
                                        var adconfig = JSON.parse(data);
                                        var advers = adconfig.version;
                                        var advers_ = [];
                                        for (var m in advers) {
                                            if (vercode != advers[m]) {
                                                advers_.push(advers[m]);
                                            }
                                        }
                                        adconfig.version = advers_;
                                        var adbuf = new Buffer(JSON.stringify(adconfig));
                                        fs.open(adpath, "w", function (err, fd) {
                                            fs.write(fd, adbuf, 0, adbuf.length, 0, function (err, written, buffer) {

                                            });
                                        });
                                    })
                                } catch (e) {
                                    console.log(">>>> " + e);
                                }
                            }
                        });
                        //redis
                        adconfig2redis.adconfig()
                        res.json({status: 1});
                    }
                });
            });
        });
    });


});

router.post('/delconfig', function (req, res, next) {
    var code = req.body.code;
    var type = req.body.type;
    var configpath;
    if (type == 'app') {
        configpath = basepath + "/config/app_config.txt";
    } else if (type == 'channel') {
        configpath = basepath + "/config/channel_config.txt";
    } else if (type == 'ad') {
        configpath = basepath + "/config/ad_config.txt";
    }

    fq.readFile(configpath, {encoding: 'utf8'}, function (err, data) {
        var apps = JSON.parse("[" + data + "]");
        var apps_ = [];
        for (var m in apps) {
            if (apps[m].code == code) {
                continue;
            }
            apps_.push(JSON.stringify(apps[m]));
        }
        var buf = new Buffer(apps_.toString());
        fs.open(configpath, "w", function (err, fd) {
            fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) {
                fq.readdir(basepath + "/config/ad/", function (err, files) {
                    if (err) {
                        console.log('read dir error');
                    } else {
                        files.forEach(function (item) {
                            var adpath = basepath + "/config/ad/" + item;
                            var configgroup = item.replace('_config.txt', '').split('@');
                            try {
                                if (type == 'app' && configgroup[0] == code) {
                                    fs.unlinkSync(adpath);
                                } else if (type == 'channel' && configgroup[1] == code) {
                                    fs.unlinkSync(adpath);
                                } else if (type == 'ad' && configgroup[2] == code) {
                                    fs.unlinkSync(adpath);
                                }
                            } catch (e) {
                                console.log(">>>> " + e);
                            }
                        });
                        //uodate redis
                        adconfig2redis.adconfig();
                        res.json({status: 1});
                    }
                });
            });
        })
    });


});
router.post('/openad', function (req, res, next) {
    var app = req.body.app;
    var channel = req.body.channel;
    var ad = req.body.ad;
    var state = req.body.state;
    var type = req.body.type;
    var result = {status: 1};
    switch (type) {
        case "app":
            result = openapp(app, state);
            break;
        case "channel":
            result = openchannel(app, channel, state);
            break;
        case "ad":
            result = openads(app, channel, ad, state);
            break;
        default :
            break;
    }
    function openapp(app, state) {
        fq.readFile(basepath + "/config/channel_config.txt", {encoding: 'utf8'}, function (err, data) {
            var channels = "[" + data + "]";
            var channelarr = JSON.parse(channels);
            for (var m in channelarr) {
                var channel = channelarr [m];
                fq.readFile(basepath + "/config/ad_config.txt", {encoding: 'utf8'}, function (err, data) {
                    var ads = "[" + data + "]";
                    var adsarr = JSON.parse(ads);
                    for (var g in adsarr) {
                        var ad = adsarr[g];
                        openads(app, channel.code, ad.code, state);
                    }
                });

            }
        });

        return {status: 0};
    }

    function openchannel(app, channel, state) {
        fq.readFile(basepath + "/config/ad_config.txt", {encoding: 'utf8'}, function (err, data) {
            var ads = "[" + data + "]";
            var adsarr = JSON.parse(ads);
            for (var m in adsarr) {
                var ad = adsarr[m];
                if (ad && ad.code) {
                    openads(app, channel, ad.code, state);
                }
            }
        });

        return {status: 0};
    }

    function openads(appcode, channel, ad, state) {
        if (!channel) {
            return {status: -1, msg: "请选择所在APP下的一个渠道！"};
        }
        var configpath = basepath + "/config/ad/" + appcode + "@" + channel + "@" + ad + "_config.txt";
        if (state == 1) {
            var version = [];

            fq.readFile(basepath + "/config/app_config.txt", {encoding: 'utf8'}, function (err, data) {
                var apps = "[" + data + "]";
                var apparr = JSON.parse(apps);
                for (var m in apparr) {
                    var app = apparr[m];
                    if (app.code == appcode) {
                        for (var m in app.version) {
                            version.push(app.version[m].code);
                        }
                        break;
                    }
                }
                var addata = {version: version, app: appcode, channel: channel, ad: ad};
                var buf = new Buffer(JSON.stringify(addata));
                fs.open(configpath, "w", function (err, fd) {
                    fs.write(fd, buf, 0, buf.length, 0, function (err, written, buffer) {

                    });
                });
            });
        } else {
            try {
                fs.unlinkSync(configpath);
            } catch (e) {
            }
        }
        return {status: 0};
    }

    //uodate redis
    adconfig2redis.adconfig();
    res.json(result);
});


module.exports = router;