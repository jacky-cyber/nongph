package cn.globalph.cms.structure.dao;

import cn.globalph.cms.structure.domain.StructuredContent;
import cn.globalph.cms.structure.domain.StructuredContentType;

import java.util.List;

/**
 * Responsible for querying and updating {@link StructuredContent} items
 * @author bpolster
 */
public interface StructuredContentDao {

    /**
     * Returns the <code>StructuredContent</code> item that matches
     * the passed in Id.
     * @param contentId
     * @return the found item or null if it does not exist
     */
    public StructuredContent findStructuredContentById(Long contentId);

    /**
     * Returns the <code>StructuredContentType</code> that matches
     * the passed in contentTypeId.
     * @param contentTypeId
     * @return the found item or null if it does not exist
     */
    public StructuredContentType findStructuredContentTypeById(Long contentTypeId);

    /**
     * Returns the list of all <code>StructuredContentType</code>s.
     *
     * @return the list of found items
     */
    public List<StructuredContentType> retrieveAllStructuredContentTypes();
    
    /**
     * Finds all content regardless of the {@link Sandbox} they are a member of
     * @return the list of {@link StructuredContent}, an empty list of none are found
     */
    public List<StructuredContent> findAllContentItems();

    /**
     * Persists the changes or saves a new content item.
     *
     * @param content
     * @return the newly saved or persisted item
     */
    public StructuredContent addOrUpdateContentItem(StructuredContent content);

    /**
     * Removes the passed in item from the underlying storage.
     *
     * @param content
     */
    public void delete(StructuredContent content);
    
    /**
     * Saves the given <b>type</b> and returns the merged instance
     */
    public StructuredContentType saveStructuredContentType(StructuredContentType type);

    /**
     * Called by the <code>DisplayContentTag</code> to locate content based
     * on the current SandBox, StructuredContentType, fullLocale and/or languageOnlyLocale.
     *
     * @param sandBox to search for the content
     * @param type of content to search for
     * @param fullLocale to restrict the search to
     * @param languageOnlyLocale locale based only on a language specified
     * @return a list of all matching content
     * @see cn.globalph.cms.web.structure.DisplayContentTag
     */
    public List<StructuredContent> findActiveStructuredContentByType(StructuredContentType type);

    /**
     * Called by the <code>DisplayContentTag</code> to locate content based
     * on the current SandBox, StructuredContentType, Name, fullLocale and/or languageOnlyLocale.
     * @param sandBox
     * @param type
     * @param name
     * @param fullLocale
     * @param languageOnlyLocale
     * @return
     */
    public List<StructuredContent> findActiveStructuredContentByNameAndType(StructuredContentType type, String name);

    /**
     * Called by the <code>DisplayContentTag</code> to locate content based
     * on the current SandBox, StructuredContentType, Name, fullLocale and/or languageOnlyLocale.
     * @param sandBox
     * @param name
     * @param fullLocale
     * @param languageOnlyLocale
     * @return
     */
    public List<StructuredContent> findActiveStructuredContentByName(String name);


    /**
     * Used to lookup the StructuredContentType by name.
     *
     * @param name
     * @return
     */
    public StructuredContentType findStructuredContentTypeByName(String name);

    /**
     * Detaches the item from the JPA session.   This is intended for internal
     * use by the CMS system.   It supports the need to clone an item as part
     * of the editing process.
     *
     * @param sc - the item to detach
     */
    public void detach(StructuredContent sc);
}
