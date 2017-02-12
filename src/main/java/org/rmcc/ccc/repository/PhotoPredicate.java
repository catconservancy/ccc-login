package org.rmcc.ccc.repository;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;
import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.model.SearchCriteria;

import static org.hibernate.jpa.criteria.ValueHandlerFactory.isNumeric;

public class PhotoPredicate {

    public PhotoPredicate(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    private SearchCriteria criteria;

    public BooleanExpression getPredicate() {
        PathBuilder<Photo> entityPath = new PathBuilder<Photo>(Photo.class, "photo");

        if (isNumeric(criteria.getValue())) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            int value = Integer.parseInt(criteria.getValue().toString());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.eq(value);
            }
            else if (criteria.getOperation().equalsIgnoreCase(">")) {
                return path.goe(value);
            }
            else if (criteria.getOperation().equalsIgnoreCase("<")) {
                return path.loe(value);
            }
        }
        else {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteria.getValue().toString());
            }
        }
        return null;
    }
}
