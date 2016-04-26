var kafka = require('kafka-node');
var client = new kafka.Client(
    "localhost:2181/",
    "clientId" + process.pid,
    {
        sessionTimeout: 30000,  //Session timeout in milliseconds, defaults to 30 seconds.
        spinDelay: 1000,    //The delay (in milliseconds) between each connection attempts.
        retries: 10  //The number of retry attempts for connection loss exception.
    },
    {
        noAckBatchSize: null,
        noAckBatchAge: null
    }
);
exports.kafkaClient = client;