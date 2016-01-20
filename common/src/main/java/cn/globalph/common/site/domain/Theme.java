package cn.globalph.common.site.domain;

import java.io.Serializable;

/**
 * Created by bpolster.
 */
public interface Theme extends Serializable {
    
    /**
     * @return the id
     */
    public Long getId();

    /**
     * Sets the id
     * @param id
     */
    public void setId(Long id);

    public String getName();

    public void setName(String name);

    /**
     * The display name for a site.  Returns blank if no theme if no path is available.   Should return
     * a path that does not start with "/" and that ends with a "/".   For example, "store/".
     * @return
     */
    public String getPath();

    /**
     * Sets the path of the theme.
     * @param path
     */
    public void setPath(String path);
}
