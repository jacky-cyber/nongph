package cn.globalph.cms.structure.domain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * A content type corresponds to an area where content should be targeted.   For example,
 * a valid content name would be "homepage banner" or "cart rhs ad".
 * <br>
 * While typically used for placement, a content type can also be used just to describe
 * the fields.    For example, a content type of message might be used to store messages
 * that can be retrieved by name.
 * <br>
 * The custom fields associated by with a <code>StructuredContentType</code>
 * <br>
 *
 */
public interface StructuredContentType extends Serializable {

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
     * Gets the description.
     * @return
     */
    @Nullable
    String getDescription();

    /**
     * Sets the description.
     */
    void setDescription(@Nullable String description);

    /**
     * Returns the template associated with this content type.
     * @return
     */
    @Nonnull
    StructuredContentFieldTemplate getStructuredContentFieldTemplate();

    /**
     * Sets the template associated with this content type.
     * @param scft
     */
    void setStructuredContentFieldTemplate(@Nonnull StructuredContentFieldTemplate scft);
}
