package cn.globalph.common.extensibility.jpa;

import cn.globalph.common.classloader.release.ThreadLocalManager;

/**
 * @author Jeff Fischer
 */
public class ConfigurationOnlyState {

    private static final ThreadLocal<ConfigurationOnlyState> CONFIGURATIONONLYSTATE = ThreadLocalManager.createThreadLocal(ConfigurationOnlyState.class);

    public static ConfigurationOnlyState getState() {
        return CONFIGURATIONONLYSTATE.get();
    }

    public static void setState(ConfigurationOnlyState state) {
        CONFIGURATIONONLYSTATE.set(state);
    }

    protected boolean isConfigurationOnly;

    public boolean isConfigurationOnly() {
        return isConfigurationOnly;
    }

    public void setConfigurationOnly(boolean configurationOnly) {
        isConfigurationOnly = configurationOnly;
    }
}
