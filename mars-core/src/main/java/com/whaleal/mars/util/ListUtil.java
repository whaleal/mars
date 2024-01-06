package com.whaleal.mars.util;



import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * List相关工具类
 *
 *
 * @author wh
 */
public class ListUtil {
    /**
     * 新建一个空List
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @return List对象
     * @since 1.0.0
     */
    public static <T> List<T> list( boolean isLinked ) {
        return isLinked ? new LinkedList<>() : new ArrayList<>();
    }

    /**
     * 新建一个List
     *
     * @param <T>      集合元素类型
     * @param isLinked 是否新建LinkedList
     * @param values   数组
     * @return List对象
     * @since 1.0.0
     */
    @SafeVarargs
    public static <T> List<T> list( boolean isLinked, T... values ) {
        if (ArrayUtil.isEmpty(values)) {
            return list(isLinked);
        }
        final List<T> arrayList = isLinked ? new LinkedList<>() : new ArrayList<>(values.length);
        Collections.addAll(arrayList, values);
        return arrayList;
    }

    /**
     * 新建一个List
     *
     * @param <T>        集合元素类型
     * @param isLinked   是否新建LinkedList
     * @param collection 集合
     * @return List对象
     * @since 1.0.0
     */
    public static <T> List<T> list( boolean isLinked, Collection<T> collection ) {
        if (null == collection) {
            return list(isLinked);
        }
        return isLinked ? new LinkedList<>(collection) : new ArrayList<>(collection);
    }



    /**
     * 新建一个List<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>        集合元素类型
     * @param isLinked   是否新建LinkedList
     * @param enumration {@link Enumeration}
     * @return ArrayList对象
     * @since 1.0.0
     */
    public static <T> List<T> list( boolean isLinked, Enumeration<T> enumration ) {
        final List<T> list = list(isLinked);
        if (null != enumration) {
            while (enumration.hasMoreElements()) {
                list.add(enumration.nextElement());
            }
        }
        return list;
    }

    /**
     * 新建一个ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList对象
     */
    @SafeVarargs
    public static <T> ArrayList<T> toList( T... values ) {
        return (ArrayList<T>) list(false, values);
    }

    /**
     * 新建LinkedList
     *
     * @param values 数组
     * @param <T>    类型
     * @return LinkedList
     * @since 1.0.0
     */
    @SafeVarargs
    public static <T> LinkedList<T> toLinkedList( T... values ) {
        return (LinkedList<T>) list(true, values);
    }

    /**
     * 数组转为一个不可变List<br>
     * 类似于Java9中的List.of
     *
     * @param ts  对象
     * @param <T> 对象类型
     * @return 不可修改List
     * @since 1.0.0
     */
    @SafeVarargs
    public static <T> List<T> of( T... ts ) {
        if (ArrayUtil.isEmpty(ts)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(toList(ts));
    }


    /**
     * Creates an empty {@code CopyOnWriteArrayList} instance.
     * 新建一个CopyOnWriteArrayList
     *
     * <p><b>Note:</b> if you need an immutable empty {@link List}, use {@link Collections#emptyList}
     * instead.
     *
     * @return a new, empty {@code CopyOnWriteArrayList}
     */
    // CopyOnWriteArrayList
    public static <E extends Object> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<>();
    }

    /**
     * 新建一个CopyOnWriteArrayList
     *
     * @param <T>        集合元素类型
     * @param collection 集合可以为空
     * @return {@link CopyOnWriteArrayList}
     */
    public static <T> CopyOnWriteArrayList<T> toCopyOnWriteArrayList( Collection<T> collection ) {
        return (null == collection) ? (new CopyOnWriteArrayList<>()) : (new CopyOnWriteArrayList<>(collection));
    }


