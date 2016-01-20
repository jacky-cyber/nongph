package cn.globalph.cms.structure.domain;

import cn.globalph.cms.field.domain.FieldGroup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * A structured content field template holds the structure for a
 * structured content.
 *
 * For example, an 'Ad' template might describe the fields 'Ad URL' and
 * 'Target URL'.   The 'Ad' template might be used in multiple StructuredContentType
 * instances such as 'Home Page Banner Ad' or 'Cart Bottom Ad', etc.
 *
 * @author bpolster
 */
public interface StructuredContentFieldTemplate extends Serializable {

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
     * Gets the name.
     *
     * @return the name
     */
    @Nonnull
    String getName();

    /**
     * Sets the name.
     */
    void setName(@Nonnull String name);

    /**
     * Returns the list of the field groups for this template.
     * @return a list of FieldGroups associated with this template
     */
    @Nullable
    List<FieldGroup> getFieldGroups();

    /**
     * Sets the list of field groups for this template.
     * @param fieldGroups
     */
    void setFieldGroups(@Nullable List<FieldGroup> fieldGroups);
}
