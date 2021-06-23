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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Simple {@link List} wrapper class that allows for elements to be
 * automatically populated as they are requested. This is particularly
 * useful for data binding to {@link List Lists}, allowing for elements
 * to be created and added to the {@link List} in a "just in time" fashion.
 *
 * <p>Note: This class is not thread-safe. To create a thread-safe version,
 * use the {@link Collections#} utility methods.
 *
 * <p>Inspired by {@code LazyList} from Commons Collections.
 */
@SuppressWarnings("serial")
public class AutoPopulatingList<E> implements List<E>, Serializable {

    /**
     * The {@link List} that all operations are eventually delegated to.
     */
    private final List<E> backingList;

    /**
     * The {@link ElementFactory} to use to create new {@link List} elements
     * on demand.
     */
    private final ElementFactory<E> elementFactory;


    /**
     * Creates a new {@code AutoPopulatingList} that is backed by a standard
     * {@link ArrayList} and adds new instances of the supplied {@link Class element Class}
     * to the backing {@link List} on demand.
     */
    public AutoPopulatingList(Class<? extends E> elementClass) {
        this(new ArrayList<>(), elementClass);
    }

    /**
     * Creates a new {@code AutoPopulatingList} that is backed by the supplied {@link List}
     * and adds new instances of the supplied {@link Class element Class} to the backing
     * {@link List} on demand.
     */
    public AutoPopulatingList(List<E> backingList, Class<? extends E> elementClass) {
        this(backingList, new ReflectiveElementFactory<>(elementClass));
    }

    /**
     * Creates a new {@code AutoPopulatingList} that is backed by a standard
     * {@link ArrayList} and creates new elements on demand using the supplied {@link ElementFactory}.
     */
    public AutoPopulatingList(ElementFactory<E> elementFactory) {
        this(new ArrayList<>(), elementFactory);
    }

    /**
     * Creates a new {@code AutoPopulatingList} that is backed by the supplied {@link List}
     * and creates new elements on demand using the supplied {@link ElementFactory}.
     */
    public AutoPopulatingList(List<E> backingList, ElementFactory<E> elementFactory) {
        Assert.notNull(backingList, "Backing List must not be null");
        Assert.notNull(elementFactory, "Element factory must not be null");
        this.backingList = backingList;
        this.elementFactory = elementFactory;
    }


    @Override
    public void add(int index, E element) {
        this.backingList.add(index, element);
    }

    @Override
    public boolean add(E o) {
        return this.backingList.add(o);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.backingList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return this.backingList.addAll(index, c);
    }

    @Override
    public void clear() {
        this.backingList.clear();
    }

    @Override
    public boolean contains(Object o) {
        return this.backingList.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.backingList.containsAll(c);
    }

    /**
     * Get the element at the supplied index, creating it if there is
     * no element at that index.
     */
    @Override
    public E get(int index) {
        int backingListSize = this.backingList.size();
        E element = null;
        if (index < backingListSize) {
            element = this.backingList.get(index);
            if (element == null) {
                element = this.elementFactory.createElement(index);
                this.backingList.set(index, element);
            }
        } else {
            for (int x = backingListSize; x < index; x++) {
                this.backingList.add(null);
            }
            element = this.elementFactory.createElement(index);
            this.backingList.add(element);
        }
        return element;
    }

    @Override
    public int indexOf(Object o) {
        return this.backingList.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return this.backingList.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return this.backingList.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.backingList.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return this.backingList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return this.backingList.listIterator(index);
    }

    @Override
    public E remove(int index) {
        return this.backingList.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        return this.backingList.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.backingList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.backingList.retainAll(c);
    }

    @Override
    public E set(int index, E element) {
        return this.backingList.set(index, element);
    }

    @Override
    public int size() {
        return this.backingList.size();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return this.backingList.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return this.backingList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.backingList.toArray(a);
    }


    @Override
    public boolean equals(@Nullable Object other) {
        return this.backingList.equals(other);
    }

    @Override
    public int hashCode() {
        return this.backingList.hashCode();
    }


    /**
     * Factory interface for creating elements for an index-based access
     * data structure such as a {@link List}.
     *
     * @param <E> the element type
     */
    @FunctionalInterface
    public interface ElementFactory<E> {

        /**
         * Create the element for the supplied index.
         *
         * @return the element object
         * @throws ElementInstantiationException if the instantiation process failed
         *                                       (any exception thrown by a target constructor should be propagated as-is)
         */
        E createElement(int index) throws ElementInstantiationException;
    }


    /**
     * Exception to be thrown from ElementFactory.
     */
    public static class ElementInstantiationException extends RuntimeException {

        public ElementInstantiationException(String msg) {
            super(msg);
        }

        public ElementInstantiationException(String message, Throwable cause) {
            super(message, cause);
        }
    }


    /**
     * Reflective implementation of the ElementFactory interface, using
     * {@code Class.getDeclaredConstructor().newInstance()} on a given element class.
     */
    private static class ReflectiveElementFactory<E> implements ElementFactory<E>, Serializable {

        private final Class<? extends E> elementClass;

        public ReflectiveElementFactory(Class<? extends E> elementClass) {
            Assert.notNull(elementClass, "Element class must not be null");
            Assert.isTrue(!elementClass.isInterface(), "Element class must not be an interface type");
            Assert.isTrue(!Modifier.isAbstract(elementClass.getModifiers()), "Element class cannot be an abstract class");
            this.elementClass = elementClass;
        }

        @Override
        public E createElement(int index) {
            try {
                return ReflectionUtils.accessibleConstructor(this.elementClass).newInstance();
            } catch (NoSuchMethodException ex) {
                throw new ElementInstantiationException(
                        "No default constructor on element class: " + this.elementClass.getName(), ex);
            } catch (InstantiationException ex) {
                throw new ElementInstantiationException(
                        "Unable to instantiate element class: " + this.elementClass.getName(), ex);
            } catch (IllegalAccessException ex) {
                throw new ElementInstantiationException(
                        "Could not access element constructor: " + this.elementClass.getName(), ex);
            } catch (InvocationTargetException ex) {
                throw new ElementInstantiationException(
                        "Failed to invoke element constructor: " + this.elementClass.getName(), ex.getTargetException());
            }
        }
    }

}
