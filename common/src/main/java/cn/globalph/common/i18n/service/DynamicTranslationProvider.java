package cn.globalph.common.i18n.service;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Convenience class to provide dynamic field translations.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class DynamicTranslationProvider {
    
    /**
     * If translations are enabled, this method will look for a translation for the specified field. If translations are
     * disabled or if this particular field did not have a translation, it will return back the defaultValue.
     * 
     * @param obj
     * @param field
     * @param defaultValue
     * @return the translated value
     */
    public static String getValue(Object obj, String field, final String defaultValue) {
        String valueToReturn = defaultValue;
        
        if (TranslationConsiderationContext.hasTranslation()) {
            TranslationService translationService = TranslationConsiderationContext.getTranslationService();
            String translatedValue = translationService.getTranslatedValue(obj, field, Locale.CHINA);
            
            if (StringUtils.isNotBlank(translatedValue)) {
                valueToReturn = translatedValue;
            }
        }
            
        return valueToReturn;
    }

}
