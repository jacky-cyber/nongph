(function(){
var module = com.felix.nsf.ModuleContainer.addModule("interview", "interviewer");
module.addDependy("exam", "examinationView");
module.addDependy("form", "render");

var package = module.getModulePackage();
package.addClass("PendingRoundToolbar");
package.addClass("PendingRoundGridPanel");

package.addClass("ExamGridPanel");
package.addClass("ViewGridPanel");
package.addClass("InterviewFormPanel");
package.addClass("InterviewInfoWindow");

package.addClass("SubmitFormWindow");
module.load();
})();
