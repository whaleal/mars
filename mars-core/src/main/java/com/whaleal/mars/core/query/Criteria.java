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

import com.mongodb.BasicDBList;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.MultiPoint;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import com.whaleal.icefrog.core.collection.CollectionUtil;
import com.whaleal.icefrog.core.lang.Precondition;

import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.mars.core.internal.InvalidMongoDbApiUsageException;
import com.whaleal.icefrog.core.codec.Base64 ;
import org.bson.BsonRegularExpression;
import org.bson.Document;
import org.bson.types.Binary;


import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static com.whaleal.icefrog.core.util.ObjectUtil.nullSafeHashCode;

/**
 * 创建查询语句的基本类，它遵循流畅的 API 风格，可以轻松地将多个查询连在一起。使用Criteria.where方法创建此类对象可以提高可读性
 */
public class Criteria implements CriteriaDefinition {

    public static final String center = "$center";
    public static final String centerSphere = "$centerSphere";
    public static final String polygon = "$polygon";
    /**
     * as we have to be able to work with {@literal null} values as well.
     * 自定义的非空对象
     */
    private static final Object NOT_SET = new Object();

    private static final int[] FLAG_LOOKUP = new int[Character.MAX_VALUE];

    static {
        FLAG_LOOKUP['g'] = 256;
        FLAG_LOOKUP['i'] = Pattern.CASE_INSENSITIVE;
        FLAG_LOOKUP['m'] = Pattern.MULTILINE;
        FLAG_LOOKUP['s'] = Pattern.DOTALL;
        FLAG_LOOKUP['c'] = Pattern.CANON_EQ;
        FLAG_LOOKUP['x'] = Pattern.COMMENTS;
        FLAG_LOOKUP['d'] = Pattern.UNIX_LINES;
        FLAG_LOOKUP['t'] = Pattern.LITERAL;
        FLAG_LOOKUP['u'] = Pattern.UNICODE_CASE;
    }

    // key  is used  to  decorate  itself
    private String key;

    // criteriaChain  is used  to  decorate  it innerData
    //  主要用于放置 前置 的 criteria ，实现 是一个 ArrayList   如果没有前置 。那么久放置自己 到该 criteraChain中 ，外部调用 主要是调用 该 参数 并获取该值
    private List<Criteria> criteriaChain;
    //  核心内容  将 查询操作 放入到 一个 LinkedHashMap  即为 本身   内部 为一个 有序 平行的结构
    //  比如 lte  gte  ne  等
    //  比如 {a:{lt:10,gt:1}}  这个里面的   lt  gt  部分 放置在 本 map 中   ，而 key  在外部 key  中
    private LinkedHashMap<String, Object> criteria = new LinkedHashMap<String, Object>();

    //  默认值 为 空对象 ，可以使用is() 设置
    private Object isValue = NOT_SET;


    public Criteria() {
        this.criteriaChain = new ArrayList<Criteria>();
    }

    //  通过key  进行构造  并初始化 criteriaChain  放置一个 空的元素
    public Criteria(String key) {
        this.criteriaChain = new ArrayList<Criteria>();
        this.criteriaChain.add(this);
        this.key = key;
    }

    //  获取上一阶段的   Criteria 的 内部 criteriaChain
    //  并将自己添加到 该criteriaChain 的 尾部。
    protected Criteria(List<Criteria> criteriaChain, String key) {
        this.criteriaChain = criteriaChain;
        this.criteriaChain.add(this);
        this.key = key;
    }

    /**
     *  根据指定属性名创建Criteria实例的静态方法
     *
     * @param key 字段属性名
     * @return Criteria实例.
     *
     */
    public static Criteria where(String key) {
        return new Criteria(key);
    }




    private static boolean requiresGeoJsonFormat(Object value) {

       return value instanceof Geometry;
    }

    /**
     * Lookup the MongoDB specific flags for a given regex option string.
     *
     * @param s the Regex option/flag to look up. Can be {@literal null}.
     * @return zero if given {@link String} is {@literal null} or empty.
     */
    private static int regexFlags( String s ) {

        int flags = 0;

        if (s == null) {
            return flags;
        }

        for (final char f : s.toLowerCase().toCharArray()) {
            flags |= regexFlag(f);
        }

        return flags;
    }

