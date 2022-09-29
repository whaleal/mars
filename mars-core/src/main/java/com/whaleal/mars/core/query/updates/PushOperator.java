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
package com.whaleal.mars.core.query.updates;




import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import com.whaleal.mars.core.domain.SortType;
import com.whaleal.mars.core.query.Sort;
import org.bson.Document;

import java.util.List;

/**
 * Defines the $push update operator
 *
 *
 * 
 */
public class PushOperator extends UpdateOperator {
    private Integer position;
    private Integer slice;
    private Integer sort;
    private Document sortDocument;

    /**
     * @param field  the field
     * @param values the values
     *
     */
    PushOperator(String field, List<?> values) {
        super("$push", field, values);
    }

    /**
     * Sets the position for the update
     *
     * @param position the position in the array for the update
     * @return this
     */
    public PushOperator position(int position) {
        if (position < 0) {
            throw new UpdateException("The position must be at least 0.");
        }
        this.position = position;
        return this;
    }

    /**
     * Sets the slice value for the update
     *
     * @param slice the slice value for the update
     * @return this
     */
    public PushOperator slice(int slice) {
        this.slice = slice;
        return this;
    }

    /**
     * Sets the sort value for the update
     *
     * @param sort the sort value for the update
     * @return this
     */
    public PushOperator sort(int sort) {
        if (sortDocument != null) {
            throw new IllegalStateException("sort document");
        }
        this.sort = sort;
        return this;
    }

    /**
     * Sets the sort value for the update
     *
     * @param value the sort criteria to add
     * @return this
     */
    public PushOperator sort( Sort value) {

        if (sort != null) {
            throw new IllegalStateException("updateSortOptions Sort document "+sort);
        }
        if (sortDocument == null) {
            sortDocument = new Document();
        }
        DocumentWriter writer = new DocumentWriter() ;
        ExpressionHelper.document(writer, () -> {
            for (SortType sorttype : value.getSorts()) {
                writer.writeName(sorttype.getField());
                sorttype.getDirection().encode(writer);
            }
        });
        sortDocument.put("$sort", writer.getDocument());
        return this;
    }

    @Override
    public Document toDocument() {
        Document document = new Document("$each", value());
        if (position != null) {
            document.put("$position", position);
        }
        if (slice != null) {
            document.put("$slice", slice);
        }
        if (sort != null) {
            document.put("$sort", sort);
        }
        if (sortDocument != null) {
            document.put("$sort", sortDocument);
        }

        return document ;
    }
}
