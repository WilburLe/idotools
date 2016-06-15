var client = require('../model/kafkaClient.js').kafkaClient;
var kafka = require('kafka-node'),
    Consumer = kafka.Consumer,
//client = new kafka.Client(),
    consumer = new Consumer(
        client,
        [
            // {topic: 't', partition: 0, offset: 0},
            // {topic: 't1', partition: 0},
            {topic: 't3', partition: 0},
            {topic: 't3', partition: 1},
            {topic: 't3', partition: 2},
            {topic: 't3', partition: 3},
            {topic: 't1p1', partition: 0},
            {topic: 't1p1', partition: 1},
            {topic: 't1p1', partition: 2},
            {topic: 'test'}
        ],
        {
            groupId: 'kafka-node-group',//consumer group id, default `kafka-node-group`
            // Auto commit config
            autoCommit: true,
            autoCommitIntervalMs: 5000,
            // The max wait time is the maximum amount of time in milliseconds to block waiting if insufficient data is available at the time the request is issued, default 100ms
            fetchMaxWaitMs: 100,
            // This is the minimum number of bytes of messages that must be available to give a response, default 1 byte
            fetchMinBytes: 1,
            // The maximum bytes to include in the message set for this partition. This helps bound the size of the response.
            fetchMaxBytes: 1024 * 10,
            // If set true, consumer will fetch message from the given offset in the payloads
            fromOffset: false,
            // If set to 'buffer', values will be returned as raw buffer objects.
            encoding: 'utf8'
        }
    );

//consumer.addTopics(['t', 't1'], function (err, added) {
//    console.log("consumer addTopics > " + JSON.stringify(added));
//
//    //consumer.commit(function (err, data) {
//    //    console.log("consumer commit > " + data);
//    //});
//});

consumer.on('message', function (message) {
    console.log("consumer message > " + JSON.stringify(message));
});
consumer.on('offsetOutOfRange', function (topic) {
    console.log("------------- offsetOutOfRange ------------");
    topic.maxNum = 2;
    offset.fetch([topic], function (err, offsets) {
        var min = Math.min.apply(null, offsets[topic.topic][topic.partition]);
        consumer.setOffset(topic.topic, topic.partition, min);
    });
});
consumer.on('error', function (err) {
    console.log("consumer error > " + JSON.stringify(err));
})
//consumer.addTopics([{topic: 't2', partition:0}], function (err, added) {
//    if(err) {
//        console.log("consumer addTopics err > "+JSON.stringify(err));
//        return;
//    }
//    console.log("consumer addTopics > "+JSON.stringify(added));
//}, true);







