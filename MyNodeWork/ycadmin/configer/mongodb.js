var mongoose = require('mongoose');
mongoose.connect(
    //'mongodb://localhost:27017/toolboxstat'
    //,{user:'wallpaper',pass:'wallpaper@#$911'}
    'mongodb://mongodb1.chinacloudapp.cn:30004/statistics'
    , {user: 'statistician', pass: 'stAtistics!sCaledata0311'}
);
exports.mongoose = mongoose;