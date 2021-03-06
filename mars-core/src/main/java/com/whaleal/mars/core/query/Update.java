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
import com.whaleal.icefrog.core.map.MapUtil;
import com.whaleal.icefrog.core.util.StrUtil;

import com.whaleal.mars.codecs.writer.DocumentWriter;
import com.whaleal.mars.core.aggregation.codecs.ExpressionHelper;
//import com.whaleal.mars.core.aggregation.stages.Sort;
import com.whaleal.mars.core.internal.InvalidMongoDbApiUsageException;

import org.bson.Document;

import java.util.*;

/**
 * ??????mongoDB??????????????????
 *
 */
public class Update implements UpdateDefinition {

    private boolean isolated = false;
    private Set<String> keysToUpdate = new HashSet<>();
    private Map<String, Object> modifierOps = new LinkedHashMap<>();
    private Map<String, PushOperatorBuilder> pushCommandBuilders = new LinkedHashMap<>(1);
    private List<ArrayFilter> arrayFilters = new ArrayList<>();

    /**
     * ???????????????????????????
     *
     * @param key ????????????????????????.
     * @return ??????????????????.
     */
    public static Update update( String key, Object value ) {
        return new Update().set(key, value);
    }

    /**
     *
     * ??????Document???????????????????????????????????????
     *
     * @param object  ????????????.
     * @param exclude ??????????????????.
     * @return update??????.
     */
    public static Update fromDocument( Document object, String... exclude) {

        Update update = new Update();
        List<String> excludeList = Arrays.asList(exclude);

        for (String key : object.keySet()) {

            if (excludeList.contains(key)) {
                continue;
            }

            Object value = object.get(key);
            update.modifierOps.put(key, value);
            if (isKeyword(key) && value instanceof Document) {
                update.keysToUpdate.addAll(((Document) value).keySet());
            } else {
                update.keysToUpdate.add(key);
            }
        }

        return update;
    }

    /**
     * ??????key????????????????????????
     *
     * @param key ???????????????.
     * @return ??????key????????????"$"?????????true.
     */
    private static boolean isKeyword(String key) {
        return StrUtil.startsWithIgnoreCase(key, "$");
    }

    /**
     * ??????$set??????????????????
     *
     * @param key   ???????????????.
     * @param value ????????????????????????null
     * @return this.
     */
    public Update set( String key, Object value ) {
        addMultiFieldOperation("$set", key, value);
        return this;
    }


    /**
     * ??????$setOnInsert?????????????????????????????????
     *
     * @param key   ?????????.
     * @param value ?????????null.
     * @return Update??????.
     */
    public Update setOnInsert( String key, Object value ) {
        addMultiFieldOperation("$setOnInsert", key, value);
        return this;
    }

    /**
     * ??????$unset??????????????????
     *
     * @param key ?????????.
     * @return Update??????.
     */
    public Update unset( String key) {
        addMultiFieldOperation("$unset", key, 1);
        return this;
    }

    /**
     * ??????????????????????????????/???
     *
     * @param key ?????????.
     * @param inc ????????????.
     * @return Update??????.
     */
    public Update inc( String key, Number inc) {
        addMultiFieldOperation("$inc", key, inc);
        return this;
    }


    @Override
    public void inc(String key) {
        inc(key, 1L);
    }

    /**
     * ??????$push??????????????????
     *
     * @param key   ?????????.
     * @param value ????????????.
     * @return Update??????.
     */
    public Update push( String key, Object value ) {
        addMultiFieldOperation("$push", key, value);
        return this;
    }

    /**
     * ???????????????key??????PushOperatorBuilder??????
     *
     * @param key the field name.
     * @return  PushOperatorBuilder??????
     */
    public PushOperatorBuilder push(String key) {

        if (!pushCommandBuilders.containsKey(key)) {
            pushCommandBuilders.put(key, new PushOperatorBuilder(key));
        }
        return pushCommandBuilders.get(key);
    }

    /**
     * ?????????key??????pushAll?????????update??????
     * @param key    ?????????.
     * @param values ????????????.
     * @return this.
     * @deprecated as of MongoDB 2.4. Removed in MongoDB 3.6. Use {@link #push(String) $push $each} instead.
     */
    @Deprecated
    public Update pushAll( String key, Object[] values) {
        addMultiFieldOperation("$pushAll", key, Arrays.asList(values));
        return this;
    }

