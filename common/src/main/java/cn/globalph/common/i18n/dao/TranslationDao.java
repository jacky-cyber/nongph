
package cn.globalph.common.i18n.dao;

import cn.globalph.common.i18n.domain.TranslatedEntity;
import cn.globalph.common.i18n.domain.Translation;

import java.util.List;
import java.util.Map;

/**
 * Provides data access for the {@link Translation} entity.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface TranslationDao {

    /**
     * Persists the given translation
     * @param translation
     * @return the saved translation
     */
    public Translation save(Translation translation);
    
    /**
     * Creates an empty translation instance that is not persisted to the database
     * 
     * @return the unsaved, empty translation
     */
    public Translation create();

    /**
     * Deletes the given translation
     * 
     * @param translation
     */
    public void delete(Translation translation);

    /**
     * Returns a map that holds the following data for the given entity:
     *  "name" --> idProperty (the name of the id property, always a String)
     *  "type" --> idProperty's type (usually either Long or String)
     *  
     * @param entity
     * @return the id property's metadata
     */
    public Map<String, Object> getIdPropertyMetadata(TranslatedEntity entity);
    
    /**
     * Reads a translation by its own primary key
     * 
     * @param translationId
     * @return the translation
     */
    public Translation readTranslationById(Long translationId);
    
    /**
     * Reads all translations for a given field
     * 
     * @param entity
     * @param entityId
     * @param fieldName
     * @return the list of translations
     */
    public List<Translation> readTranslations(TranslatedEntity entity, String entityId, String fieldName);

    /**
     * Reads a translation for the requested parameters. Returns null if there is no translation found
     * 
     * @param entity
     * @param entityId
     * @param fieldName
     * @param localeCode
     * @return the translation
     */
    public Translation readTranslation(TranslatedEntity entity, String entityId, String fieldName, String localeCode);

}
