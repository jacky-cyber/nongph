(function(){
var module = com.felix.nsf.ModuleContainer.addModule("form", "render");

var package = module.getModulePackage();
package.addClass("TextRenderer");
package.addClass("TextAreaRenderer");
package.addClass("NumberRenderer");

package.addClass("HiddenRenderer");
package.addClass("DateRenderer");
package.addClass("ComboBoxRenderer");
package.addClass("CheckBoxRenderer");

package.addClass("Renderer");

package.addClass("FormRenderer");

module.load();
})();
