package cn.globalph.openadmin.server.service.persistence.module.criteria.predicate;

import cn.globalph.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;

import org.springframework.stereotype.Component;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * @author Jeff Fischer
 */
@Component("blIsNotNullPredicateProvider")
public class IsNotNullPredicateProvider implements PredicateProvider {

    @Override
    public Predicate buildPredicate(CriteriaBuilder builder, FieldPathBuilder fieldPathBuilder, From root, String ceilingEntity,
                                    String fullPropertyName, Path explicitPath, List  directValues) {
        Path<Long> path;
        if (explicitPath != null) {
            path = explicitPath;
        } else {
            path = fieldPathBuilder.getPath(root, fullPropertyName, builder);
        }
        return builder.isNotNull(path);
    }
}
