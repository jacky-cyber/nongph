(function(){
var module = com.felix.nsf.ModuleContainer.addModule("interview", "interviewView");
module.addDependy("form", "render");
var package = module.getModulePackage();
package.addClass("ExamFormPanel");
package.addClass("ExamToolbar");
package.addClass("ExamGridPanel");
package.addClass("ExamRoundPanel");

package.addClass("ViewFormPanel");
package.addClass("ViewToolbar");
package.addClass("ViewGridPanel");
package.addClass("ViewRoundPanel");

package.addClass("InterviewFormPanel");
package.addClass("InterviewWindow");

module.load();
})();
