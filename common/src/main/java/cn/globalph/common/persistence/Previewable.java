package cn.globalph.common.persistence;

/**
 * Interface that exposes properties useful for determining if an entity is intended for preview only,
 * as opposed to standard production entities
 *
 * @author Jeff Fischer
 */
public interface Previewable {

    /**
     * Whether or not this entity is considered a preview entity for testing. You can utilize this field
     * to drive unique behavior for preview entities in your own implementation code. Additionally, this
     * field is utilized by the Enterprise version.
     *
     * @return whether or not this is a test entity
     */
    Boolean getPreview();

    /**
     * Whether or not this entity is considered a preview entity for testing. You can utilize this field
     * to drive unique behavior for preview entities in your own implementation code. Additionally, this
     * field is utilized by the Enterprise version.
     *
     * @param preview whether or not this is a test entity
     */
    void setPreview(Boolean preview);

}
