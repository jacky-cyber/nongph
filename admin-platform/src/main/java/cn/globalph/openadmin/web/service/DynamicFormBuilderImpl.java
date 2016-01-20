package cn.globalph.openadmin.web.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.exception.SecurityServiceException;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.media.domain.Media;
import cn.globalph.common.media.domain.MediaDto;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.common.presentation.client.LookupType;
import cn.globalph.common.presentation.client.PersistenceAssociationItemType;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.openadmin.dto.AdornedTargetCollectionMetadata;
import cn.globalph.openadmin.dto.AdornedTargetList;
import cn.globalph.openadmin.dto.BasicCollectionMetadata;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.CollectionMetadata;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.MapMetadata;
import cn.globalph.openadmin.dto.MapStructure;
import cn.globalph.openadmin.dto.PersistencePackageRequest;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.dto.SectionCrumb;
import cn.globalph.openadmin.server.security.domain.AdminSection;
import cn.globalph.openadmin.server.security.remote.EntityOperationType;
import cn.globalph.openadmin.server.security.remote.SecurityVerifier;
import cn.globalph.openadmin.server.security.service.RowLevelSecurityService;
import cn.globalph.openadmin.server.security.service.navigation.AdminNavigationService;
import cn.globalph.openadmin.server.service.EntityAdminService;
import cn.globalph.openadmin.server.service.persistence.module.BasicPersistenceModule;
import cn.globalph.openadmin.web.form.component.DefaultListGridActions;
import cn.globalph.openadmin.web.form.component.ListGrid;
import cn.globalph.openadmin.web.form.component.ListGridRecord;
import cn.globalph.openadmin.web.form.component.MediaField;
import cn.globalph.openadmin.web.form.component.RuleBuilderField;
import cn.globalph.openadmin.web.form.entity.CodeField;
import cn.globalph.openadmin.web.form.entity.ComboField;
import cn.globalph.openadmin.web.form.entity.DefaultEntityFormActions;
import cn.globalph.openadmin.web.form.entity.DynamicEntityFormInfo;
import cn.globalph.openadmin.web.form.entity.EntityForm;
import cn.globalph.openadmin.web.form.entity.Field;
import cn.globalph.openadmin.web.rulebuilder.DataDTODeserializer;
import cn.globalph.openadmin.web.rulebuilder.dto.DataDTO;
import cn.globalph.openadmin.web.rulebuilder.dto.DataWrapper;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

@Service("blFormBuilderService")
public class DynamicFormBuilderImpl implements DynamicFormBuilder {

    private static final Log LOG = LogFactory.getLog(DynamicFormBuilderImpl.class);

    @Resource(name = "blAdminEntityService")
    protected EntityAdminService adminEntityService;
    
    @Resource (name = "blAdminNavigationService")
    protected AdminNavigationService navigationService;
    
    @Resource(name = "blFormBuilderExtensionManager")
    protected FormBuilderExtensionManager extensionManager;
    
    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Resource(name="blAdminSecurityRemoteService")
    protected SecurityVerifier adminRemoteSecurityService;
    
    @Resource(name = "blRowLevelSecurityService")
    protected RowLevelSecurityService rowLevelSecurityService;

    protected static final VisibilityEnum[] FORM_HIDDEN_VISIBILITIES = new VisibilityEnum[] { 
            VisibilityEnum.HIDDEN_ALL, VisibilityEnum.FORM_HIDDEN 
    };
    
    protected static final VisibilityEnum[] GRID_HIDDEN_VISIBILITIES = new VisibilityEnum[] { 
            VisibilityEnum.HIDDEN_ALL, VisibilityEnum.GRID_HIDDEN 
    };