    /**
     * Lookup the MongoDB specific flags for a given character.
     *
     * @param c the Regex option/flag to look up.
     * @return
     * @throws IllegalArgumentException for unknown flags
     */
    private static int regexFlag(char c) {

        int flag = FLAG_LOOKUP[c];

        if (flag == 0) {
            throw new IllegalArgumentException(String.format("Unrecognized flag [%c]", c));
        }

        return flag;
    }

    /**
     * Static factory method to create a Criteria using the provided key
     *
     * @return new instance of {@link Criteria}.
     */
    public Criteria and(String key) {
        return new Criteria(this.criteriaChain, key);
    }

    /**
     * Creates a criterion using equality
     *
     * @param value can be {@literal null}.
     * @return this.
     * where  的 key  即为 本类对象的key
     */
    public Criteria is( Object value ) {

        if (!isValue.equals(NOT_SET)) {
            throw new InvalidMongoDbApiUsageException(
                    "Multiple 'is' values declared. You need to use 'and' with multiple criteria");
        }

        if (lastOperatorWasNot()) {
            throw new InvalidMongoDbApiUsageException("Invalid query: 'not' can't be used with 'is' - use 'ne' instead.");
        }

        this.isValue = value;
        return this;
    }

    private boolean lastOperatorWasNot() {
        return !this.criteria.isEmpty() && "$not".equals(this.criteria.keySet().toArray()[this.criteria.size() - 1]);
    }

    /**
     * Creates a criterion using the {@literal $ne} operator.
     *
     * @param value can be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/ne/">MongoDB Query operator: $ne</a>
     */
    public Criteria ne( Object value ) {
        criteria.put("$ne", value);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $lt} operator.
     *
     * @param value must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/lt/">MongoDB Query operator: $lt</a>
     */
    public Criteria lt(Object value) {
        criteria.put("$lt", value);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $lte} operator.
     *
     * @param value must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/lte/">MongoDB Query operator: $lte</a>
     */
    public Criteria lte(Object value) {
        criteria.put("$lte", value);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $gt} operator.
     *
     * @param value must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/gt/">MongoDB Query operator: $gt</a>
     */
    public Criteria gt(Object value) {
        criteria.put("$gt", value);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $gte} operator.
     *
     * @param value can be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/gte/">MongoDB Query operator: $gte</a>
     */
    public Criteria gte(Object value) {
        criteria.put("$gte", value);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $in} operator.
     *
     * @param values the values to match against
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/in/">MongoDB Query operator: $in</a>
     */
    public Criteria in(Object... values) {
        if (values.length > 1 && values[1] instanceof Collection) {
            throw new InvalidMongoDbApiUsageException(
                    "You can only pass in one argument of type " + values[1].getClass().getName());
        }
        criteria.put("$in", Arrays.asList(values));
        return this;
    }

    /**
     * Creates a criterion using the {@literal $in} operator.
     *
     * @param values the collection containing the values to match against
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/in/">MongoDB Query operator: $in</a>
     */
    public Criteria in(Collection<?> values) {
        criteria.put("$in", values);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $nin} operator.
     *
     * @param values
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/nin/">MongoDB Query operator: $nin</a>
     */
    public Criteria nin(Object... values) {
        return nin(Arrays.asList(values));
    }

    /**
     * Creates a criterion using the {@literal $nin} operator.
     *
     * @param values must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/nin/">MongoDB Query operator: $nin</a>
     */
    public Criteria nin(Collection<?> values) {
        criteria.put("$nin", values);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $mod} operator.
     *
     * @param value     must not be {@literal null}.
     * @param remainder must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/mod/">MongoDB Query operator: $mod</a>
     */
    public Criteria mod(Number value, Number remainder) {
        List<Object> l = new ArrayList<Object>();
        l.add(value);
        l.add(remainder);
        criteria.put("$mod", l);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $all} operator.
     *
     * @param values must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/all/">MongoDB Query operator: $all</a>
     */
    public Criteria all(Object... values) {
        return all(Arrays.asList(values));
    }

