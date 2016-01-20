package cn.globalph.openadmin.dto;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.presentation.client.PersistenceAssociationItemType;
import cn.globalph.openadmin.dto.visitor.MetadataVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A DTO class used to seed a persistence package.
 * 一个DTO，表示批持久操作请求，当没有子请求时表示单个持久操作请求
 */
public class PersistencePackageRequest {
	public enum Type {
        STANDARD,
        ADORNED,
        MAP
    }
    protected Type type;
    protected String rootEntityClassname;
    protected String securityRootEntityClassname;
    protected String configKey;
    protected MapStructure mapStructure;
    protected Entity entity;
    protected ForeignKey foreignKey;
    protected AdornedTargetList adornedList;
    protected Integer startIndex;
    protected Integer maxIndex;
    protected SectionCrumb[] sectionCrumbs;
    protected String sectionEntityField;
    protected String requestingEntityName;
    protected String msg;
    /*子请求*/
    protected Map<String, PersistencePackageRequest> subRequests = new LinkedHashMap<String, PersistencePackageRequest>();
    protected boolean validateUnsubmittedProperties = true;

    protected PersistenceOperationType operationTypesOverride = null;

    // These properties are accessed via getters and setters that operate on arrays.
    // We back them with a list so that we can have the convenience .add methods
    protected List<ForeignKey> additionalForeignKeys = new ArrayList<ForeignKey>();
    protected List<String> customCriteria = new ArrayList<String>();
    protected List<FilterAndSortCriteria> filterAndSortCriteria = new ArrayList<FilterAndSortCriteria>();

    

    /* ******************* */
    /* STATIC INITIALIZERS */
    /* ******************* */

    public static PersistencePackageRequest standard() {
        return new PersistencePackageRequest(Type.STANDARD);
    }

    public static PersistencePackageRequest adorned() {
        return new PersistencePackageRequest(Type.ADORNED);
    }

    public static PersistencePackageRequest map() {
        return new PersistencePackageRequest(Type.MAP);
    }

    /**
     * Creates a semi-populate PersistencePacakageRequest based on the specified FieldMetadata. This initializer
     * will copy over persistence perspective items from the metadata as well as set the appropriate OperationTypes
     * as specified in the annotation/xml configuration for the field.
     * 
     * @param md
     * @return the newly created PersistencePackageRequest
     */
    public static PersistencePackageRequest fromMetadata(FieldMetadata md, List<SectionCrumb> sectionCrumbs) {
        final PersistencePackageRequest request = new PersistencePackageRequest();

        md.accept(new MetadataVisitor() {

            @Override
            public void visit(BasicFieldMetadata fmd) {
                request.setType(Type.STANDARD);
                request.setRootEntityClassname(fmd.getForeignKeyClass());
                request.setCustomCriteria(fmd.getCustomCriteria());
            }

            @Override
            public void visit(BasicCollectionMetadata fmd) {
                ForeignKey foreignKey = (ForeignKey) fmd.getPersistenceAssociation()
                        .getPersistenceAssociationItems().get(PersistenceAssociationItemType.FOREIGNKEY);

                request.setType(Type.STANDARD);
                request.setRootEntityClassname(fmd.getCollectionRootEntity());
                request.setOperationTypesOverride(fmd.getPersistenceAssociation().getPersistenceOperationType());
                request.setForeignKey(foreignKey);
                request.setCustomCriteria(fmd.getCustomCriteria());
            }

            @Override
            public void visit(AdornedTargetCollectionMetadata fmd) {
                AdornedTargetList adornedList = (AdornedTargetList) fmd.getPersistenceAssociation()
                        .getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST);

                request.setType( Type.ADORNED );
                request.setRootEntityClassname( fmd.getCollectionRootEntity() );
                request.setOperationTypesOverride( fmd.getPersistenceAssociation().getPersistenceOperationType() );
                request.setAdornedList( adornedList );
                request.setCustomCriteria( fmd.getCustomCriteria() );
            }

            @Override
            public void visit(MapMetadata fmd) {
                MapStructure mapStructure = (MapStructure) fmd.getPersistenceAssociation()
                        .getPersistenceAssociationItems().get(PersistenceAssociationItemType.MAPSTRUCTURE);

                ForeignKey foreignKey = (ForeignKey) fmd.getPersistenceAssociation().
                        getPersistenceAssociationItems().get(PersistenceAssociationItemType.FOREIGNKEY);

                request.setType(Type.MAP);
                request.setRootEntityClassname(foreignKey.getForeignKeyClass());
                request.setOperationTypesOverride(fmd.getPersistenceAssociation().getPersistenceOperationType());
                request.setMapStructure(mapStructure);
                request.setForeignKey(foreignKey);
                request.setCustomCriteria(fmd.getCustomCriteria());
            }
        });
        
