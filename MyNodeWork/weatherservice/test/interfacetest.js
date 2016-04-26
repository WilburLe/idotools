


var $http = require('../util/httpClient').httpClient;
var host = 789;
var wea = function() {

    $http.doGet(url, path, null, null, function (result, status, headers) {
        var r = JSON.parse(result);
        var addressComponent = r.result.addressComponent;
        cityByGeo.geo(addressComponent, function (data) {
            $util.gzipAesWrite(req, res, data);
        });
    });
}