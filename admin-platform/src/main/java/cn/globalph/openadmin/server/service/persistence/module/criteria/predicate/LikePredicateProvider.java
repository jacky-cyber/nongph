package cn.globalph.openadmin.server.service.persistence.module.criteria.predicate;

import cn.globalph.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;

import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import java.util.List;

/**
 * @author felix.wu
 */
@Component("blLikePredicateProvider")
public class LikePredicateProvider implements PredicateProvider<String, String> {

    @Override
    public Predicate buildPredicate(CriteriaBuilder builder, 
    		                        FieldPathBuilder fieldPathBuilder, 
    		                        From root, 
    		                        String ceilingEntity,
                                    String fullPropertyName, 
                                    Path<String> explicitPath, 
                                    List<String> directValues) {
        Path<String> path;
        if (explicitPath != null) {
            path = explicitPath;
        } else {
            path = fieldPathBuilder.getPath(root, fullPropertyName, builder);
        }
        return builder.like( builder.lower(path), directValues.get(0) );
    }

}
