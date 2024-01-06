package com.whaleal.mars.util;






import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 类工具类 <br>
 *
 *
 * @author wh
 */
public class ClassUtil {

    private static final Map<String, PrimitiveInfo<?>> PRIMITIVES = new ConcurrentHashMap<>();
    private final static Map<Class<?>, Set<Class<?>>> assignmentTable = new ConcurrentHashMap<>();

    static {
        addPrimitive(boolean.class, "Z", Boolean.class, "booleanValue", false);
        addPrimitive(short.class, "S", Short.class, "shortValue", (short) 0);
        addPrimitive(int.class, "I", Integer.class, "intValue", 0);
        addPrimitive(long.class, "J", Long.class, "longValue", 0L);
        addPrimitive(float.class, "F", Float.class, "floatValue", 0F);
        addPrimitive(double.class, "D", Double.class, "doubleValue", 0D);
        addPrimitive(char.class, "C", Character.class, "charValue", '\0');
        addPrimitive(byte.class, "B", Byte.class, "byteValue", (byte) 0);
        addPrimitive(void.class, "V", Void.class, null, null);
    }

    static {
        // boolean可以接受：boolean
        assignmentTable.put(boolean.class, assignableSet(boolean.class));

        // byte可以接受：byte
        assignmentTable.put(byte.class, assignableSet(byte.class));

        // char可以接受：char
        assignmentTable.put(char.class, assignableSet(char.class));

        // short可以接受：short, byte
        assignmentTable.put(short.class, assignableSet(short.class, byte.class));

        // int可以接受：int、byte、short、char
        assignmentTable.put(int.class, assignableSet(int.class, byte.class, short.class, char.class));

        // long可以接受：long、int、byte、short、char
        assignmentTable.put(long.class, assignableSet(long.class, int.class, byte.class, short.class, char.class));

        // float可以接受：float, long, int, byte, short, char
        assignmentTable.put(float.class, assignableSet(float.class, long.class, int.class, byte.class, short.class, char.class));

        // double可以接受：double, float, long, int, byte, short, char
        assignmentTable.put(double.class, assignableSet(double.class, float.class, long.class, int.class, byte.class, short.class, char.class));

    }

    /**
     * {@code null}安全的获取对象类型
     *
     * @param <T> 对象类型
     * @param obj 对象，如果为{@code null} 返回{@code null}
     * @return 对象类型，提供对象如果为{@code null} 返回{@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass( T obj ) {
        return ((null == obj) ? null : (Class<T>) obj.getClass());
    }

    /**
     * 获得外围类<br>
     * 返回定义此类或匿名类所在的类，如果类本身是在包中定义的，返回{@code null}
     *
     * @param clazz 类
     * @return 外围类
     * @since 1.0.0
     */
    public static Class<?> getEnclosingClass( Class<?> clazz ) {
        return null == clazz ? null : clazz.getEnclosingClass();
    }

    /**
     * 是否为顶层类，即定义在包中的类，而非定义在类中的内部类
     *
     * @param clazz 类
     * @return 是否为顶层类
     * @since 1.0.0
     */
    public static boolean isTopLevelClass( Class<?> clazz ) {
        if (null == clazz) {
            return false;
        }
        return null == getEnclosingClass(clazz);
    }

    /**
     * 获取类名
     *
     * @param obj      获取类名对象
     * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
     * @return 类名
     * @since 1.0.0
     */
    public static String getClassName( Object obj, boolean isSimple ) {
        if (null == obj) {
            return null;
        }
        final Class<?> clazz = obj.getClass();
        return getClassName(clazz, isSimple);
    }

    // ----------------------------------------------------------------------------------------- Scan classes

    /**
     * 获取类名<br>
     * 类名并不包含“.class”这个扩展名<br>
     * 例如：ClassUtil这个类<br>
     * <p>
     * isSimple为false: "com.whaleal.icefrog.util.ClassUtil"
     * isSimple为true: "ClassUtil"
     *
     * @param clazz    类
     * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
     * @return 类名
     * @since 1.0.0
     */
    public static String getClassName( Class<?> clazz, boolean isSimple ) {
        if (null == clazz) {
            return null;
        }
        return isSimple ? clazz.getSimpleName() : clazz.getName();
    }







