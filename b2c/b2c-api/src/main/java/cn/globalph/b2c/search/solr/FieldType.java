package cn.globalph.b2c.search.solr;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extensible enumeration of entities that are used for searching and reporting
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class FieldType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, FieldType> TYPES = new LinkedHashMap<String, FieldType>();

    public static final FieldType ID = new FieldType("id", "ID");
    public static final FieldType CATEGORY = new FieldType("category", "Category");
    
    public static final FieldType INT = new FieldType("i", "Integer");
    public static final FieldType INTS = new FieldType("is", "Integer (Multi)");
    public static final FieldType STRING = new FieldType("s", "String");
    public static final FieldType STRINGS = new FieldType("ss", "String (Multi)");
    public static final FieldType LONG = new FieldType("l", "Long");
    public static final FieldType LONGS = new FieldType("ls", "Long (Multi)");
    public static final FieldType TEXT = new FieldType("t", "Text");
    public static final FieldType TEXTS = new FieldType("txt", "Text (Multi)");
    public static final FieldType BOOLEAN = new FieldType("b", "Boolean");
    public static final FieldType BOOLEANS = new FieldType("bs", "Boolean (Multi)");
    public static final FieldType DOUBLE = new FieldType("d", "Double");
    public static final FieldType DOUBLES = new FieldType("ds", "Double (Multi)");
    public static final FieldType PRICE = new FieldType("p", "Price");
    public static final FieldType DATE = new FieldType("dt", "Date");
    public static final FieldType DATES = new FieldType("dts", "Date (Multi)");
    public static final FieldType TRIEINT = new FieldType("ti", "Trie Integer");
    public static final FieldType TRIELONG = new FieldType("tl", "Trie Long");
    public static final FieldType TRIEDOUBLE = new FieldType("td", "Trie Double");
    public static final FieldType TRIEDATE = new FieldType("tdt", "Trie Date");

    public static FieldType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public FieldType() {
        //do nothing
    }

    public FieldType(final String type, final String friendlyType) {
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
        FieldType other = (FieldType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
