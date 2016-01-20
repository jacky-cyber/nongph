(function(){
var module = com.felix.nsf.ModuleContainer.addModule("eni", "task");
module.addDependy("eni", "message");
module.addDependy("eni", "caseManage");

var package = module.getModulePackage();
package.addClass("CaseForHandleToolBar");
package.addClass("MsgForHandleToolBar");
package.addClass("RefundMessageWindow");
package.addClass("TaskCommentForm");

package.addClass("TaskGridPanel");
package.addClass("TaskHistoryGridPanel");

package.addClass("TaskHistoryGridPanel");
package.addClass("TaskHistoryWindow");

package.addClass("TaskMessageViewWindow");
package.addClass("TaskNewCaseGridPanel");
package.addClass("TaskNewMessageGridPanel");
package.addClass("TaskNewInfoTabPanel");

module.load();
})();

