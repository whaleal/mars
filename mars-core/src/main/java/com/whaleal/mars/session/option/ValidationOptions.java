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

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import org.bson.conversions.Bson;

/**
 * @author cx
 * @Date 2020/12/15
 */
@Deprecated
public class ValidationOptions implements ReadConfigurable<ValidationOptions> {

    private ReadConcern readConcern;

    private ReadPreference readPreference;

    private com.mongodb.client.model.ValidationOptions originValidationOptions;

    public ValidationOptions() {
        originValidationOptions = new com.mongodb.client.model.ValidationOptions();
    }

    public ValidationOptions(com.mongodb.client.model.ValidationOptions originValidationOptions) {
        this.originValidationOptions = originValidationOptions;
    }


    public ValidationAction getValidationAction() {
        return originValidationOptions.getValidationAction();
    }


    public ValidationLevel getValidationLevel() {
        return originValidationOptions.getValidationLevel();
    }


    public Bson getValidator() {
        return originValidationOptions.getValidator();
    }

    public ValidationOptions validator( Bson validator ) {
        originValidationOptions.validator(validator);
        return this;
    }

    public ValidationOptions validationLevel( ValidationLevel validationLevel ) {
        originValidationOptions.validationLevel(validationLevel);
        return this;
    }

    public ValidationOptions validationAction( ValidationAction validationAction ) {
        originValidationOptions.validationAction(validationAction);
        return this;
    }

    @Override
    public ReadConcern getReadConcern() {
        return readConcern;
    }

    @Override
    public ReadPreference getReadPreference() {
        return readPreference;
    }

    @Override
    public ValidationOptions readConcern(ReadConcern readConcern) {
        this.readConcern = readConcern;
        return this;
    }

    @Override
    public ValidationOptions readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    @Override
    public com.mongodb.client.model.ValidationOptions getOriginOptions() {
        if (originValidationOptions == null) {
            originValidationOptions = new com.mongodb.client.model.ValidationOptions();
        }
        return originValidationOptions;
    }
}
