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
package com.whaleal.mars.util;

import com.mongodb.lang.Nullable;
import com.whaleal.mars.core.mapping.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Helper class that allows for specifying a method to invoke in a declarative
 * fashion, be it static or non-static.
 *
 * <p>Usage: Specify "targetClass"/"targetMethod" or "targetObject"/"targetMethod",
 * optionally specify arguments, prepare the invoker. Afterwards, you may
 * invoke the method any number of times, obtaining the invocation result.
 */
public class MethodInvoker {

    private static final Object[] EMPTY_ARGUMENTS = new Object[0];


    @Nullable
    protected Class<?> targetClass;

    @Nullable
    private Object targetObject;

    @Nullable
    private String targetMethod;

    @Nullable
    private String staticMethod;

    @Nullable
    private Object[] arguments;

    /**
     * The method we will call.
     */
    @Nullable
    private Method methodObject;

    /**
     * Algorithm that judges the match between the declared parameter types of a candidate method
     * and a specific list of arguments that this method is supposed to be invoked with.
     * <p>Determines a weight that represents the class hierarchy difference between types and
     * arguments. A direct match, i.e. type Integer -> arg of class Integer, does not increase
     * the result - all direct matches means weight 0. A match between type Object and arg of
     * class Integer would increase the weight by 2, due to the superclass 2 steps up in the
     * hierarchy (i.e. Object) being the last one that still matches the required type Object.
     * Type Number and class Integer would increase the weight by 1 accordingly, due to the
     * superclass 1 step up the hierarchy (i.e. Number) still matching the required type Number.
     * Therefore, with an arg of type Integer, a constructor (Integer) would be preferred to a
     * constructor (Number) which would in turn be preferred to a constructor (Object).
     * All argument weights get accumulated.
     * <p>Note: This is the algorithm used by MethodInvoker itself and also the algorithm
     * used for constructor and factory method selection in Spring's bean container (in case
     * of lenient constructor resolution which is the default for regular bean definitions).
     *
     * @param paramTypes the parameter types to match
     * @param args       the arguments to match
     * @return the accumulated weight for all arguments
     */
    public static int getTypeDifferenceWeight(Class<?>[] paramTypes, Object[] args) {
        int result = 0;
        for (int i = 0; i < paramTypes.length; i++) {
            if (!ClassUtils.isAssignableValue(paramTypes[i], args[i])) {
                return Integer.MAX_VALUE;
            }
            if (args[i] != null) {
                Class<?> paramType = paramTypes[i];
                Class<?> superClass = args[i].getClass().getSuperclass();
                while (superClass != null) {
                    if (paramType.equals(superClass)) {
                        result = result + 2;
                        superClass = null;
                    } else if (ClassUtils.isAssignable(paramType, superClass)) {
                        result = result + 2;
                        superClass = superClass.getSuperclass();
                    } else {
                        superClass = null;
                    }
                }
                if (paramType.isInterface()) {
                    result = result + 1;
                }
            }
        }
        return result;
    }

    /**
     * Return the target class on which to call the target method.
     */
    @Nullable
    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    /**
     * Set the target class on which to call the target method.
     * Only necessary when the target method is static; else,
     * a target object needs to be specified anyway.
     *
     * @see #setTargetObject
     * @see #setTargetMethod
     */
    public void setTargetClass(@Nullable Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Return the target object on which to call the target method.
     */
    @Nullable
    public Object getTargetObject() {
        return this.targetObject;
    }

    /**
     * Set the target object on which to call the target method.
     * Only necessary when the target method is not static;
     * else, a target class is sufficient.
     *
     * @see #setTargetClass
     * @see #setTargetMethod
     */
    public void setTargetObject(@Nullable Object targetObject) {
        this.targetObject = targetObject;
        if (targetObject != null) {
            this.targetClass = targetObject.getClass();
        }
    }

    /**
     * Return the name of the method to be invoked.
     */
    @Nullable
    public String getTargetMethod() {
        return this.targetMethod;
    }

    /**
     * Set the name of the method to be invoked.
     * Refers to either a static method or a non-static method,
     * depending on a target object being set.
     *
     * @see #setTargetClass
     * @see #setTargetObject
     */
    public void setTargetMethod(@Nullable String targetMethod) {
        this.targetMethod = targetMethod;
    }

    /**
     * Set a fully qualified static method name to invoke,
     * e.g. "example.MyExampleClass.myExampleMethod".
     * Convenient alternative to specifying targetClass and targetMethod.
     *
     * @see #setTargetClass
     * @see #setTargetMethod
     */
    public void setStaticMethod(String staticMethod) {
        this.staticMethod = staticMethod;
    }

    /**
     * Return the arguments for the method invocation.
     */
    public Object[] getArguments() {
        return (this.arguments != null ? this.arguments : EMPTY_ARGUMENTS);
    }

    /**
     * Set arguments for the method invocation. If this property is not set,
     * or the Object array is of length 0, a method with no arguments is assumed.
     */
    public void setArguments(Object... arguments) {
        this.arguments = arguments;
    }

    /**
     * Prepare the specified method.
     * The method can be invoked any number of times afterwards.
     *
     * @see #getPreparedMethod
     * @see #invoke
     */
    public void prepare() throws ClassNotFoundException, NoSuchMethodException {
        if (this.staticMethod != null) {
            int lastDotIndex = this.staticMethod.lastIndexOf('.');
            if (lastDotIndex == -1 || lastDotIndex == this.staticMethod.length()) {
                throw new IllegalArgumentException(
                        "staticMethod must be a fully qualified class plus method name: " +
                                "e.g. 'example.MyExampleClass.myExampleMethod'");
            }
            String className = this.staticMethod.substring(0, lastDotIndex);
            String methodName = this.staticMethod.substring(lastDotIndex + 1);
            this.targetClass = resolveClassName(className);
            this.targetMethod = methodName;
        }

        Class<?> targetClass = getTargetClass();
        String targetMethod = getTargetMethod();
        Assert.notNull(targetClass, "Either 'targetClass' or 'targetObject' is required");
        Assert.notNull(targetMethod, "Property 'targetMethod' is required");

        Object[] arguments = getArguments();
        Class<?>[] argTypes = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; ++i) {
            argTypes[i] = (arguments[i] != null ? arguments[i].getClass() : Object.class);
        }

        // Try to get the exact method first.
        try {
            this.methodObject = targetClass.getMethod(targetMethod, argTypes);
        } catch (NoSuchMethodException ex) {
            // Just rethrow exception if we can't get any match.
            this.methodObject = findMatchingMethod();
            if (this.methodObject == null) {
                throw ex;
            }
        }
    }

