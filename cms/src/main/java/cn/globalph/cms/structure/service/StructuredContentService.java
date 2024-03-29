package cn.globalph.cms.structure.service;

import net.sf.ehcache.Cache;
import cn.globalph.cms.structure.domain.StructuredContent;
import cn.globalph.cms.structure.domain.StructuredContentType;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.structure.dto.StructuredContentDTO;

import org.hibernate.Criteria;

import java.util.List;
import java.util.Map;

/**
 * Provides services to manage <code>StructuredContent</code> items.
 *
 * @author bpolster
 */
public interface StructuredContentService {


    /**
     * Returns the StructuredContent item associated with the passed in id.
     *
     * @param contentId - The id of the content item.
     * @return The associated structured content item.
     */
    StructuredContent findStructuredContentById(Long contentId);


    /**
     * Returns the <code>StructuredContentType</code> associated with the passed in id.
     *
     * @param id - The id of the content type.
     * @return The associated <code>StructuredContentType</code>.
     */
    StructuredContentType findStructuredContentTypeById(Long id);


    /**
     * Returns the <code>StructuredContentType</code> associated with the passed in
     * String value.
     *
     * @param name - The name of the content type.
     * @return The associated <code>StructuredContentType</code>.
     */
    StructuredContentType findStructuredContentTypeByName(String name);

    /**
     *
     * @return a list of all <code>StructuredContentType</code>s
     */
    List<StructuredContentType> retrieveAllStructuredContentTypes();

    /**
     * This method is intended to be called solely from the CMS admin.    Similar methods
     * exist that are intended for other clients (e.g. lookupStructuredContentItemsBy....
     * <br>
     * Returns content items for the passed in sandbox that match the passed in criteria.
     * The criteria acts as a where clause to be used in the search for content items.
     * Implementations should automatically add criteria such that no archived items
     * are returned from this method.
     * <br>
     * The SandBox parameter impacts the results as follows.  If a <code>SandBoxType</code> of
     * production is passed in, only those items in that SandBox are returned.
     * <br>
     * If a non-production SandBox is passed in, then the method will return the items associatd
     * with the related production SandBox and then merge in the results of the passed in SandBox.
     *
     * @param sandbox - the sandbox to find structured content items (null indicates items that are in production for
     *                  sites that are single tenant.
     * @param criteria - the criteria used to search for content
     * @return
     */
    List<StructuredContent> findContentItems(Criteria criteria);
    
    /**
     * Finds all content items regardless of the {@link Sandbox} they are a member of
     * @return
     */
    List<StructuredContent> findAllContentItems();
    
    /**
     * Follows the same rules as {@link #findContentItems(cn.globalph.common.sandbox.domain.SandBox, org.hibernate.Criteria) findContentItems}.
     *
     * @return the count of items in this sandbox that match the passed in Criteria
     */
    Long countContentItems(Criteria c);

    /**
     * Saves the given <b>type</b> and returns the merged instance
     */
    StructuredContentType saveStructuredContentType(StructuredContentType type);

    /**
     * This method returns content
     * <br>
     * Returns active content items for the passed in sandbox that match the passed in type.
     * <br>
     * The SandBox parameter impacts the results as follows.  If a <code>SandBoxType</code> of
     * production is passed in, only those items in that SandBox are returned.
     * <br>
     * If a non-production SandBox is passed in, then the method will return the items associatd
     * with the related production SandBox and then merge in the results of the passed in SandBox.
     * <br>
     * The secure item is used in cases where the structured content item contains an image path that needs
     * to be rewritten to use https.
     *
     * @param sandBox - the sandbox to find structured content items (null indicates items that are in production for
     *                  sites that are single tenant.
     * @param contentType - the type of content to return
     * @param count - the max number of content items to return
     * @param ruleDTOs - a Map of objects that will be used in MVEL processing.
     * @param secure - set to true if the request is being served over https
     * @return - The matching items
     * @see cn.globalph.cms.web.structure.DisplayContentTag
     */
    List<StructuredContentDTO> lookupStructuredContentItemsByType(StructuredContentType contentType,Integer count, Map<String,Object> ruleDTOs, boolean secure);

    /**
     * This method returns content by name only.
     * <br>
     * Returns active content items for the passed in sandbox that match the passed in type.
     * <br>
     * The SandBox parameter impacts the results as follows.  If a <code>SandBoxType</code> of
     * production is passed in, only those items in that SandBox are returned.
     * <br>
     * If a non-production SandBox is passed in, then the method will return the items associatd
     * with the related production SandBox and then merge in the results of the passed in SandBox.
     *
     * @param sandBox - the sandbox to find structured content items (null indicates items that are in production for
     *                  sites that are single tenant.
     * @param contentName - the name of content to return
     * @param count - the max number of content items to return
     * @param ruleDTOs - a Map of objects that will be used in MVEL processing.
     * @param secure - set to true if the request is being served over https
     * @return - The matching items
     * @see cn.globalph.cms.web.structure.DisplayContentTag
     */
    List<StructuredContentDTO> lookupStructuredContentItemsByName(String contentName, Integer count, Map<String,Object> ruleDTOs, boolean secure);



    /**
     * This method returns content by name and type.
     * <br>
     * Returns active content items for the passed in sandbox that match the passed in type.
     * <br>
     * The SandBox parameter impacts the results as follows.  If a <code>SandBoxType</code> of
     * production is passed in, only those items in that SandBox are returned.
     * <br>
     * If a non-production SandBox is passed in, then the method will return the items associatd
     * with the related production SandBox and then merge in the results of the passed in SandBox.
     *
     * @param sandBox - the sandbox to find structured content items (null indicates items that are in production for
     *                  sites that are single tenant.
     * @param contentType - the type of content to return
     * @param contentName - the name of content to return
     * @param count - the max number of content items to return
     * @param ruleDTOs - a Map of objects that will be used in MVEL processing.
     * @param secure - set to true if the request is being served over https
     * @return - The matching items
     * @see cn.globalph.cms.web.structure.DisplayContentTag
     */
    List<StructuredContentDTO> lookupStructuredContentItemsByName(StructuredContentType contentType, String contentName, Integer count, Map<String,Object> ruleDTOs, boolean secure);


    List<StructuredContentDTO> buildStructuredContentDTOList(List<StructuredContent> structuredContentList, boolean secure);

    List<StructuredContentDTO> evaluateAndPriortizeContent(List<StructuredContentDTO> structuredContentList, int count, Map<String, Object> ruleDTOs);

    void removeStructuredContentFromCache(SandBox sandBox, StructuredContent sc);

    Cache getStructuredContentCache();


    public void addStructuredContentListToCache(String key, List<StructuredContentDTO> scDTOList);


    public String buildTypeKey(SandBox currentSandbox, String contentType);


    public List<StructuredContentDTO> getStructuredContentListFromCache(String key);

    public void removeItemFromCache(String nameKey, String typeKey);
}
