var AESUtil = require('../util/AESUtil.js').AESUtil;
var zlib = require('zlib');

module.exports = {
    extend: function (target, source, flag) {
        for (var key in source) {
            if (source.hasOwnProperty(key))
                flag ?
                    (target[key] = source[key]) :
                    (target[key] === void 0 && (target[key] = source[key]));
        }
        return target;
    },
    jsonWrite: function (req, res, result) {
        if (typeof result === 'undefined') {
            var data = {};
            data.status = 'ERROR';
            data.result = {};
            res.json(data);
        } else {
            res.json(result);
        }
    },
    gzipAesWrite: function (req, res, body) {
        if (!req || !res) {
            return;
        }
        //var body = JSON.stringify(result);
        var acceptEncoding = (req.headers && req.headers['accept-encoding']) || '';
        var encryptedBody = true ? AESUtil.encrypt(body) : body;
        if (acceptEncoding.indexOf('gzip') >= 0) {
            zlib.gzip(encryptedBody, function (err, data) {
                if (!err) {
                    res.writeHead(200, {
                        'Content-Length': data.length,
                        'Content-Type': 'application/json;charset=utf-8',
                        'Content-Encoding': 'gzip',
                        'Connection': 'close'
                    });
                    res.write(data);
                } else {
                    res.write(encryptedBody);
                    logger.error(err);
                }
                res.end();
            });
        } else {
            res.writeHead(200, {
                'Content-Length': body.length,
                'Content-Type': 'application/json;charset=utf-8',
                'Connection': 'close'
            });
            res.write(encryptedBody);
            res.end();
        } // end of acceptEncoding.indexOf('gzip')
    },
    getRequestBody: function (req, callback) {
        var reqBody = '';
        req.addListener('data', function (chunk) {
            reqBody += chunk;
        });
        req.addListener('end', function () {
            callback(reqBody);
        });
    }
}
