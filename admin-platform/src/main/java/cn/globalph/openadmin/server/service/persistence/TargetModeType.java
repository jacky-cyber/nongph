package cn.globalph.openadmin.server.service.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An extendible enumeration of target mode types.
 * 
 * @author felix.wu
 */
public class TargetModeType implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Map<String, TargetModeType> TYPES = new HashMap<String, TargetModeType>();

    public static final TargetModeType SANDBOX  = new TargetModeType("sandbox", "entity manager target for the sandbox datasource");
    public static final TargetModeType STAGE  = new TargetModeType("stage", "entity manager target for the stage datasource");
    public static final TargetModeType PRODUCTION  = new TargetModeType("production", "entity manager target for the production datasource");

    public static TargetModeType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public TargetModeType() {
        //do nothing
     }

    public TargetModeType(final String type, final String friendlyType) {
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
        TargetModeType other = (TargetModeType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
