var $sql = require('./weatherSqlMapping');
var async = require('async');
var mysql = require('../conf/mysql-client').mysql;

module.exports.find = function (name, callback) {
    name = name + '%';
    mysql.getConnection(function (err, connection) {
            connection.query($sql.name, [name, name], function (err, result) {
                var data = {
                    "status": "OK"
                };
                if (!result || result.length == 0) {
                    callback({err: 'no find'});
                } else {
                    async.waterfall([
                            function (callback) {
                                var locations = [];
                                var count = 0;
                                result.forEach(function (city) {
                                    //var treePath = city.treePath.replace('[1]', '').replace(/\]\[/ig, ',');
                                    var treePath = city.treePath.replace(/\]\[/ig, ',');
                                    var ids = JSON.parse(treePath);
                                    var ps = '';
                                    if (ids.length == 1) {
                                        ps += '(?)';
                                    } else if (ids.length == 2) {
                                        ps += '(?,?)';
                                    } else if (ids.length == 3) {
                                        ps += '(?,?,?)';
                                    } else if (ids.length == 4) {
                                        ps += '(?,?,?,?)';
                                    }
                                    async.waterfall([
                                        function (callback) {
                                            mysql.getConnection(function (err, connection) {
                                                connection.query($sql.ids_city + ps, ids, function (err, result) {
                                                    callback(null, cityPackge(city, ids, result));
                                                    connection.release();
                                                });
                                            });
                                        }
                                    ], function (err, city) {
                                        count++;
                                        locations.push(city);
                                        if (count == result.length) {
                                            callback(null, locations);
                                        }
                                    });
                                });
                            }
                        ], function (err, locations) {
                            data.result = [
                                {
                                    "locations": locations
                                }
                            ];
                            connection.release();
                            callback(data)
                        }
                    );
                }
            });
        }
    )
    ;
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
    //if (ids.length > 0) {
    //    var pro = map[ids[0]];
    //    if (pro && pro.id != 1) {
    //        AdministrativeArea = {
    //            "ID": pro.id,
    //            "LocalizedName": pro.name,
    //            "EnglishName": pro.eName,
    //            "Level": 1,
    //            "LocalizedType": "省",
    //            "EnglishType": "Province",
    //            "CountryID": "CN"
    //        };
    //    }
    //}
    if (ids.length <= 1) {
        AdministrativeArea = {
            "ID": city.id,
            "LocalizedName": city.name,
            "EnglishName": city.eName,
            "Level": 1,
            "LocalizedType": "省",
            "EnglishType": "Province",
            "CountryID": "CN"
        };
        SupplementalAdminAreas = [
            {
                "Level": 2,
                "LocalizedName": city.name,
                "EnglishName": city.eName
            }
        ];
    }
    if (ids.length > 1) {
        var pro = map[ids[1]];
        if (pro) {
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
    }

    if (ids.length > 2) {
        var ci = map[ids[2]];
        if (ci) {
            SupplementalAdminAreas = [
                {
                    "Level": 2,
                    "LocalizedName": ci.name,
                    "EnglishName": ci.eName
                }
            ];
        }

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