    /**
     * Creates a criterion using the {@literal $all} operator.
     *
     * @param values must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/all/">MongoDB Query operator: $all</a>
     */
    public Criteria all(Collection<?> values) {
        criteria.put("$all", values);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $size} operator.
     *
     * @param size
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/size/">MongoDB Query operator: $size</a>
     */
    public Criteria size(int size) {
        criteria.put("$size", size);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $exists} operator.
     *
     * @param value
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/exists/">MongoDB Query operator: $exists</a>
     */
    public Criteria exists(boolean value) {
        criteria.put("$exists", value);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $type} operator.
     *
     * @param typeNumber
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/type/">MongoDB Query operator: $type</a>
     */
    public Criteria type(int typeNumber) {
        criteria.put("$type", typeNumber);
        return this;
    }


    /**
     * Creates a criterion using the {@literal $not} meta operator which affects the clause directly following
     *
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/not/">MongoDB Query operator: $not</a>
     */
    public Criteria not() {
        return not(null);
    }

    /**
     * Creates a criterion using the {@literal $not} operator.
     *
     * @param value can be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/not/">MongoDB Query operator: $not</a>
     */
    private Criteria not( Object value ) {
        criteria.put("$not", value);
        return this;
    }

    /**
     * Creates a criterion using a {@literal $regex} operator.
     *
     * @param regex must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/regex/">MongoDB Query operator: $regex</a>
     */
    public Criteria regex(String regex) {
        return regex(regex, null);
    }

    /**
     * Creates a criterion using a {@literal $regex} and {@literal $options} operator.
     *
     * @param regex   must not be {@literal null}.
     * @param options can be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/regex/">MongoDB Query operator: $regex</a>
     */
    public Criteria regex( String regex, String options ) {
        return regex(toPattern(regex, options));
    }

    /**
     * Syntactical sugar for {@link #is(Object)} making obvious that we create a regex predicate.
     *
     * @param pattern must not be {@literal null}.
     * @return this.
     */
    public Criteria regex(Pattern pattern) {

        Precondition.notNull(pattern, "Pattern must not be null!");

        if (lastOperatorWasNot()) {
            return not(pattern);
        }

        this.isValue = pattern;
        return this;
    }

    /**
     * Use a MongoDB native {@link BsonRegularExpression}.
     *
     * @param regex must not be {@literal null}.
     * @return this.
     */
    public Criteria regex(BsonRegularExpression regex) {

        if (lastOperatorWasNot()) {
            return not(regex);
        }

        this.isValue = regex;
        return this;
    }

    private Pattern toPattern( String regex, String options ) {

        Precondition.notNull(regex, "Regex string must not be null!");

        return Pattern.compile(regex, regexFlags(options));
    }

    /**
     * Creates a geospatial criterion using a {@literal $geoWithin $centerSphere} operation. This is only available for
     * Mongo 2.4 and higher.
     *
     * @param point must not be {@literal null}
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/geoWithin/">MongoDB Query operator:
     *      $geoWithin</a>
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/centerSphere/">MongoDB Query operator:
     *      $centerSphere</a>
     */
    public Criteria withinSphere(Point point,Double radius) {

        Precondition.notNull(point, "Point must not be null!");

        criteria.put("$geoWithin", new Document(centerSphere,new Object[]{new Double[]{point.getPosition().getValues().get(0),point.getPosition().getValues().get(1)},radius}));
        return this;
    }

    public Criteria withinCenter(Point point,Double radius) {

        Precondition.notNull(point, "Point must not be null!");

        criteria.put("$geoWithin", new Document(center,new Object[]{new Double[]{point.getPosition().getValues().get(0),point.getPosition().getValues().get(1)},radius}));
        return this;
    }

    public Criteria withinPolygon(MultiPoint multiPoint) {

        Precondition.notNull(multiPoint, "multiPoint must not be null!");

        List<Position> coordinates = multiPoint.getCoordinates();

        Object[] objects = new Object[coordinates.size()];
        for (int i = 0;i < coordinates.size();i++){
            List<Double> values = coordinates.get(i).getValues();
            objects[i] = values;
        }

        criteria.put("$geoWithin", new Document(polygon,objects));
        return this;
    }


