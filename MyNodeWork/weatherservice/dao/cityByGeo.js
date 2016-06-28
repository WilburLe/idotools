var $util = require('../util/util');
var $sql = require('./weatherSqlMapping');
var async = require('async');
var mysql = require('../conf/mysql-client').mysql;
var redis = require('../conf/redis').redis;

module.exports.geo = function (address, callback) {
    // callback({"text": "Not Found"}, null);
    // return;
    if (!address || address.adcode == 0) {
        callback({"text": "Not Found"}, null);
        return;
    }

    async.waterfall([
        getConnectionFunction,
        geoidFunction,
        cityFunction
    ], function (err, data, connection) {
        connection.release();
        callback(err, data);
    });


    function getConnectionFunction(callback) {
        mysql.getConnection(function (err, connection) {
            callback(null, err, connection);
        });
    }

    function geoidFunction(err, connection, callback) {
        // callback(null, {err: "no find", msg: address}, connection);
        // return;
        //
        var rkey = "geo_" + address.adcode;
        redis.get(rkey, function (err, result) {
            if (result) {
                callback(null, null, JSON.parse(result), connection);
                return;
            }
            connection.query($sql.geoid, address.adcode, function (err, result) {
                if (!err && result.length > 0) {
                    redis.set(rkey, JSON.stringify(result[0]), function (err, reply) {
                        redis.expire(rkey, 60 * 60);
                    });
                    callback(null, null, result[0], connection);
                    return;
                }
                var pName = address.province;
                var cName = address.city;
                if (pName == cName) {
                    connection.query($sql.name, [replaceName(cName), replaceName(pName)], function (err, result) {
                        if (err || result.length == 0) {
                            callback(null, {code: 404, err: "Not Found", msg: address}, null, connection);
                        } else {
                            redis.set(rkey, JSON.stringify(result[0]), function (err, reply) {
                                // redis.expire(rkey, 60 * 60 * 24);
                            });
                            callback(null, null, result[0], connection);
                        }

                    });
                } else {
                    connection.query($sql.name2, [replaceName(cName), replaceName(pName)], function (err, result) {
                        if (err || result.length == 0) {
                            callback(null, {code: 404, err: "Not Found", msg: address}, null, connection);
                        } else {
                            redis.set(rkey, JSON.stringify(result[0]), function (err, reply) {
                                // redis.expire(rkey, 60 * 60 * 24);
                            });
                            callback(null, null, result[0], connection);
                        }
                    });
                }

            });

        });
    }

    function cityFunction(err, city, connection, callback) {
        if (err) {
            callback(err, null, connection);
            return;
        }
        var treePath = JSON.stringify(city.treePath);
        treePath = treePath.replace('[1]', '').replace(/\]\[/ig, ',');
        treePath = treePath == '' ? '[' + city.id + ']' : treePath;
        var ids = JSON.parse(treePath);
        var ps = '';
        if (ids.length == 1) {
            ps += '(?)';
        } else if (ids.length == 2) {
            ps += '(?,?)';
        }
        connection.query($sql.ids_city + ps, ids, function (err, result) {
            var data = {};
            data.status = 'OK';

            if (err || result.length == 0) {
                data.result = [
                    {
                        "locations": [
                            cityPackge(city, [], [])
                        ]
                    }
                ];
            } else {
                data.result = [
                    {
                        "locations": [
                            cityPackge(city, ids, result)
                        ]
                    }
                ];
            }
            callback(null, data, connection);
        });

    }

};

//城市名字替换
var replaceName = function (name) {
    if (name.length > 2) {
        if (name.indexOf("自治") > 0) {
            name = name.substring(0, name.indexOf("自治"));
        } else if (name.lastIndexOf("地区") > 0) {
            name = name.substring(0, name.indexOf("地区"));
        } else if (name.lastIndexOf("省") > 0 || name.lastIndexOf("市") > 0 || name.lastIndexOf("县") > 0 || name.lastIndexOf("盟") > 0) {
            name = name.substring(0, name.length - 1);
        }
    }
    return name;
}
/**
 * 城市返回值组装
 * @param city 查询的最终城市
 * @param ids treePath的替换
 * @param parents 父城市组
 * @returns {*[]}
 */
var cityPackge = function (city, ids, parents) {
    var map = {};
    for (var n in parents) {
        map[parents[n].id] = parents[n];
    }
    var AdministrativeArea = {};
    var SupplementalAdminAreas = [];
    if (ids.length > 0) {
        var pro = map[ids[0]];
        AdministrativeArea = {
            "ID": pro.id,
            "LocalizedName": pro.name,
            "EnglishName": pro.eName,
            "Level": 1,
            "LocalizedType": "省",
            "EnglishType": "Province",
            "CountryID": "CN"
        };
    }

    if (ids.length > 1) {
        var ci = map[ids[1]];
        SupplementalAdminAreas = [
            {
                "Level": 2,
                "LocalizedName": ci.name,
                "EnglishName": ci.eName
            }
        ];
    }

    var location = {
        "Version": 1,
        "Key": city.weatherId,
        "Type": "City",
        "LocalizedName": city.name,
        "EnglishName": city.eName,
        "Region": {
            "ID": "ASI",
            "LocalizedName": "亚洲",
            "EnglishName": "Asia"
        },
        "Country": {
            "ID": "CN",
            "LocalizedName": "中国",
            "EnglishName": "China"
        },
        "AdministrativeArea": AdministrativeArea,
        "SupplementalAdminAreas": SupplementalAdminAreas,
        "TimeZone": {
            "Code": "CST",
            "Name": "Asia/Shanghai",
            "GmtOffset": 8
        }
    };


    return location;
}
