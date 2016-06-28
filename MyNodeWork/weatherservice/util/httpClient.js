var httpClient = {};
module.exports.httpClient = httpClient;

var http = require('http');

httpClient.doGet = function (hostname, path, port, headers, callback) {
    if (!hostname) {
        callback('', 404);
    }
    if (!path) {
        path = '/';
    }
    if (!port) {
        port = 80;
    }
    var options = {
        hostname: hostname,
        port: port,
        path: path,
        method: 'GET',
        headers: headers
    };

    var req = http.request(options, function (res) {
        var responseData = '';
        res.on('data', function (chunk) {
            responseData += chunk;
        });
        res.on('end', function () {
            callback(responseData, res.statusCode, res.headers);
        });
    }).on('error', function (e) {
        console.error(e);
        callback('', 500);
    });
    req.end();
};
