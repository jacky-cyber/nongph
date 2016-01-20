package cn.globalph.openadmin.server.security.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold the admin menus and sections for which the passed in user has permissions to view.
 * @author bpolster 
 */
public class AdminMenu {

    private List<AdminModule> adminModules = new ArrayList<AdminModule>();

    public List<AdminModule> getAdminModules() {
        return adminModules;
    }

    public void setAdminModule(List<AdminModule> adminModules) {
        this.adminModules = adminModules;
    }
}
