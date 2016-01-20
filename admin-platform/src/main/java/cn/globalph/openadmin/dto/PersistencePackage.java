package cn.globalph.openadmin.dto;

import org.apache.commons.lang3.ArrayUtils;

import cn.globalph.common.presentation.client.PersistenceAssociationItemType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 批持久操作
 * @author felix.wu
 *
 */
public class PersistencePackage implements Serializable, StateDescriptor {

    private static final long serialVersionUID = 1L;
    
    protected String rootEntityClassname;
    protected String securityRootEntityClassname;
    protected String sectionEntityField;
    protected String fetchTypeClassname;
    /*持久关联*/
    protected PersistenceAssociation persistenceAssociation;
    protected String[] customCriteria;
    protected Entity entity;
    protected String csrfToken;
    protected String requestingEntityName;
    protected Map<String, PersistencePackage> subPackages = new LinkedHashMap<String, PersistencePackage>();
    protected boolean validateUnsubmittedProperties = true;
    protected SectionCrumb[] sectionCrumbs;

    public PersistencePackage(String ceilingEntityFullyQualifiedClassname, Entity entity, PersistenceAssociation persistencePerspective, String[] customCriteria, String csrfToken) {
        this(ceilingEntityFullyQualifiedClassname, null, entity, persistencePerspective, customCriteria, csrfToken);
    }
    
    public PersistencePackage(String rootEntityClassname, String fetchTypeClassname, Entity entity, PersistenceAssociation persistencePerspective, String[] customCriteria, String csrfToken) {
        this.rootEntityClassname = rootEntityClassname;
        this.fetchTypeClassname = fetchTypeClassname;
        this.persistenceAssociation = persistencePerspective;
        this.entity = entity;
        this.customCriteria = customCriteria;
        this.csrfToken = csrfToken;
    }
    
    public PersistencePackage() {
        //do nothing
    }

    @Override
    public Property findProperty(String name) {
        return entity.findProperty(name);
    }

    @Override
    public Property[] getProperties() {
        return entity.getProperties();
    }

    @Override
    public Map<String, Property> getPMap() {
        return entity.getPMap();
    }

    public String getRootEntityClassname() {
        return rootEntityClassname;
    }
    
    public void setRootEntityClassname(
            String ceilingEntityClassname) {
        this.rootEntityClassname = ceilingEntityClassname;
    }
    
    public String getSecurityRootEntityClassname() {
        return securityRootEntityClassname;
    }

    public void setSecurityRootEntityClassname(
            String securityCeilingEntityClassname) {
        this.securityRootEntityClassname = securityCeilingEntityClassname;
    }

    public PersistenceAssociation getPersistenceAssociation() {
        return persistenceAssociation;
    }
    
    public void setPersistenceAssociation(
            PersistenceAssociation persistencePerspective) {
        this.persistenceAssociation = persistencePerspective;
    }
    
    public String[] getCustomCriteria() {
        return customCriteria;
    }
    
    public void setCustomCriteria(String[] customCriteria) {
        this.customCriteria = customCriteria;
    }

    public void addCustomCriteria(String criteria) {
        customCriteria = ArrayUtils.add(customCriteria, criteria);
    }

    public void removeCustomCriteria(String criteria) {
        int pos = containsCriteria(criteria);
        if (pos >= 0) {
            customCriteria = ArrayUtils.remove(customCriteria, pos);
        }
    }

