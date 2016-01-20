(function(){
var module = com.felix.nsf.ModuleContainer.addModule("interview", "interview");
module.addDependy("exam", "position");
module.addDependy("exam", "examinationSelect");
module.addDependy("std", "userSelect");
module.addDependy("interview", "interviewView");

var package = module.getModulePackage();
package.addClass("InterviewToolbar");
package.addClass("InterviewGridPanel");
package.addClass("InterviewSearchFormPanel");
package.addClass("InterviewSearchWindow");
package.addClass("InterviewFormPanel");
package.addClass("InterviewWindow");
package.addClass("ExaminationToolbar");
package.addClass("ExaminationGridPanel");

package.addClass("InterviewerToolbar");
package.addClass("InterviewerGridPanel");
package.addClass("DateTimeFormPanel");
package.addClass("RoundPanel");
package.addClass("ExamRoundPanel");
package.addClass("ViewRoundPanel");

module.load();
})();
