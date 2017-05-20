var UPushPlugin = function () {}

UPushPlugin.prototype.isPlatformIOS = function () {
  var isPlatformIOS = (device.platform == 'iPhone' ||
                       device.platform == 'iPad' ||
                       device.platform == 'iPod touch' ||
                       device.platform == 'iOS')
  return isPlatformIOS
}

UPushPlugin.prototype.init = function () {
  if(this.isPlatformIOS()){
      //不做任何操作
  }else{
    this.callNative('init', [], null)
  }

}

if(!window.plugins){
  window.plugins = {}
}
if(!window.plugins.uPushPlugin){
  window.plugins.uPushPlugin = new UPushPlugin()
}
module.exports = new UPushPlugin()
