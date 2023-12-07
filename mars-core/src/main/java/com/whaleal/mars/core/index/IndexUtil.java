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

package com.whaleal.mars.core.index;

import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;
import com.whaleal.mars.core.internal.diagnostics.logging.LogFactory;
import com.whaleal.mars.core.internal.diagnostics.logging.Logger;
import com.whaleal.mars.session.option.IndexOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wh
 *
 */
public class IndexUtil {

    private static final Logger LOGGER = LogFactory.getLogger(IndexUtil.class);

    public static Index of( Document indexDoc ) {
        Document o = indexDoc;

        IndexOptions indexOptions = new IndexOptions();
        if (o.get("background") != null) {

            //todo  具体值可能为多种类型  如 "true"  true  1 1.0  "1.0"  "5.0"  甚至为 任意字符"xxx"尤其是老版本 情况很多 这里并不能全部列举
            // C语言决策. C语言编程假定任何非零和非空值为真，并且如果它是零或null，那么它被假定为假值
            // 主要为 boolean  类型 String  类型  数值类型
            if (o.get("background") instanceof Boolean) {
                indexOptions.background((Boolean) o.get("background"));
            } else if (o.get("background") instanceof String) {
                indexOptions.background(true);
            } else if (o.get("background") instanceof Number) {
                // 非 0 为真
                double v;
                try {
                    v = Double.valueOf(o.get("background").toString());
                    if (v > 0) {
                        indexOptions.background(true);
                    } else indexOptions.background(v < 0);
                } catch (Exception e) {
                    LOGGER.warn(String.format("Index background Option parse error from  index name %s  with background value %s ", o.get("name"), o.get("background")));
                    indexOptions.background(true);
                }
            }

        }

        if (o.get("unique") != null) {
            indexOptions.unique((Boolean) o.get("unique"));
        }
        if (o.get("name") != null) {
            indexOptions.name((String) o.get("name"));
        }

        if (o.get("partialFilterExpression") != null) {
            indexOptions.partialFilterExpression((Bson) o.get("partialFilterExpression"));
        }
        if (o.get("sparse") != null) {
            indexOptions.sparse((Boolean) o.get("sparse"));
        }
        if (o.get("expireAfterSeconds") != null) {
            Long expireAfter = ((Double) Double.parseDouble(o.get("expireAfterSeconds").toString())).longValue();
            //秒以下会丢失
            indexOptions.expireAfter(expireAfter, TimeUnit.SECONDS);
        }

        if (o.get("hidden") != null) {
            indexOptions.hidden((Boolean) o.get("hidden"));
        }

        if (o.get("storageEngine") != null) {
            //不常用到
            indexOptions.storageEngine((Bson) o.get("storageEngine"));
        }

        //---------deal with Collation

        if (o.get("collation") != null) {
            com.mongodb.client.model.Collation.Builder collationBuilder = com.mongodb.client.model.Collation.builder();
            Document collation = (Document) o.get("collation");
            if (collation.get("locale") != null) {
                collationBuilder.locale(collation.getString("locale"));
            }
            if (collation.get("caseLevel") != null) {
                collationBuilder.caseLevel(collation.getBoolean("caseLevel"));
            }
            if (collation.get("caseFirst") != null) {
                collationBuilder.collationCaseFirst(CollationCaseFirst.fromString(collation.getString("caseFirst")));
            }
            if (collation.get("strength") != null) {
                collationBuilder.collationStrength(CollationStrength.fromInt(
                        ((Double) Double.parseDouble(collation.get("strength").toString())).intValue()
                ));
            }
            if (collation.get("numericOrdering") != null) {
                collationBuilder.numericOrdering(collation.getBoolean("numericOrdering"));
            }
            if (collation.get("alternate") != null) {
                collationBuilder.collationAlternate(CollationAlternate.fromString(collation.getString("alternate")));
            }
            if (collation.get("maxVariable") != null) {
                collationBuilder.collationMaxVariable(CollationMaxVariable.fromString(collation.getString("maxVariable")));
            }
            if (collation.get("normalization") != null) {
                collationBuilder.normalization(collation.getBoolean("normalization"));
            }
            if (collation.get("backwards") != null) {
                collationBuilder.backwards(collation.getBoolean("backwards"));
            }
            indexOptions.collation(collationBuilder.build());
        }

        //---------deal with Text


        if (o.get("weights") != null) {
            indexOptions.weights((Bson) o.get("weights"));
        }
        if (o.get("textIndexVersion") != null) {
            indexOptions.textVersion(((Double) Double.parseDouble(o.get("textIndexVersion").toString())).intValue());
        }
        if (o.get("default_language") != null) {
            indexOptions.defaultLanguage((String) o.get("default_language"));
        }
        if (o.get("language_override") != null) {
            indexOptions.languageOverride(o.get("language_override").toString());
        }

        //--------deal with wildcard

        if (o.get("wildcardProjection") != null) {
            indexOptions.wildcardProjection((Bson) o.get("wildcardProjection"));
        }

        //---------deal with geoHaystack
        if (o.get("bucketSize") != null) {
            indexOptions.bucketSize(Double.parseDouble(o.get("bucketSize").toString()));
        }
        //---------deal with  2d

        if (o.get("bits") != null) {
            indexOptions.bits(((Double) Double.parseDouble(o.get("bits").toString())).intValue());
        }
        if (o.get("max") != null) {
            indexOptions.max((Double.parseDouble(o.get("max").toString())));
        }
        if (o.get("min") != null) {
            indexOptions.min((Double.parseDouble(o.get("min").toString())));
        }

        //---------------deal with 2dsphere

        if (o.get("2dsphereIndexVersion") != null) {
            indexOptions.sphereVersion(((Double) Double.parseDouble((o.get("2dsphereIndexVersion").toString()))).intValue());
        }

        //------ let it be backgroud
        indexOptions.background(true);

        Document key = (Document) o.get("key");

        Index index = new Index();

        Set< String > strings = key.keySet();

        for (String keyName : strings) {
            index.on(keyName, IndexDirection.fromValue(key.get(keyName)));

        }
        index.setOptions(indexOptions);

        return index;

    }


    public static List< Index > of( List< Document > indexesDoc ) {
        List< Index > indexes = new ArrayList<>();
        for (Document doc : indexesDoc) {
            Index index = IndexUtil.of(doc);

            indexes.add(index);
        }
        return indexes;
    }


}
