package cn.globalph.openadmin.server.service.persistence.module.criteria.predicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import cn.globalph.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;

import org.springframework.stereotype.Component;

/**
 * This predicate provider is very similar to the {@link EqPredicateProvider}, except that it will treat
 * nulls equal to false. This implementation will provide an equality clause for the character 'N' and
 * {@link Boolean#FALSE}.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("blNullFalseEqPredicateProvider")
public class NullFalseEqPredicateProvider implements PredicateProvider<Serializable, Serializable> {

    @Override
    public Predicate buildPredicate(CriteriaBuilder builder, FieldPathBuilder fieldPathBuilder, From root, String ceilingEntity,
                                    String fullPropertyName, Path<Serializable> explicitPath, List<Serializable> directValues) {
        Path<Serializable> path;
        if (explicitPath != null) {
            path = explicitPath;
        } else {
            path = fieldPathBuilder.getPath(root, fullPropertyName, builder);
        }
        
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (Serializable directValue : directValues) {
            boolean attachNullClause = false;
            if (directValue instanceof Boolean) {
                if (((Boolean) directValue).equals(Boolean.FALSE)) {
                    attachNullClause = true;
                }
            } else if (directValue instanceof Character) {
                if (((Character) directValue).equals('N')) {
                    attachNullClause = true;
                }
            }
            
            if (attachNullClause) {
                predicates.add(
                    builder.or( 
                        builder.equal(path, directValue),
                        builder.isNull(path)
                    )
                );
            } else {
                predicates.add(builder.equal(path, directValue));
            }
        }
        
        return builder.or(predicates.toArray(new Predicate[predicates.size()]));
    }
}
