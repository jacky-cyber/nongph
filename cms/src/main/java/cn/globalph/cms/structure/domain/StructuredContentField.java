package cn.globalph.cms.structure.domain;

import cn.globalph.openadmin.audit.AdminAuditable;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Holds the values for custom fields that are part of a <code>StructuredContent</code> item.
 * <br>
 * Each item maintains a list of its custom fields.    The fields associated with an item are
 * determined by the {@link cn.globalph.cms.field.domain.FieldDefinition}s  associated
 * with the {@link StructuredContentType}.
 *
 * @see StructuredContentType
 * @see cn.globalph.cms.field.domain.FieldDefinition
 *
 */
public interface StructuredContentField extends Serializable, Cloneable {

    /**
     * Gets the primary key.
     *
     * @return the primary key
     */
    @Nullable
    public Long getId();


    /**
     * Sets the primary key.
     *
     * @param id the new primary key
     */
    public void setId(@Nullable Long id);

    /**
     * Returns the fieldKey associated with this field.   The key used for a
     * <code>StructuredContentField</code> is determined by the associated
     * {@link cn.globalph.cms.field.domain.FieldDefinition} that was used by the
     * Content Management System to create this instance.
     *
     * As an example, a <code>StructuredContentType</code> might be configured to contain a
     * field definition with a key of "targetUrl".
     *
     * @return the key associated with this item
     * @see cn.globalph.cms.field.domain.FieldDefinition
     */
    @Nonnull
    public String getFieldKey();

    /**
     * Sets the fieldKey.
     * @param fieldKey
     * @see cn.globalph.cms.field.domain.FieldDefinition
     */
    public void setFieldKey(@Nonnull String fieldKey);

    /**
     * Returns the value for this custom field.
     *
     * @param value
     */
    public void setValue(@Nonnull String value);

    /**
     * Sets the value of this custom field.
     * @return
     */
    @Nonnull
    public String getValue();

    /**
     * Returns audit information for this content item.
     *
     * @return
     */
    @Nullable
    public AdminAuditable getAuditable();

    /**
     * Sets audit information for this content item.   Default implementations automatically
     * populate this data during persistence.
     *
     * @param auditable
     */
    public void setAuditable(@Nullable AdminAuditable auditable);

    /**
     * @return a deep copy of this object. By default, clones the fieldKey and value fields and ignores the auditable
     * and id fields.
     */
    public StructuredContentField clone();

}
