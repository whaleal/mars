package com.whaleal.mars.session;

import com.mongodb.Function;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.MongoCursor;
import com.mongodb.lang.Nullable;

/**
 * @author wh
 * <p>
 * 用于客户高度自定义的游标
 * 可以将类型进行高度的自定义转换
 */
public class QueryMappingCursor< T, U > implements MongoCursor< U >, MarsCursor< U > {

    private final MongoCursor< T > proxied;
    private final Function< T, U > mapper;

    QueryMappingCursor( MongoCursor< T > proxied, Function< T, U > mapper ) {
        this.proxied = (MongoCursor) Assertions.notNull("proxied", proxied);
        this.mapper = (Function) Assertions.notNull("mapper", mapper);
    }

    public void close() {
        this.proxied.close();
    }

    public boolean hasNext() {
        return this.proxied.hasNext();
    }

    public U next() {
        return this.mapper.apply(this.proxied.next());
    }

    public int available() {
        return this.proxied.available();
    }

    @Nullable
    public U tryNext() {
        T proxiedNext = this.proxied.tryNext();
        return proxiedNext == null ? null : this.mapper.apply(proxiedNext);
    }

    public void remove() {
        this.proxied.remove();
    }

    @Nullable
    public ServerCursor getServerCursor() {
        return this.proxied.getServerCursor();
    }

    public ServerAddress getServerAddress() {
        return this.proxied.getServerAddress();
    }
}
