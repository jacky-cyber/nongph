package cn.globalph.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated use {@link MergeResourceBundleMessageSource} instead
 */
@Deprecated
public class BLResourceBundleMessageSource extends ResourceBundleMessageSource implements InitializingBean {

    private static final Log LOG = LogFactory.getLog(BLResourceBundleMessageSource.class);

    public BLResourceBundleMessageSource(String[] basenames, ResourceBundleExtensionPoint resourceBundleExtensionPoint) {
        super();

        List<String> bundles = new ArrayList<String>();
        if (resourceBundleExtensionPoint != null) {
            String[] bundleNames = resourceBundleExtensionPoint.getBasenameExtensions();
            if (bundleNames != null) {
                for (int i = 0; i < bundleNames.length; i++) {
                    bundles.add(bundleNames[i]);
                }
            }
            if (basenames != null) {
                for (int i = 0; i < basenames.length; i++) {
                    bundles.add(basenames[i]);
                }
            }
        }

        setBasenames(bundles.toArray(new String[0]));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.fatal("***INCORRECT CONFIGURATION***\n" +
                "This class should no longer be used as it does not merge property files together. If this is being used\n" +
                "in the admin application then this configuration is definitely an error as no properties will be resolved.\n" +
                "It is possible that the frontend application is not seriously effected by using this class but you should\n" +
                "modify your configuration to instead use cn.globalph.common.util.BroadleafMergeResourceBundleMessageSource\n" +
                "instead as soon as possible.");
    }

}