    // ---------------------------------------------------------------------------------------------------- Invoke start








    /**
     * 判断该类型是不是包装类型
     *
     * @param clazz 传入的class
     * @return 是否为包装类型
     */
    public static boolean isBasicClass( Class<?> clazz ) {
        boolean isPrimitive = false;
        try {
            if (clazz.isPrimitive() || clazz.isAssignableFrom(String.class)) {
                isPrimitive = true;
            } else {
                isPrimitive = ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
            }
        } catch (Exception e) {
            isPrimitive = false;
        }
        return isPrimitive;
    }




    /**
     * 指定类是否为Public
     *
     * @param clazz 类
     * @return 是否为public
     */
    public static boolean isPublic( Class<?> clazz ) {
        if (null == clazz) {
            throw new NullPointerException("Class to provided is null.");
        }
        return Modifier.isPublic(clazz.getModifiers());
    }

    /**
     * 指定方法是否为Public
     *
     * @param method 方法
     * @return 是否为public
     */
    public static boolean isPublic( Method method ) {
        Assert.notNull(method, "Method to provided is null.");
        return Modifier.isPublic(method.getModifiers());
    }

    /**
     * 指定类是否为非public
     *
     * @param clazz 类
     * @return 是否为非public
     */
    public static boolean isNotPublic( Class<?> clazz ) {
        return false == isPublic(clazz);
    }

    /**
     * 指定方法是否为非public
     *
     * @param method 方法
     * @return 是否为非public
     */
    public static boolean isNotPublic( Method method ) {
        return false == isPublic(method);
    }

