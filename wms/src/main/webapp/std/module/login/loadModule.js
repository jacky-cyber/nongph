(function(){
var loginModule = com.felix.nsf.ModuleContainer.addModule("std", "login");
loginModule.addDependy("std", "changePassword");
var package = loginModule.getModulePackage();
package.addClass("LoginFormPanel");
package.addClass("LoginWindow");
package.addClass("PasswordForm");
package.addClass("InformationForm");
package.addClass("ProfileInfoTabPanel");
package.addClass("ProfileInfoWindow");
loginModule.load();
})();

