package cn.globalph.common.admin.domain;

/**
 * When viewing entities that implement this interface in the admin, the {@link #getMainEntityName()} method will be
 * invoked to determine the title of the entity to be rendered.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface AdminMainEntity {

    public static final String MAIN_ENTITY_NAME_PROPERTY = "__adminMainEntity";

    /**
     * @return the display name of this entity for the admin screen
     */
    public String getMainEntityName();

}
