(function(){
var userModule = com.felix.nsf.ModuleContainer.addModule("std", "user");
userModule.addDependy("std", "role");
var package = userModule.getModulePackage();
package.addClass("UserGridPanel");
package.addClass("UserFormPanel");
package.addClass("UserWindow");
package.addClass("UserToolBar");
package.addClass("UserSearchFormPanel");
package.addClass("UserSearchWindow");
userModule.load();
})();

