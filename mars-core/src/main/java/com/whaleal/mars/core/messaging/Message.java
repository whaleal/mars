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
package com.whaleal.mars.core.messaging;

import com.mongodb.lang.Nullable;
import com.whaleal.mars.util.Assert;
import com.whaleal.mars.util.ObjectUtils;

public interface Message<S, T> {

    /**
     * The raw message source as emitted by the origin.
     *
     * @return can be {@literal null}.
     */
    @Nullable
    S getRaw();

    /**
     * The converted message body if available.
     *
     * @return can be {@literal null}.
     */
    @Nullable
    T getBody();

    /**
     * {@link MessageProperties} containing information about the {@link Message} origin and other metadata.
     *
     * @return never {@literal null}.
     */
    MessageProperties getProperties();


    class MessageProperties {

        private static final MessageProperties EMPTY = new MessageProperties();

        private @Nullable
        String databaseName;
        private @Nullable
        String collectionName;

        /**
         * The database name the message originates from.
         *
         * @return can be {@literal null}.
         */
        @Nullable
        public String getDatabaseName() {
            return databaseName;
        }

        /**
         * The collection name the message originates from.
         *
         * @return can be {@literal null}.
         */
        @Nullable
        public String getCollectionName() {
            return collectionName;
        }

        /**
         * @return empty {@link MessageProperties}.
         */
        public static MessageProperties empty() {
            return EMPTY;
        }

        /**
         * Obtain a shiny new {@link MessagePropertiesBuilder} and start defining options in this fancy fluent way. Just
         * don't forget to call {@link MessagePropertiesBuilder#build() build()} when your're done.
         *
         * @return new instance of {@link MessagePropertiesBuilder}.
         */
        public static MessagePropertiesBuilder builder() {
            return new MessagePropertiesBuilder();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            MessageProperties that = (MessageProperties) o;

            if (!ObjectUtils.nullSafeEquals(this.databaseName, that.databaseName)) {
                return false;
            }

            return ObjectUtils.nullSafeEquals(this.collectionName, that.collectionName);
        }

        @Override
        public int hashCode() {
            int result = ObjectUtils.nullSafeHashCode(databaseName);
            result = 31 * result + ObjectUtils.nullSafeHashCode(collectionName);
            return result;
        }

        public String toString() {
            return "Message.MessageProperties(databaseName=" + this.getDatabaseName() + ", collectionName="
                    + this.getCollectionName() + ")";
        }

        /**
         * Builder for {@link MessageProperties}.
         */
        public static class MessagePropertiesBuilder {

            private @Nullable
            String databaseName;
            private @Nullable
            String collectionName;

            /**
             * @param dbName must not be {@literal null}.
             * @return this.
             */
            public MessagePropertiesBuilder databaseName(String dbName) {

                Assert.notNull(dbName, "Database name must not be null!");

                this.databaseName = dbName;
                return this;
            }

            /**
             * @param collectionName must not be {@literal null}.
             * @return this
             */
            public MessagePropertiesBuilder collectionName(String collectionName) {

                Assert.notNull(collectionName, "Collection name must not be null!");

                this.collectionName = collectionName;
                return this;
            }

            /**
             * @return the built {@link MessageProperties}.
             */
            public MessageProperties build() {

                MessageProperties properties = new MessageProperties();

                properties.collectionName = collectionName;
                properties.databaseName = databaseName;

                return properties;
            }
        }
    }
}
