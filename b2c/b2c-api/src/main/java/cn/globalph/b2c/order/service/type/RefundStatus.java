package cn.globalph.b2c.order.service.type;

import java.io.Serializable;
import java.util.LinkedHashMap;

import cn.globalph.common.EnumerationType;

public class RefundStatus implements Serializable, EnumerationType{

	    private static final long serialVersionUID = 1L;

	    private static final LinkedHashMap<String, RefundStatus> TYPES = new LinkedHashMap<String, RefundStatus>();

	    public static final RefundStatus PENDING = new RefundStatus("PENDING", "退款申请中");
	    public static final RefundStatus APPROVED = new RefundStatus("APPROVED", "退款完成");
	    public static final RefundStatus REFUSED = new RefundStatus("REFUSED", "退款失败");
	    

	    public static RefundStatus getInstance(final String type) {
	        return TYPES.get(type);
	    }

	    private String type;
	    private String friendlyType;

	    public RefundStatus() {
	        //do nothing
	    }

	    public RefundStatus(final String type, final String friendlyType) {
	        this.friendlyType = friendlyType;
	        setType(type);
	    }

	    @Override
	    public String getType() {
	        return type;
	    }

	    @Override
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
	        RefundStatus other = (RefundStatus) obj;
	        if (type == null) {
	            if (other.type != null)
	                return false;
	        } else if (!type.equals(other.type))
	            return false;
	        return true;
	    }

	}

