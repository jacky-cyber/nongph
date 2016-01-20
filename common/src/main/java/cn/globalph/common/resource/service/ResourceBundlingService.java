package cn.globalph.common.resource.service;

import net.sf.ehcache.Cache;
import cn.globalph.common.web.processor.ResourceBundleProcessor;
import cn.globalph.common.web.resource.WebResourceHttpRequestHandler;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This service is responsible for interaction with the {@link ResourceBundleProcessor} to generate
 * versioned names for bundles as well as the bundle content.
 * 
 */
public interface ResourceBundlingService {

    /**
     * For the given versioned bundle name, returns a Resource that holds the contents of the combined, and
     * possibly minified (if enabled) bundle.
     * 
     * @param versionedBundleName
     * @return the Resource
     */
    public Resource getBundle(String versionedBundleName);

    /**
     * For a given unversioned bundle name, such as "global.js", returns the currently known versioned bundle
     * name, such as "global12345.js".
     * 
     * @param unversionedBundleName
     * @return the versioned bundle name
     */
    public String getVersionedBundleName(String unversionedBundleName);

    /**
     * Registers a new bundle with the given name to its files. Will utilize the locations map in handler as well as 
     * any configured generated resource handlers in the handler to determine legitimate paths for each of the files
     * in the list.
     * 
     * @param bundleName
     * @param files
     * @param handler
     * @return the versioned bundle name
     * @throws IOException
     */
    public String registerBundle(String bundleName, List<String> files, WebResourceHttpRequestHandler handler) 
            throws IOException;

    /**
     * @param versionedBundle
     * @return whether or not the given versioned bundle name is currently registered in the system
     */
    public boolean hasBundle(String versionedBundle);

    /**
     * @param bundleName
     * @return a list of additional files that are registered for the given bundle name
     */
    public List<String> getAdditionalBundleFiles(String bundleName);

    /**
     * @return the Cache used to store known bundle versions
     */
    public Cache getBundleVersionsCache();

    /**
     * @return the map of known versioned bundle names to the collection of resources they contain
     */
    public Map<String, Collection<Resource>> getBundles();

}
