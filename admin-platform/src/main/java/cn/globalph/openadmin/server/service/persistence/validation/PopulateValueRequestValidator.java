package cn.globalph.openadmin.server.service.persistence.validation;

import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.server.service.persistence.module.BasicPersistenceModule;
import cn.globalph.openadmin.server.service.persistence.module.provider.FieldPersistenceProvider;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;

import java.io.Serializable;


/**
 * <p>
 * This is injected into the {@link BasicPersistenceModule} and invoked prior to any attempts to actually populate values
 * from the {@link Entity} DTO representation into the Hibernate entity using the {@link FieldPersistenceProvider} paradigm.
 * </p>
 * <p>
 * An example validator would ensure that Booleans are actually booleans, integers are actually integers, etc. since all
 * values come in as Strings by default
 * </p>
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link FieldPersistenceProvider}
 * @see {@link PopulateValueRequest}
 * @see {@link BasicPersistenceModule#createPopulatedInstance(Serializable, Entity, java.util.Map, Boolean)}
 */
public interface PopulateValueRequestValidator {
    
    /**
     * Validates a population request prior to invoking any {@link FieldPersistenceProvider}s. If no validation could be
     * performed for the given {@link PopulateValueRequest} then return <b>true</b> to let it pass on to a different
     * {@link PopulateValueRequestValidator} or on to a {@link FieldPersistenceProvider}.
     * 
     * @param populateValueRequest the {@link PopulateValueRequest} that should be validated
     * @param instance the Hibernate entity that will attempt to be populated
     * @return false if the {@link PopulateValueRequest} failed validation. In this case, the request should not be passed
     * to any {@link FieldPersistenceProvider}s.
     */
    public PropertyValidationResult validate(PopulateValueRequest populateValueRequest, Serializable instance);
    
}