    /**
     * Creates a geospatial criterion using a {@literal $geoWithin $centerSphere} operation. This is only available for
     * Mongo 2.4 and higher.
     *
     * @param geometry must not be {@literal null}
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/geoWithin/">MongoDB Query operator:
     *      $geoWithin</a>
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/centerSphere/">MongoDB Query operator:
     *      $centerSphere</a>
     */
    public Criteria geowithin(Geometry geometry) {

        Precondition.notNull(geometry, "geometry must not be null!");

        criteria.put("$geoWithin", geometry);
        return this;
    }





    /**
     * Creates a geospatial criterion using a {@literal $near} operation.
     *
     * @param point must not be {@literal null}
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/near/">MongoDB Query operator: $near</a>
     */
    public Criteria near(Point point) {

        Precondition.notNull(point, "Point must not be null!");

        criteria.put("$near", point);
        return this;
    }

    /**
     * Creates a geospatial criterion using a {@literal $nearSphere} operation. This is only available for Mongo 1.7 and
     * higher.
     *
     * @param point must not be {@literal null}
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/nearSphere/">MongoDB Query operator:
     * $nearSphere</a>
     */
    public Criteria nearSphere( Point point) {

        Precondition.notNull(point, "Point must not be null!");

        criteria.put("$nearSphere", point);
        return this;
    }

    /**
     * Creates criterion using {@code $geoIntersects} operator which matches intersections of the given {@code geoJson}
     * structure and the documents one. Requires MongoDB 2.4 or better.
     *
     * @return this.
     */
    @SuppressWarnings("rawtypes")
    public Criteria geointersects( Geometry geometry) {

        Precondition.notNull(geometry, "geometry must not be null!");
        criteria.put("$geoIntersects", geometry);
        return this;
    }

    /**
     * Creates a geo-spatial criterion using a {@literal $maxDistance} operation, for use with $near
     *
     * @param maxDistance
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/maxDistance/">MongoDB Query operator:
     * $maxDistance</a>
     */
    public Criteria maxDistance(double maxDistance) {

        if (createNearCriteriaForCommand("$near", "$maxDistance", maxDistance)
                || createNearCriteriaForCommand("$nearSphere", "$maxDistance", maxDistance)) {
            return this;
        }

        criteria.put("$maxDistance", maxDistance);
        return this;
    }

    /**
     * Creates a geospatial criterion using a {@literal $minDistance} operation, for use with {@literal $near} or
     * {@literal $nearSphere}.
     *
     * @param minDistance
     * @return this.
     */
    public Criteria minDistance(double minDistance) {

        if (createNearCriteriaForCommand("$near", "$minDistance", minDistance)
                || createNearCriteriaForCommand("$nearSphere", "$minDistance", minDistance)) {
            return this;
        }

        criteria.put("$minDistance", minDistance);
        return this;
    }

    /**
     * Creates a criterion using the {@literal $elemMatch} operator
     *
     * @param criteria must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/elemMatch/">MongoDB Query operator:
     * $elemMatch</a>
     */
    public Criteria elemMatch(Criteria criteria) {
        this.criteria.put("$elemMatch", criteria.getCriteriaObject());
        return this;
    }


    /**
     * Use {@link BitwiseCriteriaOperators} as gateway to create a criterion using one of the
     * <a href="https://docs.mongodb.com/manual/reference/operator/query-bitwise/">bitwise operators</a> like
     * {@code $bitsAllClear}.
     *
     * @return new instance of {@link BitwiseCriteriaOperators}. Never {@literal null}.
     */
    public BitwiseCriteriaOperators bits() {
        return new BitwiseCriteriaOperatorsImpl(this);
    }

    /**
     * Creates an 'or' criteria using the $or operator for all of the provided criteria
     * <p>
     * Note that mongodb doesn't support an $or operator to be wrapped in a $not operator.
     * <p>
     *
     * @param criteria must not be {@literal null}.
     * @return this.
     * @throws IllegalArgumentException if {@link #orOperator(Criteria...)} follows a not() call directly.
     */
    public Criteria orOperator(Criteria... criteria) {
        BasicDBList bsonList = createCriteriaList(criteria);
        return registerCriteriaChainElement(new Criteria("$or").is(bsonList));
    }

