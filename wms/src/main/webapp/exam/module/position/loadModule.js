(function(){
var module = com.felix.nsf.ModuleContainer.addModule("exam", "position");

var package = module.getModulePackage();
package.addClass("PositionDataStore");
package.addClass("PositionGridPanel");
module.load();
})();
