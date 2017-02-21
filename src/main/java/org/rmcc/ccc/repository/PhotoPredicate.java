package org.rmcc.ccc.repository;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;
import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.model.QPhoto;
import org.rmcc.ccc.model.SearchCriteria;

import java.sql.Timestamp;

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
            } else if (criteria.getOperation().equalsIgnoreCase(">")) {
                return path.goe(value);
            } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                return path.loe(value);
            }
        } else if (criteria.getValue() instanceof Timestamp) {
            DateTimePath<Timestamp> path = entityPath.getDateTime(criteria.getKey(), Timestamp.class);
            Timestamp value = (Timestamp) criteria.getValue();
            if (criteria.getOperation().equalsIgnoreCase(">")) {
                return path.after(value);
            } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                return path.before(value);
            }
        } else if (criteria.getValue() instanceof Boolean) {
            BooleanPath path = entityPath.getBoolean(criteria.getKey());
            Boolean value = (Boolean) criteria.getValue();
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.eq(value);
            }
        } else if (criteria.getValue() instanceof Integer[]) {
            return speciesIdIn((Integer[])criteria.getValue());
//            ArrayPath path = entityPath.getArray(criteria.getKey(),Integer[].class);
//            Integer[] value = (Integer[]) criteria.getValue();
//            if (criteria.getOperation().equalsIgnoreCase("in")) {
//                return path.in(value);
//            }
        } else {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteria.getValue().toString());
            }
        }
        return null;
    }
    public static BooleanExpression speciesIdIn(final Integer[] ids) {
        return QPhoto.photo.detections.any().species.id.in(ids);
    }
}
