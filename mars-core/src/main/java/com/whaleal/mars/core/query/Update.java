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
import com.whaleal.icefrog.core.util.ObjectUtil;
import com.whaleal.icefrog.core.util.StrUtil;
import com.whaleal.icefrog.json.JSONUtil;
import com.whaleal.mars.core.internal.InvalidMongoDbApiUsageException;

import org.bson.Document;

import java.util.*;

/**
 * Class to easily construct MongoDB update clauses.
 */
public class Update implements UpdateDefinition {

    private boolean isolated = false;
    private Set<String> keysToUpdate = new HashSet<>();
    private Map<String, Object> modifierOps = new LinkedHashMap<>();
    private Map<String, PushOperatorBuilder> pushCommandBuilders = new LinkedHashMap<>(1);
    private List<ArrayFilter> arrayFilters = new ArrayList<>();

    /**
     * Static factory method to create an Update using the provided key
     *
     * @param key the field to update.
     * @return new instance of {@link Update}.
     */
    public static Update update( String key, Object value ) {
        return new Update().set(key, value);
    }

    /**
     * Creates an {@link Update} instance from the given {@link Document}. Allows to explicitly exclude fields from making
     * it into the created {@link Update} object. Note, that this will set attributes directly and <em>not</em> use
     * {@literal $set}. This means fields not given in the {@link Document} will be nulled when executing the update. To
     * create an only-updating {@link Update} instance of a {@link Document}, call {@link #set(String, Object)} for each
     * value in it.
     *
     * @param object  the source {@link Document} to create the update from.
     * @param exclude the fields to exclude.
     * @return new instance of {@link Update}.
     */
    public static Update fromDocument(Document object, String... exclude) {

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
    public Update set( String key, Object value ) {
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
    public Update setOnInsert( String key, Object value ) {
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
    public Update unset(String key) {
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
    public Update inc(String key, Number inc) {
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
    public Update push( String key, Object value ) {
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
    public Update pushAll(String key, Object[] values) {
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
    public Update addToSet( String key, Object value ) {
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
    public Update pop(String key, Position pos) {
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
    public Update pull( String key, Object value ) {
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
    public Update pullAll(String key, Object[] values) {
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
    public Update rename(String oldName, String newName) {
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
    public Update currentDate(String key) {

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
    public Update currentTimestamp(String key) {

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
    public Update multiply(String key, Number multiplier) {

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
    public Update max(String key, Object value) {

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
    public Update min(String key, Object value) {

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
    public Update filterArray(CriteriaDefinition criteria) {

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
    public Update filterArray(String identifier, Object expression) {

        this.arrayFilters.add(() -> new Document(identifier, expression));
        return this;
    }


    public Boolean isIsolated() {
        return isolated;
    }


    public Document getUpdateObject() {
        Document docs = new Document();

        for (Map.Entry<String,Object> entry :modifierOps.entrySet()){

            if(entry.getValue() instanceof Modifier){
                Document value = modifier2Doc((Modifier) entry.getValue());
                docs.put(entry.getKey() ,value);
            }else if(entry.getValue() instanceof Modifiers){
                Document value = modifiers2Doc((Modifiers) entry.getValue());
                docs.put(entry.getKey() ,value);
            }else if(entry.getValue() instanceof Map){

                Object o = modifyObject(entry.getValue());
                docs.put(entry.getKey(),o);


            }
            else {
                docs.put(entry.getKey() ,entry.getValue());
            }

        }

        return docs;
        //return new Document(modifierOps);
    }


    private Object modifyObject(Object obj){

        if(obj instanceof Modifier){
            return modifier2Doc((Modifier) obj);

        }else if(obj instanceof Modifiers){
           return modifiers2Doc((Modifiers) obj);

        }else if(obj instanceof Map){
            Document doc = new Document();
            for(Map.Entry<String,Object> data :((Map<String, Object>) obj).entrySet()){
                doc.put(data.getKey(),modifyObject(data.getValue()));

            }
            return doc ;

        }
        else {
           return obj;
        }




    }





    public Document modifier2Doc(Modifier modifier){
        if(modifier.getValue() instanceof Modifier){
            Document docInner = modifier2Doc((Modifier) modifier.getValue());

            return new Document(modifier.getKey(),docInner);
        }else if(modifier.getValue() instanceof Modifiers){
            Document docInner = modifiers2Doc((Modifiers) modifier.getValue());
            return new Document(modifier.getKey(),docInner);

        }   else {
            return new Document(modifier.getKey(),modifier.getValue());
        }
    }

    public Document modifiers2Doc(Modifiers modifiers){
        Document docs = new Document();
        for(Modifier modifier :modifiers.getModifiers()){

            Object value = modifier.getValue();

            if(value instanceof Modifier){
                Document value1 = modifier2Doc((Modifier)value);
                docs.put(modifier.getKey(),value1);

            }else if(value instanceof Modifiers){

                Document value1 = modifiers2Doc((Modifiers) value);
                docs.put(modifier.getKey(),value1);

            }else {
                docs.put(modifier.getKey(),value);
            }

        }
        return docs;

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

        return doc.toJson();
    }

    public enum Position {
        LAST, FIRST
    }

    /**
     * Marker interface of nested commands.
     */
    public interface Modifier {

        /**
         * @return the command to send eg. {@code $push}
         */
        String getKey();

        /**
         * @return value to be sent with command
         */
        Object getValue();

        /**
         * @return a safely serialized JSON representation.
         */
        default String toJsonString() {
            return JSONUtil.toJsonStr(Collections.singletonMap(getKey(), getValue()));
        }
    }

    /**
     * Modifiers holds a distinct collection of {@link Modifier}
     */
    public static class Modifiers {

        private Map<String, Modifier> modifiers;

        public Modifiers() {
            this.modifiers = new LinkedHashMap<>(1);
        }

        public Collection<Modifier> getModifiers() {
            return Collections.unmodifiableCollection(this.modifiers.values());
        }

        public void addModifier(Modifier modifier) {
            this.modifiers.put(modifier.getKey(), modifier);
        }

        /**
         * @return true if no modifiers present.
         */
        public boolean isEmpty() {
            return modifiers.isEmpty();
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(modifiers);
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

            Modifiers that = (Modifiers) obj;
            return Objects.equals(this.modifiers, that.modifiers);
        }

        @Override
        public String toString() {
            return JSONUtil.toJsonStr(this.modifiers);
        }
    }

    /**
     * Abstract {@link Modifier} implementation with defaults for {@link Object#equals(Object)}, {@link Object#hashCode()}
     * and {@link Object#toString()}.
     */
    private static abstract class AbstractModifier implements Modifier {

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return ObjectUtil.nullSafeHashCode(getKey()) + ObjectUtil.nullSafeHashCode(getValue());
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object that) {

            if (this == that) {
                return true;
            }

            if (that == null || getClass() != that.getClass()) {
                return false;
            }

            if (!Objects.equals(getKey(), ((Modifier) that).getKey())) {
                return false;
            }

            return Objects.deepEquals(getValue(), ((Modifier) that).getValue());
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return toJsonString();
        }
    }

    /**
     * Implementation of {@link Modifier} representing {@code $each}.
     */
    private static class Each extends AbstractModifier {

        private Object[] values;

        Each(Object... values) {
            this.values = extractValues(values);
        }

        private Object[] extractValues(Object[] values) {

            if (values == null || values.length == 0) {
                return values;
            }

            if (values.length == 1 && values[0] instanceof Collection) {
                return ((Collection<?>) values[0]).toArray();
            }

            return Arrays.copyOf(values, values.length);
        }


        @Override
        public String getKey() {
            return "$each";
        }


        @Override
        public Object getValue() {
            return this.values;
        }
    }

    /**
     * {@link Modifier} implementation used to propagate {@code $position}.
     */
    private static class PositionModifier extends AbstractModifier {

        private final int position;

        PositionModifier(int position) {
            this.position = position;
        }

        @Override
        public String getKey() {
            return "$position";
        }

        @Override
        public Object getValue() {
            return position;
        }
    }

    /**
     * Implementation of {@link Modifier} representing {@code $slice}.
     */
    private static class Slice extends AbstractModifier {

        private int count;

        Slice(int count) {
            this.count = count;
        }


        @Override
        public String getKey() {
            return "$slice";
        }


        @Override
        public Object getValue() {
            return this.count;
        }
    }

    /**
     * Implementation of {@link Modifier} representing {@code $sort}.
     */
    private static class SortModifier extends AbstractModifier {

        private final Object sort;

        /**
         * @param direction must not be {@literal null}.
         */
        SortModifier(Sort.Direction direction) {

            Precondition.notNull(direction, "Direction must not be null!");
            this.sort = direction.isAscending() ? 1 : -1;
        }

        /**
         * Creates a new {@link SortModifier} instance given {@link Sort}.
         *
         * @param sort must not be {@literal null}.
         */
        SortModifier(Sort sort) {

            Precondition.notNull(sort, "Sort must not be null!");

            for (Sort.Order order : sort) {

                if (order.isIgnoreCase()) {
                    throw new IllegalArgumentException(String.format("Given sort contained an Order for %s with ignore case! "
                            + "MongoDB does not support sorting ignoring case currently!", order.getProperty()));
                }
            }

            this.sort = sort;
        }


        @Override
        public String getKey() {
            return "$sort";
        }


        @Override
        public Object getValue() {
            return this.sort;
        }
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
        protected BitwiseOperatorBuilder(Update reference, String key) {

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
        public Update and(long value) {

            addFieldOperation(BitwiseOperator.AND, value);
            return reference;
        }

        /**
         * Updates to the result of a bitwise or operation between the current value and the given one.
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update or(long value) {

            addFieldOperation(BitwiseOperator.OR, value);
            return reference;
        }

        /**
         * Updates to the result of a bitwise xor operation between the current value and the given one.
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update xor(long value) {

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
        private final Modifiers modifiers;

        PushOperatorBuilder(String key) {
            this.key = key;
            this.modifiers = new Modifiers();
        }

        /**
         * Propagates {@code $each} to {@code $push}
         *
         * @param values
         * @return never {@literal null}.
         */
        public Update each(Object... values) {

            this.modifiers.addModifier(new Each(values));
            return Update.this.push(key, this.modifiers);
        }

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

            this.modifiers.addModifier(new Slice(count));
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
            this.modifiers.addModifier(new SortModifier(direction));
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
            this.modifiers.addModifier(new SortModifier(sort));
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

            this.modifiers.addModifier(new PositionModifier(position));
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

            this.modifiers.addModifier(new PositionModifier(0));

            return this;
        }

        /**
         * Propagates {@link #value(Object)} to {@code $push}
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update value(Object value) {

            if (this.modifiers.isEmpty()) {
                return Update.this.push(key, value);
            }

            this.modifiers.addModifier(new Each(Collections.singletonList(value)));
            return Update.this.push(key, this.modifiers);
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

        private Update getOuterType() {
            return Update.this;
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
        public Update each(Object... values) {
            return Update.this.addToSet(this.key, new Each(values));
        }

        /**
         * Propagates {@link #value(Object)} to {@code $addToSet}
         *
         * @param value
         * @return never {@literal null}.
         */
        public Update value(Object value) {
            return Update.this.addToSet(this.key, value);
        }
    }
}
