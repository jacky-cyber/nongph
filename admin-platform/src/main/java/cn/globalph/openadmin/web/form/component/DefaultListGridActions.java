package cn.globalph.openadmin.web.form.component;


/**
 * Convenience class to deal with the common actions on most list grids. If you are using one of these static variables
 * as a base for a new action, <b>do not modify them directly</b> or your changes will persist for the remainder of the
 * application. Instead, use the clone() method from {@link ListGridAction}. For instance:
 * 
 * <pre>
 *  {@code
 *  ListGridAction newAddAction = DefaultListGridActions.ADD.clone().withDisplayText("New Add Text");
 *  }
 * </pre>
 *
 * @author Andre Azzolini
 * @see {@link ListGridAction#clone()}
 */
public class DefaultListGridActions {
    
    // Actions for the main list grid toolbar
    public static final ListGridAction ADD = new ListGridAction(ListGridAction.ADD)
        .withButtonClass("sub-list-grid-add")
        .withUrlPostfix("/add")
        .withIconClass("icon-plus")
        .withDisplayText("Add");
    
    public static final ListGridAction REORDER = new ListGridAction(ListGridAction.REORDER)
        .withButtonClass("sub-list-grid-reorder")
        .withUrlPostfix("/update")
        .withIconClass("icon-move")
        .withDisplayText("Reorder");

    // Actions for row-level
    public static final ListGridAction REMOVE = new ListGridAction(ListGridAction.REMOVE)
        .withButtonClass("sub-list-grid-remove")
        .withUrlPostfix("/delete")
        .withIconClass("icon-remove")
        .withDisplayText("Delete");
    
    public static final ListGridAction UPDATE = new ListGridAction(ListGridAction.UPDATE)
        .withButtonClass("sub-list-grid-update")
        .withIconClass("icon-pencil")
        .withDisplayText("Edit");

    public static final ListGridAction VIEW = new ListGridAction(ListGridAction.VIEW)
        .withButtonClass("sub-list-grid-view")
        .withIconClass("icon-book")
        .withDisplayText("View")
        .withUrlPostfix("/view")
        .withForListGridReadOnly(true);
    
}
