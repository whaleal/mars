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
package com.whaleal.mars.session.option;

import com.mongodb.client.model.Collation;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.util.OptionalUtil;
import com.whaleal.mars.core.validation.Validator;


import javax.annotation.Nullable;
import java.util.Optional;

public class CollectionOptions {


    private @Nullable Long maxDocuments;
    private @Nullable Long size;
    private @Nullable Boolean capped;
    private @Nullable Collation collation;
    private ValidationOptions validationOptions;
    private @Nullable TimeSeriesOptions timeSeriesOptions;

    /**
     * Constructs a new <code>CollectionOptions</code> instance.
     *
     * @param size the collection size in bytes, this data space is preallocated. Can be {@literal null}.
     * @param maxDocuments the maximum number of documents in the collection. Can be {@literal null}.
     * @param capped true to created a "capped" collection (fixed size with auto-FIFO behavior based on insertion order),
     *          false otherwise. Can be {@literal null}.
     *
     */

    public CollectionOptions(@Nullable Long size, @Nullable Long maxDocuments, @Nullable Boolean capped) {
        this(size, maxDocuments, capped, null, ValidationOptions.none(), null);
    }

    private CollectionOptions(@Nullable Long size, @Nullable Long maxDocuments, @Nullable Boolean capped,
                              @Nullable Collation collation, ValidationOptions validationOptions,
                              @Nullable TimeSeriesOptions timeSeriesOptions) {

        this.maxDocuments = maxDocuments;
        this.size = size;
        this.capped = capped;
        this.collation = collation;
        this.validationOptions = validationOptions;
        this.timeSeriesOptions = timeSeriesOptions;
    }

    /**
     * Create new {@link CollectionOptions} by just providing the {@link Collation} to use.
     *
     * @param collation must not be {@literal null}.
     * @return new {@link CollectionOptions}.
     * 
     */
    public static CollectionOptions just(Collation collation) {

        Precondition.notNull(collation, "Collation must not be null!");

        return new CollectionOptions(null, null, null, collation, ValidationOptions.none(), null);
    }

    /**
     * Create new empty {@link CollectionOptions}.
     *
     * @return new {@link CollectionOptions}.
     * 
     */
    public static CollectionOptions empty() {
        return new CollectionOptions(null, null, null, null, ValidationOptions.none(), null);
    }