    public int containsCriteria(String criteria) {
        if (ArrayUtils.isEmpty(customCriteria)) {
            return -1;
        }
        
        for (int i = 0; i < customCriteria.length; i++) {
            if (customCriteria[i] != null && customCriteria[i].equals(criteria)) {
                return i;
            } else if (customCriteria[i] == null && criteria == null) {
                return i;
            }
        }

        return -1;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getFetchTypeClassname() {
        return fetchTypeClassname;
    }

    public void setFetchTypeClassname(String fetchTypeFullyQualifiedClassname) {
        this.fetchTypeClassname = fetchTypeFullyQualifiedClassname;
    }

    public String getSectionEntityField() {
        return sectionEntityField;
    }

    public void setSectionEntityField(String sectionEntityField) {
        this.sectionEntityField = sectionEntityField;
    }

    public String getRequestingEntityName() {
        return requestingEntityName;
    }

    public void setRequestingEntityName(String requestingEntityName) {
        this.requestingEntityName = requestingEntityName;
    }

    public Map<PersistenceAssociationItemType, PersistenceAssociationItem> getPersistencePerspectiveItems() {
        if (persistenceAssociation != null) {
            return persistenceAssociation.getPersistenceAssociationItems();
        }
        return new HashMap<PersistenceAssociationItemType, PersistenceAssociationItem>();
    }

    public Map<String, PersistencePackage> getSubPackages() {
        return subPackages;
    }

    public void setSubPackages(Map<String, PersistencePackage> subPackages) {
        this.subPackages = subPackages;
    }

    public boolean isValidateUnsubmittedProperties() {
        return validateUnsubmittedProperties;
    }

    public void setValidateUnsubmittedProperties(boolean validateUnsubmittedProperties) {
        this.validateUnsubmittedProperties = validateUnsubmittedProperties;
    }

    public SectionCrumb[] getSectionCrumbs() {
        return sectionCrumbs;
    }

    public void setSectionCrumbs(SectionCrumb[] sectionCrumbs) {
        this.sectionCrumbs = sectionCrumbs;
    }

    public SectionCrumb getClosetCrumb(String myCeiling) {
        if (ArrayUtils.isEmpty(sectionCrumbs)) {
            return new SectionCrumb();
        } else {
            SectionCrumb previous = sectionCrumbs[sectionCrumbs.length-1];
            for (SectionCrumb sectionCrumb : sectionCrumbs) {
                if (sectionCrumb.getSectionIdentifier().equals(myCeiling)) {
                    break;
                } else {
                    previous = sectionCrumb;
                }
            }
            return previous;
        }
    }

    public SectionCrumb getBottomCrumb() {
        if (ArrayUtils.isEmpty(sectionCrumbs)) {
            return new SectionCrumb();
        }
        return sectionCrumbs[sectionCrumbs.length-1];
    }

    public SectionCrumb getTopCrumb() {
        if (ArrayUtils.isEmpty(sectionCrumbs)) {
            return new SectionCrumb();
        }
        return sectionCrumbs[0];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersistencePackage{");
        sb.append("ceilingEntityFullyQualifiedClassname='").append(rootEntityClassname).append('\'');
        sb.append(", securityCeilingEntityFullyQualifiedClassname='").append
                (securityRootEntityClassname).append('\'');
        sb.append(", sectionEntityField='").append(sectionEntityField).append('\'');
        sb.append(", fetchTypeFullyQualifiedClassname='").append(fetchTypeClassname).append('\'');
        sb.append(", persistencePerspective=").append(persistenceAssociation);
        sb.append(", customCriteria=").append(Arrays.toString(customCriteria));
        sb.append(", entity=").append(entity);
        sb.append(", csrfToken='").append(csrfToken).append('\'');
        sb.append(", requestingEntityName='").append(requestingEntityName).append('\'');
        sb.append(", subPackages=").append(subPackages);
        sb.append(", validateUnsubmittedProperties=").append(validateUnsubmittedProperties);
        sb.append(", sectionCrumbs=").append(Arrays.toString(sectionCrumbs));
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistencePackage)) return false;

        PersistencePackage that = (PersistencePackage) o;

        if (rootEntityClassname != null ? !rootEntityClassname.equals(that
                .rootEntityClassname) : that.rootEntityClassname != null)
            return false;
        if (csrfToken != null ? !csrfToken.equals(that.csrfToken) : that.csrfToken != null) return false;
        if (!Arrays.equals(customCriteria, that.customCriteria)) return false;
        if (entity != null ? !entity.equals(that.entity) : that.entity != null) return false;
        if (fetchTypeClassname != null ? !fetchTypeClassname.equals(that
                .fetchTypeClassname) : that.fetchTypeClassname != null)
            return false;
        if (persistenceAssociation != null ? !persistenceAssociation.equals(that.persistenceAssociation) : that
                .persistenceAssociation != null)
            return false;
        if (sectionEntityField != null ? !sectionEntityField.equals(that.sectionEntityField) : that
                .sectionEntityField != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rootEntityClassname != null ? rootEntityClassname.hashCode() : 0;
        result = 31 * result + (sectionEntityField != null ? sectionEntityField.hashCode() : 0);
        result = 31 * result + (fetchTypeClassname != null ? fetchTypeClassname.hashCode
                () : 0);
        result = 31 * result + (persistenceAssociation != null ? persistenceAssociation.hashCode() : 0);
        result = 31 * result + (customCriteria != null ? Arrays.hashCode(customCriteria) : 0);
        result = 31 * result + (entity != null ? entity.hashCode() : 0);
        result = 31 * result + (csrfToken != null ? csrfToken.hashCode() : 0);
        return result;
    }
}
