var express = require('express');
var router = express.Router();
var $util = require('../util/util');
var $http = require('../util/httpClient').httpClient;
var weatherDao = require('../dao/weatherDao');
var cityByName = require('../dao/cityByName');
var cityByGeo = require('../dao/cityByGeo');
var hotCity = require('../dao/hotCity');
var redis = require('../conf/redis').redis;

//searchLocationByKeyword
router.post('/searchLocationByKeyword', function (req, res, next) {
    //$util.getRequestBody(req, function (body) {
    //    if (body || body == '') {
    //        $util.gzipAesWrite(req, res, {});
    //    } else {
    //        var p = body.Normal[0];
    //        var keyword = p.keyword;
    //        cityByName.find(keyword, function (data) {
    //            $util.gzipAesWrite(req, res, data);
    //        });
    //    }
    //});

    var p = req.body.Normal[0];
    var keyword = p.keyword;
    var cityid_key = "findCity_" + keyword;
    redis.get(cityid_key, function (err, result) {
        if (result) {
            $util.gzipAesWrite(req, res, result);
        } else {
            cityByName.find(keyword, function (result) {
                if (result && !result.err) {
                    redis.set(cityid_key, JSON.stringify(result), function (err, reply) {
                        redis.expire(cityid_key, 60 * 60 * 24);
                    });
                }
                $util.gzipAesWrite(req, res, JSON.stringify(result));
            });

        }
    });
});

//searchLocationByCoordinate
router.post('/searchLocationByCoordinate', function (req, res, next) {
    //$util.getRequestBody(req, function (body) {
    //    if (body || body == '') {
    //        $util.gzipAesWrite(req, res, {});
    //    } else {
    //        var p = body.Normal[0];
    //        var latlng = p.latitude + "," + p.longitude;
    //        var url = "api.map.baidu.com";
    //        var path = "/geocoder/v2/?ak=FDb8a96df0329f10675a4d84fd172b8a" + //
    //                //"&callback=result" +//
    //            "&location=" + latlng + //
    //            "&output=json" + //
    //            "&pois=0";
    //        $http.doGet(url, path, null, null, function (result, status, headers) {
    //            var r = JSON.parse(result);
    //            var addressComponent = r.result.addressComponent;
    //            cityByGeo.geo(addressComponent, function (data) {
    //                $util.gzipAesWrite(req, res, data);
    //            });
    //        });
    //    }
    //});

    var p = req.body.Normal[0];
    var latlng = p.latitude + "," + p.longitude;
    var url = "api.map.baidu.com";
    var path = "/geocoder/v2/?ak=FDb8a96df0329f10675a4d84fd172b8a" + //
            //"&callback=result" +//
        "&location=" + latlng + //
        "&output=json" + //
        "&pois=0";
    $http.doGet(url, path, null, null, function (result, status, headers) {
        var r = JSON.parse(result);
        var address = r.result.addressComponent;
        if (!address || address.adcode == 0) {
            $util.gzipAesWrite(req, res, JSON.stringify({err: "no find", msg: address}));
        } else {
            var cityid_key = "geoCityId_" + address.adcode;
            redis.get(cityid_key, function (err, result) {
                    if (result) {
                        $util.gzipAesWrite(req, res, result);
                    } else {
                        cityByGeo.geo(address, function (result) {
                                if (result && !result.err) {
                                    redis.set(cityid_key, JSON.stringify(result), function (err, reply) {
                                        //redis.expire(cityid_key, 60 * 5);
                                    });
                                }
                                $util.gzipAesWrite(req, res, JSON.stringify(result));
                            }
                        );
                    }
                }
            );
        }
    });
});


router.post('/', function (req, res, next) {
    //$util.getRequestBody(req, function (body) {
    //    if (body || body == '') {
    //        $util.gzipAesWrite(req, res, {});
    //    } else {
    //        var p = body.Normal[0];
    //        var cityId = p.cityId;
    //        weatherDao.weather(cityId, function (data) {
    //            $util.gzipAesWrite(req, res, data);
    //        });
    //    }
    //});

    var p = req.body.Normal[0];

    var cityId = p.cityId;
    var cityid_key = "weather_" + cityId;

    redis.get(cityid_key, function (err, result) {
        if (result) {
            $util.gzipAesWrite(req, res, result);
        } else {
            weatherDao.weather(cityId, function (result) {
                if (result && !result.err) {
                    redis.set(cityid_key, JSON.stringify(result), function (err, reply) {
                        redis.expire(cityid_key, 60 * 5);
                    });
                }
                $util.gzipAesWrite(req, res, JSON.stringify(result));
            });
        }
    });


});

router.get('/aaa', function (req, res, next) {
    res.render('index', { title: 'Jade' });
});
//getSearchHots
router.post('/getSearchHots', function (req, res, next) {
    //$util.getRequestBody(req, function (body) {
    //    if (body || body == '') {
    //        $util.gzipAesWrite(req, res, {});
    //    } else {
    //        var p = body.Normal[0];
    //        var hotCount = p.hotCount;
    //        console.log("hotCount >> " + hotCount);
    //        hotCity.hots(function (result) {
    //            $util.gzipAesWrite(req, res, result);
    //        });
    //    }
    //});
    var Normal = req.body.Normal[0];
    hotCity.hots(function (result) {
        $util.gzipAesWrite(req, res, JSON.stringify(result));
    });

});

module.exports = router;