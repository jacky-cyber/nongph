package cn.globalph.openadmin.server.service.persistence.module.criteria.predicate;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import cn.globalph.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;

import org.springframework.stereotype.Component;

/**
 * @author Jeff Fischer
 */
@Component("blEqPredicateProvider")
public class EqPredicateProvider implements PredicateProvider<Serializable, Serializable> {

    @Override
    public Predicate buildPredicate(CriteriaBuilder builder, FieldPathBuilder fieldPathBuilder, From root, String ceilingEntity,
                                    String fullPropertyName, Path<Serializable> explicitPath, List<Serializable> directValues) {
        Path<Serializable> path;
        if (explicitPath != null) {
            path = explicitPath;
        } else {
            path = fieldPathBuilder.getPath(root, fullPropertyName, builder);
        }
        return path.in(directValues);
    }
}
