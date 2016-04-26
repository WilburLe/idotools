// dao/userSqlMapping.js
// CRUD SQL语句
var weather = {
    name: 'select * from geo_weather_city where (name like ? or eName like ?)',
    weaid: 'select * from geo_weather_city where weatherId=?',
    geoid: 'select * from geo_weather_city where geoCityId=?',
    name2: 'SELECT c1.* FROM ' +
            'geo_weather_city c1 join geo_weather_city c2 ' +
            'on (c1.parentId=c2.id) ' +
            'where c1.name like ? and c2.name like ?',
    ids: 'select * from geo_weather_city where id in ',
    ids_city: 'select * from city where id in '
};

module.exports = weather;