    /**
     * Creates a 'nor' criteria using the $nor operator for all of the provided criteria.
     * <p>
     * Note that mongodb doesn't support an $nor operator to be wrapped in a $not operator.
     * <p>
     *
     * @param criteria must not be {@literal null}.
     * @return this.
     * @throws IllegalArgumentException if {@link #norOperator(Criteria...)} follows a not() call directly.
     */
    public Criteria norOperator(Criteria... criteria) {
        BasicDBList bsonList = createCriteriaList(criteria);
        return registerCriteriaChainElement(new Criteria("$nor").is(bsonList));
    }

    /**
     * Creates an 'and' criteria using the $and operator for all of the provided criteria.
     * <p>
     * Note that mongodb doesn't support an $and operator to be wrapped in a $not operator.
     * <p>
     *
     * @param criteria must not be {@literal null}.
     * @return this.
     * @throws IllegalArgumentException if {@link #andOperator(Criteria...)} follows a not() call directly.
     */
    public Criteria andOperator(Criteria... criteria) {
        BasicDBList bsonList = createCriteriaList(criteria);
        return registerCriteriaChainElement(new Criteria("$and").is(bsonList));
    }

    private Criteria registerCriteriaChainElement(Criteria criteria) {

        if (lastOperatorWasNot()) {
            throw new IllegalArgumentException(
                    "operator $not is not allowed around criteria chain element: " + criteria.getCriteriaObject());
        } else {
            criteriaChain.add(criteria);
        }
        return this;
    }

    /*
     * @see CriteriaDefinition#getKey()
     */
    @Override

    public String getKey() {
        return this.key;
    }

    /*
     * (non-Javadoc)
     * @see CriteriaDefinition#getCriteriaObject()
     *
     *  初始化的时候 把自己放入  criteriaChain  中
     *
     *  获取时  如果size  刚好为1  就直接放入
     *
     *  否则 criteriaChain  中有多个元素  或者为空
     *
     *  如果为空 则 直接返回 空的  criteria
     *
     *  如果不为空
     *  则 遍历 criteriaChain 并 迭代 后放入 同一个 document  中
     *
     *
     *
     */
    @Override
    public Document getCriteriaObject() {

        if (this.criteriaChain.size() == 1) {
            return criteriaChain.get(0).getSingleCriteriaObject();
        } else if (CollectionUtil.isEmpty(this.criteriaChain) && !CollectionUtil.isEmpty(this.criteria)) {
            return getSingleCriteriaObject();
        } else {

            Document criteriaObject = new Document();
            for (Criteria c : this.criteriaChain) {
                Document document = c.getSingleCriteriaObject();
                for (String k : document.keySet()) {
                    setValue(criteriaObject, k, document.get(k));
                }
            }
            return criteriaObject;
        }
    }

    /*
     * (non-Javadoc)
     * @see CriteriaBackupDefinition#getCriteriaObject()
     *
     *  初始化的时候 把自己放入  criteriaChain  中
     *
     *  获取时  如果size  刚好为1  就直接放入
     *
     *  否则 criteriaChain  中有多个元素  或者为空
     *
     *  如果为空 则 直接返回 空的  criteria
     *
     *  如果不为空
     *  则 遍历 criteriaChain 并 迭代 后放入 同一个 document  中
     *
     *
     *
     */
    protected Document getSingleCriteriaObject() {

        Document document = new Document();
        //  设置为 false
        boolean not = false;

        //  因为 保存的是个 linked  hashed  Map    当然  在 "key" -> "value"  时候 该 criteria  为空  ；在 "key" -> {filterOp ：value  } 时  该criteria  有值
        for (Entry<String, Object> entry : criteria.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            if (requiresGeoJsonFormat(value)) {
                //  地理位置这块 key  fieldName  在外面
                /**
                 * <location field>: {
                 *       $geoWithin: {
                 *          $geometry: {
                 *             type: <"Polygon" or "MultiPolygon"> ,
                 *             coordinates: [ <coordinates> ]
                 *          }
                 *       }
                 *    }
                 */

                value = new Document("$geometry", value);
            }

            if (not) {
                Document notDocument = new Document();
                notDocument.put(key, value);
                document.put("$not", notDocument);
                not = false;
            } else {
                if ("$not".equals(key) && value == null) {
                    not = true;
                } else {
                    document.put(key, value);
                }
            }
        }

        if (!StrUtil.hasText(this.key)) {
            if (not) {
                return new Document("$not", document);
            }
            return document;
        }

        Document queryCriteria = new Document();

        if (!NOT_SET.equals(isValue)) {
            queryCriteria.put(this.key, this.isValue);
            queryCriteria.putAll(document);
        } else {
            queryCriteria.put(this.key, document);
        }

        return queryCriteria;
    }