    /**
     * Quick way to set up {@link CollectionOptions} for a Time Series collection. For more advanced settings use
     * {@link #timeSeries(TimeSeriesOptions)}.
     *
     * @param timeField The name of the property which contains the date in each time series document. Must not be
     *          {@literal null}.
     * @return new instance of {@link CollectionOptions}.
     * @see #timeSeries(TimeSeriesOptions)
     * 
     */
    public static CollectionOptions timeSeries(String timeField) {
        return empty().timeSeries(TimeSeriesOptions.timeSeries(timeField));
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and capped set to {@literal true}. <br />
     * <strong>NOTE</strong> Using capped collections requires defining {@link #size(long)}.
     *
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions capped() {
        return new CollectionOptions(size, maxDocuments, true, collation, validationOptions, null);
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code maxDocuments} set to given value.
     *
     * @param maxDocuments can be {@literal null}.
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions maxDocuments(long maxDocuments) {
        return new CollectionOptions(size, maxDocuments, capped, collation, validationOptions, timeSeriesOptions);
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code size} set to given value.
     *
     * @param size can be {@literal null}.
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions size(long size) {
        return new CollectionOptions(size, maxDocuments, capped, collation, validationOptions, timeSeriesOptions);
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code collation} set to given value.
     *
     * @param collation can be {@literal null}.
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions collation(@Nullable Collation collation) {
        return new CollectionOptions(size, maxDocuments, capped, collation, validationOptions, timeSeriesOptions);
    }

 
    /**
     * Create new {@link CollectionOptions} with already given settings and {@code validationOptions} set to given
     * {@link Validator}.
     *
     * @param validator can be {@literal null}.
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions validator(@Nullable Validator validator) {
        return validation(validationOptions.validator(validator));
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code validationLevel} set to
     * {@link ValidationLevel#OFF}.
     *
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions disableValidation() {
        return schemaValidationLevel(ValidationLevel.OFF);
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code validationLevel} set to
     * {@link ValidationLevel#STRICT}.
     *
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions strictValidation() {
        return schemaValidationLevel(ValidationLevel.STRICT);
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code validationLevel} set to
     * {@link ValidationLevel#MODERATE}.
     *
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions moderateValidation() {
        return schemaValidationLevel(ValidationLevel.MODERATE);
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code validationAction} set to
     * {@link ValidationAction#WARN}.
     *
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions warnOnValidationError() {
        return schemaValidationAction(ValidationAction.WARN);
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code validationAction} set to
     * {@link ValidationAction#ERROR}.
     *
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions failOnValidationError() {
        return schemaValidationAction(ValidationAction.ERROR);
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code validationLevel} set given
     * {@link ValidationLevel}.
     *
     * @param validationLevel must not be {@literal null}.
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions schemaValidationLevel(ValidationLevel validationLevel) {

        Precondition.notNull(validationLevel, "ValidationLevel must not be null!");
        return validation(validationOptions.validationLevel(validationLevel));
    }

    /**
     * Create new {@link CollectionOptions} with already given settings and {@code validationAction} set given
     * {@link ValidationAction}.
     *
     * @param validationAction must not be {@literal null}.
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions schemaValidationAction(ValidationAction validationAction) {

        Precondition.notNull(validationAction, "ValidationAction must not be null!");
        return validation(validationOptions.validationAction(validationAction));
    }

    /**
     * Create new {@link CollectionOptions} with the given {@link ValidationOptions}.
     *
     * @param validationOptions must not be {@literal null}. Use {@link ValidationOptions#none()} to remove validation.
     * @return new {@link CollectionOptions}.
     * 
     */
    public CollectionOptions validation(ValidationOptions validationOptions) {

        Precondition.notNull(validationOptions, "ValidationOptions must not be null!");
        return new CollectionOptions(size, maxDocuments, capped, collation, validationOptions, timeSeriesOptions);
    }

    /**
     * Create new {@link CollectionOptions} with the given {@link TimeSeriesOptions}.
     *
     * @param timeSeriesOptions must not be {@literal null}.
     * @return new instance of {@link CollectionOptions}.
     * 
     */
    public CollectionOptions timeSeries(TimeSeriesOptions timeSeriesOptions) {

        Precondition.notNull(timeSeriesOptions, "TimeSeriesOptions must not be null!");
        return new CollectionOptions(size, maxDocuments, capped, collation, validationOptions, timeSeriesOptions);
    }

    /**
     * Get the max number of documents the collection should be limited to.
     *
     * @return {@link Optional#empty()} if not set.
     */
    public Optional<Long> getMaxDocuments() {
        return Optional.ofNullable(maxDocuments);
    }

    /**
     * Get the {@literal size} in bytes the collection should be limited to.
     *
     * @return {@link Optional#empty()} if not set.
     */
    public Optional<Long> getSize() {
        return Optional.ofNullable(size);
    }

    /**
     * Get if the collection should be capped.
     *
     * @return {@link Optional#empty()} if not set.
     * 
     */
    public Optional<Boolean> getCapped() {
        return Optional.ofNullable(capped);
    }

    /**
     * Get the {@link Collation} settings.
     *
     * @return {@link Optional#empty()} if not set.
     * 
     */
    public Optional<Collation> getCollation() {
        return Optional.ofNullable(collation);
    }

    /**
     * Get the {@link ValidationOptions} for the collection.
     *
     * @return {@link Optional#empty()} if not set.
     * 
     */
    public Optional<ValidationOptions> getValidationOptions() {
        return validationOptions.isEmpty() ? Optional.empty() : Optional.of(validationOptions);
    }

    /**
     * Get the {@link TimeSeriesOptions} if available.
     *
     * @return {@link Optional#empty()} if not specified.
     * 
     */
    public Optional<TimeSeriesOptions> getTimeSeriesOptions() {
        return Optional.ofNullable(timeSeriesOptions);
    }

    /**
     * Encapsulation of ValidationOptions options.
     *
     * 
     * 
     * 
     */
    public static class ValidationOptions {

        private static final ValidationOptions NONE = new ValidationOptions(null, null, null);

        private final @Nullable Validator validator;
        private final @Nullable ValidationLevel validationLevel;
        private final @Nullable ValidationAction validationAction;

        public ValidationOptions(Validator validator, ValidationLevel validationLevel, ValidationAction validationAction) {

            this.validator = validator;
            this.validationLevel = validationLevel;
            this.validationAction = validationAction;
        }

        /**
         * Create an empty {@link ValidationOptions}.
         *
         * @return never {@literal null}.
         */
        public static ValidationOptions none() {
            return NONE;
        }

        /**
         * Define the {@link Validator} to be used for document validation.
         *
         * @param validator can be {@literal null}.
         * @return new instance of {@link ValidationOptions}.
         */
        public ValidationOptions validator(@Nullable Validator validator) {
            return new ValidationOptions(validator, validationLevel, validationAction);
        }

        /**
         * Define the validation level to apply.
         *
         * @param validationLevel can be {@literal null}.
         * @return new instance of {@link ValidationOptions}.
         */
        public ValidationOptions validationLevel(ValidationLevel validationLevel) {
            return new ValidationOptions(validator, validationLevel, validationAction);
        }

        /**
         * Define the validation action to take.
         *
         * @param validationAction can be {@literal null}.
         * @return new instance of {@link ValidationOptions}.
         */
        public ValidationOptions validationAction(ValidationAction validationAction) {
            return new ValidationOptions(validator, validationLevel, validationAction);
        }

        /**
         * Get the {@link Validator} to use.
         *
         * @return never {@literal null}.
         */
        public Optional<Validator> getValidator() {
            return Optional.ofNullable(validator);
        }

        /**
         * Get the {@code validationLevel} to apply.
         *
         * @return {@link Optional#empty()} if not set.
         */
        public Optional<ValidationLevel> getValidationLevel() {
            return Optional.ofNullable(validationLevel);
        }

        /**
         * Get the {@code validationAction} to perform.
         *
         * @return {@link Optional#empty()} if not set.
         */
        public Optional<ValidationAction> getValidationAction() {
            return Optional.ofNullable(validationAction);
        }

        /**
         * @return {@literal true} if no arguments set.
         */
        boolean isEmpty() {
            return !OptionalUtil.isAnyPresent(getValidator(), getValidationAction(), getValidationLevel());
        }
    }

}
