package cn.globalph.openadmin.web.form.entity;



public class DefaultEntityFormActions {
    
    public static final EntityFormAction SAVE = new EntityFormAction(EntityFormAction.SAVE)
        .withButtonType("submit")
        .withButtonClass("submit-button")
        .withDisplayText("Save");

    public static final EntityFormAction DELETE = new EntityFormAction(EntityFormAction.DELETE)
        .withButtonClass("delete-button alert")
        .withDisplayText("Delete");
    
    public static final EntityFormAction PREVIEW = new EntityFormAction(EntityFormAction.PREVIEW)
        .withButtonClass("preview-button")
        .withDisplayText("Preview");

}