    private BasicDBList createCriteriaList(Criteria[] criteria) {
        BasicDBList bsonList = new BasicDBList();
        for (Criteria c : criteria) {
            bsonList.add(c.getCriteriaObject());
        }
        return bsonList;
    }

    private void setValue(Document document, String key, Object value) {
        Object existing = document.get(key);
        if (existing == null) {
            document.put(key, value);
        } else {
            throw new InvalidMongoDbApiUsageException("Due to limitations of the com.mongodb.BasicDocument, "
                    + "you can't add a second '" + key + "' expression specified as '" + key + " : " + value + "'. "
                    + "Criteria already contains '" + key + " : " + existing + "'.");
        }
    }

    private boolean createNearCriteriaForCommand(String command, String operation, double maxDistance) {

        if (!criteria.containsKey(command)) {
            return false;
        }

        Object existingNearOperationValue = criteria.get(command);

        if (existingNearOperationValue instanceof Document) {

            ((Document) existingNearOperationValue).put(operation, maxDistance);

            return true;

        }

        return false;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }

        Criteria that = (Criteria) obj;

        if (this.criteriaChain.size() != that.criteriaChain.size()) {
            return false;
        }

        for (int i = 0; i < this.criteriaChain.size(); i++) {

            Criteria left = this.criteriaChain.get(i);
            Criteria right = that.criteriaChain.get(i);

            if (!simpleCriteriaEquals(left, right)) {
                return false;
            }
        }

