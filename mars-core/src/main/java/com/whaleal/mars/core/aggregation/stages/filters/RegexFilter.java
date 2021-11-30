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
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
import org.bson.BsonRegularExpression;
import org.bson.BsonWriter;
import org.bson.codecs.EncoderContext;

import java.util.regex.Pattern;

/**
 * Defines a regular expression filter
 */
public class RegexFilter extends Filter {
    private String regex;
    private String options;

    RegexFilter(String field) {
        super("$regex", field, null);
    }

    @Override
    public void encode(MongoMappingContext mapper, BsonWriter writer, EncoderContext context) {
        writer.writeStartDocument(path(mapper));
        if (isNot()) {
            writer.writeStartDocument("$not");
        }
        ExpressionHelper.value(mapper, writer, "$regex", new BsonRegularExpression(regex), context);
        ExpressionHelper.value(mapper, writer, "$options", options, context);
        if (isNot()) {
            writer.writeEndDocument();
        }
        writer.writeEndDocument();
    }

    /**
     * Optional options to apply to the regex
     *
     * @param options the options
     * @return this
     */
    public RegexFilter options(String options) {
        this.options = options;
        return this;
    }

    /**
     * The regular expression
     *
     * @param pattern the regular expression
     * @return this
     */
    public RegexFilter pattern(String pattern) {
        this.regex = pattern;
        return this;
    }

    /**
     * The regular expression
     *
     * @param pattern the regular expression
     * @return this
     */
    public RegexFilter pattern(Pattern pattern) {
        this.regex = pattern.pattern();
        return this;
    }

    /**
     * Case insensitivity to match upper and lower cases.
     *
     * @return this
     */
    public RegexFilter caseInsensitive() {
        add("i");
        return this;
    }

    /**
     * “Extended” capability to ignore all white space characters in the $regex pattern unless escaped or included in a character class.
     * <p>
     * Additionally, it ignores characters in-between and including an un-escaped hash/pound (#) character and the next new line, so that
     * you may include comments in complicated patterns. This only applies to data characters; white space characters may never appear
     * within special character sequences in a pattern.
     * <p>
     * The x option does not affect the handling of the VT character (i.e. code 11).
     *
     * @return this
     */
    public RegexFilter extended() {
        add("x");
        return this;
    }

    /**
     * For patterns that include anchors (i.e. ^ for the start, $ for the end), match at the beginning or end of each line for strings
     * with multiline values. Without this option, these anchors match at beginning or end of the string.
     *
     * @return this
     */
    public RegexFilter multiline() {
        add("m");
        return this;
    }

    /**
     * Allows the dot character (i.e. .) to match all characters including newline characters.
     *
     * @return this
     */
    public RegexFilter special() {
        add("s");
        return this;
    }

    private void add(String option) {
        if (options == null) {
            options = "";
        }
        if (!options.contains(option)) {
            options += option;
        }
    }
}
