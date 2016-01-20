package cn.globalph.common.value;

import java.io.Serializable;

/**
 * Describes a class that contains searchable information. Can be used by the framework search engine to create
 * search indexes and indicate that information in this class should be searched for search terms during actual
 * searches.
 *
 * @author Jeff Fischer
 */
public interface Searchable<T extends Serializable> extends ValueAssignable<T> {

    /**
     * Whether or not this class contains searchable information
     *
     * @return Whether or not this class contains searchable information
     */
    Boolean getSearchable();

    /**
     * Whether or not this class contains searchable information
     *
     * @param searchable Whether or not this class contains searchable information
     */
    void setSearchable(Boolean searchable);

}