    /**
     * ???????????????key??????AddToSetBuilder??????
     *
     * @param key ?????????.
     * @return AddToSetBuilder??????.
     */
    public AddToSetBuilder addToSet(String key) {
        return new AddToSetBuilder(key);
    }

    /**
     * ?????????key????????????addToSet?????????update??????
     *
     * @param key   ?????????.
     * @param value ????????????.
     * @return this.
     */
    public Update addToSet( String key, Object value ) {
        addMultiFieldOperation("$addToSet", key, value);
        return this;
    }

    /**
     * ?????????key????????????pop?????????update??????
     *
     * @param key ?????????.
     * @param pos ????????????.
     * @return this.
     */
    public Update pop( String key, Position pos) {
        addMultiFieldOperation("$pop", key, pos == Position.FIRST ? -1 : 1);
        return this;
    }

    /**
     * ??????key????????????pull?????????update??????
     *
     * @param key   the field name.
     * @param value can be {@literal null}.
     * @return this.
     */
    public Update pull( String key, Object value ) {
        addMultiFieldOperation("$pull", key, value);
        return this;
    }

    /**
     * ?????????key??????pullAll???????????????update??????
     *
     * @param key    ?????????.
     * @param values ????????????.
     * @return this.
     */
    public Update pullAll( String key, Object[] values) {
        addMultiFieldOperation("$pullAll", key, Arrays.asList(values));
        return this;
    }

    /**
     * ??????rename?????????update??????
     *
     * @param oldName ????????????.
     * @param newName ????????????.
     * @return this.
     */
    public Update rename( String oldName, String newName) {
        addMultiFieldOperation("$rename", oldName, newName);
        return this;
    }

    /**
     * ?????????key???$currentDate???????????????update??????.
     *
     * @param key ?????????.
     * @return this.
     */
    public Update currentDate( String key) {

        addMultiFieldOperation("$currentDate", key, true);
        return this;
    }

    /**
     * ?????????????????? ??????key?????????????????????????????????timestamp
     *
     * @param key ?????????.
     * @return this.
     */
    public Update currentTimestamp( String key) {

        addMultiFieldOperation("$currentDate", key, new Document("$type", "timestamp"));
        return this;
    }

    /**
     * ????????????????????????key?????????????????????.
     *
     * @param key        must not be {@literal null}.
     * @param multiplier must not be {@literal null}.
     * @return this.
     */
    public Update multiply( String key, Number multiplier) {

        Precondition.notNull(multiplier, "Multiplier must not be null.");
        addMultiFieldOperation("$mul", key, multiplier.doubleValue());
        return this;
    }

    /**
     * Update given key to the {@code value} if the {@code value} is greater than the current value of the field.
     *
     * @param key   must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @return this.
     */
    public Update max( String key, Object value) {

        Precondition.notNull(value, "Value for max operation must not be null.");
        addMultiFieldOperation("$max", key, value);
        return this;
    }

    /**
     * Update given key to the {@code value} if the {@code value} is less than the current value of the field.
     *
     * @param key   must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @return this.
     */
    public Update min( String key, Object value) {

        Precondition.notNull(value, "Value for min operation must not be null.");
        addMultiFieldOperation("$min", key, value);
        return this;
    }

    /**
     * The operator supports bitwise {@code and}, bitwise {@code or}, and bitwise {@code xor} operations.
     *
     * @param key the field name.
     * @return this.
     */
    public BitwiseOperatorBuilder bitwise(String key) {
        return new BitwiseOperatorBuilder(this, key);
    }

    /**
     * Prevents a write operation that affects <strong>multiple</strong> documents from yielding to other reads or writes
     * once the first document is written. <br />
     *
     * @return this.
     */
    public Update isolated() {

        isolated = true;
        return this;
    }

    /**
     * Filter elements in an array that match the given criteria for update. {@link CriteriaDefinition} is passed directly
     * to the driver without further type or field mapping.
     *
     * @param criteria must not be {@literal null}.
     * @return this.
     */
    public Update filterArray( CriteriaDefinition criteria) {

        this.arrayFilters.add(criteria::getCriteriaObject);
        return this;
    }

