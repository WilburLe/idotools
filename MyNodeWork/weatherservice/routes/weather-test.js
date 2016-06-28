var express = require('express');
var router = express.Router();
var $util = require('../util/util');
var $http = require('../util/httpClient').httpClient;
var weatherDao = require('../dao/weatherDao');
var cityByName = require('../dao/cityByName');
var cityByGeo = require('../dao/cityByGeo');
var hotCity = require('../dao/hotCity');
var redis = require('../conf/redis').redis;
var async = require('async');


router.get('/', function (req, res, next) {
    res.render('index', {city: {result: []}, weather: {weather: []}});
});

router.post('/wea', function (req, res, next) {
    var lat = req.body.lat;
    var lng = req.body.lng;
    async.waterfall([
        searchLocationByCoordinate,
        getWeatherByCityId
    ], function (city, weather) {
        res.render('index', {city: city, weather: JSON.parse(weather), lat: lat, lng: lng});
    });


//searchLocationByCoordinate
    function searchLocationByCoordinate(callback) {
        var latlng = lat + "," + lng;
        var url = "api.map.baidu.com";
        var path = "/geocoder/v2/?ak=FDb8a96df0329f10675a4d84fd172b8a" + //
            //"&callback=result" +//
            "&location=" + latlng + //
            "&output=json" + //
            "&pois=0";

        async.waterfall([
            geoFunction,
            parseFunction,
            resultFunction
        ], function (status, result) {
            callback(null, status, JSON.parse(result));
        });

        var cycle = 0;

        function geoFunction(callback) {
            $http.doGet(url, path, null, null, function (result, status, headers) {
                if (status != 200 && cycle < 3) {
                    cycle++;
                    geoFunction(callback);
                }
                callback(null, result, status);
            });
        }

        function parseFunction(geoData, httpStatus, callback) {
            if (httpStatus != 200) {
                callback(null, 404, JSON.stringify({result: [],"text": "Not Found"}));
                return;
            }
            var r = JSON.parse(geoData);
            var address = r.result.addressComponent;
            if (!address || address.adcode == 0) {
                callback(null, 404, JSON.stringify({result: [],"text": "Not Found"}));
                return;
            }
            callback(null, 200, address);
        }

        function resultFunction(status, address, callback) {
            if (status != 200) {
                callback(status, address);
                return;
            }
            var cityid_key = "geoCityId_" + address.adcode;
            redis.get(cityid_key, function (err, result) {
                if (result) {
                    callback(200, result);
                    return;
                }
                cityByGeo.geo(address, function (err, result) {
                        if (err) {
                            callback(404, JSON.stringify({"text": "Not Found"}));
                            return;
                        }
                        redis.set(cityid_key, JSON.stringify(result), function (err, reply) {
                            redis.expire(cityid_key, 60 * 5);
                        });
                        callback(200, JSON.stringify(result));
                    }
                );
            });
        }
    }

    function getWeatherByCityId(status, data, callback) {
        console.log("1 > " + status);
        console.log("2 > " + JSON.stringify(data));
        if(status != 200) {
            callback(data, JSON.stringify({weather: []}));
            return;
        }
        var cityId = data.result[0].locations[0].Key;
        var cityid_key = "weather_" + cityId;
        redis.get(cityid_key, function (err, result) {
            if (result) {
                callback(data, result);
            } else {
                weatherDao.weather(cityId, function (result) {
                    if (result && !result.err) {
                        redis.set(cityid_key, JSON.stringify(result), function (err, reply) {
                            redis.expire(cityid_key, 60 * 5);
                        });
                    }
                    callback(data, JSON.stringify(result));
                });
            }
        });
    }
});

//getSearchHots
router.post('/getSearchHots', function (req, res, next) {
    hotCity.hots(function (result) {
        $util.gzipAesWrite(200, req, res, JSON.stringify(result));
    });

});

module.exports = router;