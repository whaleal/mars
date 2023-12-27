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
package com.whaleal.mars.core.validation;

import com.whaleal.mars.core.query.CriteriaDefinition;
import com.whaleal.mars.util.Assert;
import org.bson.Document;


public interface Validator {

    /**
     * Get the {@link Document} containing the validation specific rules. The document may contain fields that may require
     * type and/or field name mapping.
     *
     * @return a MongoDB {@code validator} {@link Document}. Never {@literal null}.
     */
    Document toDocument();

    /**
     * Creates a basic {@link Validator} checking documents against a given set of rules.
     *
     * @param validationRules must not be {@literal null}.
     * @return new instance of {@link Validator}.
     * @throws IllegalArgumentException if validationRules is {@literal null}.
     */
    static Validator document(Document validationRules) {

        Assert.notNull(validationRules, "ValidationRules must not be null!");
        return DocumentValidator.of(validationRules);
    }

    /**
     * Creates a new {@link Validator} checking documents against the structure defined in .
     *
     * @param schema must not be {@literal null}.
     * @return new instance of {@link Validator}.
     * @throws IllegalArgumentException if schema is {@literal null}.
     */
	/*static Validator schema(MongoJsonSchema schema) {

		Assert.notNull(schema, "Schema must not be null!");
		return JsonSchemaValidator.of(schema);
	}*/

    /**
     * Creates a new {@link Validator} checking documents against a given query structure expressed by
     * {@link CriteriaDefinition}. <br />
     *
     * @param criteria must not be {@literal null}.
     * @return new instance of {@link Validator}.
     * @throws IllegalArgumentException if criteria is {@literal null}.
     */
    static Validator criteria(CriteriaDefinition criteria) {

        Assert.notNull(criteria, "Criteria must not be null!");
        return CriteriaValidator.of(criteria);
    }
}
