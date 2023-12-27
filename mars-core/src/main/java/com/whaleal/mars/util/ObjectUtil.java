package com.whaleal.mars.util;

import com.whaleal.icefrog.core.collection.IterUtil;
import com.whaleal.icefrog.core.comparator.CompareUtil;
import com.whaleal.icefrog.core.convert.Convert;
import com.whaleal.icefrog.core.exceptions.UtilException;
import com.whaleal.icefrog.core.lang.Precondition;
import com.whaleal.icefrog.core.map.MapUtil;
import com.whaleal.icefrog.core.util.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;

/**
 * 对象工具类，包括判空、克隆、序列化等操作
 *
 * @author Looly
 * @author wh
 */
public class ObjectUtil {
    private static final int INITIAL_HASH = 7;
    private static final int MULTIPLIER = 31;
    private static final String EMPTY_STRING = "";
    private static final String NULL_STRING = "null";
    private static final String ARRAY_START = "{";
    private static final String ARRAY_END = "}";
    private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
    private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

    private ObjectUtil() {
    }

    /**
     * 比较两个对象是否相等，此方法是 {@link #equal(Object, Object)}的别名方法。<br>
     * 相同的条件有两个，满足其一即可：<br>
     * <ol>
     * <li>obj1 == null &amp;&amp; obj2 == null</li>
     * <li>obj1.equals(obj2)</li>
     * <li>如果是BigDecimal比较，0 == obj1.compareTo(obj2)</li>
     * </ol>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     * @see #equal(Object, Object)
     * @since 1.0.0
     */
    public static boolean equals( Object obj1, Object obj2 ) {
        return equal(obj1, obj2);
    }

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     * <ol>
     * <li>obj1 == null &amp;&amp; obj2 == null</li>
     * <li>obj1.equals(obj2)</li>
     * <li>如果是BigDecimal比较，0 == obj1.compareTo(obj2)</li>
     * </ol>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     * @see Objects#equals(Object, Object)
     */
    public static boolean equal( Object obj1, Object obj2 ) {
        if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
            return NumberUtil.equals((BigDecimal) obj1, (BigDecimal) obj2);
        }

