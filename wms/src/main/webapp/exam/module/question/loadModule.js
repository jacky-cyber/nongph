(function(){
var module = com.felix.nsf.ModuleContainer.addModule("exam", "question");
module.addDependy("exam", "questionView");
var package = module.getModulePackage();
package.addClass("QuestionGridPanel");
package.addClass("QuestionSearchFormPanel");
package.addClass("QuestionSearchWindow");
package.addClass("QuestionToolbar");
package.addClass("QuestionFormPanel");
package.addClass("QuestionChoiceToolbar");
package.addClass("QuestionChoiceGridPanel");
package.addClass("QuestionWindow");
module.load();
})();
