var AESUtil = {};
exports.AESUtil = AESUtil;

var crypto = require('crypto');

var ALGORITHM = 'aes-128-ecb';
var PASSWORD = '5uCItjTAohgFxo9a';
var ENCODING = 'utf8';
var OUTPUT_ENCODING = 'hex';

/**
 * do AES encrypt
 *
 * @param plaintext {string}
 * @returns {string}
 */
AESUtil.encrypt = function(plaintext) {
    var cipher = crypto.createCipher(ALGORITHM, PASSWORD);
    return cipher.update(plaintext, ENCODING, OUTPUT_ENCODING) + cipher.final(OUTPUT_ENCODING);
};

/**
 * do AES decrypt
 *
 * @param cryptogram {string}
 * @returns {string}
 */
AESUtil.decrypt = function(cryptogram) {
    var decipher = crypto.createDecipher(ALGORITHM, PASSWORD);
    return decipher.update(cryptogram, OUTPUT_ENCODING, ENCODING) + decipher.final(ENCODING);
};
