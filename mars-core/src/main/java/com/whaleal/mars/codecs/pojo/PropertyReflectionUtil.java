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

final class PropertyReflectionUtil {
    private PropertyReflectionUtil() {
    }


    /**
     * 一些解析方法
     */
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";

    /**
     * 判断一个方法 是否是一个 Getter  方法
     * @param method
     * @return
     */
    static boolean isGetter(final Method method) {



        if (method.getParameterTypes().length > 0) {
            return false;
        }else if (method.getName().startsWith(GET_PREFIX) && method.getName().length() > GET_PREFIX.length()) {
            char c = method.getName().charAt(GET_PREFIX.length());
            return Character.isUpperCase(c) || (!Character.isUpperCase(c) && !Character.isLowerCase(c));
        } else if (method.getName().startsWith(IS_PREFIX) && method.getName().length() > IS_PREFIX.length()) {

            char c = method.getName().charAt(IS_PREFIX.length());
            return Character.isUpperCase(c)||(!Character.isUpperCase(c) && !Character.isLowerCase(c));
        }
        return false;
    }


    /**
     * 判断一个方法 是否属于 Setter
     * @param method
     * @return
     */
    static boolean isSetter(final Method method) {


        if (method.getName().startsWith(SET_PREFIX) && method.getName().length() > SET_PREFIX.length()
                && method.getParameterTypes().length == 1) {

            char c = method.getName().charAt(SET_PREFIX.length());
            return Character.isUpperCase(c)||(!Character.isUpperCase(c) && !Character.isLowerCase(c));

        }

        return false;
    }

    /**
     * 根据 getter setter 方法 名称
     * 来获取他的属性字段名称
     * @param method
     * @return
     */
    static String toPropertyName(final Method method) {
        String name = method.getName();
        String propertyName = name.substring(name.startsWith(IS_PREFIX) ? 2 : 3, name.length());
        char[] chars = propertyName.toCharArray();
        //  这里将 首字母 转为 小写
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * 获取该类内部属性的相关方法  方法
     * 调用过程 先添加 继承的接口的
     * 如有多个则迭代
     * 再添加本类对象自身的方法
     *
     *
     * @param clazz
     * @return
     */
    static PropertyMethods getPropertyMethods(final Class<?> clazz) {

        //  分为 两组 分别为 getter  && setter
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
        // 开放的方法 并且不是 jvm 生成的桥接方法
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
