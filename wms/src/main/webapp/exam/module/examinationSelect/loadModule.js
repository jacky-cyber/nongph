(function(){
var module = com.felix.nsf.ModuleContainer.addModule("exam", "examinationSelect");
module.addDependy("exam", "position");
module.addDependy("exam", "examinationView");

var package = module.getModulePackage();
package.addClass("ExaminationSelectFormPanel");
package.addClass("ExaminationSelectToolbar");
package.addClass("ExaminationSelectGridPanel");
package.addClass("ExaminationSelectWindow");

module.load();
})();
