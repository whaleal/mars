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


import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import com.whaleal.mars.core.aggregation.stages.Stage;
import org.bson.BsonWriter;
import org.bson.Document;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;


/**
 * stage 接口在这里只是个装饰
 *
 * @see com.whaleal.mars.core.aggregation.stages.Stage
 * @see com.whaleal.mars.core.aggregation.stages.Sort
 *
 * @author wh
 *
 *
 */
public class Sort extends Stage implements  Serializable {

    private final List< SortType > sorts = new ArrayList<>();

    protected Sort() {
        super("$sort");
    }

    public Sort( List< SortType> sorts ) {
        super("$sort");
        this.sorts.addAll(sorts);
    }


    public static Sort on() {
        return new Sort();
    }


    public Sort ascending( String field, String... additional) {
        sorts.add(new Sort.SortType(field, Sort.Direction.ASCENDING));
        for (String another : additional) {
            sorts.add(new Sort.SortType(another, Sort.Direction.ASCENDING));
        }
        return this;
    }


    public Sort descending( String field, String... additional) {
        sorts.add(new Sort.SortType(field, Sort.Direction.DESCENDING));
        for (String another : additional) {
            sorts.add(new Sort.SortType(another, Sort.Direction.DESCENDING));
        }
        return this;
    }

    /**
     * Returns a new {@link Sort} consisting of the {@link SortType}s of the current {@link Sort} combined with the given
     * ones.
     *
     * @param sort must not be {@literal null}.
     * @return
     */
    public Sort and(Sort sort) {

        Precondition.notNull(sort, "Sort must not be null!");

        ArrayList<SortType> these = new ArrayList<>(this.sorts);

        for (SortType order : sort.sorts) {
            these.add(order);
        }

        return Sort.by(these);
    }
    /**
     * Creates a new {@link Sort} for the given {@link SortType}s.
     *
     * @param orders must not be {@literal null}.
     * @return
     */
    public static Sort by(List<SortType> orders) {

        Precondition.notNull(orders, "Orders must not be null!");

        return orders.isEmpty() ? Sort.on() : new Sort(orders);
    }

    /**
     * Creates a new {@link Sort} for the given {@link SortType}s.
     *
     * @param orders must not be {@literal null}.
     * @return
     */
    public static Sort by(SortType... orders) {

        Precondition.notNull(orders, "Orders must not be null!");

        return new Sort(Arrays.asList(orders));
    }

    /**
     * @return the sort {@link Document}.
     */
    public Document getSortObject() {

        if (this.sorts.isEmpty()) {
            return new Document();
        }

        Document document = new Document();

        this.sorts.stream()//
                .forEach(sortType -> document.put(sortType.getField(), sortType.getDirection().toData()));

        return document;
    }



    public List< Sort.SortType > getSorts() {
        return sorts;
    }


    public Sort meta( String field) {
        sorts.add(new Sort.SortType(field, Sort.Direction.META));
        return this;
    }

    
    public Iterator< SortType > iterator() {
        return sorts.iterator();
    }

    /**
     * The sort types
     */
    public enum Direction {
        ASCENDING {
            @Override
            public Object toData( ) {
                return 1;
            }
        },
        DESCENDING {
            @Override
            public Object toData(){
                return -1;
            }
        },
        META {
            @Override
            public Object toData() {
                return new Document("$meta", "textScore");
            }
        };

        public abstract Object toData();
    }


    public class SortType {
        private final String field;
        private final Sort.Direction direction;

        protected SortType(String field, Sort.Direction direction) {
            this.field = field;
            this.direction = direction;
        }


        public Sort.Direction getDirection() {
            return direction;
        }


        public String getField() {
            return field;
        }
    }



}