    /**
     * 是否为静态方法
     *
     * @param method 方法
     * @return 是否为静态方法
     */
    public static boolean isStatic( Method method ) {
        Assert.notNull(method, "Method to provided is null.");
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 设置方法为可访问
     *
     * @param method 方法
     * @return 方法
     */
    public static Method setAccessible( Method method ) {
        if (null != method && false == method.isAccessible()) {
            method.setAccessible(true);
        }
        return method;
    }

    /**
     * 是否为抽象类
     *
     * @param clazz 类
     * @return 是否为抽象类
     */
    public static boolean isAbstract( Class<?> clazz ) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    /**
     * 是否为标准的类<br>
     * 这个类必须：
     * <p>
     * <p>
     * 1、非接口
     * 2、非抽象类
     * 3、非Enum枚举
     * 4、非数组
     * 5、非注解
     * 6、非原始类型（int, long等）
     *
     * @param clazz 类
     * @return 是否为标准类
     */
    public static boolean isNormalClass( Class<?> clazz ) {
        return null != clazz //
                && false == clazz.isInterface() //
                && false == isAbstract(clazz) //
                && false == clazz.isEnum() //
                && false == clazz.isArray() //
                && false == clazz.isAnnotation() //
                && false == clazz.isSynthetic() //
                && false == clazz.isPrimitive();//
    }

    /**
     * 判断类是否为枚举类型
     *
     * @param clazz 类
     * @return 是否为枚举类型
     * @since 1.0.0
     */
    public static boolean isEnum( Class<?> clazz ) {
        return null != clazz && clazz.isEnum();
    }



    /**
     * 获取指定类型分的默认值<br>
     * 默认值规则为：
     * <p>
     * <p>
     * 1、如果为原始类型，返回0
     * 2、非原始类型返回{@code null}
     *
     * @param clazz 类
     * @return 默认值
     * @since 1.0.0
     */
    public static Object getDefaultValue( Class<?> clazz ) {
        if (clazz.isPrimitive()) {
            if (long.class == clazz) {
                return 0L;
            } else if (int.class == clazz) {
                return 0;
            } else if (short.class == clazz) {
                return (short) 0;
            } else if (char.class == clazz) {
                return (char) 0;
            } else if (byte.class == clazz) {
                return (byte) 0;
            } else if (double.class == clazz) {
                return 0D;
            } else if (float.class == clazz) {
                return 0f;
            } else if (boolean.class == clazz) {
                return false;
            }
        }

        return null;
    }

    /**
     * 获得默认值列表
     *
     * @param classes 值类型
     * @return 默认值列表
     * @since 1.0.0
     */
    public static Object[] getDefaultValues( Class<?>... classes ) {
        final Object[] values = new Object[classes.length];
        for (int i = 0; i < classes.length; i++) {
            values[i] = getDefaultValue(classes[i]);
        }
        return values;
    }

    /**
     * 是否为JDK中定义的类或接口，判断依据：
     * <p>
     * <p>
     * 1、以java.、javax.开头的包名
     * 2、ClassLoader为null
     *
     * @param clazz 被检查的类
     * @return 是否为JDK中定义的类或接口
     * @since 1.0.0
     */
    public static boolean isJdkClass( Class<?> clazz ) {
        final Package objectPackage = clazz.getPackage();
        if (null == objectPackage) {
            return false;
        }
        final String objectPackageName = objectPackage.getName();
        return objectPackageName.startsWith("java.") //
                || objectPackageName.startsWith("javax.") //
                || clazz.getClassLoader() == null;
    }

    /**
     * 获取class类路径URL, 不管是否在jar包中都会返回文件夹的路径<br>
     * class在jar包中返回jar所在文件夹,class不在jar中返回文件夹目录<br>
     * jdk中的类不能使用此方法
     *
     * @param clazz 类
     * @return URL
     * @since 1.0.0
     */
    public static URL getLocation( Class<?> clazz ) {
        if (null == clazz) {
            return null;
        }
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }

    // ==========================================================================
    // 取得友好类名和package名的方法。
    // ==========================================================================



    /**
     * 从接口端计算深度
     *
     * @param pInterface  入参
     * @param targetClass 入参
     * @return 返回 int 值
     */
    public static int checkInterfaceDeepth( Class<?> pInterface, Class<?> targetClass ) {
        if (targetClass.isAssignableFrom(pInterface))
            return 0;
        int min = -1;
        for (Class<?> c : targetClass.getInterfaces()) {
            if (pInterface.isAssignableFrom(c)) {
                int i = checkInterfaceDeepth(pInterface, c) + 1;
                if (min == -1 || min > i) {
                    min = i;
                }
            }
        }
        return min;
    }

    /**
     * @param supperClass 入参
     * @param targetClass 入参
     * @return 返回值
     */
    public static int checkSupperClassDeepth( Class<?> supperClass, Class<?> targetClass ) {
        if (!supperClass.isAssignableFrom(targetClass)) {
            return -1;
        }
        if (targetClass.isAssignableFrom(supperClass))
            return 0;
        int supperDeepth = checkSupperClassDeepth(supperClass, targetClass.getSuperclass()) + 1;
        int interfaceDeepth = checkInterfaceDeepth(supperClass, targetClass.getSuperclass()) + 1;
        if (supperDeepth < interfaceDeepth)
            return supperDeepth;
        return interfaceDeepth;
    }

    /**
     * 采取驼峰规则
     *
     * @param clazz 入参
     * @param <T>   入参中的泛型
     * @return 返回值
     */
    public static <T> String humpString( Class<T> clazz ) {
        String simpleName = clazz.getSimpleName();
        String first = simpleName.charAt(0) + "";
        return first.toLowerCase() + simpleName.substring(1);
    }



    /**
     * 取得对象所属的类的资源名。
     * <p>
     * 例如：
     * <p>
     * <p>
     * <p>
     * ClassUtil.getResourceNameForObjectClass(&quot;This is a string&quot;) = &quot;java/lang/String.class&quot;
     *
     * <p>
     *
     * @param object 要显示类名的对象
     * @return 指定对象所属类的资源名，如果对象为空，则返回<code>null</code>
     */
    public static String getResourceNameForObjectClass( Object object ) {
        if (object == null) {
            return null;
        }

        return object.getClass().getName().replace('.', '/') + ".class";
    }

    /**
     * 取得指定类的资源名。
     * <p>
     * 例如：
     * <p>
     * <p>
     * <p>
     * ClassUtil.getResourceNameForClass(String.class) = &quot;java/lang/String.class&quot;
     *
     * <p>
     *
     * @param clazz 要显示类名的类
     * @return 指定类的资源名，如果指定类为空，则返回<code>null</code>
     */
    public static String getResourceNameForClass( Class<?> clazz ) {
        if (clazz == null) {
            return null;
        }

        return clazz.getName().replace('.', '/') + ".class";
    }

    // ==========================================================================
    // 取得数组类。
    // ==========================================================================

    /**
     * 取得指定类的资源名。
     * <p>
     * 例如：
     * <p>
     * <p>
     * <p>
     * ClassUtil.getResourceNameForClass(&quot;java.lang.String&quot;) = &quot;java/lang/String.class&quot;
     *
     * <p>
     *
     * @param className 要显示的类名
     * @return 指定类名对应的资源名，如果指定类名为空，则返回<code>null</code>
     */
    public static String getResourceNameForClass( String className ) {
        if (className == null) {
            return null;
        }

        return className.replace('.', '/') + ".class";
    }





    /**
     * 取得指定一维数组类.
     * <p>
     *
     * @param componentType 数组的基础类
     * @return 数组类，如果数组的基类为 <code>null</code> ，则返回 <code>null</code>
     */
    public static Class<?> getArrayClass( Class<?> componentType ) {
        return getArrayClass(componentType, 1);
    }

    /**
     * 取得指定维数的 <code>Array</code>类.
     * <p>
     *
     * @param dimension      维数，如果小于 <code>0</code> 则看作 <code>0</code>
     * @param componentClass 类名称
     * @return 如果维数为0, 则返回基类本身, 否则返回数组类，如果数组的基类为 <code>null</code> ，则返回
     * <code>null</code>
     */
    public static Class<?> getArrayClass( Class<?> componentClass, int dimension ) {
        if (componentClass == null) {
            return null;
        }

        switch (dimension) {
            case 1:
                return Array.newInstance(componentClass, 0).getClass();

            case 0:
                return componentClass;

            default:

                return Array.newInstance(componentClass, new int[dimension]).getClass();
        }
    }

    /**
     * 取得primitive类。
     * <p>
     * 例如：
     * ClassUtil.getPrimitiveType(&quot;int&quot;) = int.class;
     * ClassUtil.getPrimitiveType(&quot;long&quot;) = long.class;
     * <p>
     *
     * @param name 传入的类
     * @return 类
     */
    public static Class<?> getPrimitiveType( String name ) {
        PrimitiveInfo<?> info = PRIMITIVES.get(name);

        if (info != null) {
            return info.type;
        }

        return null;
    }

    /**
     * 取得primitive类。
     * <p>
     * 例如：
     * ClassUtil.getPrimitiveType(Integer.class) = int.class;
     * ClassUtil.getPrimitiveType(Long.class) = long.class;
     * <p>
     *
     * @param type 传入的类型
     * @return 获取的原始类型
     */
    public static Class<?> getPrimitiveType( Class<?> type ) {
        return getPrimitiveType(type.getName());
    }

    /**
     * 取得primitive类型的wrapper。如果不是primitive，则原样返回。
     * <p>
     * 例如：
     * <p>
     * ClassUtil.getPrimitiveWrapperType(int.class) = Integer.class;
     * ClassUtil.getPrimitiveWrapperType(int[].class) = int[].class;
     * ClassUtil.getPrimitiveWrapperType(int[][].class) = int[][].class;
     * ClassUtil.getPrimitiveWrapperType(String[][].class) = String[][].class;
     *
     * <p>
     *
     * @param type 传入的基本类型
     * @param <T>  基本类型的泛型
     * @return 返回基本类型的包装类
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getWrapperTypeIfPrimitive( Class<T> type ) {
        if (type.isPrimitive()) {
            return ((PrimitiveInfo<T>) PRIMITIVES.get(type.getName())).wrapperType;
        }

        return type;
    }

    /**
     * 取得primitive类型的默认值。如果不是primitive，则返回<code>null</code>。
     * <p>
     * 例如：
     * ClassUtil.getPrimitiveDefaultValue(int.class) = 0;
     * ClassUtil.getPrimitiveDefaultValue(boolean.class) = false;
     * ClassUtil.getPrimitiveDefaultValue(char.class) = '\0';
     * <p>
     *
     * @param type 传入的基本类型
     * @param <T>  泛型
     * @return 各基本类型的默认值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPrimitiveDefaultValue( Class<T> type ) {
        PrimitiveInfo<T> info = (PrimitiveInfo<T>) PRIMITIVES.get(type.getName());

        if (info != null) {
            return info.defaultValue;
        }

        return null;
    }

    // ==========================================================================
    // 类型匹配。
    // ==========================================================================

    private static <T> void addPrimitive( Class<T> type, String typeCode, Class<T> wrapperType, String unwrapMethod, T defaultValue ) {
        PrimitiveInfo<T> info = new PrimitiveInfo<T>(type, typeCode, wrapperType, unwrapMethod, defaultValue);

        PRIMITIVES.put(type.getName(), info);
        PRIMITIVES.put(wrapperType.getName(), info);
    }


    /**
     * @param types 入参
     * @return 返回的set
     */
    private static Set<Class<?>> assignableSet( Class<?>... types ) {
        Set<Class<?>> assignableSet = new HashSet<>();

        for (Class<?> type : types) {
            assignableSet.add(getPrimitiveType(type));
            assignableSet.add(getWrapperTypeIfPrimitive(type));
        }

        return assignableSet;
    }

    /**
     * 在class loader中查找class的位置。
     *
     * @param clazz 传入的class
     * @return class 的包名
     */
    public static String locateClass( Class<?> clazz ) {
        return locateClass(clazz.getName(), clazz.getClassLoader());
    }

    // ==========================================================================
    // 定位class的位置。
    // ==========================================================================

    /**
     * 在class loader中查找class的位置。
     *
     * @param className 传入的class 字符串名称
     * @return class的位置。
     */
    public static String locateClass( String className ) {
        return locateClass(className, null);
    }

    /**
     * 在class loader中查找class的位置。
     *
     * @param className 类名称
     * @param loader    类加载器
     * @return class的位置。
     */
    public static String locateClass( String className, ClassLoader loader ) {
        if (loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
        }

        String classFile = className.replace('.', '/') + ".class";
        URL locationURL = loader.getResource(classFile);
        String location = null;

        if (locationURL != null) {
            location = locationURL.toExternalForm();

            if (location.endsWith(classFile)) {
                location = location.substring(0, location.length() - classFile.length());
            }

            location = location.replaceAll("^(jar|zip):|!/$", "");
        }

        return location;
    }






    /**
     * @param method the method
     * @return the qualified name of the method
     */
    public static String getQualifiedMethodName( Method method ) {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    /**
     * @param instance the instance to analyze for interfaces
     * @return all interfaces that the given instance implements as array
     */
    public static Class[] getAllInterfaces( Object instance ) {
        return getAllInterfacesForClass(instance.getClass());
    }

    /**
     * @param clazz the class to analyze for interfaces
     * @return all interfaces that the given object implements as array
     */
    public static Class<?>[] getAllInterfacesForClass( Class<?> clazz ) {
        return getAllInterfacesForClass(clazz, null);
    }

    /**
     * @param clazz       the class to analyze for interfaces
     * @param classLoader the ClassLoader that the interfaces need to be visible in
     *                    (may be <code>null</code> when accepting all declared interfaces)
     * @return all interfaces that the given object implements as array
     */
    public static Class<?>[] getAllInterfacesForClass( Class<?> clazz, ClassLoader classLoader ) {
        Set<Class> ifcs = getAllInterfacesForClassAsSet(clazz, classLoader);
        return ifcs.toArray(new Class[ifcs.size()]);
    }

    /**
     * @param instance the instance to analyze for interfaces
     * @return all interfaces that the given instance implements as Set
     */
    public static Set<Class> getAllInterfacesAsSet( Object instance ) {
        return getAllInterfacesForClassAsSet(instance.getClass());
    }

    /**
     * @param clazz the class to analyze for interfaces
     * @return all interfaces that the given object implements as Set
     */
    public static Set<Class> getAllInterfacesForClassAsSet( Class clazz ) {
        return getAllInterfacesForClassAsSet(clazz, null);
    }

    /**
     * @param clazz       the class to analyze for interfaces
     * @param classLoader the ClassLoader that the interfaces need to be visible in
     *                    (may be <code>null</code> when accepting all declared interfaces)
     * @return all interfaces that the given object implements as Set
     */
    public static Set<Class> getAllInterfacesForClassAsSet( Class clazz, ClassLoader classLoader ) {
        if (clazz.isInterface() && isVisible(clazz, classLoader)) {
            return Collections.singleton(clazz);
        }
        Set<Class> interfaces = new LinkedHashSet<Class>();
        while (clazz != null) {
            Class<?>[] ifcs = clazz.getInterfaces();
            for (Class<?> ifc : ifcs) {
                interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
            }
            clazz = clazz.getSuperclass();
        }
        return interfaces;
    }

    /**
     * Check whether the given class is visible in the given ClassLoader.
     *
     * @param clazz       the class to check (typically an interface)
     * @param classLoader the ClassLoader to check against (may be <code>null</code>,
     *                    in which case this method will always return <code>true</code>)
     * @return 返回是否可见
     */
    public static boolean isVisible( Class<?> clazz, ClassLoader classLoader ) {
        if (classLoader == null) {
            return true;
        }
        try {
            Class<?> actualClass = classLoader.loadClass(clazz.getName());
            return (clazz == actualClass);
            // Else: different interface class found...
        } catch (ClassNotFoundException ex) {
            // No interface class found...
            return false;
        }
    }



    /**
     * 代表一个primitive类型的信息。
     */
    @SuppressWarnings("unused")
    private static class PrimitiveInfo<T> {
        final Class<T> type;
        final String typeCode;
        final Class<T> wrapperType;
        final String unwrapMethod;
        final T defaultValue;

        public PrimitiveInfo( Class<T> type, String typeCode, Class<T> wrapperType, String unwrapMethod, T defaultValue ) {
            this.type = type;
            this.typeCode = typeCode;
            this.wrapperType = wrapperType;
            this.unwrapMethod = unwrapMethod;
            this.defaultValue = defaultValue;
        }
    }


    /**
     * 检查目标类是否可以从原类转化<br>
     * 转化包括：<br>
     * 1、原类是对象，目标类型是原类型实现的接口<br>
     * 2、目标类型是原类型的父类<br>
     * 3、两者是原始类型或者包装类型（相互转换）
     *
     * @param targetType 目标类型
     * @param sourceType 原类型
     * @return 是否可转化
     */
    public static boolean isAssignable( Class<?> targetType, Class<?> sourceType ) {
        if (null == targetType || null == sourceType) {
            return false;
        }

        // 对象类型
        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }

        // 基本类型
        if (targetType.isPrimitive()) {
            // 原始类型
            Class<?> resolvedPrimitive = BasicType.WRAPPER_PRIMITIVE_MAP.get(sourceType);
            return targetType.equals(resolvedPrimitive);
        } else {
            // 包装类型
            Class<?> resolvedWrapper = BasicType.PRIMITIVE_WRAPPER_MAP.get(sourceType);
            return resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper);
        }
    }

}
