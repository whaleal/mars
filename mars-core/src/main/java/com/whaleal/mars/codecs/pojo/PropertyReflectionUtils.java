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
package com.whaleal.mars.codecs.pojo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.reflect.Modifier.isPublic;

final class PropertyReflectionUtils {
    private PropertyReflectionUtils() {
    }

    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";

    static boolean isGetter(final Method method) {
        if (method.getParameterTypes().length > 0) {
            return false;
        } else if (method.getName().startsWith(GET_PREFIX) && method.getName().length() > GET_PREFIX.length()) {
            return Character.isUpperCase(method.getName().charAt(GET_PREFIX.length()));
        } else if (method.getName().startsWith(IS_PREFIX) && method.getName().length() > IS_PREFIX.length()) {
            return Character.isUpperCase(method.getName().charAt(IS_PREFIX.length()));
        }
        return false;
    }

    static boolean isSetter(final Method method) {
        if (method.getName().startsWith(SET_PREFIX) && method.getName().length() > SET_PREFIX.length()
                && method.getParameterTypes().length == 1) {
            return Character.isUpperCase(method.getName().charAt(SET_PREFIX.length()));
        }
        return false;
    }

    static String toPropertyName(final Method method) {
        String name = method.getName();
        String propertyName = name.substring(name.startsWith(IS_PREFIX) ? 2 : 3, name.length());
        char[] chars = propertyName.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * 获取该类内部属性的相关方法  方法
     *
     * @param clazz
     * @return
     */
    static PropertyMethods getPropertyMethods(final Class<?> clazz) {
        List<Method> setters = new ArrayList<Method>();
        List<Method> getters = new ArrayList<Method>();

        // get all the default method from interface
        for (Class<?> i : clazz.getInterfaces()) {
            for (Method method : i.getDeclaredMethods()) {
                if (method.isDefault()) {
                    verifyAddMethodToList(method, getters, setters);
                }
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            verifyAddMethodToList(method, getters, setters);
        }

        return new PropertyMethods(getters, setters);
    }

    private static void verifyAddMethodToList(final Method method, final List<Method> getters, final List<Method> setters) {
        // Note that if you override a getter to provide a more specific return type, getting the declared methods
        // on the subclass will return the overridden method as well as the method that was overridden from
        // the super class. This original method is copied over into the subclass as a bridge method, so we're
        // excluding them here to avoid multiple getters of the same property with different return types
        if (isPublic(method.getModifiers()) && !method.isBridge()) {
            if (isGetter(method)) {
                getters.add(method);
            } else if (isSetter(method)) {
                // Setters are a bit more tricky - don't do anything fancy here
                setters.add(method);
            }
        }
    }

    static class PropertyMethods {
        private final Collection<Method> getterMethods;
        private final Collection<Method> setterMethods;

        PropertyMethods(final Collection<Method> getterMethods, final Collection<Method> setterMethods) {
            this.getterMethods = getterMethods;
            this.setterMethods = setterMethods;
        }

        Collection<Method> getGetterMethods() {
            return getterMethods;
        }

        Collection<Method> getSetterMethods() {
            return setterMethods;
        }
    }
}
