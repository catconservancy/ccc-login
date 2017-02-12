package org.rmcc.ccc.repository;

import com.mysema.query.types.expr.BooleanExpression;
import org.rmcc.ccc.model.SearchCriteria;

import java.util.ArrayList;
import java.util.List;

public class PhotoPredicatesBuilder {
    private List<SearchCriteria> params;

    public PhotoPredicatesBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public PhotoPredicatesBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        if (params.size() == 0) {
            return null;
        }

        List<BooleanExpression> predicates = new ArrayList<BooleanExpression>();
        PhotoPredicate predicate;
        for (SearchCriteria param : params) {
            predicate = new PhotoPredicate(param);
            BooleanExpression exp = predicate.getPredicate();
            if (exp != null) {
                predicates.add(exp);
            }
        }

        BooleanExpression result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = result.and(predicates.get(i));
        }
        return result;
    }
}
