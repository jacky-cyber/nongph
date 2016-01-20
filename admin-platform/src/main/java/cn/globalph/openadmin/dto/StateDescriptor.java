package cn.globalph.openadmin.dto;

import java.util.Map;

/**
 * The current state of an entity
 */
public interface StateDescriptor {

    Property[] getProperties();

    Property findProperty(String name);

    Map<String, Property> getPMap();

}
