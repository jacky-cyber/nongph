package cn.globalph.openadmin.server.service.persistence.module.criteria.predicate;

import cn.globalph.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import java.util.List;

/**
 * @author felix.wu
 */
public interface PredicateProvider<T, Y> {

    Predicate buildPredicate(CriteriaBuilder builder, 
    		                 FieldPathBuilder fieldPathBuilder, 
    		                 From root, 
    		                 String ceilingEntity,
                             String fullPropertyName, 
                             Path<T> explicitPath, 
                             List<Y> directValues);

}
