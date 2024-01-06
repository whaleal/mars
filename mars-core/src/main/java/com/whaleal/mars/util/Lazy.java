package com.whaleal.mars.util;


import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author wh
 */

public class Lazy< T > implements Serializable {
    private static final long serialVersionUID = 1L;

    private Supplier< T > supplier;
    /**
     * 被加载对象
     */
    private volatile T object;

    /**
     * 为给定的 {@link Supplier} 创建一个新的 {@link Lazy}，值以及它是否已解决。
     *
     * @param supplier must not be {@literal null}.
     */
    private Lazy( Supplier< T > supplier ) {
        this.supplier = supplier;
    }

    /**
     * 创建一个新的 {@link Lazy} 以延迟生成对象
     *
     * @param <T>      the type of which to produce an object of eventually.
     * @param supplier the {@link Supplier} to create the object lazily.
     * @return
     */
    public static < T > Lazy< T > of( Supplier< T > supplier ) {
        return new Lazy< T >(supplier);
    }

    /**
     * 获取一个对象，第一次调用此方法时初始化对象然后返回，之后调用此方法直接返回原对象
     */


    public T get() {

        if (object == null) {
            synchronized (this) {
                object = supplier.get();
            }
        }
        return object;
    }

}