    /**
     * Filter elements in an array that match the given criteria for update. {@code expression} is used directly with the
     * driver without further further type or field mapping.
     *
     * @param identifier the positional operator identifier filter criteria name.
     * @param expression the positional operator filter expression.
     * @return this.
     */
    public Update filterArray( String identifier, Object expression) {

        this.arrayFilters.add(() -> new Document(identifier, expression));
        return this;
    }


    public Boolean isIsolated() {
        return isolated;
    }


    public Document getUpdateObject() {
        return new Document(modifierOps);
    }

    public List<ArrayFilter> getArrayFilters() {
        return Collections.unmodifiableList(this.arrayFilters);
    }

    /**
     * This method is not called anymore rather override {@link #addMultiFieldOperation(String, String, Object)}.
     *
     * @param operator
     * @param key
     * @param value
     * @deprectaed Use {@link #addMultiFieldOperation(String, String, Object)} instead.
     */
    @Deprecated
    protected void addFieldOperation(String operator, String key, Object value) {

        Precondition.hasText(key, "Key/Path for update must not be null or blank.");

        modifierOps.put(operator, new Document(key, value));
        this.keysToUpdate.add(key);
    }

    protected void addMultiFieldOperation( String operator, String key, Object value ) {

        Precondition.hasText(key, "Key/Path for update must not be null or blank.");
        Object existingValue = this.modifierOps.get(operator);
        Document keyValueMap;

        if (existingValue == null) {
            keyValueMap = new Document();
            this.modifierOps.put(operator, keyValueMap);
        } else {
            if (existingValue instanceof Document) {
                keyValueMap = (Document) existingValue;
            } else {
                throw new InvalidMongoDbApiUsageException(
                        "Modifier Operations should be a LinkedHashMap but was " + existingValue.getClass());
            }
        }

        keyValueMap.put(key, value);
        this.keysToUpdate.add(key);
    }

    /**
     * Determine if a given {@code key} will be touched on execution.
     *
     * @param key the field name.
     * @return {@literal true} if given field is updated.
     */
    public boolean modifies(String key) {
        return this.keysToUpdate.contains(key);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUpdateObject(), isolated);
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

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Update that = (Update) obj;
        if (this.isolated != that.isolated) {
            return false;
        }

        return Objects.equals(this.getUpdateObject(), that.getUpdateObject());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        Document doc = getUpdateObject();

        if (isIsolated()) {
            doc.append("$isolated", 1);
        }

