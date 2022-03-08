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
import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.icefrog.json.JSONUtil;
import com.whaleal.mars.core.aggregation.stages.Stage;
import com.whaleal.mars.core.internal.InvalidMongoDbApiUsageException;
import com.whaleal.mars.core.query.codec.stage.EachStage;
import com.whaleal.mars.core.query.codec.stage.PositionStage;
import com.whaleal.mars.core.query.codec.stage.SliceStage;
import com.whaleal.mars.core.query.codec.stage.SortStage;
import org.bson.Document;

import java.util.*;

/**
 * Class to easily construct MongoDB update clauses.
 */
public class Update1 implements UpdateDefinition {

    private boolean isolated = false;
    private Set<String> keysToUpdate = new HashSet<>();
    private Map<String, Object> modifierOps = new LinkedHashMap<>();
    private Map<String, PushOperatorBuilder> pushCommandBuilders = new LinkedHashMap<>(1);
    private List<ArrayFilter> arrayFilters = new ArrayList<>();

    /**
     * Static factory method to create an Update using the provided key
     *
     * @param key the field to update.
     * @return new instance of {@link Update1}.
     */
    public static Update1 update(String key, Object value ) {
        return new Update1().set(key, value);
    }

    /**
     * Creates an {@link Update1} instance from the given {@link Document}. Allows to explicitly exclude fields from making
     * it into the created {@link Update1} object. Note, that this will set attributes directly and <em>not</em> use
     * {@literal $set}. This means fields not given in the {@link Document} will be nulled when executing the update. To
     * create an only-updating {@link Update1} instance of a {@link Document}, call {@link #set(String, Object)} for each
     * value in it.
     *
     * @param object  the source {@link Document} to create the update from.
     * @param exclude the fields to exclude.
     * @return new instance of {@link Update1}.
     */
    public static Update1 fromDocument(Document object, String... exclude) {

        Update1 update = new Update1();
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
     * Inspects given {@code key} for '$'.
     *
     * @param key the field name.
     * @return {@literal true} if given key is prefixed.
     */
    private static boolean isKeyword(String key) {
        return StrUtil.startsWithIgnoreCase(key, "$");
    }

    /**
     * Update using the {@literal $set} update modifier
     *
     * @param key   the field name.
     * @param value can be {@literal null}. In this case the property remains in the db with a {@literal null} value. To
     *              remove it use {@link #unset(String)}.
     * @return this.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/update/set/">MongoDB Update operator: $set</a>
     */
    public Update1 set(String key, Object value ) {
        addMultiFieldOperation("$set", key, value);
        return this;
    }


    /**
     * Update using the {@literal $setOnInsert} update modifier
     *
     * @param key   the field name.
     * @param value can be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/setOnInsert/">MongoDB Update operator:
     * $setOnInsert</a>
     */
    public Update1 setOnInsert(String key, Object value ) {
        addMultiFieldOperation("$setOnInsert", key, value);
        return this;
    }

    /**
     * Update using the {@literal $unset} update modifier
     *
     * @param key the field name.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/unset/">MongoDB Update operator: $unset</a>
     */
    public Update1 unset(String key) {
        addMultiFieldOperation("$unset", key, 1);
        return this;
    }

    /**
     * Update using the {@literal $inc} update modifier
     *
     * @param key the field name.
     * @param inc must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/inc/">MongoDB Update operator: $inc</a>
     */
    public Update1 inc(String key, Number inc) {
        addMultiFieldOperation("$inc", key, inc);
        return this;
    }


    @Override
    public void inc(String key) {
        inc(key, 1L);
    }

    /**
     * Update using the {@literal $push} update modifier
     *
     * @param key   the field name.
     * @param value can be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/push/">MongoDB Update operator: $push</a>
     */
    public Update1 push(String key, Object value ) {
        addMultiFieldOperation("$push", key, value);
        return this;
    }

    /**
     * Update using {@code $push} modifier. <br/>
     * Allows creation of {@code $push} command for single or multiple (using {@code $each}) values as well as using
     * {@code $position}.
     *
     * @param key the field name.
     * @return {@link PushOperatorBuilder} for given key
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/push/">MongoDB Update operator: $push</a>
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/each/">MongoDB Update operator: $each</a>
     */
    public PushOperatorBuilder push(String key) {

        if (!pushCommandBuilders.containsKey(key)) {
            pushCommandBuilders.put(key, new PushOperatorBuilder(key));
        }
        return pushCommandBuilders.get(key);
    }

    /**
     * Update using the {@code $pushAll} update modifier. <br>
     * <b>Note</b>: In MongoDB 2.4 the usage of {@code $pushAll} has been deprecated in favor of {@code $push $each}.
     * <b>Important:</b> As of MongoDB 3.6 {@code $pushAll} is not longer supported. Use {@code $push $each} instead.
     * {@link #push(String)}) returns a builder that can be used to populate the {@code $each} object.
     *
     * @param key    the field name.
     * @param values must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/pushAll/">MongoDB Update operator:
     * $pushAll</a>
     * @deprecated as of MongoDB 2.4. Removed in MongoDB 3.6. Use {@link #push(String) $push $each} instead.
     */
    @Deprecated
    public Update1 pushAll(String key, Object[] values) {
        addMultiFieldOperation("$pushAll", key, Arrays.asList(values));
        return this;
    }

    /**
     * Update using {@code $addToSet} modifier. <br/>
     * Allows creation of {@code $push} command for single or multiple (using {@code $each}) values
     *
     * @param key the field name.
     * @return new instance of {@link AddToSetBuilder}.
     */
    public AddToSetBuilder addToSet(String key) {
        return new AddToSetBuilder(key);
    }

    /**
     * Update using the {@literal $addToSet} update modifier
     *
     * @param key   the field name.
     * @param value can be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/addToSet/">MongoDB Update operator:
     * $addToSet</a>
     */
    public Update1 addToSet(String key, Object value ) {
        addMultiFieldOperation("$addToSet", key, value);
        return this;
    }

    /**
     * Update using the {@literal $pop} update modifier
     *
     * @param key the field name.
     * @param pos must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/pop/">MongoDB Update operator: $pop</a>
     */
    public Update1 pop(String key, Position pos) {
        addMultiFieldOperation("$pop", key, pos == Position.FIRST ? -1 : 1);
        return this;
    }

    /**
     * Update using the {@literal $pull} update modifier
     *
     * @param key   the field name.
     * @param value can be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/pull/">MongoDB Update operator: $pull</a>
     */
    public Update1 pull(String key, Object value ) {
        addMultiFieldOperation("$pull", key, value);
        return this;
    }

    /**
     * Update using the {@literal $pullAll} update modifier
     *
     * @param key    the field name.
     * @param values must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/pullAll/">MongoDB Update operator:
     * $pullAll</a>
     */
    public Update1 pullAll(String key, Object[] values) {
        addMultiFieldOperation("$pullAll", key, Arrays.asList(values));
        return this;
    }

    /**
     * Update using the {@literal $rename} update modifier
     *
     * @param oldName must not be {@literal null}.
     * @param newName must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/rename/">MongoDB Update operator:
     * $rename</a>
     */
    public Update1 rename(String oldName, String newName) {
        addMultiFieldOperation("$rename", oldName, newName);
        return this;
    }

    /**
     * Update given key to current date using {@literal $currentDate} modifier.
     *
     * @param key the field name.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/currentDate/">MongoDB Update operator:
     * $currentDate</a>
     */
    public Update1 currentDate(String key) {

        addMultiFieldOperation("$currentDate", key, true);
        return this;
    }

    /**
     * Update given key to current date using {@literal $currentDate : &#123; $type : "timestamp" &#125;} modifier.
     *
     * @param key the field name.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/currentDate/">MongoDB Update operator:
     * $currentDate</a>
     */
    public Update1 currentTimestamp(String key) {

        addMultiFieldOperation("$currentDate", key, new Document("$type", "timestamp"));
        return this;
    }

    /**
     * Multiply the value of given key by the given number.
     *
     * @param key        must not be {@literal null}.
     * @param multiplier must not be {@literal null}.
     * @return this.
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/mul/">MongoDB Update operator: $mul</a>
     */
    public Update1 multiply(String key, Number multiplier) {

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
     * @see <a href="https://docs.mongodb.com/manual/reference/bson-type-comparison-order/">Comparison/Sort Order</a>
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/max/">MongoDB Update operator: $max</a>
     */
    public Update1 max(String key, Object value) {

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
     * @see <a href="https://docs.mongodb.com/manual/reference/bson-type-comparison-order/">Comparison/Sort Order</a>
     * @see <a href="https://docs.mongodb.org/manual/reference/operator/update/min/">MongoDB Update operator: $min</a>
     */
    public Update1 min(String key, Object value) {

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
    public Update1 isolated() {

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
    public Update1 filterArray(CriteriaDefinition criteria) {

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
    public Update1 filterArray(String identifier, Object expression) {

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

        Update1 that = (Update1) obj;
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

        return doc.toJson();
    }

    public enum Position {
        LAST, FIRST
    }


    public static class BitwiseOperatorBuilder {

        private static final String BIT_OPERATOR = "$bit";
        private final String key;
        private final Update1 reference;

        /**
         * Creates a new {@link BitwiseOperatorBuilder}.
         *
         * @param reference must not be {@literal null}
         * @param key       must not be {@literal null}
         */
        protected BitwiseOperatorBuilder(Update1 reference, String key) {

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
        public Update1 and(long value) {

            addFieldOperation(BitwiseOperator.AND, value);
            return reference;
        }

        /**
         * Updates to the result of a bitwise or operation between the current value and the given one.
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update1 or(long value) {

            addFieldOperation(BitwiseOperator.OR, value);
            return reference;
        }

        /**
         * Updates to the result of a bitwise xor operation between the current value and the given one.
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update1 xor(long value) {

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
        public Update1 each(Object... values) {

            //this.addModifier(new EachStage(values));
            this.modifiers.put("$each",values);
            return Update1.this.push(key, this.modifiers);
        }

       /* public void addModifier(Stage modifier) {
            this.modifiers.put(modifier.getStageName(), modifier);
        }*/



        /**
         * Propagates {@code $slice} to {@code $push}. {@code $slice} requires the {@code $each operator}. <br />
         * If {@literal count} is zero, {@code $slice} updates the array to an empty array. <br />
         * If {@literal count} is negative, {@code $slice} updates the array to contain only the last {@code count}
         * elements. <br />
         * If {@literal count} is positive, {@code $slice} updates the array to contain only the first {@code count}
         * elements. <br />
         *
         * @param count
         * @return never {@literal null}.
         */
        public PushOperatorBuilder slice(int count) {

            this.modifiers.put("$slice",count);
            //this.addModifier(new SliceStage(count));
            return this;
        }

        /**
         * Propagates {@code $sort} to {@code $push}. {@code $sort} requires the {@code $each} operator. Forces elements to
         * be sorted by values in given {@literal direction}.
         *
         * @param direction must not be {@literal null}.
         * @return never {@literal null}.
         */
        public PushOperatorBuilder sort(Sort.Direction direction) {

            Precondition.notNull(direction, "Direction must not be null.");
            this.modifiers.put("$sort",direction);
            //this.addModifier(new SortStage(direction));
            return this;
        }

        /**
         * Propagates {@code $sort} to {@code $push}. {@code $sort} requires the {@code $each} operator. Forces document
         * elements to be sorted in given {@literal order}.
         *
         * @param sort must not be {@literal null}.
         * @return never {@literal null}.
         */
        public PushOperatorBuilder sort(Sort sort) {

            Precondition.notNull(sort, "Sort must not be null.");
            //SortStage sortStage = new SortStage(sort);
            this.modifiers.put("$sort",sort);
            //this.addModifier(new SortStage(sort));
            return this;
        }

        /**
         * Forces values to be added at the given {@literal position}.
         *
         * @param position the position offset. As of MongoDB 3.6 use a negative value to indicate starting from the end,
         *                 counting (but not including) the last element of the array.
         * @return never {@literal null}.
         */
        public PushOperatorBuilder atPosition(int position) {
            PositionStage positionStage = new PositionStage(position);

            this.modifiers.put(positionStage.getKey(),positionStage.getValue());
            //this.addModifier(positionStage);
            return this;
        }

        /**
         * Forces values to be added at given {@literal position}.
         *
         * @param position can be {@literal null} which will be appended at the last position.
         * @return never {@literal null}.
         */
        public PushOperatorBuilder atPosition( Position position ) {

            if (position == null || Position.LAST.equals(position)) {
                return this;
            }

            this.modifiers.put(new PositionStage(0).getKey(),new PositionStage(0).getValue());
            //this.addModifier(new PositionStage(0));

            return this;
        }

        /**
         * Propagates {@link #value(Object)} to {@code $push}
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update1 value(Object value) {

            if (this.modifiers.isEmpty()) {
                return Update1.this.push(key, value);
            }

            EachStage eachStage = new EachStage(Collections.singletonList(value));
            this.modifiers.put(eachStage.getStageName(),eachStage.getValue());
            //this.addModifier(new EachStage(Collections.singletonList(value)));
            return Update1.this.push(key, this.modifiers);
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hash(getOuterType(), key, modifiers);
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

            PushOperatorBuilder that = (PushOperatorBuilder) obj;

            if (!Objects.equals(getOuterType(), that.getOuterType())) {
                return false;
            }

            return Objects.equals(this.key, that.key) && Objects.equals(this.modifiers, that.modifiers);
        }

        private Update1 getOuterType() {
            return Update1.this;
        }
    }

    /**
     * Builder for creating {@code $addToSet} modifier.
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
        public Update1 each(Object... values) {
            return Update1.this.addToSet(this.key, new EachStage(values));
        }

        /**
         * Propagates {@link #value(Object)} to {@code $addToSet}
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update1 value(Object value) {
            return Update1.this.addToSet(this.key, value);
        }
    }

}
