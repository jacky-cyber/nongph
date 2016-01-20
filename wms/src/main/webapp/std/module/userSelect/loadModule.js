(function(){
var module = com.felix.nsf.ModuleContainer.addModule("std", "userSelect");

var package = module.getModulePackage();
package.addClass("UserSelectFormPanel");
package.addClass("UserSelectToolbar");
package.addClass("UserSelectGridPanel");
package.addClass("UserSelectWindow");

module.load();
})();