    /**
     * 新建一个ArrayList
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @return ArrayList对象
     */
    public static <T> ArrayList<T> toList( Collection<T> collection ) {
        return (ArrayList<T>) list(false, collection);
    }



    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>      集合元素类型
     * @param iterator {@link Iterator}
     * @return ArrayList对象
     * @since 1.0.0
     */
    public static <T> ArrayList<T> toList( Iterator<T> iterator ) {
        return (ArrayList<T>) list(false, iterator);
    }

    /**
     * 新建一个ArrayList<br>
     * 提供的参数为null时返回空{@link ArrayList}
     *
     * @param <T>         集合元素类型
     * @param enumeration {@link Enumeration}
     * @return ArrayList对象
     * @since 1.0.0
     */
    public static <T> ArrayList<T> toList( Enumeration<T> enumeration ) {
        return (ArrayList<T>) list(false, enumeration);
    }




    /**
     * 针对List排序，排序会修改原List
     *
     * @param <T>  元素类型
     * @param list 被排序的List
     * @param c    {@link Comparator}
     * @return 原list
     * @see Collections#sort(List, Comparator)
     */
    public static <T> List<T> sort( List<T> list, Comparator<? super T> c ) {
        if (ListUtil.isEmpty(list)) {
            return list;
        }
        list.sort(c);
        return list;
    }



    /**
     * 反序给定List，会在原List基础上直接修改
     *
     * @param <T>  元素类型
     * @param list 被反转的List
     * @return 反转后的List
     * @since 1.0.0
     */
    public static <T> List<T> reverse( List<T> list ) {
        Collections.reverse(list);
        return list;
    }



    /**
     * 设置或增加元素。当index小于List的长度时，替换指定位置的值，否则在尾部追加
     *
     * @param <T>     元素类型
     * @param list    List列表
     * @param index   位置
     * @param element 新元素
     * @return 原List
     * @since 1.0.0
     */
    public static <T> List<T> setOrAppend( List<T> list, int index, T element ) {
        if (index < list.size()) {
            list.set(index, element);
        } else {
            list.add(element);
        }
        return list;
    }

    /**
     * 截取集合的部分
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @return 截取后的数组，当开始位置超过最大时，返回空的List
     */
    public static <T> List<T> sub( List<T> list, int start, int end ) {
        return sub(list, start, end, 1);
    }

    /**
     * 截取集合的部分<br>
     * 此方法与{@link List#subList(int, int)} 不同在于子列表是新的副本，操作子列表不会影响原列表。
     *
     * @param <T>   集合元素类型
     * @param list  被截取的数组
     * @param start 开始位置（包含）
     * @param end   结束位置（不包含）
     * @param step  步进
     * @return 截取后的数组，当开始位置超过最大时，返回空的List
     * @since 1.0.0
     */
    public static <T> List<T> sub( List<T> list, int start, int end, int step ) {
        if (list == null) {
            return null;
        }

        if (list.isEmpty()) {
            return new ArrayList<>(0);
        }

        final int size = list.size();
        if (start < 0) {
            start += size;
        }
        if (end < 0) {
            end += size;
        }
        if (start == size) {
            return new ArrayList<>(0);
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > size) {
            if (start >= size) {
                return new ArrayList<>(0);
            }
            end = size;
        }

        if (step < 1) {
            step = 1;
        }

        final List<T> result = new ArrayList<>();
        for (int i = start; i < end; i += step) {
            result.add(list.get(i));
        }
        return result;
    }






    /**
     * 获取一个空List，这个空List不可变
     *
     * @param <T> 元素类型
     * @return 空的List
     * @see Collections#emptyList()
     * @since 1.0.0
     */
    public static <T> List<T> empty() {
        return Collections.emptyList();
    }





    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty( Collection<?> collection ) {
        return collection == null || collection.isEmpty();
    }



    /**
     * 集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty( Collection<?> collection ) {
        return false == isEmpty(collection);
    }




    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     * @return 是否为空
     */
    public static boolean isNotEmpty( Enumeration<?> enumeration ) {
        return null != enumeration && enumeration.hasMoreElements();
    }


    // ---------------------------------------------------------------------- zip



}