        if (md instanceof CollectionMetadata) {
            request.setCustomCriteria(((CollectionMetadata) md).getCustomCriteria());
        }

        if (sectionCrumbs != null) {
            request.setSectionCrumbs(sectionCrumbs.toArray(new SectionCrumb[sectionCrumbs.size()]));
        }

        return request;
    }

    /* ************ */
    /* CONSTRUCTORS */
    /* ************ */

    public PersistencePackageRequest() {

    }

    public PersistencePackageRequest(Type type) {
        this.type = type;
    }

    /* ************ */
    /* WITH METHODS */
    /* ************ */

    public PersistencePackageRequest withType(Type type) {
        setType(type);
        return this;
    }

    public PersistencePackageRequest withRootEntityClassname(String className) {
        setRootEntityClassname(className);
        return this;
    }

    public PersistencePackageRequest withSecurityCeilingEntityClassname(String className) {
        setSecurityRootEntityClassname(className);
        return this;
    }

    public PersistencePackageRequest withForeignKey(ForeignKey foreignKey) {
        setForeignKey(foreignKey);
        return this;
    }

    public PersistencePackageRequest withConfigKey(String configKey) {
        setConfigKey(configKey);
        return this;
    }

    public PersistencePackageRequest withFilterAndSortCriteria(FilterAndSortCriteria[] filterAndSortCriteria) {
        if (ArrayUtils.isNotEmpty(filterAndSortCriteria)) {
            setFilterAndSortCriteria(filterAndSortCriteria);
        }
        return this;
    }

    public PersistencePackageRequest withAdornedList(AdornedTargetList adornedList) {
        setAdornedList(adornedList);
        return this;
    }

    public PersistencePackageRequest withMapStructure(MapStructure mapStructure) {
        setMapStructure(mapStructure);
        return this;
    }

    public PersistencePackageRequest withCustomCriteria(String[] customCriteria) {
        if (ArrayUtils.isNotEmpty(customCriteria)) {
            setCustomCriteria(customCriteria);
        }
        return this;
    }

    public PersistencePackageRequest withEntity(Entity entity) {
        setEntity(entity);
        return this;
    }
    
    public PersistencePackageRequest withStartIndex(Integer startIndex) {
        setStartIndex(startIndex);
        return this;
    }
    
    public PersistencePackageRequest withMaxIndex(Integer maxIndex) {
        setMaxIndex(maxIndex);
        return this;
    }

    public PersistencePackageRequest withSectionCrumbs(List<SectionCrumb> sectionCrumbs) {
        setSectionCrumbs(sectionCrumbs.toArray(new SectionCrumb[sectionCrumbs.size()]));
        return this;
    }

    public PersistencePackageRequest withSectionEntityField(String sectionEntityField) {
        setSectionEntityField(sectionEntityField);
        return this;
    }
    
    public PersistencePackageRequest withRequestingEntityName(String requestingEntityName) {
        setRequestingEntityName(requestingEntityName);
        return this;
    }

    public PersistencePackageRequest withMsg(String msg) {
        setMsg(msg);
        return this;
    }

    /* *********** */
    /* ADD METHODS */
    /* *********** */

    public PersistencePackageRequest addAdditionalForeignKey(ForeignKey foreignKey) {
        additionalForeignKeys.add(foreignKey);
        return this;
    }

    public PersistencePackageRequest addSubRequest(String infoPropertyName, PersistencePackageRequest subRequest) {
        subRequests.put(infoPropertyName, subRequest);
        return this;
    }

    public PersistencePackageRequest addCustomCriteria(String customCriteria) {
        if (StringUtils.isNotBlank(customCriteria)) {
            this.customCriteria.add(customCriteria);
        }
        return this;
    }

    public PersistencePackageRequest addFilterAndSortCriteria(FilterAndSortCriteria filterAndSortCriteria) {
        this.filterAndSortCriteria.add(filterAndSortCriteria);
        return this;
    }
    
    public PersistencePackageRequest addFilterAndSortCriteria(FilterAndSortCriteria[] filterAndSortCriteria) {
        if (filterAndSortCriteria != null) {
            this.filterAndSortCriteria.addAll(Arrays.asList(filterAndSortCriteria));
        }
        return this;
    }
    
    public PersistencePackageRequest addFilterAndSortCriteria(List<FilterAndSortCriteria> filterAndSortCriteria) {
        this.filterAndSortCriteria.addAll(filterAndSortCriteria);
        return this;
    }

    /* ************** */
    /* REMOVE METHODS */
    /* ************** */
    
    public PersistencePackageRequest removeFilterAndSortCriteria(String name) {
        Iterator<FilterAndSortCriteria> it = filterAndSortCriteria.listIterator();
        while (it.hasNext()) {
            FilterAndSortCriteria fasc = it.next();
            if (fasc.getPropertyId().equals(name)) {
                it.remove();
            }
        }
        return this;
    }

    /* ************************ */
    /* CUSTOM GETTERS / SETTERS */
    /* ************************ */

    public String[] getCustomCriteria() {
        String[] arr = new String[this.customCriteria.size()];
        arr = this.customCriteria.toArray(arr);
        return arr;
    }
    
    public ForeignKey[] getAdditionalForeignKeys() {
        ForeignKey[] arr = new ForeignKey[this.additionalForeignKeys.size()];
        arr = this.additionalForeignKeys.toArray(arr);
        return arr;
    }
    
    public void setAdditionalForeignKeys(ForeignKey[] additionalForeignKeys) {
        this.additionalForeignKeys.addAll(Arrays.asList(additionalForeignKeys));
    }

    public void setCustomCriteria(String[] customCriteria) {
        this.customCriteria.addAll(Arrays.asList(customCriteria));
    }

    public FilterAndSortCriteria[] getFilterAndSortCriteria() {
        FilterAndSortCriteria[] arr = new FilterAndSortCriteria[this.filterAndSortCriteria.size()];
        arr = this.filterAndSortCriteria.toArray(arr);
        return arr;
    }

    public void setFilterAndSortCriteria(FilterAndSortCriteria[] filterAndSortCriteria) {
        this.filterAndSortCriteria.addAll(Arrays.asList(filterAndSortCriteria));
    }

    /* ************************** */
    /* STANDARD GETTERS / SETTERS */
    /* ************************** */

    public ForeignKey getForeignKey() {
        return foreignKey;
    }
    
    public void setForeignKey(ForeignKey foreignKey) {
        this.foreignKey = foreignKey;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    /**
     * Returns the entity that should be checked for security purposes.   If no value is defined explicitly, returns the 
     * value for {@link #getRootEntityClassname()}.
     * 
     * @return
     */
    public String getSecurityRootEntityClassname() {
        if (securityRootEntityClassname != null) {
            return securityRootEntityClassname;
        } else {
            return getRootEntityClassname();
        }
    }

    public void setSecurityRootEntityClassname(String securityCeilingEntityClassname) {
        this.securityRootEntityClassname = securityCeilingEntityClassname;
    }

    public String getRootEntityClassname() {
        return rootEntityClassname;
    }
    
    public void setRootEntityClassname(String ceilingEntityClassname) {
        this.rootEntityClassname = ceilingEntityClassname;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public AdornedTargetList getAdornedList() {
        return adornedList;
    }

    public void setAdornedList(AdornedTargetList adornedList) {
        this.adornedList = adornedList;
    }

    public MapStructure getMapStructure() {
        return mapStructure;
    }

    public void setMapStructure(MapStructure mapStructure) {
        this.mapStructure = mapStructure;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public PersistenceOperationType getOperationTypesOverride() {
        return operationTypesOverride;
    }

    public void setOperationTypesOverride(PersistenceOperationType operationTypesOverride) {
        this.operationTypesOverride = operationTypesOverride;
    }
    
    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(Integer maxIndex) {
        this.maxIndex = maxIndex;
    }

    public SectionCrumb[] getSectionCrumbs() {
        return sectionCrumbs;
    }

    public void setSectionCrumbs(SectionCrumb[] sectionCrumbs) {
        this.sectionCrumbs = sectionCrumbs;
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
    
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, PersistencePackageRequest> getSubRequests() {
        return subRequests;
    }

    public void setSubRequests(Map<String, PersistencePackageRequest> subRequests) {
        this.subRequests = subRequests;
    }
    public boolean isValidateUnsubmittedProperties() {
        return validateUnsubmittedProperties;
    }

    public void setValidateUnsubmittedProperties(boolean validateUnsubmittedProperties) {
        this.validateUnsubmittedProperties = validateUnsubmittedProperties;
    }
}
