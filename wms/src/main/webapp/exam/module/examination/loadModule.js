(function(){
var module = com.felix.nsf.ModuleContainer.addModule("exam", "examination");
module.addDependy("exam", "questionSelect");
module.addDependy("exam", "questionView");
module.addDependy("exam", "questionType");
module.addDependy("exam", "positionSelect");
module.addDependy("exam", "examinationView");
var package = module.getModulePackage();
package.addClass("ExaminationSearchFormPanel");
package.addClass("ExaminationSearchWindow");
package.addClass("ExaminationToolbar");
package.addClass("ExaminationGridPanel");

package.addClass("CatalogQuestionToolbar");
package.addClass("CatalogQuestionGridPanel");
package.addClass("CatalogFormPanel");
package.addClass("CatalogPanel");
package.addClass("ExaminationFormPanel");
package.addClass("ExaminationWindow");

module.load();
})();
