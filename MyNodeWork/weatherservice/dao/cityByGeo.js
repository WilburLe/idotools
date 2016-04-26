var mysql = require('mysql');
var $conf = require('../conf/db');
var $util = require('../util/util');
var $sql = require('./weatherSqlMapping');
var async = require('async');
var pool = mysql.createPool($util.extend({}, $conf.mysql));

module.exports.geo = function (address, callback) {
    async.waterfall([
        function (callback) {
            if (!address || address.adcode == 0) {
                callback(null, {err: "no find", msg: address});
            } else {
                pool.getConnection(function (err, connection) {
                    connection.query($sql.geoid, address.adcode, function (err, result) {
                        if (!result || result.length == 0) {
                            var pName = address.province;
                            var cName = address.city;
                            connection.query($sql.name2, [replaceName(cName), replaceName(pName)], function (err, result) {
                                callback(null, result);
                            });
                        } else {
                            callback(null, result);
                        }
                        connection.release();
                    });
                });
            }
        }
    ], function (err, city) {
        if (city.err) {
            $util.gzipAesWrite(req, res, cityPackge(city, [], []));
        } else {
            var city = city[0];
            var treePath = city.treePath.replace('[1]', '').replace(/\]\[/ig, ',');
            var ids = JSON.parse(treePath);
            var ps = '';
            if (ids.length == 1) {
                ps += '(?)';
            } else if (ids.length == 2) {
                ps += '(?,?)';
            }
            pool.getConnection(function (err, connection) {
                connection.query($sql.ids_city + ps, ids, function (err, result) {
                    var data = {};
                    data.status = 'OK';
                    data.result = [
                        {
                            "locations": [
                                cityPackge(city, ids, result)
                            ]
                        }
                    ];
                    callback(data);
                    connection.release();
                });
            });
        }
    });

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
    console.log("ids > " + JSON.stringify(ids) + " > " + JSON.stringify(parents));
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
