package cn.globalph.openadmin.server.service.persistence.module.provider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.client.ForeignKeyRestrictionType;
import cn.globalph.common.presentation.client.PersistenceAssociationItemType;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.FilterAndSortCriteria;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationException;
import cn.globalph.openadmin.server.service.persistence.module.EmptyFilterValues;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;
import cn.globalph.openadmin.server.service.persistence.module.FieldNotAvailableException;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FieldPath;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FilterMapping;
import cn.globalph.openadmin.server.service.persistence.module.criteria.Restriction;
import cn.globalph.openadmin.server.service.persistence.module.criteria.RestrictionType;
import cn.globalph.openadmin.server.service.persistence.module.criteria.predicate.IsNotNullPredicateProvider;
import cn.globalph.openadmin.server.service.persistence.module.criteria.predicate.IsNullPredicateProvider;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.AddSearchMappingRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jeff Fischer
 */
@Component("blBasicFieldPersistenceProvider")
@Scope("prototype")
public class BasicFieldPersistenceProvider extends FieldPersistenceProviderAdapter {

    protected static final Log LOG = LogFactory.getLog(BasicFieldPersistenceProvider.class);

    protected boolean canHandlePersistence(PopulateValueRequest populateValueRequest, Serializable instance) {
        BasicFieldMetadata metadata = populateValueRequest.getMetadata();
        Property property = populateValueRequest.getProperty();
        //don't handle map fields here - we'll get them in a separate provider
        boolean response = detectBasicType(metadata, property);
        if (!response) {
            //we'll allow this provider to handle money filter mapping for persistence
            response = metadata.getFieldType() == SupportedFieldType.MONEY;
        }
        return response;
    }

    protected boolean detectBasicType(BasicFieldMetadata metadata, Property property) {
        return (metadata.getFieldType() == SupportedFieldType.BOOLEAN ||
                metadata.getFieldType() == SupportedFieldType.DATE ||
                metadata.getFieldType() == SupportedFieldType.INTEGER ||
                metadata.getFieldType() == SupportedFieldType.DECIMAL ||
                metadata.getFieldType() == SupportedFieldType.EMAIL ||
                metadata.getFieldType() == SupportedFieldType.FOREIGN_KEY ||
                metadata.getFieldType() == SupportedFieldType.ADDITIONAL_FOREIGN_KEY ||
                metadata.getFieldType() == SupportedFieldType.STRING ||
                metadata.getFieldType() == SupportedFieldType.CODE ||
                metadata.getFieldType() == SupportedFieldType.HTML ||
                metadata.getFieldType() == SupportedFieldType.HTML_BASIC ||
                metadata.getFieldType() == SupportedFieldType.ID) &&
                (property == null ||
                        !property.getName().contains(FieldManager.MAPFIELDSEPARATOR));
    }
    
    protected boolean detectAdditionalSearchTypes(BasicFieldMetadata metadata, Property property) {
        return ( metadata.getFieldType() == SupportedFieldType.BROADLEAF_ENUMERATION ||
                 metadata.getFieldType() == SupportedFieldType.EXPLICIT_ENUMERATION ||
                 metadata.getFieldType() == SupportedFieldType.DATA_DRIVEN_ENUMERATION) &&
                (property == null || !property.getName().contains(FieldManager.MAPFIELDSEPARATOR));
    }

