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
package com.whaleal.mars.core.aggregation.stages.filters;

import com.whaleal.mars.codecs.MongoMappingContext;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

/**
 * Defines a text search filter
 */
public class TextSearchFilter extends Filter {
    private final String searchText;
    private String language;
    private Boolean caseSensitive;
    private Boolean diacriticSensitive;

    protected TextSearchFilter(String searchText) {
        super("$text");
        this.searchText = searchText;
    }

    /**
     * Sets the search as case sensitive or not
     *
     * @param caseSensitive the case sensitivity
     * @return this
     */
    public TextSearchFilter caseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    /**
     * Sets the search as diacritic sensitive or not
     *
     * @param diacriticSensitive the diacritic sensitivity
     * @return this
     */
    public TextSearchFilter diacriticSensitive(Boolean diacriticSensitive) {
        this.diacriticSensitive = diacriticSensitive;
        return this;
    }

    @Override
    public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
        writer.writeStartDocument(getName());
        writeNamedValue("$search", searchText, mapper, writer, context);
        if (language != null) {
            writeNamedValue("$language", language, mapper, writer, context);
        }
        if (Boolean.TRUE.equals(caseSensitive)) {
            writeNamedValue("$caseSensitive", caseSensitive, mapper, writer, context);
        }
        if (Boolean.TRUE.equals(diacriticSensitive)) {
            writeNamedValue("$diacriticSensitive", diacriticSensitive, mapper, writer, context);
        }
        writer.writeEndDocument();
    }

    /**
     * Sets the language to use
     *
     * @param language the language
     * @return this
     */
    public TextSearchFilter language(String language) {
        this.language = language;
        return this;
    }
}
