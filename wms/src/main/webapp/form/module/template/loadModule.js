(function(){
var module = com.felix.nsf.ModuleContainer.addModule("form", "template");
module.addDependy("form", "render");

var package = module.getModulePackage();
package.addClass("FormTemplateSearchFormPanel");
package.addClass("FormTemplateSearchWindow");
package.addClass("FormTemplateToolbar");
package.addClass("FormTemplateGridPanel");

package.addClass("FormTemplateWindow");
package.addClass("FormTemplateFormPanel");
package.addClass("FormTemplateFieldGridPanel");
package.addClass("FormTemplateFieldToolbar");

package.addClass("FormTemplateFieldWindow");
package.addClass("FormTemplateFieldFormPanel");
package.addClass("FormTemplateFieldOptionGridPanel");
package.addClass("FormTemplateFieldOptionToolbar");

module.load();
})();
