package cn.globalph.common.storage.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Any module within the BLC framework that needs to store and retrieve files from a remote storage such as
 * Rackspace CloudFiles or AmazonS3 should declare an application file type.
 * 
 * Many (or perhaps even most) implementations will just configure a provider for ALL.   Implementations that deploy to
 * a single file system may use the default FileSystemProvider and not configure anything.  
 * 
 * This enumeration is intended to provide a deployment team with the ability to make granular decisions on how files are 
 * accessed and stored.  
 * 
 * For example, Images might be stored on S3 whereas SiteMap creation might be stored on CloudFiles.   These are just
 * arbitrary examples, but the intent is to give an implementation the ability to alter the storage paradigm based
 * on cost/reliability/performance needs that might vary by file type.
 */
public class FileApplicationType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, FileApplicationType> TYPES = new LinkedHashMap<String, FileApplicationType>();

    public static final FileApplicationType ALL = new FileApplicationType("ALL", "All"); // fall-through
    public static final FileApplicationType IMAGE = new FileApplicationType("IMAGE", "Images");
    public static final FileApplicationType STATIC = new FileApplicationType("STATIC", "Static Assets");
    public static final FileApplicationType SITE_MAP = new FileApplicationType("SITEMAP", "Site Map");

    public static FileApplicationType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public FileApplicationType() {
    }

    public FileApplicationType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileApplicationType other = (FileApplicationType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
