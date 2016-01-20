(function(){
var module = com.felix.nsf.ModuleContainer.addModule("exam", "positionSelect");
module.addDependy("exam", "position");

var package = module.getModulePackage();
package.addClass("SelectPositionFormPanel");
package.addClass("SelectPositionWindow");

module.load();
})();
