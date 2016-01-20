package cn.globalph.common.presentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to describe an array of map fields that allow map members
 * to be displayed as regular fields in the admin tool.
 *
 * @author Jeff Fischer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AdminPresentationMapFields {

    /**
     * Members of this map can be displayed as form fields, rather than in a standard grid. When populated,
     * mapDisplayFields informs the form building process to create the fields described here and persist those fields
     * in this map structure.
     *
     * @return the fields to display that represent the members of this map
     */
    AdminPresentationMapField[] mapDisplayFields();

}
