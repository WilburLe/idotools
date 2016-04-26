var mysql = require('mysql');
var $conf = require('../conf/db');
var $util = require('../util/util');
var $sql = require('./weatherSqlMapping');
var async = require('async');
var pool = mysql.createPool($util.extend({}, $conf.mysql));

module.exports.hots = function (callback) {
    var result = {
        "status": "OK",
        "hots_list": [{
            "city_id": "101020100",
            "city": [{"language": "zh_CN", "text": "上海市"}, {"language": "en_US", "text": "Shanghai"}],
            "province": [{"language": "zh_CN", "text": "上海市"}, {"language": "en_US", "text": "Shanghai"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101280101",
            "city": [{"language": "zh_CN", "text": "广州市"}, {"language": "en_US", "text": "Guangzhou"}],
            "province": [{"language": "zh_CN", "text": "广东省"}, {"language": "en_US", "text": "Guangdong"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101010100",
            "city": [{"language": "zh_CN", "text": "北京市"}, {"language": "en_US", "text": "Beijing"}],
            "province": [{"language": "zh_CN", "text": "北京市"}, {"language": "en_US", "text": "Beijing"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101040100",
            "city": [{"language": "zh_CN", "text": "重庆市"}, {"language": "en_US", "text": "Chongqing"}],
            "province": [{"language": "zh_CN", "text": "重庆市"}, {"language": "en_US", "text": "Chongqing"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101280601",
            "city": [{"language": "zh_CN", "text": "深圳市"}, {"language": "en_US", "text": "Shenzhen"}],
            "province": [{"language": "zh_CN", "text": "广东省"}, {"language": "en_US", "text": "Guangdong"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101270101",
            "city": [{"language": "zh_CN", "text": "成都市"}, {"language": "en_US", "text": "Chengdu"}],
            "province": [{"language": "zh_CN", "text": "四川省"}, {"language": "en_US", "text": "Sichuan"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101190401",
            "city": [{"language": "zh_CN", "text": "苏州市"}, {"language": "en_US", "text": "Suzhou"}],
            "province": [{"language": "zh_CN", "text": "江苏省"}, {"language": "en_US", "text": "Jiangsu"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101200101",
            "city": [{"language": "zh_CN", "text": "武汉市"}, {"language": "en_US", "text": "Wuhan"}],
            "province": [{"language": "zh_CN", "text": "湖北省"}, {"language": "en_US", "text": "Hubei"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101180101",
            "city": [{"language": "zh_CN", "text": "郑州市"}, {"language": "en_US", "text": "Zhengzhou"}],
            "province": [{"language": "zh_CN", "text": "河南省"}, {"language": "en_US", "text": "Henan"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101210101",
            "city": [{"language": "zh_CN", "text": "杭州市"}, {"language": "en_US", "text": "Hangzhou"}],
            "province": [{"language": "zh_CN", "text": "浙江省"}, {"language": "en_US", "text": "Zhejiang"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101281601",
            "city": [{"language": "zh_CN", "text": "东莞市"}, {"language": "en_US", "text": "Dongguan"}],
            "province": [{"language": "zh_CN", "text": "广东省"}, {"language": "en_US", "text": "Guangdong"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101280800",
            "city": [{"language": "zh_CN", "text": "佛山市"}, {"language": "en_US", "text": "Foshan"}],
            "province": [{"language": "zh_CN", "text": "广东省"}, {"language": "en_US", "text": "Guangdong"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101110101",
            "city": [{"language": "zh_CN", "text": "西安市"}, {"language": "en_US", "text": "Xi'an"}],
            "province": [{"language": "zh_CN", "text": "陕西省"}, {"language": "en_US", "text": "Shaanxi"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101210701",
            "city": [{"language": "zh_CN", "text": "温州市"}, {"language": "en_US", "text": "Wenzhou"}],
            "province": [{"language": "zh_CN", "text": "浙江省"}, {"language": "en_US", "text": "Zhejiang"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101030100",
            "city": [{"language": "zh_CN", "text": "天津市"}, {"language": "en_US", "text": "Tianjin"}],
            "province": [{"language": "zh_CN", "text": "天津市"}, {"language": "en_US", "text": "Tianjin"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101250101",
            "city": [{"language": "zh_CN", "text": "长沙市"}, {"language": "en_US", "text": "Changsha"}],
            "province": [{"language": "zh_CN", "text": "湖南省"}, {"language": "en_US", "text": "Hunan"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101230101",
            "city": [{"language": "zh_CN", "text": "福州市"}, {"language": "en_US", "text": "Fuzhou"}],
            "province": [{"language": "zh_CN", "text": "福建省"}, {"language": "en_US", "text": "Fujian"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101190101",
            "city": [{"language": "zh_CN", "text": "南京市"}, {"language": "en_US", "text": "Nanjing"}],
            "province": [{"language": "zh_CN", "text": "江苏省"}, {"language": "en_US", "text": "Jiangsu"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101210401",
            "city": [{"language": "zh_CN", "text": "宁波市"}, {"language": "en_US", "text": "Ningbo"}],
            "province": [{"language": "zh_CN", "text": "浙江省"}, {"language": "en_US", "text": "Zhejiang"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101290101",
            "city": [{"language": "zh_CN", "text": "昆明市"}, {"language": "en_US", "text": "Kunming"}],
            "province": [{"language": "zh_CN", "text": "云南省"}, {"language": "en_US", "text": "Yunnan"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101260101",
            "city": [{"language": "zh_CN", "text": "贵阳市"}, {"language": "en_US", "text": "Guiyang"}],
            "province": [{"language": "zh_CN", "text": "贵州省"}, {"language": "en_US", "text": "Guizhou"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101190201",
            "city": [{"language": "zh_CN", "text": "无锡市"}, {"language": "en_US", "text": "Wuxi"}],
            "province": [{"language": "zh_CN", "text": "江苏省"}, {"language": "en_US", "text": "Jiangsu"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101210901",
            "city": [{"language": "zh_CN", "text": "金华市"}, {"language": "en_US", "text": "Jinhua"}],
            "province": [{"language": "zh_CN", "text": "浙江省"}, {"language": "en_US", "text": "Zhejiang"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101190501",
            "city": [{"language": "zh_CN", "text": "南通市"}, {"language": "en_US", "text": "Nantong"}],
            "province": [{"language": "zh_CN", "text": "江苏省"}, {"language": "en_US", "text": "Jiangsu"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101280301",
            "city": [{"language": "zh_CN", "text": "惠州市"}, {"language": "en_US", "text": "Huizhou"}],
            "province": [{"language": "zh_CN", "text": "广东省"}, {"language": "en_US", "text": "Guangdong"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101230501",
            "city": [{"language": "zh_CN", "text": "泉州市"}, {"language": "en_US", "text": "Quanzhou"}],
            "province": [{"language": "zh_CN", "text": "福建省"}, {"language": "en_US", "text": "Fujian"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101281101",
            "city": [{"language": "zh_CN", "text": "江门市"}, {"language": "en_US", "text": "Jiangmen"}],
            "province": [{"language": "zh_CN", "text": "广东省"}, {"language": "en_US", "text": "Guangdong"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101120201",
            "city": [{"language": "zh_CN", "text": "青岛市"}, {"language": "en_US", "text": "Qingdao"}],
            "province": [{"language": "zh_CN", "text": "山东省"}, {"language": "en_US", "text": "Shandong"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101070101",
            "city": [{"language": "zh_CN", "text": "沈阳市"}, {"language": "en_US", "text": "Shenyang"}],
            "province": [{"language": "zh_CN", "text": "辽宁省"}, {"language": "en_US", "text": "Liaoning"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Shanghai", "Code": "CST"}
        }, {
            "city_id": "101050101",
            "city": [{"language": "zh_CN", "text": "哈尔滨市"}, {"language": "en_US", "text": "Harbin"}],
            "province": [{"language": "zh_CN", "text": "黑龙江省"}, {"language": "en_US", "text": "Heilongjiang"}],
            "country": [{"language": "zh_CN", "text": "中国"}, {"language": "en_US", "text": "China"}],
            "region": [{"language": "zh_CN", "text": "亚洲"}, {"language": "en_US", "text": "Asia"}],
            "TimeZone": {"GmtOffset": 8, "Name": "Asia/Harbin", "Code": "CST"}
        }]
    }
    callback(result);
};