    protected boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property) {
        BasicFieldMetadata metadata = extractValueRequest.getMetadata();
        //don't handle map fields here - we'll get them in a separate provider
        return detectBasicType(metadata, property);
    }

    protected boolean canHandleSearchMapping(AddSearchMappingRequest addSearchMappingRequest,
                                             List<FilterMapping> filterMappings) {
        BasicFieldMetadata metadata = (BasicFieldMetadata) addSearchMappingRequest.getMergedProperties().get
                (addSearchMappingRequest.getPropertyName());
        Property property = null;
        //don't handle map fields here - we'll get them in a separate provider
        boolean response = detectBasicType(metadata, property) || detectAdditionalSearchTypes(metadata, property);
        if (!response) {
            //we'll allow this provider to handle money filter mapping for searches
            response = metadata.getFieldType() == SupportedFieldType.MONEY;
        }

        return response;
    }

    @Override
    public FieldProviderResponse populateValue(PopulateValueRequest populateValueRequest, Serializable instance) {
        if (!canHandlePersistence(populateValueRequest, instance)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        boolean dirty = false;
        try {
            Property prop = populateValueRequest.getProperty();
            Object origValue = populateValueRequest.getFieldManager().getFieldValue(instance, prop.getName());
            switch (populateValueRequest.getMetadata().getFieldType()) {
                case BOOLEAN:
                    boolean v = Boolean.valueOf(populateValueRequest.getRequestedValue());
                    prop.setOriginalValue(String.valueOf(origValue));
                    prop.setOriginalDisplayValue(prop.getOriginalValue());
                    try {
                        dirty = checkDirtyState(populateValueRequest, instance, v);
                        populateValueRequest.getFieldManager().setFieldValue(instance,
                                populateValueRequest.getProperty().getName(), v);
                    } catch (IllegalArgumentException e) {
                        char c = v ? 'Y' : 'N';
                        dirty = checkDirtyState(populateValueRequest, instance, c);
                        populateValueRequest.getFieldManager().setFieldValue(instance,
                                populateValueRequest.getProperty().getName(), c);
                    }
                    break;
                case DATE:
                    Date date = (Date) populateValueRequest.getFieldManager().getFieldValue(instance, populateValueRequest.getProperty().getName());
                    String oldValue = null;
                    if (date != null) {
                        oldValue = populateValueRequest.getDataFormatProvider().getSimpleDateFormatter().format(date);
                    }
                    prop.setOriginalValue(oldValue);
                    prop.setOriginalDisplayValue(prop.getOriginalValue());
                    dirty = !StringUtils.equals(oldValue, populateValueRequest.getRequestedValue());
                    populateValueRequest.getFieldManager().setFieldValue(instance,
                            populateValueRequest.getProperty().getName(), populateValueRequest.getDataFormatProvider().
                            getSimpleDateFormatter().parse(populateValueRequest.getRequestedValue()));
                    break;
                case DECIMAL:
                    if (origValue != null) {
                        prop.setOriginalValue(String.valueOf(origValue));
                        prop.setOriginalDisplayValue(prop.getOriginalValue());
                    }
                    if (BigDecimal.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        dirty = checkDirtyState(populateValueRequest, instance, new BigDecimal(populateValueRequest.getRequestedValue()));

                        populateValueRequest.getFieldManager().setFieldValue(instance,
                                populateValueRequest.getProperty().getName(), new BigDecimal(populateValueRequest.getRequestedValue()));
                    } else {
                        dirty = checkDirtyState(populateValueRequest, instance, new Double(populateValueRequest.getRequestedValue()));
                        
                        populateValueRequest.getFieldManager().setFieldValue(instance, populateValueRequest.getProperty().getName(), new Double(populateValueRequest.getRequestedValue()));
                    }
                    break;
                case MONEY:
                    if (origValue != null) {
                        prop.setOriginalValue(String.valueOf(origValue));
                        prop.setOriginalDisplayValue(prop.getOriginalValue());
                    }
                    if (BigDecimal.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        dirty = checkDirtyState(populateValueRequest, instance, new BigDecimal(populateValueRequest.getRequestedValue()));
                        
                        populateValueRequest.getFieldManager().setFieldValue(instance, populateValueRequest.getProperty().getName(), new BigDecimal(populateValueRequest.getRequestedValue()));
                    } else if (Double.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        dirty = checkDirtyState(populateValueRequest, instance, new BigDecimal(populateValueRequest.getRequestedValue()));
                        
                        LOG.warn("The requested Money field is of type double and could result in a loss of precision." +
                        		" Broadleaf recommends that the type of all Money fields are 'BigDecimal' in order to avoid" +
                        		" this loss of precision that could occur.");
                        populateValueRequest.getFieldManager().setFieldValue(instance, populateValueRequest.getProperty().getName(), new Double(populateValueRequest.getRequestedValue()));
                    } else {
                        dirty = checkDirtyState(populateValueRequest, instance, new BigDecimal(populateValueRequest.getRequestedValue()));

                        populateValueRequest.getFieldManager().setFieldValue(instance, populateValueRequest.getProperty().getName(), new Money(new BigDecimal(populateValueRequest.getRequestedValue())));
                    }
                    break;
                case INTEGER:
                    if (origValue != null) {
                        prop.setOriginalValue(String.valueOf(origValue));
                        prop.setOriginalDisplayValue(prop.getOriginalValue());
                    }
                    if (int.class.isAssignableFrom(populateValueRequest.getReturnType()) || Integer.class
                            .isAssignableFrom(populateValueRequest.getReturnType())) {
                        dirty = checkDirtyState(populateValueRequest, instance, Integer.valueOf(populateValueRequest.getRequestedValue()));
                        populateValueRequest.getFieldManager().setFieldValue(instance,
                                populateValueRequest.getProperty().getName(), Integer.valueOf(populateValueRequest
                                .getRequestedValue()));
                    } else if (byte.class.isAssignableFrom(populateValueRequest.getReturnType()) || Byte.class
                            .isAssignableFrom(populateValueRequest.getReturnType())) {
                        dirty = checkDirtyState(populateValueRequest, instance, Byte.valueOf(populateValueRequest.getRequestedValue()));
                        populateValueRequest.getFieldManager().setFieldValue(instance,
                                populateValueRequest.getProperty().getName(), Byte.valueOf(populateValueRequest
                                .getRequestedValue()));
                    } else if (short.class.isAssignableFrom(populateValueRequest.getReturnType()) || Short.class
                            .isAssignableFrom(populateValueRequest.getReturnType())) {
                        dirty = checkDirtyState(populateValueRequest, instance, Short.valueOf(populateValueRequest.getRequestedValue()));
                        populateValueRequest.getFieldManager().setFieldValue(instance,
                                populateValueRequest.getProperty().getName(), Short.valueOf(populateValueRequest
                                .getRequestedValue()));
                    } else if (long.class.isAssignableFrom(populateValueRequest.getReturnType()) || Long.class
                            .isAssignableFrom(populateValueRequest.getReturnType())) {
                        dirty = checkDirtyState(populateValueRequest, instance, Long.valueOf(populateValueRequest.getRequestedValue()));
                        populateValueRequest.getFieldManager().setFieldValue(instance,
                                populateValueRequest.getProperty().getName(), Long.valueOf(populateValueRequest
                                .getRequestedValue()));
                    }
                    break;
                case CODE:
                    // **NOTE** We want to fall through in this case, do not break.
                    setNonDisplayableValues(populateValueRequest);
                case STRING:
                case HTML_BASIC:
                case HTML:
                case EMAIL:
                    if (origValue != null) {
                        prop.setOriginalValue(String.valueOf(origValue));
                        prop.setOriginalDisplayValue(prop.getOriginalValue());
                    }
                    dirty = checkDirtyState(populateValueRequest, instance, populateValueRequest.getRequestedValue());
                    populateValueRequest.getFieldManager().setFieldValue(instance, populateValueRequest.getProperty()
                            .getName(), populateValueRequest.getRequestedValue());
                    break;
                case FOREIGN_KEY: {
                    if (origValue != null) {
                        prop.setOriginalValue(String.valueOf(origValue));
                    }
                    Serializable foreignInstance;
                    if (StringUtils.isEmpty(populateValueRequest.getRequestedValue())) {
                        foreignInstance = null;
                    } else {
                        if (SupportedFieldType.INTEGER.toString().equals(populateValueRequest.getMetadata()
                                .getSecondaryType().toString())) {
                            foreignInstance = populateValueRequest.getPersistenceManager().getDynamicEntityDao()
                                    .retrieve(Class.forName(populateValueRequest.getMetadata().getForeignKeyClass()),
                                            Long.valueOf(populateValueRequest.getRequestedValue()));
                        } else {
                            foreignInstance = populateValueRequest.getPersistenceManager().getDynamicEntityDao()
                                    .retrieve(Class.forName(populateValueRequest.getMetadata().getForeignKeyClass()),
                                            populateValueRequest.getRequestedValue());
                        }
                    }

                    if (Collection.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        Collection collection;
                        try {
                            collection = (Collection) populateValueRequest.getFieldManager().getFieldValue(instance,
                                    populateValueRequest.getProperty().getName());
                        } catch (FieldNotAvailableException e) {
                            throw new IllegalArgumentException(e);
                        }
                        if (!collection.contains(foreignInstance)) {
                            collection.add(foreignInstance);
                            dirty = true;
                        }
                    } else if (Map.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        throw new IllegalArgumentException("Map structures are not supported for foreign key fields.");
                    } else {
                        dirty = checkDirtyState(populateValueRequest, instance, foreignInstance);
                        populateValueRequest.getFieldManager().setFieldValue(instance,
                                populateValueRequest.getProperty().getName(), foreignInstance);
                    }
                    break;
                }
                case ADDITIONAL_FOREIGN_KEY: {
                    Serializable foreignInstance;
                    if (StringUtils.isEmpty(populateValueRequest.getRequestedValue())) {
                        foreignInstance = null;
                    } else {
                        if (SupportedFieldType.INTEGER.toString().equals(populateValueRequest.getMetadata()
                                .getSecondaryType().toString())) {
                            foreignInstance = populateValueRequest.getPersistenceManager().getDynamicEntityDao()
                                    .retrieve(Class.forName(populateValueRequest.getMetadata().getForeignKeyClass()),
                                            Long.valueOf(populateValueRequest.getRequestedValue()));
                        } else {
                            foreignInstance = populateValueRequest.getPersistenceManager().getDynamicEntityDao()
                                    .retrieve(Class.forName(populateValueRequest.getMetadata().getForeignKeyClass()),
                                            populateValueRequest.getRequestedValue());
                        }
                    }

                    // Best guess at grabbing the original display value
                    String fkProp = populateValueRequest.getMetadata().getForeignKeyDisplayValueProperty();
                    Object origDispVal = null;
                    if (origValue != null) {
                        if (AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY.equals(fkProp)) {
                            if (origValue instanceof AdminMainEntity) {
                                origDispVal = ((AdminMainEntity) origValue).getMainEntityName();
                            }
                        } else {
                            origDispVal = populateValueRequest.getFieldManager().getFieldValue(origValue, fkProp);
                        }
                    }
                    if (origDispVal != null) {
                        prop.setOriginalDisplayValue(String.valueOf(origDispVal));
                        Session session = populateValueRequest.getPersistenceManager().getDynamicEntityDao().getStandardEntityManager().unwrap(Session.class);
                        prop.setOriginalValue(String.valueOf(session.getIdentifier(foreignInstance)));
                    }

                    if (Collection.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        Collection collection;
                        try {
                            collection = (Collection) populateValueRequest.getFieldManager().getFieldValue(instance,
                                    populateValueRequest.getProperty().getName());
                        } catch (FieldNotAvailableException e) {
                            throw new IllegalArgumentException(e);
                        }
                        if (!collection.contains(foreignInstance)) {
                            collection.add(foreignInstance);
                            dirty = true;
                        }
                    } else if (Map.class.isAssignableFrom(populateValueRequest.getReturnType())) {
                        throw new IllegalArgumentException("Map structures are not supported for foreign key fields.");
                    } else {
                        dirty = checkDirtyState(populateValueRequest, instance, foreignInstance);
                        populateValueRequest.getFieldManager().setFieldValue(instance,
                                populateValueRequest.getProperty().getName(), foreignInstance);
                    }
                    break;
                }
                case ID:
                    if (populateValueRequest.getSetId()) {
                        switch (populateValueRequest.getMetadata().getSecondaryType()) {
                            case INTEGER:
                                dirty = checkDirtyState(populateValueRequest, instance, Long.valueOf(populateValueRequest.getRequestedValue()));
                                populateValueRequest.getFieldManager().setFieldValue(instance,
                                        populateValueRequest.getProperty().getName(),
                                        Long.valueOf(populateValueRequest.getRequestedValue()));
                                break;
                            case STRING:
                                dirty = checkDirtyState(populateValueRequest, instance, populateValueRequest.getRequestedValue());
                                populateValueRequest.getFieldManager().setFieldValue(instance,
                                        populateValueRequest.getProperty().getName(),
                                        populateValueRequest.getRequestedValue());
                                break;
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            throw new PersistenceOperationException(e);
        }
        populateValueRequest.getProperty().setIsDirty(dirty);
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public FieldProviderResponse extractValue(ExtractValueRequest extractValueRequest,
                                              Property property) throws PersistenceOperationException {
        if (!canHandleExtraction(extractValueRequest, property)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        try {
            if (extractValueRequest.getRequestedValue() != null) {
                String val = null;
                if (extractValueRequest.getMetadata().getForeignKeyCollection()) {
                    ((BasicFieldMetadata) property.getFieldMetadata()).setFieldType(extractValueRequest.getMetadata()
                            .getFieldType());
                } else if (extractValueRequest.getMetadata().getFieldType().equals(SupportedFieldType.BOOLEAN) &&
                        extractValueRequest.getRequestedValue() instanceof Character) {
                    val = (extractValueRequest.getRequestedValue().equals('Y')) ? "true" : "false";
                } else if (Date.class.isAssignableFrom(extractValueRequest.getRequestedValue().getClass())) {
                    val = extractValueRequest.getDataFormatProvider().getSimpleDateFormatter
                            ().format((Date) extractValueRequest.getRequestedValue());
                } else if (Timestamp.class.isAssignableFrom(extractValueRequest.getRequestedValue().getClass())) {
                    val = extractValueRequest.getDataFormatProvider().getSimpleDateFormatter
                            ().format(new Date(((Timestamp) extractValueRequest.getRequestedValue()).getTime()));
                } else if (Calendar.class.isAssignableFrom(extractValueRequest.getRequestedValue().getClass())) {
                    val = extractValueRequest.getDataFormatProvider().getSimpleDateFormatter
                            ().format(((Calendar) extractValueRequest.getRequestedValue()).getTime());
                } else if (Double.class.isAssignableFrom(extractValueRequest.getRequestedValue().getClass())) {
                    val = extractValueRequest.getDataFormatProvider().getDecimalFormatter().format
                            (extractValueRequest.getRequestedValue());
                } else if (BigDecimal.class.isAssignableFrom(extractValueRequest.getRequestedValue().getClass())) {
                    BigDecimal decimal = (BigDecimal) extractValueRequest.getRequestedValue();
                    DecimalFormat format = extractValueRequest.getDataFormatProvider().getDecimalFormatter();
                    //track all the decimal places in the scale of the BigDecimal - even if they're all zeros
                    StringBuilder sb = new StringBuilder();
                    sb.append("0");
                    if (decimal.scale() > 0) {
                        sb.append(".");
                        for (int j=0;j<decimal.scale();j++) {
                            sb.append("0");
                        }
                    }
                    format.applyPattern(sb.toString());
                    val = format.format(extractValueRequest.getRequestedValue());
                } else if (extractValueRequest.getMetadata().getForeignKeyClass() != null) {
                    try {
                        val = extractValueRequest.getFieldManager().getFieldValue
                                (extractValueRequest.getRequestedValue(), extractValueRequest.getMetadata()
                                        .getForeignKeyProperty()).toString();
                        //see if there's a name property and use it for the display value
                        String entityName = null;
                        if (extractValueRequest.getRequestedValue() instanceof AdminMainEntity) {
                            entityName = ((AdminMainEntity) extractValueRequest.getRequestedValue())
                                    .getMainEntityName();
                        }

                        Object temp = null;
                        if (!StringUtils.isEmpty(extractValueRequest.getMetadata().getForeignKeyDisplayValueProperty
                                ())) {
                            String nameProperty = extractValueRequest.getMetadata().getForeignKeyDisplayValueProperty();
                            try {
                                temp = extractValueRequest.getFieldManager().getFieldValue(extractValueRequest
                                        .getRequestedValue(), nameProperty);
                            } catch (FieldNotAvailableException e) {
                                //do nothing
                            }
                        }

                        if (temp == null && StringUtils.isEmpty(entityName)) {
                            try {
                                temp = extractValueRequest.getFieldManager().getFieldValue(extractValueRequest
                                        .getRequestedValue(), "name");
                            } catch (FieldNotAvailableException e) {
                                //do nothing
                            }
                        }

                        if (temp != null) {
                            extractValueRequest.setDisplayVal(temp.toString());
                        } else if (!StringUtils.isEmpty(entityName)) {
                            extractValueRequest.setDisplayVal(entityName);
                        }
                    } catch (FieldNotAvailableException e) {
                        throw new IllegalArgumentException(e);
                    }
                } else {
                    val = extractValueRequest.getRequestedValue().toString();
                }
                property.setValue(val);
                property.setDisplayValue(extractValueRequest.getDisplayVal());
            }
        } catch (IllegalAccessException e) {
            throw new PersistenceOperationException(e);
        }
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public FieldProviderResponse addSearchMapping(AddSearchMappingRequest req,
                                                  List<FilterMapping> filterMappings) {
        if (!canHandleSearchMapping(req, filterMappings)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        Class<?> clazz;
        try {
            clazz = Class.forName( req.getMergedProperties().get( req.getPropertyName() ).getInheritedFromType() );
        } catch (ClassNotFoundException e) {
            throw new PersistenceOperationException(e);
        }
        Field field = req.getFieldManager().getField( clazz, req.getPropertyName() );
        Class<?> targetType = null;
        if (field != null) {
            targetType = field.getType();
        }
        BasicFieldMetadata metadata = (BasicFieldMetadata) req.getMergedProperties().get(req.getPropertyName());
        
        FilterAndSortCriteria fasc = req.getRequestedCto().get(req.getPropertyName());

        FilterMapping filterMapping = new FilterMapping()
                						  .withInheritedFromClass(clazz)
                						  .withFullPropertyName(req.getPropertyName())
                                          .withFilterValues(fasc.getFilterValues())
                                          .withSortDirection(fasc.getSortDirection());
        filterMappings.add(filterMapping);
        
        if (fasc.hasSpecialFilterValue()) {
            filterMapping.setDirectFilterValues(new EmptyFilterValues());
            
            // Handle special values on a case by case basis
            List<String> specialValues = fasc.getSpecialFilterValues();
            if ( specialValues.contains(FilterAndSortCriteria.IS_NULL_FILTER_VALUE) ) {
                filterMapping.setRestriction( new Restriction().withPredicateProvider(new IsNullPredicateProvider()));
            } 
            if ( specialValues.contains(FilterAndSortCriteria.IS_NOT_NULL_FILTER_VALUE) ) {
                filterMapping.setRestriction( new Restriction().withPredicateProvider(new IsNotNullPredicateProvider()));
            } 
        } else {
            switch (metadata.getFieldType()) {
            case BOOLEAN:
                if (targetType == null || targetType.equals(Boolean.class) || targetType.equals(boolean.class)) {
                    filterMapping.setRestriction( req.getRestrictionFactory().getRestriction
                            (RestrictionType.BOOLEAN.getType(), req.getPropertyName()));
                } else {
                    filterMapping.setRestriction(req.getRestrictionFactory().getRestriction
                            (RestrictionType.CHARACTER.getType(), req.getPropertyName()));
                }
                break;
            case DATE:
                filterMapping.setRestriction( req.getRestrictionFactory().getRestriction
                        (RestrictionType.DATE.getType(), req.getPropertyName()) );
                break;
            case DECIMAL:
            case MONEY:
                filterMapping.setRestriction( req.getRestrictionFactory().getRestriction
                        (RestrictionType.DECIMAL.getType(), req.getPropertyName()));
                break;
            case INTEGER:
                filterMapping.setRestriction( req.getRestrictionFactory().getRestriction
                        (RestrictionType.LONG.getType(), req.getPropertyName()));
                break;
            case BROADLEAF_ENUMERATION:
                filterMapping.setRestriction(req.getRestrictionFactory().getRestriction(RestrictionType.STRING_EQUAL.getType(), req.getPropertyName()));
                break;
            default:
                filterMapping.setRestriction(req.getRestrictionFactory().getRestriction
                        (RestrictionType.STRING_LIKE.getType(), req.getPropertyName()));
                break;
            case FOREIGN_KEY:
                if (!req.getRequestedCto().get(req.getPropertyName()).getFilterValues().isEmpty()) {
                    ForeignKey foreignKey = (ForeignKey) req.getPersistenceAssociation()
                            .getPersistenceAssociationItems().get
                            (PersistenceAssociationItemType.FOREIGNKEY);
                    if ( metadata.getForeignKeyCollection() ) {
                        if (ForeignKeyRestrictionType.COLLECTION_SIZE_EQ.toString().equals(foreignKey
                                .getRestrictionType().toString())) {
                            filterMapping.setRestriction(req.getRestrictionFactory()
                                    .getRestriction(RestrictionType.COLLECTION_SIZE_EQUAL.getType(),
                                            req.getPropertyName()));
                            filterMapping.setFieldPath(new FieldPath());
                        } else {
                        	Restriction rest = req.getRestrictionFactory().getRestriction(RestrictionType.LONG.getType(), req.getPropertyName());
                            filterMapping.setRestriction( rest );
                            filterMapping.setFieldPath( new FieldPath().withTargetProperty(req.getPropertyName() + "." + metadata.getForeignKeyProperty()));
                        }
                    } else if (req.getRequestedCto().get(req.getPropertyName()).getFilterValues().get(0) == null 
                    		  || "null".equals(req.getRequestedCto().get(req.getPropertyName()).getFilterValues().get(0))) {
                        filterMapping.setRestriction( req.getRestrictionFactory().getRestriction(RestrictionType.IS_NULL_LONG.getType(), req.getPropertyName()));
                    } else if (metadata.getSecondaryType() == SupportedFieldType.STRING) {
                        filterMapping.setRestriction( req.getRestrictionFactory().getRestriction(RestrictionType.STRING_EQUAL.getType(), req.getPropertyName()));
                        filterMapping.setFieldPath( new FieldPath().withTargetProperty(req.getPropertyName() + "." + metadata.getForeignKeyProperty()));
                    } else {
                        filterMapping.setRestriction( req.getRestrictionFactory().getRestriction(RestrictionType.LONG_EQUAL.getType(), req.getPropertyName()));
                        filterMapping.setFieldPath( new FieldPath().withTargetProperty(req.getPropertyName() + "." + metadata.getForeignKeyProperty()));
                    }
                }
                break;
            case ADDITIONAL_FOREIGN_KEY:
                int additionalForeignKeyIndexPosition = Arrays.binarySearch(req
                    .getPersistenceAssociation()
                    .getAdditionalForeignKeys(), new ForeignKey(req.getPropertyName(),
                    null, null),
                    new Comparator<ForeignKey>() {
                        @Override
                        public int compare(ForeignKey o1, ForeignKey o2) {
                            return o1.getManyToField().compareTo(o2.getManyToField());
                        }
                    });
                ForeignKey foreignKey = null;
                if (additionalForeignKeyIndexPosition >= 0) {
                    foreignKey = req.getPersistenceAssociation().getAdditionalForeignKeys()[additionalForeignKeyIndexPosition];
                }
                // in the case of a to-one lookup, an explicit ForeignKey is not passed in. The system should then
                // default to just using a ForeignKeyRestrictionType.ID_EQ
                if (metadata.getForeignKeyCollection()) {
                    if (ForeignKeyRestrictionType.COLLECTION_SIZE_EQ.toString().equals(foreignKey
                            .getRestrictionType().toString())) {
                        filterMapping.setRestriction(req.getRestrictionFactory()
                                .getRestriction(RestrictionType.COLLECTION_SIZE_EQUAL.getType(),
                                        req.getPropertyName()));
                        filterMapping.setFieldPath(new FieldPath());
                    } else {
                        filterMapping.setRestriction(req.getRestrictionFactory().getRestriction(RestrictionType.LONG.getType(), req.getPropertyName()));
                        filterMapping.setFieldPath(new FieldPath().withTargetProperty(req.getPropertyName() + "." + metadata.getForeignKeyProperty()));
                    }
                } else if (CollectionUtils.isEmpty(req.getRequestedCto().get(req.getPropertyName()).getFilterValues()) ||
                        req.getRequestedCto().get(req.getPropertyName()) .getFilterValues().get(0) == null || "null".equals(req.getRequestedCto().get
                        (req.getPropertyName()).getFilterValues().get(0))) {
                    filterMapping.setRestriction(req.getRestrictionFactory().getRestriction(RestrictionType.IS_NULL_LONG.getType(), req.getPropertyName()));
                } else if (metadata.getSecondaryType() == SupportedFieldType.STRING) {
                    filterMapping.setRestriction(req.getRestrictionFactory().getRestriction(RestrictionType.STRING_EQUAL.getType(), req.getPropertyName()));
                    filterMapping.setFieldPath(new FieldPath().withTargetProperty(req.getPropertyName() + "." + metadata.getForeignKeyProperty()));
                } else {
                    filterMapping.setRestriction(req.getRestrictionFactory().getRestriction(RestrictionType.LONG_EQUAL.getType(), req.getPropertyName()));
                    filterMapping.setFieldPath(new FieldPath().withTargetProperty(req.getPropertyName() + "." + metadata.getForeignKeyProperty()));
                }
                break;
            case ID:
                switch (metadata.getSecondaryType()) {
                    case INTEGER:
                        filterMapping.setRestriction(req.getRestrictionFactory().getRestriction
                                (RestrictionType.LONG_EQUAL.getType(), req.getPropertyName()));
                        break;
                    case STRING:
                        filterMapping.setRestriction(req.getRestrictionFactory().
                                getRestriction(RestrictionType.STRING_EQUAL.getType(),
                                        req.getPropertyName()));
                        break;
                }
                break;
            }
        }
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public int getOrder() {
        return FieldPersistenceProvider.BASIC;
    }
}
