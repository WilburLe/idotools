var $http = require('../util/httpClient').httpClient;



function test_latlng() {
    var url = "127.0.0.1:3008/searchLocationByCoordinate";
    $http.doGet(url, path, null, null, function (result, status, headers) {
        var r = JSON.parse(result);
        var address = r.result.addressComponent;
        if (!address || address.adcode == 0) {
            $util.gzipAesWrite(req, res, JSON.stringify({err: "no find", msg: address}));
        } else {

        }
    });


}