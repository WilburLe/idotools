var $weathercode = require('./weatherCode');
var $http = require('../util/httpClient').httpClient;
var async = require('async');

module.exports.weather = function (cityid, callback) {
    async.waterfall([
        function (callback) {
            var url = "apis.baidu.com";
            var path = "/apistore/weatherservice/weather?cityid=" + cityid;
            var headers = {
                apikey: "04c36c547c9fa053d178d880a47b343e"
            };
            $http.doGet(url, path, null, headers, function (result, status, headers) {
                callback(null, result);
            });
        },
        function (todaywea, callback) {
            var url = "apis.baidu.com";
            var path = "/apistore/weatherservice/recentweathers?cityid=" + cityid;
            var headers = {
                apikey: "04c36c547c9fa053d178d880a47b343e"
            };
            $http.doGet(url, path, null, headers, function (result, status, headers) {
                callback(null, todaywea, result);
            });
        }
    ], function (err, todaywea, daylist) {
        //$util.gzipAesWrite(req,res, JSON.parse(result));
        callback(weatherPackge(JSON.parse(todaywea), JSON.parse(daylist)));
    });

};

var weatherPackge = function (today, daylist) {
    if (today.errNum != 0) {
        return {err: 'no weather'};
    }
    var c = today.retData;
    var weacode = $weathercode.getweather(c.weather);
    var pubTime = c.time.substring(0, 2);
    var wea = null;
    if (pubTime > 18 && weacode.n) {
        wea = weacode.n;
    } else {
        wea = weacode.d;
    }
    var data = {};
    data.status = 'OK';
    data.weather = [
        {
            "city_id": c.citycode,
            //"last_update": new Date('20' + c.date + " " + c.time),
            //"Expires":"Mon Apr 18 08:04:00 UTC 2016",
            "last_update": "2016-04-18T13:21:00+08:00",
            "Expires": new Date(),
            "now": {
                "text": [
                    {
                        "language": "zh_CN",
                        "text": c.weather
                    },
                    {
                        "language": "en_US",
                        "text": wea.en //需要比对替换
                    }
                ],
                "code": wea.code + "",    //需要比对替换
                "isDayTime": true,
                "temperature": c.temp.replace('℃', ''),
                "feels_like": c.temp.replace('℃', ''),
                "wind_direction": [
                    {
                        "language": "zh_CN",
                        "text": c.WD
                    },
                    {
                        "language": "en_US",
                        "text": $weathercode.getwinddir(c.WD)
                    }
                ],
                "wind_speed": $weathercode.getwindlev(c.WS),    //  c.WS: "微风(<10m/h)", //风力
                "wind_scale": " ",
                "humidity": " ", //湿度
                "visibility": " ", //能见度
                "pressure": " ",   //大气压
                //"wind_scale": "3",
                //"humidity": "27",
                //"visibility": "22.5",
                //"pressure": "1013.3",
                "pressure_rising": [
                    {
                        "language": "zh_CN",
                        "text": "稳定"
                    },
                    {
                        "language": "en_US",
                        "text": "Steady"
                    }
                ],
                "mobileLink": 'http://m.weather.com.cn/weather1d/' + c.cityid + '.shtml',
                "uv": [
                    {
                        "language": "zh_CN",
                        "text": " "
                    },
                    {
                        "language": "en_US",
                        "text": " "
                    }
                ],
                "air_quality": {
                    "aqi": "",
                    "pm25": "",
                    "quality": "",
                    "qualityCode": "",
                    "last_update": ""
                }
            },
            "today": {
                "sunrise": c.sunrise,
                "sunset": c.sunset
            },
            "future": future(daylist),
            "hourly": []

        }
    ]
    return data;
}

var future = function (daylist) {
    var future = [];
    if (daylist.errNum > 0) {
        return future;
    }
    var forecast = daylist.retData.forecast;

    var today = daylist.retData.today;

    var todaycode = $weathercode.getweather(today.type);
    var weat = todaycode.d;
    var weant = todaycode.n ? todaycode.n : weat;
    future.push({
        "date": today.date,
        "day": today.week,
        "text": [
            {
                "language": "zh_CN",
                "text": today.type
            },
            {
                "language": "en_US",
                "text": weat.en
            }
        ],
        "code1": weat.code,
        "code2": weant.code,
        "high": today.hightemp.replace('℃', ''),
        "low": today.lowtemp.replace('℃', ''),
        "cop": " ",
        "wind": $weathercode.getwindlev(today.fengli), //data.fengli = "微风级",
        "mobileLink": 'http://m.weather.com.cn/weather1d/' + daylist.retData.cityid + '.shtml',
    });
    for (var m in forecast) {
        var data = forecast[m];
        var weacode = $weathercode.getweather(data.type);
        var wea = weacode.d;
        var wean = weacode.n ? weacode.n : wea;
        var ft = {
            "date": data.date,
            "day": data.week,
            "text": [
                {
                    "language": "zh_CN",
                    "text": data.type
                },
                {
                    "language": "en_US",
                    "text": wea.en
                }
            ],
            "code1": wea.code,
            "code2": wean.code,
            "high": data.hightemp.replace('℃', ''),
            "low": data.lowtemp.replace('℃', ''),
            "cop": " ",
            "wind": $weathercode.getwindlev(data.fengli), //data.fengli = "微风级",
            "mobileLink": 'http://m.weather.com.cn/weather1d/' + daylist.retData.cityid + '.shtml',
        };
        future.push(ft);
    }
    future.push({
        "date": today.date,
        "day": today.week,
        "text": [
            {
                "language": "zh_CN",
                "text": today.type
            },
            {
                "language": "en_US",
                "text": weat.en
            }
        ],
        "code1": weat.code,
        "code2": weant.code,
        "high": today.hightemp.replace('℃', ''),
        "low": today.lowtemp.replace('℃', ''),
        "cop": " ",
        "wind": $weathercode.getwindlev(today.fengli), //data.fengli = "微风级",
        "mobileLink": 'http://m.weather.com.cn/weather1d/' + daylist.retData.cityid + '.shtml',
    });
    return future;
}
