(function(){
var module = com.felix.nsf.ModuleContainer.addModule("exam", "questionType");

var package = module.getModulePackage();
package.addClass("QuestionTypeGridPanel");
package.addClass("QuestionTypeStore");
module.load();
})();
