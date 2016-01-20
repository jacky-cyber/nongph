package cn.globalph.openadmin.web.form;

public class TranslationForm {

    protected String ceilingEntity;
    protected String entityId;
    protected String propertyName;
    protected String localeCode;
    protected String translatedValue;
    protected Long translationId;
    protected Boolean isRte;

    public String getCeilingEntity() {
        return ceilingEntity;
    }

    public void setCeilingEntity(String ceilingEntity) {
        this.ceilingEntity = ceilingEntity;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public String getTranslatedValue() {
        return translatedValue;
    }

    public void setTranslatedValue(String translatedValue) {
        this.translatedValue = translatedValue;
    }

    public Long getTranslationId() {
        return translationId;
    }

    public void setTranslationId(Long translationId) {
        this.translationId = translationId;
    }
    
    public Boolean getIsRte() {
        return isRte == null ? false : isRte;
    }

    public void setIsRte(Boolean isRte) {
        this.isRte = isRte;
    }

}
