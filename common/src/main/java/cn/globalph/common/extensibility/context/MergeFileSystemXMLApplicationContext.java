package cn.globalph.common.extensibility.context;

import cn.globalph.common.extensibility.context.merge.ImportProcessor;
import cn.globalph.common.extensibility.context.merge.exceptions.MergeException;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Standalone XML application context, taking the locations of one or more
 * source applicationContext xml files and one or more patch xml files.
 * 
 * <p>One or more source files merge together in pure override mode. Source
 * files are merged in the order specified. If a bean id is repeated in a subsequent
 * source file, the subsequent bean definition will always win. This is the same behavior
 * as Spring's default mechanism for merging 1 to N applicationContext files.</p>
 * 
 * <p>Each patch file is merged with the combined source, one patch file at a time. This
 * merge is performed in true merge mode. Therefore, if a bean id is delivered in a patch
 * file with the same id as a bean in the source, the patch will merge with the source. This
 * could result in an override of the class definition for the bean, or additional or changed
 * property elements within the bean definition.</p>
 * 
 * @felix.wu
 *
 */
public class MergeFileSystemXMLApplicationContext extends AbstractMergeXMLApplicationContext {

    public MergeFileSystemXMLApplicationContext(ApplicationContext parent) {
        super(parent);
    }
    
    /**
     * Create a new MergeClassPathXMLApplicationContext, loading the definitions from the given files. Note,
     * all sourceLocation files will be merged using standard Spring configuration override rules. However, the patch
     * files are fully merged into the result of the sourceLocations simple merge. Patch merges are first executed according
     * to beans with the same id. Subsequent merges within a bean are executed against tagnames - ignoring any
     * further id attributes.
     * 
     * @param sourceLocations array of absolute file system paths for the source application context files
     * @param patchLocations array of absolute file system paths for the patch application context files
     * @throws BeansException
     */
    public MergeFileSystemXMLApplicationContext(String[] sourceLocations, String[] patchLocations) throws BeansException {
        this(sourceLocations, patchLocations, null);
    }
    
    /**
     * Create a new MergeClassPathXMLApplicationContext, loading the definitions from the given files. Note,
     * all sourceLocation files will be merged using standard Spring configuration override rules. However, the patch
     * files are fully merged into the result of the sourceLocations simple merge. Patch merges are first executed according
     * to beans with the same id. Subsequent merges within a bean are executed against tagnames - ignoring any
     * further id attributes.
     * 
     * @param sourceLocations array of absolute file system paths for the source application context files
     * @param patchLocations array of absolute file system paths for the patch application context files
     * @param parent the parent context
     * @throws BeansException
     */
    public MergeFileSystemXMLApplicationContext(String[] sourceLocations, String[] patchLocations, ApplicationContext parent) throws BeansException {
        this(parent);
        
        ResourceInputStream[] sources;
        ResourceInputStream[] patches;
        try {
            sources = new ResourceInputStream[sourceLocations.length];
            for (int j=0;j<sourceLocations.length;j++){
                File temp = new File(sourceLocations[j]);
                sources[j] = new ResourceInputStream(new BufferedInputStream(new FileInputStream(temp)), sourceLocations[j]);
            }
            
            patches = new ResourceInputStream[patchLocations.length];
            for (int j=0;j<patches.length;j++){
                File temp = new File(patchLocations[j]);
                sources[j] = new ResourceInputStream(new BufferedInputStream(new FileInputStream(temp)), patchLocations[j]);
            }
        } catch (FileNotFoundException e) {
            throw new FatalBeanException("Unable to merge context files", e);
        }

        ImportProcessor importProcessor = new ImportProcessor(this);
        try {
            sources = importProcessor.extract(sources);
            patches = importProcessor.extract(patches);
        } catch (MergeException e) {
            throw new FatalBeanException("Unable to merge source and patch locations", e);
        }

        this.configResources = new MergeApplicationContextXmlConfigResource().getConfigResources(sources, patches);
        refresh();
    }

}
