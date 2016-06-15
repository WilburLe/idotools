var client = require('../model/kafkaClient.js').kafkaClient;

var kafka = require('kafka-node'),
    Producer = kafka.Producer,
    KeyedMessage = kafka.KeyedMessage,
//client = new kafka.Client(),
    producer = new Producer(client, {
        //metadata.broker.list:"loclhost:9092,loclhost:1092",
        // Configuration for when to consider a message as acknowledged, default 1
        requireAcks: 1,
        // The amount of time in milliseconds to wait for all acks before considered, default 100ms
        ackTimeoutMs: 100,
        // Partitioner type (default = 0, random = 1, cyclic = 2, keyed = 3), default 0
        partitionerType: 3
    }),
    km = new KeyedMessage('key', 'message');
//attributes
//0: No compressionconfig/server.properties &

//1: Compress using GZip
//2: Compress using snappy
var msg = [
    {topic: 't1p1', messages: 'hi-tip000', partition: 0, attributes: 2},
    {topic: 't1p1', messages: 'hi-tip11111', partition: 1, attributes: 2},
    {topic: 't1p1', messages: 'hi-tip2222222', partition: 2},
    {topic: 't3', messages: 'hi-t3 p0000000000000', partition: 0},
    {topic: 't3', messages: 'hi-t3 p11111111111', partition: 1},
    {topic: 't3', messages: 'hi-t3 p222222222', partition: 2},
    {topic: 't3', messages: 'hi-t3 33333333333', partition: 3},
    {topic: 't3', messages: 'hi-t3 p222222222', partition: 2},
    // {topic: 't1', messages: ['hello 1', 'world 2', km], partition: 0},
    {topic: 'test', messages: '你还哦啊',partition: 0},
];

producer.on('ready', function () {
    // Create topics sync
    //producer.createTopics(['t1p1', 't', 't1', 't2'], false, function (err, data) {
    //    if (err) {
    //        console.log("producer createTopics err > " + err);
    //    } else {
    //        console.log("producer createTopics > " + data);
    //    }
    //
    //
    //});
    producer.send(msg, function (err, data) {
        if (err) {
            console.log("producer send err > " + JSON.stringify(err));
        } else {
            console.log("producer send > " + JSON.stringify(data));
        }

        //process.exit();
    });
});


producer.on('error', function (err) {
    console.log("producer error > " + JSON.stringify(err));
})


//consumer.addTopics([{topic: 't1', offset: 10}], function (err, added) {
//}, true);







