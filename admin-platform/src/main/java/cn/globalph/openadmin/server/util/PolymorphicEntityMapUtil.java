package cn.globalph.openadmin.server.util;

import cn.globalph.openadmin.dto.ClassTree;

import java.util.LinkedHashMap;

/**
 * @author Elbert Bautista (elbertbautista)
 *
 * Utility class to convert the Polymorphic ClassTree into a Map
 */
public class PolymorphicEntityMapUtil {

    public LinkedHashMap<String, String> convertClassTreeToMap(ClassTree polymorphicEntityTree) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(polymorphicEntityTree.getRight()/2);
        buildPolymorphicEntityMap(polymorphicEntityTree, map);
        return map;
    }

    protected void buildPolymorphicEntityMap(ClassTree entity, LinkedHashMap<String, String> map) {
        String friendlyName = entity.getFriendlyName();
        if (friendlyName != null && !friendlyName.equals("")) {

            //TODO: fix this for i18N
            //check if the friendly name is an i18N key
            //String val = BLCMain.getMessageManager().getString(friendlyName);
            //if (val != null) {
            //    friendlyName = val;
            //}

        }
        map.put(entity.getClassname(), friendlyName!=null?friendlyName:entity.getName());
        for (ClassTree child : entity.getChildren()) {
            buildPolymorphicEntityMap(child, map);
        }
    }

}
