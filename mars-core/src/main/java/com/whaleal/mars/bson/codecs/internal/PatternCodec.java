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
package com.whaleal.mars.bson.codecs.internal;

import org.bson.BsonReader;
import org.bson.BsonRegularExpression;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class PatternCodec implements Codec<Pattern> {
    @Override
    public void encode(final BsonWriter writer, final Pattern value, final EncoderContext encoderContext) {
        writer.writeRegularExpression(new BsonRegularExpression(value.pattern(), getOptionsAsString(value)));
    }

    @Override
    public Pattern decode(final BsonReader reader, final DecoderContext decoderContext) {
        BsonRegularExpression regularExpression = reader.readRegularExpression();
        return Pattern.compile(regularExpression.getPattern(), getOptionsAsInt(regularExpression));
    }

    @Override
    public Class<Pattern> getEncoderClass() {
        return Pattern.class;
    }

    private static String getOptionsAsString(final Pattern pattern) {
        int flags = pattern.flags();
        StringBuilder buf = new StringBuilder();

        for (final RegexFlag flag : RegexFlag.values()) {
            if ((pattern.flags() & flag.javaFlag) > 0) {
                buf.append(flag.flagChar);
                flags -= flag.javaFlag;
            }
        }

        if (flags > 0) {
            throw new IllegalArgumentException("some flags could not be recognized.");
        }

        return buf.toString();
    }

    private static int getOptionsAsInt(final BsonRegularExpression regularExpression) {
        int optionsInt = 0;

        String optionsString = regularExpression.getOptions();

        if (optionsString == null || optionsString.length() == 0) {
            return optionsInt;
        }

        optionsString = optionsString.toLowerCase();

        for (int i = 0; i < optionsString.length(); i++) {
            RegexFlag flag = RegexFlag.getByCharacter(optionsString.charAt(i));
            if (flag != null) {
                optionsInt |= flag.javaFlag;
                if (flag.unsupported != null) {
                    // TODO: deal with logging
                    // warnUnsupportedRegex( flag.unsupported );
                }
            } else {
                // TODO: throw a better exception here
                throw new IllegalArgumentException("unrecognized flag [" + optionsString.charAt(i) + "] " + (int) optionsString.charAt(i));
            }
        }
        return optionsInt;
    }


    private static final int GLOBAL_FLAG = 256;

    private enum RegexFlag {
        CANON_EQ(Pattern.CANON_EQ, 'c', "Pattern.CANON_EQ"),
        UNIX_LINES(Pattern.UNIX_LINES, 'd', "Pattern.UNIX_LINES"),
        GLOBAL(GLOBAL_FLAG, 'g', null),
        CASE_INSENSITIVE(Pattern.CASE_INSENSITIVE, 'i', null),
        MULTILINE(Pattern.MULTILINE, 'm', null),
        DOTALL(Pattern.DOTALL, 's', "Pattern.DOTALL"),
        LITERAL(Pattern.LITERAL, 't', "Pattern.LITERAL"),
        UNICODE_CASE(Pattern.UNICODE_CASE, 'u', "Pattern.UNICODE_CASE"),
        COMMENTS(Pattern.COMMENTS, 'x', null);

        private static final Map<Character, RegexFlag> BY_CHARACTER = new HashMap<Character, RegexFlag>();

        private final int javaFlag;
        private final char flagChar;
        private final String unsupported;

        static {
            for (final RegexFlag flag : values()) {
                BY_CHARACTER.put(flag.flagChar, flag);
            }
        }

        public static RegexFlag getByCharacter(final char ch) {
            return BY_CHARACTER.get(ch);
        }

        RegexFlag(final int f, final char ch, final String u) {
            javaFlag = f;
            flagChar = ch;
            unsupported = u;
        }
    }

}
