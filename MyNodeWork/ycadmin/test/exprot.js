

var getName = function(name) {
    this.name = name;
    console.log("My name is "+this.name);
    this.writeName = function() {
        console.log("My name is "+this.name);
    }
}


exports.name1 = getName;
module.exports.name2 = getName;