        try{
            return doc.toJson().replaceAll("\":", "\" :").replaceAll("\\{\"", "{ \"");
        }catch (Exception e){

           return MapUtil.toString((Map<?, ?>) doc);

        }

    }

    public enum Position {
        LAST, FIRST
    }


    public static class BitwiseOperatorBuilder {

        private static final String BIT_OPERATOR = "$bit";
        private final String key;
        private final Update reference;

        /**
         * Creates a new {@link BitwiseOperatorBuilder}.
         *
         * @param reference must not be {@literal null}
         * @param key       must not be {@literal null}
         */
        protected BitwiseOperatorBuilder( Update reference, String key) {

            Precondition.notNull(reference, "Reference must not be null!");
            Precondition.notNull(key, "Key must not be null!");

            this.reference = reference;
            this.key = key;
        }

        /**
         * Updates to the result of a bitwise and operation between the current value and the given one.
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update and( long value) {

            addFieldOperation(BitwiseOperator.AND, value);
            return reference;
        }

        /**
         * Updates to the result of a bitwise or operation between the current value and the given one.
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update or( long value) {

            addFieldOperation(BitwiseOperator.OR, value);
            return reference;
        }

        /**
         * Updates to the result of a bitwise xor operation between the current value and the given one.
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update xor( long value) {

            addFieldOperation(BitwiseOperator.XOR, value);
            return reference;
        }

        private void addFieldOperation(BitwiseOperator operator, Number value) {
            reference.addMultiFieldOperation(BIT_OPERATOR, key, new Document(operator.toString(), value));
        }

        private enum BitwiseOperator {
            AND, OR, XOR;

            @Override
            public String toString() {
                return super.toString().toLowerCase();
            }

            ;
        }
    }

    /**
     * Builder for creating {@code $push} modifiers
     */
    public class PushOperatorBuilder {

        private final String key;
        private final Document modifiers;

        PushOperatorBuilder(String key) {
            this.key = key;
            this.modifiers =new Document();
        }

        /**
         * Propagates {@code $each} to {@code $push}
         *
         * @param values
         * @return never {@literal null}.
         */
        public Update each( Object... values) {

            //this.addModifier(new EachStage(values));
            this.modifiers.put("$each",values);
            return Update.this.push(key, this.modifiers);
        }


        /**
         * ????????????$slice????????????PushOperatorBuilder?????????
         *
         * @param count
         * @return never {@literal null}.
         */
        public PushOperatorBuilder slice(int count) {

            this.modifiers.put("$slice",count);

            return this;
        }

        /**
         * Propagates {@code $sort} to {@code $push}. {@code $sort} requires the {@code $each} operator. Forces elements to
         * be sorted by values in given {@literal direction}.
         * ??????PushOperatorBuilder???????????????????????????????????????????????????
         *
         * @param direction must not be {@literal null}.
         * @return never {@literal null}.
         */
        public PushOperatorBuilder sort( int direction) {

            Precondition.notNull(direction, "Direction must not be null.");
            this.modifiers.put("$sort",direction);
            return this;
        }

        /**
         * Propagates {@code $sort} to {@code $push}. {@code $sort} requires the {@code $each} operator. Forces document
         * elements to be sorted in given {@literal order}.
         *
         * @param sort must not be {@literal null}.
         * @return never {@literal null}.
         */
        public PushOperatorBuilder sort(Sort... sort) {

            Precondition.notNull(sort, "Sort must not be null.");

            DocumentWriter writer = new DocumentWriter() ;
            ExpressionHelper.document(writer, () -> {
                for (Sort sort1 : sort) {
                    writer.writeName(sort1.getField());
                    writer.writeInt32(sort1.getOrder());
                }
            });

            //todo
            this.modifiers.put("$sort",writer.getDocument());

            return this;
        }

        /**
         * ??????????????????????????????
         *
         * @param position ??????????????????. ??? MongoDB 3.6 ????????????????????????????????????????????????????????????????????????????????????????????????
         * @return PushOperatorBuilder??????.
         */
        public PushOperatorBuilder atPosition(int position) {


            this.modifiers.put("$position",position);

            return this;
        }

        /**
         * ??????????????????????????????
         *
         * @param position ????????????????????????????????????????????????.
         * @return PushOperatorBuilder??????.
         */
        public PushOperatorBuilder atPosition( Position position ) {

            if (position == null || Position.LAST.equals(position)) {
                return this;
            }

            this.modifiers.put("$position",0);


            return this;
        }

        /**
         * Propagates {@link #value(Object)} to {@code $push}
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update value( Object value) {

            if (this.modifiers.isEmpty()) {
                return Update.this.push(key, value);
            }

            List< Object > objects = Collections.singletonList(value);
            this.modifiers.put("$each",objects);
            //this.addModifier(new EachStage(Collections.singletonList(value)));
            return Update.this.push(key, this.modifiers);
        }

        @Override
        public int hashCode() {
            return Objects.hash(getOuterType(), key, modifiers);
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            PushOperatorBuilder that = (PushOperatorBuilder) obj;

            if (!Objects.equals(getOuterType(), that.getOuterType())) {
                return false;
            }

            return Objects.equals(this.key, that.key) && Objects.equals(this.modifiers, that.modifiers);
        }

        private Update getOuterType() {
            return Update.this;
        }
    }

    /**
     * AddToSetBuilder?????????
     */
    public class AddToSetBuilder {

        private final String key;

        public AddToSetBuilder(String key) {
            this.key = key;
        }

        /**
         * Propagates {@code $each} to {@code $addToSet}
         *
         * @param values must not be {@literal null}.
         * @return never {@literal null}.
         */
        public Update each( Object... values) {
            return Update.this.addToSet(this.key, new Document("$each",values));
        }

        /**
         * Propagates {@link #value(Object)} to {@code $addToSet}
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update value( Object value) {
            return Update.this.addToSet(this.key, value);
        }
    }

}