    @Override
    public ListGrid buildMainListGrid(DynamicResultSet drs, ClassMetadata cmd, String sectionKey, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {

        List<Field> headerFields = new ArrayList<Field>();
        ListGrid.Type type = ListGrid.Type.MAIN;
        String idProperty = "id";

        for (Property p : cmd.getProperties()) {
            if (p.getFieldMetadata() instanceof BasicFieldMetadata) {
                BasicFieldMetadata fmd = (BasicFieldMetadata) p.getFieldMetadata();
                
                if (SupportedFieldType.ID.equals(fmd.getFieldType())) {
                    idProperty = fmd.getName();
                }
                
                if (fmd.isProminent() != null && fmd.isProminent() 
                        && !ArrayUtils.contains(getGridHiddenVisibilities(), fmd.getVisibility())) {
                    Field hf = createHeaderField(p, fmd);
                    headerFields.add(hf);
                }
            }
        }

        ListGrid listGrid = createListGrid(cmd.getClassName(), headerFields, type, drs, sectionKey, 0, idProperty, sectionCrumbs);
        
        if (CollectionUtils.isNotEmpty(listGrid.getHeaderFields())) {
            // Set the first column to be able to link to the main entity
            listGrid.getHeaderFields().iterator().next().setMainEntityLink(true);
        } else {
            String message = "There are no listgrid header fields configured for the class " + cmd.getClassName();
            message += "Please mark some @AdminPresentation fields with 'prominent = true'";
            LOG.error(message);
        }
        
        return listGrid;
    }
    
    protected Field createHeaderField(Property p, BasicFieldMetadata fmd) {
        Field hf;
        if (fmd.getFieldType().equals(SupportedFieldType.EXPLICIT_ENUMERATION) ||
                fmd.getFieldType().equals(SupportedFieldType.BROADLEAF_ENUMERATION) ||
                fmd.getFieldType().equals(SupportedFieldType.DATA_DRIVEN_ENUMERATION) ||
                fmd.getFieldType().equals(SupportedFieldType.EMPTY_ENUMERATION)) {
            hf = new ComboField();
            ((ComboField) hf).setOptions(fmd.getEnumerationValues());
        } else {
            hf = new Field();
        }
        
        hf.withName(p.getName())
          .withFriendlyName(fmd.getFriendlyName())
          .withOrder(fmd.getGridOrder())
          .withColumnWidth(fmd.getColumnWidth())
          .withForeignKeyDisplayValueProperty(fmd.getForeignKeyDisplayValueProperty())
          .withForeignKeyClass(fmd.getForeignKeyClass())
          .withOwningEntityClass(fmd.getOwningClass() != null ? fmd.getOwningClass() : fmd.getTargetClass());
        String fieldType = fmd.getFieldType() == null ? null : fmd.getFieldType().toString();
        hf.setFieldType(fieldType);
        
        return hf;
    }

    @Override
    public ListGrid buildCollectionListGrid(String containingEntityId, DynamicResultSet drs, Property field, 
            String sectionKey, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {
        FieldMetadata fmd = field.getFieldMetadata();
        // Get the class metadata for this particular field
        PersistencePackageRequest ppr = PersistencePackageRequest.fromMetadata(fmd, sectionCrumbs);
        ClassMetadata cmd = adminEntityService.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();

        List<Field> headerFields = new ArrayList<Field>();
        ListGrid.Type type = null;
        boolean editable = false;
        boolean sortable = false;
        boolean readOnly = false;
        boolean hideIdColumn = false;
        boolean canFilterAndSort = true;
        String idProperty = cmd.getIdProperty();
        // Get the header fields for this list grid. Note that the header fields are different depending on the
        // kind of field this is.
        if( fmd instanceof BasicFieldMetadata ) {
            readOnly = ((BasicFieldMetadata) fmd).getReadOnly();
            for( Property p : cmd.getProperties()) {
                if( p.getFieldMetadata() instanceof BasicFieldMetadata ) {
                    BasicFieldMetadata md = (BasicFieldMetadata) p.getFieldMetadata();
                    if( SupportedFieldType.ID==md.getFieldType() ) {
                        idProperty = md.getName();
                    }
                    
                    if (md.isProminent() != null && md.isProminent() 
                            && !ArrayUtils.contains(getGridHiddenVisibilities(), md.getVisibility())) {
                        Field hf = createHeaderField(p, md);
                        headerFields.add(hf);
                    }
                }
            }

            type = ListGrid.Type.TO_ONE;
        } else if (fmd instanceof BasicCollectionMetadata) {
            readOnly = !((BasicCollectionMetadata) fmd).isMutable();
            for( Property p : cmd.getProperties() ) {
                if( p.getFieldMetadata() instanceof BasicFieldMetadata ) {
                    BasicFieldMetadata md = (BasicFieldMetadata) p.getFieldMetadata();
                    if( md.isProminent() != null && md.isProminent() 
                        && !ArrayUtils.contains( getGridHiddenVisibilities(), md.getVisibility() ) ) {
                        Field hf = createHeaderField(p, md);
                        headerFields.add( hf );
                    }
                }
            }

            type = ListGrid.Type.BASIC;
            
            if( ( (BasicCollectionMetadata) fmd).getAddMethodType() == AddMethodType.PERSIST ) {
                editable = true;
            }
        } else if (fmd instanceof AdornedTargetCollectionMetadata) {
        	AdornedTargetCollectionMetadata atcmd = (AdornedTargetCollectionMetadata) fmd;
        	readOnly = !atcmd.isMutable();

            for (String fieldName : atcmd.getGridVisibleFields()) {
                Property p = cmd.getPMap().get(fieldName);
                BasicFieldMetadata md = (BasicFieldMetadata) p.getFieldMetadata();
                
                Field hf = createHeaderField(p, md);
                headerFields.add(hf);
            }

            type = ListGrid.Type.ADORNED;

            if (atcmd.getMaintainedAdornedTargetFields().length > 0) {
                editable = true;
            }
            
            AdornedTargetList adornedList = (AdornedTargetList) atcmd.getPersistenceAssociation()
                    .getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST);
            sortable = StringUtils.isNotBlank( adornedList.getSortField() );
        } else if (fmd instanceof MapMetadata) {
        	MapMetadata mmd = (MapMetadata) fmd;
            readOnly = !mmd.isMutable();

            Property p2 = cmd.getPMap().get("key");
            BasicFieldMetadata keyMd = (BasicFieldMetadata) p2.getFieldMetadata();
            keyMd.setFriendlyName("Key");
            Field hf = createHeaderField(p2, keyMd);
            headerFields.add(hf);
            
            if (mmd.isSimpleValue()) {
                Property valueProperty = cmd.getPMap().get("value");
                BasicFieldMetadata valueMd = (BasicFieldMetadata) valueProperty.getFieldMetadata();
                valueMd.setFriendlyName("Value");
                hf = createHeaderField(valueProperty, valueMd);
                headerFields.add(hf);
                idProperty = "key";
                hideIdColumn = true;
            } else {
                for (Property p : cmd.getProperties()) {
                    if (p.getFieldMetadata() instanceof BasicFieldMetadata) {
                        BasicFieldMetadata md = (BasicFieldMetadata) p.getFieldMetadata();
                        if (md.getTargetClass().equals(mmd.getValueClassName())) {
                            if (md.isProminent() != null && md.isProminent() 
                                    && !ArrayUtils.contains(getGridHiddenVisibilities(), md.getVisibility())) {
                                hf = createHeaderField(p, md);
                                headerFields.add(hf);
                            }
                        }
                    }
                }
            }

            type = ListGrid.Type.MAP;
            editable = true;
            canFilterAndSort = false;
        }

        String ceilingType = "";
        if (fmd instanceof BasicFieldMetadata) {
            ceilingType = cmd.getClassName();
        } else if (fmd instanceof CollectionMetadata) {
            ceilingType = ((CollectionMetadata) fmd).getCollectionRootEntity();
        }
        
        if (CollectionUtils.isEmpty(headerFields)) {
            String message = "There are no listgrid header fields configured for the class " + ceilingType + " and property '" +
            	field.getName() + "'.";
            if (type == ListGrid.Type.ADORNED || type == ListGrid.Type.ADORNED_WITH_FORM) {
                message += " Please configure 'gridVisibleFields' in your @AdminPresentationAdornedTargetCollection configuration";
            } else {
                message += " Please mark some @AdminPresentation fields with 'prominent = true'";
            }
            LOG.error(message);
        }
        
        ListGrid listGrid = createListGrid(ceilingType, headerFields, type, drs, sectionKey, fmd.getOrder(), idProperty, sectionCrumbs);
        listGrid.setSubCollectionFieldName(field.getName());
        listGrid.setFriendlyName( field.getFieldMetadata().getFriendlyName() );
        if ( StringUtils.isEmpty(listGrid.getFriendlyName()) ) {
            listGrid.setFriendlyName(field.getName());
        }
        listGrid.setContainingEntityId(containingEntityId);
        listGrid.setReadOnly(readOnly);
        listGrid.setHideIdColumn(hideIdColumn);
        listGrid.setCanFilterAndSort(canFilterAndSort);
        
        if (editable) {
            listGrid.getRowActions().add(DefaultListGridActions.UPDATE);
        }
        if (readOnly) {
            listGrid.getRowActions().add(DefaultListGridActions.VIEW);
        }
        if (sortable) {
            listGrid.setCanFilterAndSort(false);
            listGrid.getToolbarActions().add(DefaultListGridActions.REORDER);
        }
        listGrid.getRowActions().add(DefaultListGridActions.REMOVE);

        return listGrid;
    }

    protected ListGrid createListGrid(String className, List<Field> headerFields, ListGrid.Type type, DynamicResultSet drs, 
            String sectionKey, int order, String idProperty, List<SectionCrumb> sectionCrumbs) {
        // Create the list grid and set some basic attributes
        ListGrid listGrid = new ListGrid();
        listGrid.setClassName(className);
        listGrid.getHeaderFields().addAll(headerFields);
        listGrid.setListGridType(type);
        listGrid.setSectionCrumbs(sectionCrumbs);
        listGrid.setSectionKey(sectionKey);
        listGrid.setOrder(order);
        listGrid.setIdProperty(idProperty);
        listGrid.setStartIndex(drs.getStartIndex());
        listGrid.setTotalRecords(drs.getTotalRecords());
        listGrid.setPageSize(drs.getPageSize());
        
        AdminSection section = navigationService.findAdminSectionByClass(className);
        if (section != null) {
            listGrid.setExternalEntitySectionKey(section.getUrl());
        }

        // For each of the entities (rows) in the list grid, we need to build the associated
        // ListGridRecord and set the required fields on the record. These fields are the same ones
        // that are used for the header fields.
        for (Entity e : drs.getRecords()) {
            ListGridRecord record = new ListGridRecord();
            record.setListGrid(listGrid);
            record.setDirty(e.isDirty());

            if (e.findProperty("hasError") != null) {
                Boolean hasError = Boolean.parseBoolean(e.findProperty("hasError").getValue());
                record.setIsError(hasError);
                record.setErrorKey("listgrid.record.error");
            }

            if (e.findProperty(idProperty) != null) {
                record.setId( e.findProperty(idProperty).getValue() );
            }

            for( Field headerField : headerFields ) {
                Property p = e.findProperty(headerField.getName());
                if (p != null) {
                    Field recordField = new Field().withName(headerField.getName())
                                                   .withFriendlyName(headerField.getFriendlyName())
                                                   .withOrder(p.getFieldMetadata().getOrder());
                    
                    if (headerField instanceof ComboField) {
                        recordField.setValue( ((ComboField) headerField).getOption(p.getValue()) );
                        recordField.setDisplayValue( p.getDisplayValue() );
                    } else {
                        recordField.setValue(p.getValue());
                        recordField.setDisplayValue(p.getDisplayValue());
                    }
                    
                    recordField.setDerived( isDerivedField(headerField, recordField, p) );
                    
                    record.getFields().add(recordField);
                }
            }

            if (e.findProperty(AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY) != null) {
                Field hiddenField = new Field().withName(AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY);
                hiddenField.setValue(e.findProperty(AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY).getValue());
                record.getHiddenFields().add(hiddenField);
            }

            if (e.findProperty(BasicPersistenceModule.ALTERNATE_ID_PROPERTY) != null) {
                record.setAltId(e.findProperty(BasicPersistenceModule.ALTERNATE_ID_PROPERTY).getValue());
            }
            
            extensionManager.getHandlerProxy().modifyListGridRecord(className, record, e);

            listGrid.getRecords().add(record);
        }

        return listGrid;
    }
    
    /**
     * Determines whether or not a particular field in a record is derived. By default this checks the {@link BasicFieldMetadata}
     * for the given Property to see if something on the backend has marked it as derived
     * 
     * @param headerField the header for this recordField
     * @param recordField the recordField being populated
     * @param p the property that relates to this recordField
     * @return whether or not this field is derived
     * @see {@link #createListGrid(String, List, cn.globalph.openadmin.web.form.component.ListGrid.Type, DynamicResultSet, String, int, String)}
     */
    protected Boolean isDerivedField(Field headerField, Field recordField, Property p) {
        return BooleanUtils.isTrue(((BasicFieldMetadata) p.getFieldMetadata()).getIsDerived());
    }

    protected void setEntityFormFields(EntityForm ef, List<Property> properties) {
        for (Property property : properties) {
            if (property.getFieldMetadata() instanceof BasicFieldMetadata) {
                BasicFieldMetadata fm = (BasicFieldMetadata) property.getFieldMetadata();
                
                if ( !ArrayUtils.contains(getFormHiddenVisibilities(), fm.getVisibility()) ) {
                    // Depending on visibility, field for the particular property is not created on the form
                	SupportedFieldType fieldType = fm.getFieldType() == null ? null : fm.getFieldType();
                    
                    // Create the field and set some basic attributes
                    Field ff;
                    
                    if ( fieldType==SupportedFieldType.BROADLEAF_ENUMERATION
                       ||fieldType==SupportedFieldType.EXPLICIT_ENUMERATION
                       ||fieldType==SupportedFieldType.DATA_DRIVEN_ENUMERATION
                       ||fieldType==SupportedFieldType.EMPTY_ENUMERATION ) {
                        // We're dealing with fields that should render as drop-downs, so set their possible values
                        ff = new ComboField();
                        ((ComboField) ff).setOptions( fm.getEnumerationValues() );
                    } else if ( fieldType==SupportedFieldType.CODE) {
                        ff = new CodeField();
                    } else if ( fieldType==SupportedFieldType.RULE_SIMPLE
                             || fieldType==SupportedFieldType.RULE_WITH_QUANTITY ) {
                        // We're dealing with rule builders, so we'll create those specialized fields
                        ff = new RuleBuilderField();
                        ((RuleBuilderField) ff).setJsonFieldName(property.getName() + "Json");
                        ((RuleBuilderField) ff).setDataWrapper( new DataWrapper() );
                        ((RuleBuilderField) ff).setFieldBuilder( fm.getRuleIdentifier() );
                        
                        String blankJsonString =  "{\"data\":[]}";
                        ((RuleBuilderField) ff).setJson(blankJsonString);
                        DataWrapper dw = convertJsonToDataWrapper(blankJsonString);
                        if (dw != null) {
                            ((RuleBuilderField) ff).setDataWrapper(dw);
                        }
                        
                        if ( fieldType==SupportedFieldType.RULE_SIMPLE ) {
                            ((RuleBuilderField) ff).setStyleClass("rule-builder-simple");
                        } else if ( fieldType==SupportedFieldType.RULE_WITH_QUANTITY ) {
                            ((RuleBuilderField) ff).setStyleClass("rule-builder-complex");
                        }
                    } else if (LookupType.DROPDOWN.equals(fm.getLookupType())) {
                        // We're dealing with a to-one field that wants to be rendered as a dropdown instead of in a 
                        // modal, so we'll provision the combo field here. Available options will be set as part of a
                        // subsequent operation
                        ff = new ComboField();
                    } else if (fieldType==SupportedFieldType.MEDIA ) {
                        ff = new MediaField();
                    } else {
                        // Create a default field since there was no specialized handler
                        ff = new Field();
                    }
                    
                    Boolean required = fm.getRequiredOverride();
                    if (required == null) {
                        required = fm.getRequired();
                    }

                    ff.withName( property.getName() )
                      .withFieldType( fieldType.toString() )
                      .withOrder( fm.getOrder() )
                      .withFriendlyName( fm.getFriendlyName() )
                      .withForeignKeyDisplayValueProperty( fm.getForeignKeyDisplayValueProperty() )
                      .withForeignKeyClass( fm.getForeignKeyClass() )
                      .withOwningEntityClass( fm.getOwningClass()!=null?fm.getOwningClass():fm.getInheritedFromType())
                      .withRequired( required )
                      .withReadOnly( fm.getReadOnly() )
                      .withTranslatable( fm.getTranslatable() )
                      .withAlternateOrdering( (Boolean) fm.getAdditionalMetadata().get(Field.ALTERNATE_ORDERING))
                      .withLargeEntry( fm.isLargeEntry() )
                      .withHint( fm.getHint() )
                      .withTooltip( fm.getTooltip() )
                      .withHelp( fm.getHelpText() )
                      .withTypeaheadEnabled(fm.getEnableTypeaheadLookup());

                    if (StringUtils.isBlank( ff.getFriendlyName() ) ) {
                        ff.setFriendlyName( ff.getName() );
                    }

                    // Add the field to the appropriate FieldGroup
                    ef.addField(ff, fm.getGroup(), fm.getGroupOrder(), fm.getTab(), fm.getTabOrder());
                }
            }
        }
    }
    
    @Override
    public void removeNonApplicableFields(ClassMetadata cmd, EntityForm entityForm, String entityType) {
        for (Property p : cmd.getProperties()) {
            if (!ArrayUtils.contains(p.getFieldMetadata().getAvailableToTypes(), entityType)) {
                entityForm.removeField(p.getName());
            }
        }
    }

    @Override
    public EntityForm createEntityForm(ClassMetadata cmd, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {
        EntityForm ef = createStandardEntityForm();
        populateEntityForm(cmd, ef, sectionCrumbs);
        return ef;
    }
    
    @Override
    public void populateEntityForm(ClassMetadata cm, EntityForm ef, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {
        ef.setRootEntityClassname( cm.getClassName() );
        
        AdminSection section = navigationService.findAdminSectionByClass( cm.getClassName() );
        if (section != null) {
            ef.setSectionKey(section.getUrl());
        } else {
            ef.setSectionKey(cm.getClassName());
        }
        ef.setSectionCrumbsImpl(sectionCrumbs);

        setEntityFormFields(ef, Arrays.asList(cm.getProperties()));
        
        populateDropdownToOneFields(ef, cm);
        
        extensionManager.getHandlerProxy().modifyUnpopulatedEntityForm(ef);
    }
    
    /**
     * This method is invoked when EntityForms are created and is meant to provide a hook to add
     * additional entity form actions for implementors of Broadleaf. Broadleaf modules will typically
     * leverage {@link FormBuilderExtensionHandler#addAdditionalFormActions(EntityForm)} method.
     * @param ef
     */
    protected void addAdditionalFormActions(EntityForm ef) {
        
    }
    
    @Override
    public EntityForm createEntityForm(ClassMetadata cmd, Entity entity, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {
        EntityForm ef = createStandardEntityForm();
        populateEntityForm(cmd, entity, ef, sectionCrumbs);
        addAdditionalFormActions(ef);
        extensionManager.getHandlerProxy().addAdditionalFormActions(ef);
        return ef;
    }

    @Override
    public void populateEntityForm(ClassMetadata cmd, Entity entity, EntityForm ef, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {
        // Get the empty form with appropriate fields
        populateEntityForm(cmd, ef, sectionCrumbs);
        String idProperty = cmd.getIdProperty();
        ef.setId( entity.findProperty(idProperty).getValue() );
        ef.setEntityType( entity.getType()[0] );

        populateEntityFormFieldValues(cmd, entity, ef);
        
        Property p = entity.findProperty(BasicPersistenceModule.MAIN_ENTITY_NAME_PROPERTY);
        if (p != null) {
            ef.setMainEntityName(p.getValue());
        }
        
        extensionManager.getHandlerProxy().modifyPopulatedEntityForm(ef, entity);
    }

    @Override
    public void populateEntityFormFieldValues(ClassMetadata cmd, Entity entity, EntityForm ef) {
        // Set the appropriate property values
        for (Property p : cmd.getProperties()) {
            if (p.getFieldMetadata() instanceof BasicFieldMetadata) {
                BasicFieldMetadata basicFM = (BasicFieldMetadata) p.getFieldMetadata();

                Property entityProp = entity.findProperty(p.getName());
                
                boolean explicitlyShown = VisibilityEnum.FORM_EXPLICITLY_SHOWN==basicFM.getVisibility();
                //always show special map fields
                if ( p.getName().equals("key") || p.getName().equals("priorKey") ) {
                    explicitlyShown = true;
                }
                
                if (entityProp == null && explicitlyShown) {
                    Field field = ef.findField(p.getName());
                    if (field != null) {
                        field.setValue(null);
                    }
                } else if (entityProp == null && SupportedFieldType.PASSWORD_CONFIRM!=basicFM.getExplicitFieldType()) {
                    ef.removeField(p.getName());
                } else {
                    Field field = ef.findField(p.getName());
                    if (field != null) {
                        if (entityProp != null) {
                            //protect against null - can happen with password confirmation fields (i.e. admin user)
                            field.setDirty(entityProp.getIsDirty());
                        }
                        if( basicFM.getFieldType()==SupportedFieldType.RULE_SIMPLE
                           || basicFM.getFieldType()==SupportedFieldType.RULE_WITH_QUANTITY ) {
                            RuleBuilderField rbf = (RuleBuilderField) field;
                            if ( entity.getPMap().containsKey( rbf.getJsonFieldName() ) ) {
                                String json = entity.getPMap().get(rbf.getJsonFieldName()).getValue();
                                rbf.setJson(json);
                                DataWrapper dw = convertJsonToDataWrapper(json);
                                if (dw != null) {
                                    rbf.setDataWrapper(dw);
                                }
                            }
                        } 
                        if (basicFM.getFieldType() == SupportedFieldType.MEDIA) {
                            field.setValue( entityProp.getValue() );
                            field.setDisplayValue( entityProp.getDisplayValue() );
                            MediaField mf = (MediaField) field;
                            mf.setMedia( convertJsonToMedia( entityProp.getUnHtmlEncodedValue() ) );
                        } else if (!SupportedFieldType.PASSWORD_CONFIRM.equals(basicFM.getExplicitFieldType())){
                            field.setValue(entityProp.getValue());
                            field.setDisplayValue(entityProp.getDisplayValue());
                        }
                    }
                }
            }
        }
    }

    protected Media convertJsonToMedia(String json) {
        if (json != null && !"".equals(json)) {
            try {
                ObjectMapper om = new ObjectMapper();
                om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return om.readValue(json, entityConfiguration.lookupEntityClass(MediaDto.class.getName(), MediaDto.class));
            } catch (Exception e) {
                LOG.warn("Error parsing json to media " + json, e);
            }
        }
        return entityConfiguration.createEntityInstance(MediaDto.class.getName(), MediaDto.class);
    }

    /**
     * When using Thymeleaf, we need to convert the JSON string back to
     * a DataWrapper object because Thymeleaf escapes JSON strings.
     * Thymeleaf uses it's own object de-serializer
     * see: https://github.com/thymeleaf/thymeleaf/issues/84
     * see: http://forum.thymeleaf.org/Spring-Javascript-and-escaped-JSON-td4024739.html
     * @param json
     * @return DataWrapper
     * @throws IOException
     */
    protected DataWrapper convertJsonToDataWrapper(String json) {
        ObjectMapper mapper = new ObjectMapper();
        DataDTODeserializer dtoDeserializer = new DataDTODeserializer();
        SimpleModule module = new SimpleModule("DataDTODeserializerModule", new Version(1, 0, 0, null));
        module.addDeserializer(DataDTO.class, dtoDeserializer);
        mapper.registerModule(module);
        try {
            return mapper.readValue(json, DataWrapper.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void populateDropdownToOneFields(EntityForm ef, ClassMetadata cmd) 
            throws ServiceException {
        for (Property p : cmd.getProperties()) {
            if (p.getFieldMetadata() instanceof BasicFieldMetadata) {
                BasicFieldMetadata fmd = (BasicFieldMetadata) p.getFieldMetadata();
                if ( LookupType.DROPDOWN==fmd.getLookupType() 
                        && !ArrayUtils.contains(getFormHiddenVisibilities(), fmd.getVisibility())) {
                    // Get the records
                    PersistencePackageRequest toOnePpr = PersistencePackageRequest.standard()
                                                         	.withRootEntityClassname(fmd.getForeignKeyClass());
                    // Determine the id field
                    String idProp = null;
                    ClassMetadata foreignCM = adminEntityService.getPersistenceResponse(toOnePpr).getDynamicResultSet().getClassMetaData();
                    for ( Property foreignP : foreignCM.getProperties() ) {
                        if (foreignP.getFieldMetadata() instanceof BasicFieldMetadata) {
                            BasicFieldMetadata foreignFmd = (BasicFieldMetadata) foreignP.getFieldMetadata();
                            if ( SupportedFieldType.ID==foreignFmd.getFieldType() ) {
                                idProp = foreignP.getName();
                                break;
                            }
                        }
                    }
                    
                    if (idProp == null) {
                        throw new RuntimeException("Could not determine ID property for " + fmd.getForeignKeyClass());
                    }
                    
                    // Determine the display field
                    String displayProp = fmd.getLookupDisplayProperty();
                    
                    // Build the options map
                    Entity[] rows = adminEntityService.getRecords(toOnePpr).getDynamicResultSet().getRecords();
                    Map<String, String> options = new HashMap<String, String>();
                    for (Entity row : rows) {
                        String displayValue = row.findProperty(displayProp).getDisplayValue();
                        if (StringUtils.isBlank(displayValue)) {
                            displayValue = row.findProperty(displayProp).getValue();
                        }
                        options.put(row.findProperty(idProp).getValue(), displayValue);
                    }
                    
                    // Set the options on the entity field
                    ComboField cf = (ComboField) ef.findField(p.getName());
                    cf.setOptions(options);
                }
            }
        }
    }

    @Override
    public EntityForm createEntityForm(ClassMetadata cmd, Entity entity, Map<String, DynamicResultSet> collectionRecords, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {
        EntityForm ef = createStandardEntityForm();
        populateEntityForm(cmd, entity, collectionRecords, ef, sectionCrumbs);
        addAdditionalFormActions(ef);
        extensionManager.getHandlerProxy().addAdditionalFormActions(ef);
        return ef;
    }
    
    @Override
    public void populateEntityForm(ClassMetadata cmd, Entity entity, Map<String, DynamicResultSet> collectionRecords, EntityForm ef, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {
        // Get the form with values for this entity
        populateEntityForm(cmd, entity, ef, sectionCrumbs);
        
        // Attach the sub-collection list grids and specialty UI support
        for (Property p : cmd.getProperties()) {
            if (p.getFieldMetadata() instanceof BasicFieldMetadata) {
                continue;
            }
            
            if (!ArrayUtils.contains(p.getFieldMetadata().getAvailableToTypes(), entity.getType()[0])) {
                continue;
            }

            if (collectionRecords != null) {
                DynamicResultSet subCollectionEntities = collectionRecords.get( p.getName() );
                String containingEntityId = entity.getPMap().get( ef.getIdProperty() ).getValue();
                ListGrid listGrid = buildCollectionListGrid(containingEntityId, subCollectionEntities, p, ef.getSectionKey(), sectionCrumbs);
                listGrid.setListGridType( ListGrid.Type.INLINE );

                CollectionMetadata md = ((CollectionMetadata) p.getFieldMetadata());
                ef.addListGrid(listGrid, md.getTab(), md.getTabOrder());
            }
        }
        
        for (ListGrid lg : ef.getAllListGrids()) {
            // We always want the add option to be the first toolbar action for consistency
            if (lg.getToolbarActions().isEmpty()) {
                lg.addToolbarAction(DefaultListGridActions.ADD);
            } else {
                lg.getToolbarActions().add(0, DefaultListGridActions.ADD);
            }
        }
        
        if (CollectionUtils.isEmpty(ef.getActions())) {
            ef.addAction(DefaultEntityFormActions.SAVE);
        }
        
        addDeleteActionIfAllowed(ef, cmd, entity);
        setReadOnlyState(ef, cmd, entity);
        
        extensionManager.getHandlerProxy().modifyDetailEntityForm(ef);
    }
    
    /**
     * Adds the {@link DefaultEntityFormActions#DELETE} if the user is allowed to delete the <b>entity</b>. The user can
     * delete an entity for the following cases:
     * <ol>
     *  <li>The user has the security to {@link EntityOperationType#DELETE} the given class name represented by
     *  the <b>entityForm</b> (determined by {@link #getSecurityClassname(EntityForm, ClassMetadata)})</li>
     *  <li>The user has the security necessary to delete the given <b>entity</b> according to the
     *  {@link RowLevelSecurityService#canDelete(Entity)}</li>
     * </ol>
     * 
     * @param entityForm the form being generated
     * @param cmd the metatadata used to build the <b>entityForm</b> for the <b>entity</b>
     * @param entity the entity being edited
     * @see {@link SecurityVerifier#securityCheck(String, EntityOperationType)}
     * @see {@link #getSecurityClassname(EntityForm, ClassMetadata)}
     * @see {@link RowLevelSecurityService#canDelete(Entity)}
     */
    protected void addDeleteActionIfAllowed(EntityForm entityForm, ClassMetadata cmd, Entity entity) {
        boolean canDelete = true;
        try {
            String securityEntityClassname = getSecurityClassname(entityForm, cmd);
            adminRemoteSecurityService.securityCheck(securityEntityClassname, EntityOperationType.REMOVE);
        } catch (ServiceException e) {
            if (e instanceof SecurityServiceException) {
                canDelete = false;
            }
        }
        
        // If I cannot update a record then I certainly cannot delete it either
        if (canDelete) {
            canDelete = rowLevelSecurityService.canUpdate(adminRemoteSecurityService.getPersistentAdminUser(), entity);
        }
        
        if (canDelete) {
            canDelete = rowLevelSecurityService.canRemove(adminRemoteSecurityService.getPersistentAdminUser(), entity);
        }
        
        if (canDelete) {
            entityForm.addAction(DefaultEntityFormActions.DELETE);
        }
    }
    
    /**
     * The given <b>entityForm</b> is marked as readonly for the following cases:
     * <ol>
     *  <li>All of the properties from <b>cmd</b> are readonly</b></li>
     *  <li>The user does not have the security to {@link EntityOperationType#UPDATE} the given class name represented by
     *  the <b>entityForm</b> (determined by {@link #getSecurityClassname(EntityForm, ClassMetadata)})</li>
     *  <li>The user does not have the security necessary to modify the given <b>entity</b> according to the
     *  {@link RowLevelSecurityService#canUpdate(Entity)}</li>
     * </ol>
     * 
     * @param entityForm the form being generated
     * @param cmd the metatadata used to build the <b>entityForm</b> for the <b>entity</b>
     * @param entity the entity being edited
     * @see {@link SecurityVerifier#securityCheck(String, EntityOperationType)}
     * @see {@link #getSecurityClassname(EntityForm, ClassMetadata)}
     * @see {@link RowLevelSecurityService#canUpdate(Entity)}
     */
    protected void setReadOnlyState(EntityForm entityForm, ClassMetadata cmd, Entity entity) {
        boolean readOnly = true;
        
        // If all of the fields are read only, we'll mark the form as such
        for (Property property : cmd.getProperties()) {
            FieldMetadata fieldMetadata = property.getFieldMetadata();
            if (fieldMetadata instanceof BasicFieldMetadata) {
                readOnly = ((BasicFieldMetadata) fieldMetadata).getReadOnly() != null && ((BasicFieldMetadata) fieldMetadata).getReadOnly();
                if (!readOnly) {
                    break;
                }
            } else {
                readOnly = ((CollectionMetadata) fieldMetadata).isMutable();
                if (!readOnly) {
                    break;
                }
            }
        }

        if (!readOnly) {
            // If the user does not have edit permissions, we will go ahead and make the form read only to prevent confusion
            try {
                String securityEntityClassname = getSecurityClassname(entityForm, cmd);
                adminRemoteSecurityService.securityCheck(securityEntityClassname, EntityOperationType.UPDATE);
            } catch (ServiceException e) {
                if (e instanceof SecurityServiceException) {
                    readOnly = true;
                }
            }
        }
        
        // if the normal admin security service has not deemed this readonly and the all of the properties on the entity
        // are not readonly, then check the row-level security
        if (!readOnly) {
            readOnly = !rowLevelSecurityService.canUpdate(adminRemoteSecurityService.getPersistentAdminUser(), entity);
        }

        if (readOnly) {
            entityForm.setReadOnly();
        }
    }
    
    /**
     * Obtains the class name suitable for passing along to the {@link SecurityVerifier}
     * @param form
     * @param cmd
     * @return
     */
    protected String getSecurityClassname(EntityForm entityForm, ClassMetadata cmd) {
        String securityEntityClassname = entityForm.getRootEntityClassname();

        if (!StringUtils.isEmpty(cmd.getSecurityCeilingType())) {
            securityEntityClassname = cmd.getSecurityCeilingType();
        } else {
            if (entityForm.getDynamicFormInfos() != null) {
                for (DynamicEntityFormInfo info : entityForm.getDynamicFormInfos().values()) {
                    if (!StringUtils.isEmpty(info.getSecurityCeilingClassName())) {
                        securityEntityClassname = info.getSecurityCeilingClassName();
                        break;
                    }
                }
            }
        }
        
        return securityEntityClassname;
    }
    
    @Override
    public void populateEntityFormFields(EntityForm ef, Entity entity) {
        populateEntityFormFields(ef, entity, true, true);
    }

    @Override
    public void populateEntityFormFields(EntityForm ef, Entity entity, boolean populateType, boolean populateId) {
        if (populateId) {
            ef.setId(entity.findProperty(ef.getIdProperty()).getValue());
        }
        if (populateType) {
            ef.setEntityType(entity.getType()[0]);
        }

        for (Entry<String, Field> entry : ef.getFields().entrySet()) {
            Property entityProp = entity.findProperty(entry.getKey());
            if (entityProp != null) {
                entry.getValue().setValue(entityProp.getValue());
                entry.getValue().setDisplayValue(entityProp.getDisplayValue());
            }
        }
    }

    @Override
    public void populateAdornedEntityFormFields(EntityForm ef, Entity entity, AdornedTargetList adornedList) {
        Field field = ef.getFields().get(adornedList.getTargetObjectPath() + "." + adornedList.getTargetIdProperty());
        Property entityProp = entity.findProperty(ef.getIdProperty());
        field.setValue(entityProp.getValue());

        if (StringUtils.isNotBlank(adornedList.getSortField())) {
            field = ef.getFields().get(adornedList.getSortField());
            entityProp = entity.findProperty(adornedList.getSortField());
            if (field != null && entityProp != null) {
                field.setValue(entityProp.getValue());
            }
        }
    }

    @Override
    public void populateMapEntityFormFields(EntityForm ef, Entity entity) {
        Field field = ef.getFields().get("priorKey");
        Property entityProp = entity.findProperty("key");
        if (field != null && entityProp != null) {
            field.setValue(entityProp.getValue());
        }
    }

    @Override
    public EntityForm buildAdornedListForm(AdornedTargetCollectionMetadata adornedMd, AdornedTargetList adornedList,
            String parentId)
            throws ServiceException {
        EntityForm ef = createStandardEntityForm();
        return buildAdornedListForm(adornedMd, adornedList, parentId, ef);
    }
    
    @Override
    public EntityForm buildAdornedListForm(AdornedTargetCollectionMetadata adornedMd, AdornedTargetList adornedList,
            String parentId, EntityForm ef)
            throws ServiceException {
        ef.setEntityType(adornedList.getAdornedTargetEntityClassname());

        // Get the metadata for this adorned field
        PersistencePackageRequest request = PersistencePackageRequest.adorned()
                .withRootEntityClassname(adornedMd.getCollectionRootEntity())
                .withAdornedList(adornedList);
        ClassMetadata collectionMetadata = adminEntityService.getPersistenceResponse(request).getDynamicResultSet().getClassMetaData();

        // We want our entity form to only render the maintained adorned target fields
        List<Property> entityFormProperties = new ArrayList<Property>();
        for (String targetFieldName : adornedMd.getMaintainedAdornedTargetFields()) {
            Property p = collectionMetadata.getPMap().get(targetFieldName);
            if (p.getFieldMetadata() instanceof BasicFieldMetadata) {
                ((BasicFieldMetadata) p.getFieldMetadata()).setVisibility(VisibilityEnum.VISIBLE_ALL);
                entityFormProperties.add(p);
            }
        }

        // Set the maintained fields on the form
        setEntityFormFields(ef, entityFormProperties);

        // Add these two additional hidden fields that are required for persistence
        Field f = new Field()
                .withName(adornedList.getLinkedObjectPath() + "." + adornedList.getLinkedIdProperty())
                .withFieldType(SupportedFieldType.HIDDEN.toString())
                .withValue(parentId);
        ef.addHiddenField(f);

        f = new Field()
                .withName(adornedList.getTargetObjectPath() + "." + adornedList.getTargetIdProperty())
                .withFieldType(SupportedFieldType.HIDDEN.toString())
                .withIdOverride("adornedTargetIdProperty");
        ef.addHiddenField(f);

        if (StringUtils.isNotBlank(adornedList.getSortField())) {
            f = new Field()
                    .withName(adornedList.getSortField())
                    .withFieldType(SupportedFieldType.HIDDEN.toString());
            ef.addHiddenField(f);
        }

        ef.setParentId(parentId);

        return ef;
    }


    @Override
    public EntityForm buildMapForm(MapMetadata mapMd, final MapStructure mapStructure, ClassMetadata cmd, String parentId)
            throws ServiceException {
        EntityForm ef = createStandardEntityForm();
        return buildMapForm(mapMd, mapStructure, cmd, parentId, ef);
    }
    
    @Override
    public EntityForm buildMapForm(MapMetadata mapMd, final MapStructure mapStructure, ClassMetadata cmd, String parentId, EntityForm ef)
            throws ServiceException {
        ForeignKey foreignKey = (ForeignKey) mapMd.getPersistenceAssociation()
                .getPersistenceAssociationItems().get(PersistenceAssociationItemType.FOREIGNKEY);
        ef.setEntityType(foreignKey.getForeignKeyClass());

        Field keyField;
        if (!mapMd.getForceFreeFormKeys()) {
            // We will use a combo field to render the key choices
            ComboField temp = new ComboField();
            temp.withName("key")
                    .withFieldType("combo_field")
                    .withFriendlyName("Key");
            if (mapMd.getKeys() != null) {
                // The keys can be explicitly set in the annotation...
                temp.setOptions(mapMd.getKeys());
            } else {
                // Or they could be based on a different entity
                PersistencePackageRequest ppr = PersistencePackageRequest.standard()
                        .withRootEntityClassname(mapMd.getMapKeyOptionEntityClass());

                DynamicResultSet drs = adminEntityService.getRecords(ppr).getDynamicResultSet();
    
                for (Entity entity : drs.getRecords()) {
                    String keyValue = entity.getPMap().get(mapMd.getMapKeyOptionEntityValueField()).getValue();
                    String keyDisplayValue = entity.getPMap().get(mapMd.getMapKeyOptionEntityDisplayField()).getValue();
                    temp.putOption(keyValue, keyDisplayValue);
                }
            }
            keyField = temp;
        } else {
            keyField = new Field().withName("key")
                                .withFieldType(SupportedFieldType.STRING.toString())
                                .withFriendlyName("Key");
        }
        keyField.setRequired(true);
        ef.addMapKeyField(keyField);
        
        // Set the fields for this form
        List<Property> mapFormProperties;
        if (mapMd.isSimpleValue()) {
            ef.setIdProperty("key");
            mapFormProperties = new ArrayList<Property>();
            Property valueProp = cmd.getPMap().get("value");
            mapFormProperties.add(valueProp);
        } else {
            mapFormProperties = new ArrayList<Property>(Arrays.asList(cmd.getProperties()));
            CollectionUtils.filter(mapFormProperties, new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    Property p = (Property) object;
                    return ArrayUtils.contains(p.getFieldMetadata().getAvailableToTypes(), mapStructure.getValueClassName());
                }
            });
        }

        setEntityFormFields(ef, mapFormProperties);

        Field f = new Field()
                .withName("priorKey")
                .withFieldType(SupportedFieldType.HIDDEN.toString());
        ef.addHiddenField(f);

        ef.setParentId(parentId);

        return ef;
    }
    
    protected EntityForm createStandardEntityForm() {
        EntityForm ef = new EntityForm();
        ef.addAction(DefaultEntityFormActions.SAVE);
        return ef;
    }
    
    protected VisibilityEnum[] getGridHiddenVisibilities() {
        return DynamicFormBuilderImpl.GRID_HIDDEN_VISIBILITIES;
    }
    
    protected VisibilityEnum[] getFormHiddenVisibilities() {
        return DynamicFormBuilderImpl.FORM_HIDDEN_VISIBILITIES;
    }

}