        return Objects.equals(obj1, obj2);
    }

    /**
     * 比较两个对象是否不相等。<br>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否不等
     * @since 1.0.0
     */
    public static boolean notEqual( Object obj1, Object obj2 ) {
        return false == equal(obj1, obj2);
    }

    /**
     * 计算对象长度，如果是字符串调用其length函数，集合类调用其size函数，数组调用其length属性，其他可遍历对象遍历计算长度<br>
     * 支持的类型包括：
     * <ul>
     * <li>CharSequence</li>
     * <li>Map</li>
     * <li>Iterator</li>
     * <li>Enumeration</li>
     * <li>Array</li>
     * </ul>
     *
     * @param obj 被计算长度的对象
     * @return 长度
     */
    public static int length( Object obj ) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        }

        int count;
        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            count = 0;
            while (iter.hasNext()) {
                count++;
                iter.next();
            }
            return count;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            count = 0;
            while (enumeration.hasMoreElements()) {
                count++;
                enumeration.nextElement();
            }
            return count;
        }
        if (obj.getClass().isArray() == true) {
            return Array.getLength(obj);
        }
        return -1;
    }

    /**
     * 对象中是否包含元素<br>
     * 支持的对象类型包括：
     * <ul>
     * <li>String</li>
     * <li>Collection</li>
     * <li>Map</li>
     * <li>Iterator</li>
     * <li>Enumeration</li>
     * <li>Array</li>
     * </ul>
     *
     * @param obj     对象
     * @param element 元素
     * @return 是否包含
     */
    public static boolean contains( Object obj, Object element ) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            if (element == null) {
                return false;
            }
            return ((String) obj).contains(element.toString());
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).contains(element);
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).containsValue(element);
        }

        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            while (iter.hasNext()) {
                Object o = iter.next();
                if (equal(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            while (enumeration.hasMoreElements()) {
                Object o = enumeration.nextElement();
                if (equal(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj.getClass().isArray() == true) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object o = Array.get(obj, i);
                if (equal(o, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查对象是否为null<br>
     * 判断标准为：
     *
     * <pre>
     * 1. == null
     * 2. equals(null)
     * </pre>
     * 当使用 equals  是 必然返回 false;
     * 需要两遍校验 ，因为在 json  中
     * 存在 str != null 但是 str.equals(null) when using org.json
     *
     * @param obj 对象
     * @return 是否为null
     * @see Objects#equals(Object, Object)
     * {@code Objects.equals(obj ,null);}
     */
    public static boolean isNull( Object obj ) {
        //noinspection ConstantConditions
        return null == obj || obj.equals(null);
    }

    /**
     * 检查对象是否不为null
     *
     * @param obj 对象
     * @return 是否为null
     */
    public static boolean isNotNull( Object obj ) {
        return false == isNull(obj);
    }

    /**
     * 判断指定对象是否为空，支持：
     *
     * <pre>
     * 1. CharSequence
     * 2. Map
     * 3. Iterable
     * 4. Iterator
     * 5. Array
     * </pre>
     *
     * @param obj 被判断的对象
     * @return 是否为空，如果类型不支持，返回false
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty( Object obj ) {


        if (null == obj) {
            return true;
        }
        if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        } else if (obj instanceof CharSequence) {
            return StrUtil.isEmpty((CharSequence) obj);
        } else if (obj instanceof Map) {
            return MapUtil.isEmpty((Map) obj);
        } else if (obj instanceof Iterable) {
            return IterUtil.isEmpty((Iterable) obj);
        } else if (obj instanceof Iterator) {
            return IterUtil.isEmpty((Iterator) obj);
        } else if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.isEmpty(obj);
        }

        return false;
    }

    /**
     * 判断指定对象是否为非空，支持：
     *
     * <pre>
     * 1. CharSequence
     * 2. Map
     * 3. Iterable
     * 4. Iterator
     * 5. Array
     * </pre>
     *
     * @param obj 被判断的对象
     * @return 是否为空，如果类型不支持，返回true
     * @since 1.0.0
     */
    public static boolean isNotEmpty( Object obj ) {
        return false == isEmpty(obj);
    }

    /**
     * 如果给定对象为{@code null}返回默认值
     *
     * <pre>
     * ObjectUtil.defaultIfNull(null, null)      = null
     * ObjectUtil.defaultIfNull(null, "")        = ""
     * ObjectUtil.defaultIfNull(null, "zz")      = "zz"
     * ObjectUtil.defaultIfNull("abc", *)        = "abc"
     * ObjectUtil.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
     * </pre>
     *
     * @param <T>          对象类型
     * @param object       被检查对象，可能为{@code null}
     * @param defaultValue 被检查对象为{@code null}返回的默认值，可以为{@code null}
     * @return 被检查对象为{@code null}返回默认值，否则返回原值
     * @since 1.0.0
     */
    public static <T> T defaultIfNull( final T object, final T defaultValue ) {
        return (null != object) ? object : defaultValue;
    }

    /**
     * 如果给定对象为{@code null} 返回默认值, 如果不为null 返回自定义handle处理后的返回值
     *
     * @param source       Object 类型对象
     * @param handle       自定义的处理方法
     * @param defaultValue 默认为空的返回值
     * @param <T>          被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
     * @return 处理后的返回值
     * @since 1.0.0
     */
    public static <T> T defaultIfNull( Object source, Supplier<? extends T> handle, final T defaultValue ) {
        if (Objects.nonNull(source)) {
            return handle.get();
        }
        return defaultValue;
    }

    /**
     * 如果给定对象为{@code null}或者""返回默认值, 否则返回自定义handle处理后的返回值
     *
     * @param str          String 类型
     * @param handle       自定义的处理方法
     * @param defaultValue 默认为空的返回值
     * @param <T>          被检查对象为{@code null}或者 ""返回默认值，否则返回自定义handle处理后的返回值
     * @return 处理后的返回值
     * @since 1.0.0
     */
    public static <T> T defaultIfEmpty( String str, Supplier<? extends T> handle, final T defaultValue ) {
        if (StrUtil.isNotEmpty(str)) {
            return handle.get();
        }
        return defaultValue;
    }

    /**
     * 如果给定对象为{@code null}或者 "" 返回默认值
     *
     * <pre>
     * ObjectUtil.defaultIfEmpty(null, null)      = null
     * ObjectUtil.defaultIfEmpty(null, "")        = ""
     * ObjectUtil.defaultIfEmpty("", "zz")      = "zz"
     * ObjectUtil.defaultIfEmpty(" ", "zz")      = " "
     * ObjectUtil.defaultIfEmpty("abc", *)        = "abc"
     * </pre>
     *
     * @param <T>          对象类型（必须实现CharSequence接口）
     * @param str          被检查对象，可能为{@code null}
     * @param defaultValue 被检查对象为{@code null}或者 ""返回的默认值，可以为{@code null}或者 ""
     * @return 被检查对象为{@code null}或者 ""返回默认值，否则返回原值
     * @since 1.0.0
     */
    public static <T extends CharSequence> T defaultIfEmpty( final T str, final T defaultValue ) {
        return StrUtil.isEmpty(str) ? defaultValue : str;
    }

    /**
     * 如果给定对象为{@code null}或者""或者空白符返回默认值
     *
     * <pre>
     * ObjectUtil.defaultIfBlank(null, null)      = null
     * ObjectUtil.defaultIfBlank(null, "")        = ""
     * ObjectUtil.defaultIfBlank("", "zz")      = "zz"
     * ObjectUtil.defaultIfBlank(" ", "zz")      = "zz"
     * ObjectUtil.defaultIfBlank("abc", *)        = "abc"
     * </pre>
     *
     * @param <T>          对象类型（必须实现CharSequence接口）
     * @param str          被检查对象，可能为{@code null}
     * @param defaultValue 被检查对象为{@code null}或者 ""或者空白符返回的默认值，可以为{@code null}或者 ""或者空白符
     * @return 被检查对象为{@code null}或者 ""或者空白符返回默认值，否则返回原值
     * @since 1.0.0
     */
    public static <T extends CharSequence> T defaultIfBlank( final T str, final T defaultValue ) {
        return StrUtil.isBlank(str) ? defaultValue : str;
    }

    /**
     * 克隆对象<br>
     * 如果对象实现Cloneable接口，调用其clone方法<br>
     * 如果实现Serializable接口，执行深度克隆<br>
     * 否则返回{@code null}
     *
     * @param <T> 对象类型
     * @param obj 被克隆对象
     * @return 克隆后的对象
     */
    public static <T> T clone( T obj ) {
        T result = ArrayUtil.clone(obj);
        if (null == result) {
            if (obj instanceof Cloneable) {
                result = ReflectUtil.invoke(obj, "clone");
            } else {
                result = cloneByStream(obj);
            }
        }
        return result;
    }

    /**
     * 返回克隆后的对象，如果克隆失败，返回原对象
     *
     * @param <T> 对象类型
     * @param obj 对象
     * @return 克隆后或原对象
     */
    public static <T> T cloneIfPossible( final T obj ) {
        T clone = null;
        try {
            clone = clone(obj);
        } catch (Exception e) {
            // pass
        }
        return clone == null ? obj : clone;
    }

    /**
     * 序列化后拷贝流的方式克隆<br>
     * 对象必须实现Serializable接口
     *
     * @param <T> 对象类型
     * @param obj 被克隆对象
     * @return 克隆后的对象
     * @throws UtilException IO异常和ClassNotFoundException封装
     */
    public static <T> T cloneByStream( T obj ) {
        return SerializeUtil.clone(obj);
    }

    /**
     * 序列化<br>
     * 对象必须实现Serializable接口
     *
     * @param <T> 对象类型
     * @param obj 要被序列化的对象
     * @return 序列化后的字节码
     */
    public static <T> byte[] serialize( T obj ) {
        return SerializeUtil.serialize(obj);
    }

    /**
     * 反序列化<br>
     * 对象必须实现Serializable接口
     *
     * <p>
     * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
     * </p>
     *
     * @param <T>   对象类型
     * @param bytes 反序列化的字节码
     * @return 反序列化后的对象
     */
    public static <T> T deserialize( byte[] bytes ) {
        return SerializeUtil.deserialize(bytes);
    }

    /**
     * 是否为基本类型，包括包装类型和非包装类型
     *
     * @param object 被检查对象
     * @return 是否为基本类型
     * @see ClassUtil#isBasicType(Class)
     */
    public static boolean isBasicType( Object object ) {
        return ClassUtil.isBasicType(object.getClass());
    }

    /**
     * 检查是否为有效的数字<br>
     * 检查Double和Float是否为无限大，或者Not a Number<br>
     * 非数字类型和Null将返回true
     *
     * @param obj 被检查类型
     * @return 检查结果，非数字类型和Null将返回true
     */
    public static boolean isValidIfNumber( Object obj ) {
        if (obj instanceof Number) {
            return NumberUtil.isValidNumber((Number) obj);
        }
        return true;
    }

    /**
     * {@code null}安全的对象比较，{@code null}对象排在末尾
     *
     * @param <T> 被比较对象类型
     * @param c1  对象1，可以为{@code null}
     * @param c2  对象2，可以为{@code null}
     * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
     * @see java.util.Comparator#compare(Object, Object)
     * @since 1.0.0
     */
    public static <T extends Comparable<? super T>> int compare( T c1, T c2 ) {
        return CompareUtil.compare(c1, c2);
    }

    /**
     * {@code null}安全的对象比较
     *
     * @param <T>         被比较对象类型
     * @param c1          对象1，可以为{@code null}
     * @param c2          对象2，可以为{@code null}
     * @param nullGreater 当被比较对象为null时是否排在前面
     * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
     * @see java.util.Comparator#compare(Object, Object)
     * @since 1.0.0
     */
    public static <T extends Comparable<? super T>> int compare( T c1, T c2, boolean nullGreater ) {
        return CompareUtil.compare(c1, c2, nullGreater);
    }

    /**
     * 获得给定类的第一个泛型参数
     *
     * @param obj 被检查的对象
     * @return {@link Class}
     * @since 1.0.0
     */
    public static Class<?> getTypeArgument( Object obj ) {
        return getTypeArgument(obj, 0);
    }

    /**
     * 获得给定类的第一个泛型参数
     *
     * @param obj   被检查的对象
     * @param index 泛型类型的索引号，即第几个泛型类型
     * @return {@link Class}
     * @since 1.0.0
     */
    public static Class<?> getTypeArgument( Object obj, int index ) {
        return ClassUtil.getTypeArgument(obj.getClass(), index);
    }

    /**
     * 将Object转为String<br>
     * 策略为：
     * <pre>
     *  1、null转为"null"
     *  2、调用Convert.toStr(Object)转换
     * </pre>
     *
     * @param obj Bean对象
     * @return Bean所有字段转为Map后的字符串
     * @since 1.0.0
     */
    public static String toString( Object obj ) {
        if (null == obj) {
            return StrUtil.NULL;
        }
        if (obj instanceof Map) {
            return obj.toString();
        }

        return Convert.toStr(obj);
    }

    /**
     * 存在多少个{@code null}或空对象，通过{@link ObjectUtil#isEmpty(Object)} 判断元素
     *
     * @param objs 被检查的对象,一个或者多个
     * @return 存在{@code null}的数量
     */
    public static int emptyCount( Object... objs ) {
        return ArrayUtil.emptyCount(objs);
    }

    /**
     * 是否存在{@code null}对象，通过{@link ObjectUtil#isNull(Object)} 判断元素
     *
     * @param objs 被检查对象
     * @return 是否存在
     * @see ArrayUtil#hasNull(Object[])
     * @since 1.0.0
     */
    public static boolean hasNull( Object... objs ) {
        return ArrayUtil.hasNull(objs);
    }

    /**
     * 是否存在{@code null}或空对象，通过{@link ObjectUtil#isEmpty(Object)} 判断元素
     *
     * @param objs 被检查对象
     * @return 是否存在
     * @see ArrayUtil#hasEmpty(Object...)
     */
    public static boolean hasEmpty( Object... objs ) {
        return ArrayUtil.hasEmpty(objs);
    }

    /**
     * 是否全都为{@code null}或空对象，通过{@link ObjectUtil#isEmpty(Object)} 判断元素
     *
     * @param objs 被检查的对象,一个或者多个
     * @return 是否都为空
     */
    public static boolean isAllEmpty( Object... objs ) {
        return ArrayUtil.isAllEmpty(objs);
    }

    /**
     * 是否全都不为{@code null}或空对象，通过{@link ObjectUtil#isEmpty(Object)} 判断元素
     *
     * @param objs 被检查的对象,一个或者多个
     * @return 是否都不为空
     */
    public static boolean isAllNotEmpty( Object... objs ) {
        return ArrayUtil.isAllNotEmpty(objs);
    }

    /**
     * Generates a hash code for multiple values. The hash code is generated by calling {@link
     * Arrays#hashCode(Object[])}. Note that array arguments to this method, with the exception of a
     * single Object array, do not get any special handling; their hash codes are based on identity
     * and not contents.
     *
     * <p>This is useful for implementing {@link Object#hashCode()}. For example, in an object that
     * has three properties, {@code x}, {@code y}, and {@code z}, one could write:
     *
     * <pre>{@code
     * public int hashCode() {
     *   return Objects.hashCode(getX(), getY(), getZ());
     * }
     * }</pre>
     *
     * <p><b>Warning:</b> When a single object is supplied, the returned hash code does not equal the
     * hash code of that object.
     *
     * <p><b>Note for Java 7 and later:</b> This method should be treated as deprecated; use {@link
     * java.util.Objects#hash} instead.
     *
     * @param objects objects
     * @return int
     */
    public static int hashCode( Object... objects ) {

        return Objects.hash(objects);
    }

    /**
     * Return whether the given throwable is a checked exception:
     * that is, neither a RuntimeException nor an Error.
     *
     * @param ex the throwable to check
     * @return whether the throwable is a checked exception
     * @see Exception
     * @see RuntimeException
     * @see Error
     */
    public static boolean isCheckedException( Throwable ex ) {
        return !(ex instanceof RuntimeException || ex instanceof Error);
    }

    /**
     * Check whether the given exception is compatible with the specified
     * exception types, as declared in a throws clause.
     *
     * @param ex                 the exception to check
     * @param declaredExceptions the exception types declared in the throws clause
     * @return whether the given exception is compatible
     */
    public static boolean isCompatibleWithThrowsClause( Throwable ex, Class<?>... declaredExceptions ) {
        if (!isCheckedException(ex)) {
            return true;
        }
        if (declaredExceptions != null) {
            for (Class<?> declaredException : declaredExceptions) {
                if (declaredException.isInstance(ex)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象，如果为{@code null} 返回false
     */
    public static boolean isArray( Object obj ) {
        return ArrayUtil.isArray(obj);
    }

    /**
     * 数组是否为空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty( T[] array ) {
        return ArrayUtil.isEmpty(array);
    }


    /**
     * Unwrap the given object which is potentially a {@link Optional}.
     *
     * @param obj the candidate object
     * @return either the value held within the {@code Optional}, {@code null}
     * if the {@code Optional} is empty, or simply the given object as-is
     */

    public static Object unwrapOptional( Object obj ) {
        if (obj instanceof Optional) {
            Optional<?> optional = (Optional<?>) obj;
            if (!optional.isPresent()) {
                return null;
            }
            Object result = optional.get();
            Precondition.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
            return result;
        }
        return obj;
    }

    /**
     * Check whether the given array contains the given element.
     *
     * @param array   the array to check (may be {@code null},
     *                in which case the return value will always be {@code false})
     * @param element the element to check for
     * @return whether the element has been found in the given array
     */
    public static boolean containsElement( Object[] array, Object element ) {
        if (array == null) {
            return false;
        }
        for (Object arrayEle : array) {
            if (nullSafeEquals(arrayEle, element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given array of enum constants contains a constant with the given name,
     * ignoring case when determining a match.
     *
     * @param enumValues the enum values to check, typically obtained via {@code MyEnum.values()}
     * @param constant   the constant name to find (must not be null or empty string)
     * @return whether the constant has been found in the given array
     */
    public static boolean containsConstant( Enum<?>[] enumValues, String constant ) {
        return containsConstant(enumValues, constant, false);
    }

    /**
     * Check whether the given array of enum constants contains a constant with the given name.
     *
     * @param enumValues    the enum values to check, typically obtained via {@code MyEnum.values()}
     * @param constant      the constant name to find (must not be null or empty string)
     * @param caseSensitive whether case is significant in determining a match
     * @return whether the constant has been found in the given array
     */
    public static boolean containsConstant( Enum<?>[] enumValues, String constant, boolean caseSensitive ) {
        for (Enum<?> candidate : enumValues) {
            if (caseSensitive ? candidate.toString().equals(constant) :
                    candidate.toString().equalsIgnoreCase(constant)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Case insensitive alternative to {@link Enum#valueOf(Class, String)}.
     *
     * @param <E>        the concrete Enum type
     * @param enumValues the array of all Enum constants in question, usually per {@code Enum.values()}
     * @param constant   the constant to get the enum value of
     * @return E Enum type
     * @throws IllegalArgumentException if the given constant is not found in the given array
     *                                  of enum values. Use {@link #containsConstant(Enum[], String)} as a guard to avoid this exception.
     */
    public static <E extends Enum<?>> E caseInsensitiveValueOf( E[] enumValues, String constant ) {
        for (E candidate : enumValues) {
            if (candidate.toString().equalsIgnoreCase(constant)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Constant [" + constant + "] does not exist in enum type " +
                enumValues.getClass().getComponentType().getName());
    }





    //---------------------------------------------------------------------
    // Convenience methods for content-based equality/hash-code handling
    //---------------------------------------------------------------------

    /**
     * Determine if the given objects are equal, returning {@code true} if
     * both are {@code null} or {@code false} if only one is {@code null}.
     * <p>Compares arrays with {@code Arrays.equals}, performing an equality
     * check based on the array elements rather than the array reference.
     *
     * @param o1 first Object to compare
     * @param o2 second Object to compare
     * @return whether the given objects are equal
     * @see Object#equals(Object)
     * @see Arrays#equals
     */
    public static boolean nullSafeEquals( Object o1, Object o2 ) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return arrayEquals(o1, o2);
        }
        return false;
    }

    /**
     * Compare the given arrays with {@code Arrays.equals}, performing an equality
     * check based on the array elements rather than the array reference.
     *
     * @param o1 first array to compare
     * @param o2 second array to compare
     * @return whether the given objects are equal
     * @see #nullSafeEquals(Object, Object)
     * @see Arrays#equals
     */
    private static boolean arrayEquals( Object o1, Object o2 ) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        }
        if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        }
        if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        }
        if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        }
        if (o1 instanceof short[] && o2 instanceof short[]) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }
        return false;
    }

    /**
     * Return as hash code for the given object; typically the value of
     * {@code Object#hashCode()}}. If the object is an array,
     * this method will delegate to any of the {@code nullSafeHashCode}
     * methods for arrays in this class. If the object is {@code null},
     * this method returns 0.
     *
     * @param obj object
     * @return int
     * @see Object#hashCode()
     * @see #nullSafeHashCode(Object[])
     * @see #nullSafeHashCode(boolean[])
     * @see #nullSafeHashCode(byte[])
     * @see #nullSafeHashCode(char[])
     * @see #nullSafeHashCode(double[])
     * @see #nullSafeHashCode(float[])
     * @see #nullSafeHashCode(int[])
     * @see #nullSafeHashCode(long[])
     * @see #nullSafeHashCode(short[])
     */
    public static int nullSafeHashCode( Object obj ) {
        if (obj == null) {
            return 0;
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return nullSafeHashCode((Object[]) obj);
            }
            if (obj instanceof boolean[]) {
                return nullSafeHashCode((boolean[]) obj);
            }
            if (obj instanceof byte[]) {
                return nullSafeHashCode((byte[]) obj);
            }
            if (obj instanceof char[]) {
                return nullSafeHashCode((char[]) obj);
            }
            if (obj instanceof double[]) {
                return nullSafeHashCode((double[]) obj);
            }
            if (obj instanceof float[]) {
                return nullSafeHashCode((float[]) obj);
            }
            if (obj instanceof int[]) {
                return nullSafeHashCode((int[]) obj);
            }
            if (obj instanceof long[]) {
                return nullSafeHashCode((long[]) obj);
            }
            if (obj instanceof short[]) {
                return nullSafeHashCode((short[]) obj);
            }
        }
        return obj.hashCode();
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     *
     * @param array array
     * @return int
     */
    public static int nullSafeHashCode( Object[] array ) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (Object element : array) {
            hash = MULTIPLIER * hash + nullSafeHashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     *
     * @param array array
     * @return int
     */
    public static int nullSafeHashCode( boolean[] array ) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (boolean element : array) {
            hash = MULTIPLIER * hash + Boolean.hashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     *
     * @param array array
     * @return int
     */
    public static int nullSafeHashCode( byte[] array ) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (byte element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     *
     * @param array array
     * @return int
     */
    public static int nullSafeHashCode( char[] array ) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (char element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     *
     * @param array array
     * @return int
     */
    public static int nullSafeHashCode( double[] array ) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (double element : array) {
            hash = MULTIPLIER * hash + Double.hashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     *
     * @param array array
     * @return int
     */
    public static int nullSafeHashCode( float[] array ) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (float element : array) {
            hash = MULTIPLIER * hash + Float.hashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     *
     * @param array array
     * @return int
     */
    public static int nullSafeHashCode( int[] array ) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (int element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     *
     * @param array array
     * @return int
     */
    public static int nullSafeHashCode( long[] array ) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (long element : array) {
            hash = MULTIPLIER * hash + Long.hashCode(element);
        }
        return hash;
    }

    /**
     * Return a hash code based on the contents of the specified array.
     * If {@code array} is {@code null}, this method returns 0.
     *
     * @param array array
     * @return int
     */
    public static int nullSafeHashCode( short[] array ) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (short element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }


    //---------------------------------------------------------------------
    // Convenience methods for toString output
    //---------------------------------------------------------------------

    /**
     * Return a String representation of an object's overall identity.
     *
     * @param obj the object (may be {@code null})
     * @return the object's identity as String representation,
     * or an empty String if the object was {@code null}
     */
    public static String identityToString( Object obj ) {
        if (obj == null) {
            return EMPTY_STRING;
        }
        return obj.getClass().getName() + "@" + getIdentityHexString(obj);
    }

    /**
     * Return a hex String form of an object's identity hash code.
     *
     * @param obj the object
     * @return the object's identity code in hex notation
     */
    public static String getIdentityHexString( Object obj ) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    /**
     * Return a content-based String representation if {@code obj} is
     * not {@code null}; otherwise returns an empty String.
     * <p>Differs from {@link #nullSafeToString(Object)} in that it returns
     * an empty String rather than "null" for a {@code null} value.
     *
     * @param obj the object to build a display String for
     * @return a display String representation of {@code obj}
     * @see #nullSafeToString(Object)
     */
    public static String getDisplayString( Object obj ) {
        if (obj == null) {
            return EMPTY_STRING;
        }
        return nullSafeToString(obj);
    }

    /**
     * Determine the class name for the given object.
     * <p>Returns a {@code "null"} String if {@code obj} is {@code null}.
     *
     * @param obj the object to introspect (may be {@code null})
     * @return the corresponding class name
     */
    public static String nullSafeClassName( Object obj ) {
        return (obj != null ? obj.getClass().getName() : NULL_STRING);
    }

    /**
     * Return a String representation of the specified Object.
     * <p>Builds a String representation of the contents in case of an array.
     * Returns a {@code "null"} String if {@code obj} is {@code null}.
     *
     * @param obj the object to build a String representation for
     * @return a String representation of {@code obj}
     */
    public static String nullSafeToString( Object obj ) {
        if (obj == null) {
            return NULL_STRING;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Object[]) {
            return nullSafeToString((Object[]) obj);
        }
        if (obj instanceof boolean[]) {
            return nullSafeToString((boolean[]) obj);
        }
        if (obj instanceof byte[]) {
            return nullSafeToString((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return nullSafeToString((char[]) obj);
        }
        if (obj instanceof double[]) {
            return nullSafeToString((double[]) obj);
        }
        if (obj instanceof float[]) {
            return nullSafeToString((float[]) obj);
        }
        if (obj instanceof int[]) {
            return nullSafeToString((int[]) obj);
        }
        if (obj instanceof long[]) {
            return nullSafeToString((long[]) obj);
        }
        if (obj instanceof short[]) {
            return nullSafeToString((short[]) obj);
        }
        String str = obj.toString();
        return (str != null ? str : EMPTY_STRING);
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>The String representation consists of a list of the array's elements,
     * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space).
     * Returns a {@code "null"} String if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString( Object[] array ) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (Object o : array) {
            stringJoiner.add(String.valueOf(o));
        }
        return stringJoiner.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>The String representation consists of a list of the array's elements,
     * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space).
     * Returns a {@code "null"} String if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString( boolean[] array ) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (boolean b : array) {
            stringJoiner.add(String.valueOf(b));
        }
        return stringJoiner.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>The String representation consists of a list of the array's elements,
     * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space).
     * Returns a {@code "null"} String if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString( byte[] array ) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (byte b : array) {
            stringJoiner.add(String.valueOf(b));
        }
        return stringJoiner.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>The String representation consists of a list of the array's elements,
     * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space).
     * Returns a {@code "null"} String if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString( char[] array ) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (char c : array) {
            stringJoiner.add('\'' + String.valueOf(c) + '\'');
        }
        return stringJoiner.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>The String representation consists of a list of the array's elements,
     * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space).
     * Returns a {@code "null"} String if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString( double[] array ) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (double d : array) {
            stringJoiner.add(String.valueOf(d));
        }
        return stringJoiner.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>The String representation consists of a list of the array's elements,
     * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space).
     * Returns a {@code "null"} String if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString( float[] array ) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (float f : array) {
            stringJoiner.add(String.valueOf(f));
        }
        return stringJoiner.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>The String representation consists of a list of the array's elements,
     * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space).
     * Returns a {@code "null"} String if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString( int[] array ) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (int i : array) {
            stringJoiner.add(String.valueOf(i));
        }
        return stringJoiner.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>The String representation consists of a list of the array's elements,
     * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space).
     * Returns a {@code "null"} String if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString( long[] array ) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (long l : array) {
            stringJoiner.add(String.valueOf(l));
        }
        return stringJoiner.toString();
    }

    /**
     * Return a String representation of the contents of the specified array.
     * <p>The String representation consists of a list of the array's elements,
     * enclosed in curly braces ({@code "{}"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space).
     * Returns a {@code "null"} String if {@code array} is {@code null}.
     *
     * @param array the array to build a String representation for
     * @return a String representation of {@code array}
     */
    public static String nullSafeToString( short[] array ) {
        if (array == null) {
            return NULL_STRING;
        }
        int length = array.length;
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR, ARRAY_START, ARRAY_END);
        for (short s : array) {
            stringJoiner.add(String.valueOf(s));
        }
        return stringJoiner.toString();
    }
}
