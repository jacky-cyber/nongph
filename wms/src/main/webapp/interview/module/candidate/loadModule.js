(function(){
var module = com.felix.nsf.ModuleContainer.addModule("interview", "candidate");
module.addDependy("exam", "positionSelect");
module.addDependy("interview", "interview");
var package = module.getModulePackage();

package.addClass("CandidateToolbar");
package.addClass("CandidateGridPanel");
package.addClass("CandidateSearchFormPanel");
package.addClass("CandidateSearchWindow");
package.addClass("CandidateFormPanel");
package.addClass("CandidateWindow");
package.addClass("CandidateViewWindow");

module.load();
})();
