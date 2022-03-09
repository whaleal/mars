/**
 *    Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the Server Side Public License, version 1,
 *    as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 *
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Server Side Public License for more details.
 *
 *    You should have received a copy of the Server Side Public License
 *    along with this program. If not, see
 *    <http://www.whaleal.com/licensing/server-side-public-license>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the Server Side Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
package com.whaleal.mars.core.query;

import com.mongodb.client.model.geojson.*;
import com.whaleal.mars.core.aggregation.stages.filters.Filter;
import com.whaleal.mars.core.aggregation.stages.filters.Filters;



import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

/**
 * Defines various query filter operators
 *
 * @deprecated use {@link Filters} and {@link Filter} references instead
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated()
public enum FilterOperator {

    WITHIN_CIRCLE("$center") {
        @Override
        public Filter apply(String prop, Object value) {
            throw new UnsupportedOperationException("filterMappingNotSupported  WITHIN_CIRCLE");
        }
    },

    WITHIN_CIRCLE_SPHERE("$centerSphere") {
        @Override
        public Filter apply(String prop, Object value) {
            throw new UnsupportedOperationException("filterMappingNotSupported WITHIN_CIRCLE_SPHERE");
        }
    },

    WITHIN_BOX("$box") {
        @Override
        public Filter apply(String prop, Object value) {
            if (!(value instanceof Point[])) {
                throw new IllegalArgumentException("illegalArgument " + value.getClass().getCanonicalName()+
                    Point[].class.getCanonicalName());
            }
            Point[] points = (Point[]) value;
            return Filters.box(prop, points[0], points[1]);
        }
    },

    EQUAL("$eq", "=", "==") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.eq(prop, value);
        }
    },

    NOT_EQUAL("$ne", "!=", "<>") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.ne(prop, value);
        }
    },

    GREATER_THAN("$gt", ">") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.gt(prop, value);
        }
    },

    GREATER_THAN_OR_EQUAL("$gte", ">=") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.gte(prop, value);
        }
    },

    LESS_THAN("$lt", "<") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.lt(prop, value);
        }
    },

    LESS_THAN_OR_EQUAL("$lte", "<=") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.lte(prop, value);
        }
    },

    EXISTS("$exists", "exists") {
        @Override
        public Filter apply(String prop, Object value) {
            Filter exists = Filters.exists(prop);
            if (Boolean.FALSE.equals(value)) {
                exists.not();
            }
            return exists;
        }
    },

    TYPE("$type", "type") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.type(prop, (Type) value);
        }
    },

    NOT("$not") {
        @Override
        public Filter apply(String prop, Object value) {
            throw new UnsupportedOperationException("translationNotCurrentlySupported");
        }
    },

    MOD("$mod", "mod") {
        @Override
        public Filter apply(String prop, Object value) {
            long[] values = (long[]) value;
            return Filters.mod(prop, values[0], values[1]);
        }
    },

    SIZE("$size", "size") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.size(prop, (Integer) value);
        }
    },

    IN("$in", "in") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.in(prop, (Iterable<?>) value);
        }
    },

    NOT_IN("$nin", "nin") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.nin(prop, value);
        }
    },

    ALL("$all", "all") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.all(prop, value);
        }
    },

    ELEMENT_MATCH("$elemMatch", "elem", "elemMatch") {
        @Override
        public Filter apply(String prop, Object value) {
            throw new UnsupportedOperationException("filterMappingNotSupported" +ELEMENT_MATCH);
            //            return Filters.elemMatch(prop, (Query<?>) value);
        }
    },

    WHERE("$where") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.where(value.toString());
        }
    },

    // GEO
    NEAR("$near", "near") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.near(prop, (Point) convertToGeometry(value));
        }
    },

    NEAR_SPHERE("$nearSphere") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.nearSphere(prop, (Point) convertToGeometry(value));
        }
    },

    GEO_NEAR("$geoNear", "geoNear") {
        @Override
        public Filter apply(String prop, Object value) {
            throw new UnsupportedOperationException("An aggregation operator called in a query context?");
        }
    },

    GEO_WITHIN("$geoWithin", "geoWithin") {
        @Override
        @SuppressWarnings("removal")
        public Filter apply(String prop, Object value) {
            if (value instanceof Shape) {
                Shape shape = (Shape) value;
                if (shape instanceof Shape.Center) {
                    final Shape.Center center = (Shape.Center) shape;
                    if ("$center".equals(center.getGeometry())) {
                        return Filters.center(prop, shape.getPoints()[0], center.getRadius());
                    } else if ("$centerSphere".equals(center.getGeometry())) {
                        return Filters.centerSphere(prop, shape.getPoints()[0], center.getRadius());
                    }
                } else if ("$box".equals(shape.getGeometry())) {
                    return Filters.box(prop, shape.getPoints()[0], shape.getPoints()[1]);
                } else if ("$polygon".equals(shape.getGeometry())) {
                    return Filters.polygon(prop, shape.getPoints());
                }
                throw new UnsupportedOperationException("conversionNotSupported " + value.getClass().getCanonicalName());
            } else if (value instanceof Polygon) {
                return Filters.geoWithin(prop, (Polygon) value);
            } else if (value instanceof MultiPolygon) {
                return Filters.geoWithin(prop, (MultiPolygon) value);
            } else {
                throw new UnsupportedOperationException("conversionNotSupported " + value.getClass().getCanonicalName());
            }
        }
    },

    INTERSECTS("$geoIntersects", "geoIntersects") {
        @Override
        public Filter apply(String prop, Object value) {
            return Filters.geoIntersects(prop, convertToGeometry(value));
        }
    };

    private final String value;
    private final List<String> filters;

    FilterOperator(String val, String... filterValues) {
        value = val;
        filters = Arrays.asList(filterValues);
    }

    private static Geometry convertToGeometry(Object value) {
        Geometry converted;
        if (value instanceof double[]) {
            final double[] coordinates = (double[]) value;
            converted = new Point(new Position(coordinates[0], coordinates[1]));
        } else if (value instanceof Geometry) {
            converted = (Geometry) value;
        } else {
            throw new UnsupportedOperationException("conversionNotSupported " + value.getClass().getCanonicalName());
        }

        return converted;
    }

    /**
     * Creates a FilterOperator from a String
     *
     * @param operator the String to convert
     * @return the FilterOperator
     */
    public static FilterOperator fromString(String operator) {
        final String filter = operator.trim().toLowerCase();
        for (FilterOperator filterOperator : FilterOperator.values()) {
            if (filterOperator.matches(filter)) {
                return filterOperator;
            }
        }
        throw new IllegalArgumentException(format("Unknown operator '%s'", operator));
    }

    /**
     * Converts a {@link FilterOperator} to a {@link Filter}
     *
     * @param prop  the document property name
     * @param value the value to apply to the filter
     * @return the new Filter
     *
     */
    public abstract Filter apply(String prop, Object value);

    /**
     * Returns true if the given filter matches the filters on this FilterOperator
     *
     * @param filter the filter to check
     * @return true if the given filter matches the filters on this FilterOperator
     */
    public boolean matches(String filter) {
        return filter != null && filters.contains(filter.trim().toLowerCase());
    }

    /**
     * @return the value of this FilterOperator
     */
    public String val() {
        return value;
    }
}