    /**
     * Resolve the given class name into a Class.
     * <p>The default implementations uses {@code ClassUtils.forName},
     * using the thread context class loader.
     *
     * @param className the class name to resolve
     * @return the resolved Class
     * @throws ClassNotFoundException if the class name was invalid
     */
    protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
        return ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
    }

    /**
     * Find a matching method with the specified name for the specified arguments.
     *
     * @return a matching method, or {@code null} if none
     * @see #getTargetClass()
     * @see #getTargetMethod()
     * @see #getArguments()
     */
    @Nullable
    protected Method findMatchingMethod() {
        String targetMethod = getTargetMethod();
        Object[] arguments = getArguments();
        int argCount = arguments.length;

        Class<?> targetClass = getTargetClass();
        Assert.state(targetClass != null, "No target class set");
        Method[] candidates = ReflectionUtils.getAllDeclaredMethods(targetClass);
        int minTypeDiffWeight = Integer.MAX_VALUE;
        Method matchingMethod = null;

        for (Method candidate : candidates) {
            if (candidate.getName().equals(targetMethod)) {
                if (candidate.getParameterCount() == argCount) {
                    Class<?>[] paramTypes = candidate.getParameterTypes();
                    int typeDiffWeight = getTypeDifferenceWeight(paramTypes, arguments);
                    if (typeDiffWeight < minTypeDiffWeight) {
                        minTypeDiffWeight = typeDiffWeight;
                        matchingMethod = candidate;
                    }
                }
            }
        }

        return matchingMethod;
    }

    /**
     * Return the prepared Method object that will be invoked.
     * <p>Can for example be used to determine the return type.
     *
     * @return the prepared Method object (never {@code null})
     * @throws IllegalStateException if the invoker hasn't been prepared yet
     * @see #prepare
     * @see #invoke
     */
    public Method getPreparedMethod() throws IllegalStateException {
        if (this.methodObject == null) {
            throw new IllegalStateException("prepare() must be called prior to invoke() on MethodInvoker");
        }
        return this.methodObject;
    }

    /**
     * Return whether this invoker has been prepared already,
     * i.e. whether it allows access to {@link #getPreparedMethod()} already.
     */
    public boolean isPrepared() {
        return (this.methodObject != null);
    }

    /**
     * Invoke the specified method.
     * <p>The invoker needs to have been prepared before.
     *
     * @return the object (possibly null) returned by the method invocation,
     * or {@code null} if the method has a void return type
     * @throws InvocationTargetException if the target method threw an exception
     * @throws IllegalAccessException    if the target method couldn't be accessed
     * @see #prepare
     */
    @Nullable
    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        // In the static case, target will simply be {@code null}.
        Object targetObject = getTargetObject();
        Method preparedMethod = getPreparedMethod();
        if (targetObject == null && !Modifier.isStatic(preparedMethod.getModifiers())) {
            throw new IllegalArgumentException("Target method must not be non-static without a target");
        }
        ReflectionUtils.makeAccessible(preparedMethod);
        return preparedMethod.invoke(targetObject, getArguments());
    }

}