        return true;
    }

    private boolean simpleCriteriaEquals(Criteria left, Criteria right) {

        boolean keyEqual = left.key == null ? right.key == null : left.key.equals(right.key);
        boolean criteriaEqual = left.criteria.equals(right.criteria);
        boolean valueEqual = isEqual(left.isValue, right.isValue);

        return keyEqual && criteriaEqual && valueEqual;
    }

    /**
     * Checks the given objects for equality. Handles {@link Pattern} and arrays correctly.
     *
     * 比较两个对象是否相同
     * @param left
     * @param right
     * @return
     */
    private boolean isEqual(Object left, Object right) {

        if (left == null) {
            return right == null;
        }

        if (Pattern.class.isInstance(left)) {

            if (!Pattern.class.isInstance(right)) {
                return false;
            }

            Pattern leftPattern = (Pattern) left;
            Pattern rightPattern = (Pattern) right;

            return leftPattern.pattern().equals(rightPattern.pattern()) //
                    && leftPattern.flags() == rightPattern.flags();
        }

        return ObjectUtil.nullSafeEquals(left, right);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result += nullSafeHashCode(key);
        result += criteria.hashCode();
        result += nullSafeHashCode(isValue);

        return result;
    }

    /**
     * MongoDB specific like {@code $bitsAllClear, $bitsAllSet,...} for usage with {@link Criteria#bits()} and {@link Query}.
     *
     */
    public interface BitwiseCriteriaOperators {

        /**
         * Creates a criterion using {@literal $bitsAllClear} matching documents where all given bit positions are clear
         * (i.e. 0).
         *
         * @param numericBitmask non-negative numeric bitmask.
         * @return target {@link Criteria}.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAllClear/">MongoDB Query operator:
         * $bitsAllClear</a>
         */
        Criteria allClear(int numericBitmask);

        /**
         * Creates a criterion using {@literal $bitsAllClear} matching documents where all given bit positions are clear
         * (i.e. 0).
         *
         * @param bitmask string representation of a bitmask that will be converted to its base64 encoded {@link Binary}
         *                representation. Must not be {@literal null} nor empty.
         * @return target {@link Criteria}.
         * @throws IllegalArgumentException when bitmask is {@literal null} or empty.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAllClear/">MongoDB Query operator:
         * $bitsAllClear</a>
         */
        Criteria allClear(String bitmask);

        /**
         * Creates a criterion using {@literal $bitsAllClear} matching documents where all given bit positions are clear
         * (i.e. 0).
         *
         * @param positions list of non-negative integer positions. Positions start at 0 from the least significant bit.
         *                  Must not be {@literal null} nor contain {@literal null} elements.
         * @return target {@link Criteria}.
         * @throws IllegalArgumentException when positions is {@literal null} or contains {@literal null} elements.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAllClear/">MongoDB Query operator:
         * $bitsAllClear</a>
         */
        Criteria allClear(List<Integer> positions);

        /**
         * Creates a criterion using {@literal $bitsAllSet} matching documents where all given bit positions are set (i.e.
         * 1).
         *
         * @param numericBitmask non-negative numeric bitmask.
         * @return target {@link Criteria}.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAllSet/">MongoDB Query operator:
         * $bitsAllSet</a>
         */
        Criteria allSet(int numericBitmask);

        /**
         * Creates a criterion using {@literal $bitsAllSet} matching documents where all given bit positions are set (i.e.
         * 1).
         *
         * @param bitmask string representation of a bitmask that will be converted to its base64 encoded {@link Binary}
         *                representation. Must not be {@literal null} nor empty.
         * @return target {@link Criteria}.
         * @throws IllegalArgumentException when bitmask is {@literal null} or empty.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAllSet/">MongoDB Query operator:
         * $bitsAllSet</a>
         */
        Criteria allSet(String bitmask);

        /**
         * Creates a criterion using {@literal $bitsAllSet} matching documents where all given bit positions are set (i.e.
         * 1).
         *
         * @param positions list of non-negative integer positions. Positions start at 0 from the least significant bit.
         *                  Must not be {@literal null} nor contain {@literal null} elements.
         * @return target {@link Criteria}.
         * @throws IllegalArgumentException when positions is {@literal null} or contains {@literal null} elements.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAllSet/">MongoDB Query operator:
         * $bitsAllSet</a>
         */
        Criteria allSet(List<Integer> positions);

        /**
         * Creates a criterion using {@literal $bitsAllClear} matching documents where any given bit positions are clear
         * (i.e. 0).
         *
         * @param numericBitmask non-negative numeric bitmask.
         * @return target {@link Criteria}.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAnyClear/">MongoDB Query operator:
         * $bitsAnyClear</a>
         */
        Criteria anyClear(int numericBitmask);

        /**
         * Creates a criterion using {@literal $bitsAllClear} matching documents where any given bit positions are clear
         * (i.e. 0).
         *
         * @param bitmask string representation of a bitmask that will be converted to its base64 encoded {@link Binary}
         *                representation. Must not be {@literal null} nor empty.
         * @return target {@link Criteria}.
         * @throws IllegalArgumentException when bitmask is {@literal null} or empty.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAnyClear/">MongoDB Query operator:
         * $bitsAnyClear</a>
         */
        Criteria anyClear(String bitmask);

        /**
         * Creates a criterion using {@literal $bitsAllClear} matching documents where any given bit positions are clear
         * (i.e. 0).
         *
         * @param positions list of non-negative integer positions. Positions start at 0 from the least significant bit.
         *                  Must not be {@literal null} nor contain {@literal null} elements.
         * @return target {@link Criteria}.
         * @throws IllegalArgumentException when positions is {@literal null} or contains {@literal null} elements.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAnyClear/">MongoDB Query operator:
         * $bitsAnyClear</a>
         */
        Criteria anyClear(List<Integer> positions);

        /**
         * Creates a criterion using {@literal $bitsAllSet} matching documents where any given bit positions are set (i.e.
         * 1).
         *
         * @param numericBitmask non-negative numeric bitmask.
         * @return target {@link Criteria}.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAnySet/">MongoDB Query operator:
         * $bitsAnySet</a>
         */
        Criteria anySet(int numericBitmask);

        /**
         * Creates a criterion using {@literal $bitsAnySet} matching documents where any given bit positions are set (i.e.
         * 1).
         *
         * @param bitmask string representation of a bitmask that will be converted to its base64 encoded {@link Binary}
         *                representation. Must not be {@literal null} nor empty.
         * @return target {@link Criteria}.
         * @throws IllegalArgumentException when bitmask is {@literal null} or empty.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAnySet/">MongoDB Query operator:
         * $bitsAnySet</a>
         */
        Criteria anySet(String bitmask);

        /**
         * Creates a criterion using {@literal $bitsAnySet} matching documents where any given bit positions are set (i.e.
         * 1).
         *
         * @param positions list of non-negative integer positions. Positions start at 0 from the least significant bit.
         *                  Must not be {@literal null} nor contain {@literal null} elements.
         * @return target {@link Criteria}.
         * @throws IllegalArgumentException when positions is {@literal null} or contains {@literal null} elements.
         * @see <a href="https://docs.mongodb.com/manual/reference/operator/query/bitsAnySet/">MongoDB Query operator:
         * $bitsAnySet</a>
         */
        Criteria anySet(List<Integer> positions);

    }

    /**
     * Default implementation of {@link BitwiseCriteriaOperators}.
     *
     * @currentRead Beyond the Shadows - Brent Weeks
     */
    private static class BitwiseCriteriaOperatorsImpl implements BitwiseCriteriaOperators {

        private final Criteria target;

        BitwiseCriteriaOperatorsImpl(Criteria target) {
            this.target = target;
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#allClear(int)
         */
        @Override
        public Criteria allClear(int numericBitmask) {
            return numericBitmask("$bitsAllClear", numericBitmask);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#allClear(java.lang.String)
         */
        @Override
        public Criteria allClear(String bitmask) {
            return stringBitmask("$bitsAllClear", bitmask);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#allClear(java.util.List)
         */
        @Override
        public Criteria allClear(List<Integer> positions) {
            return positions("$bitsAllClear", positions);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#allSet(int)
         */
        @Override
        public Criteria allSet(int numericBitmask) {
            return numericBitmask("$bitsAllSet", numericBitmask);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#allSet(java.lang.String)
         */
        @Override
        public Criteria allSet(String bitmask) {
            return stringBitmask("$bitsAllSet", bitmask);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#allSet(java.util.List)
         */
        @Override
        public Criteria allSet(List<Integer> positions) {
            return positions("$bitsAllSet", positions);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#anyClear(int)
         */
        @Override
        public Criteria anyClear(int numericBitmask) {
            return numericBitmask("$bitsAnyClear", numericBitmask);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#anyClear(java.lang.String)
         */
        @Override
        public Criteria anyClear(String bitmask) {
            return stringBitmask("$bitsAnyClear", bitmask);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#anyClear(java.util.List)
         */
        @Override
        public Criteria anyClear(List<Integer> positions) {
            return positions("$bitsAnyClear", positions);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#anySet(int)
         */
        @Override
        public Criteria anySet(int numericBitmask) {
            return numericBitmask("$bitsAnySet", numericBitmask);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#anySet(java.lang.String)
         */
        @Override
        public Criteria anySet(String bitmask) {
            return stringBitmask("$bitsAnySet", bitmask);
        }

        /*
         * (non-Javadoc)
         * @see BitwiseCriteriaOperators#anySet(java.util.Collection)
         */
        @Override
        public Criteria anySet(List<Integer> positions) {
            return positions("$bitsAnySet", positions);
        }

        private Criteria positions(String operator, List<Integer> positions) {

            Precondition.notNull(positions, "Positions must not be null!");
            Precondition.noNullElements(positions.toArray(), "Positions must not contain null values.");

            target.criteria.put(operator, positions);
            return target;
        }

        private Criteria stringBitmask(String operator, String bitmask) {

            Precondition.hasText(bitmask, "Bitmask must not be null!");

            target.criteria.put(operator, new Binary(Base64.decode(bitmask)));
            return target;
        }

        private Criteria numericBitmask(String operator, int bitmask) {

            target.criteria.put(operator, bitmask);
            return target;
        }
    }
}
