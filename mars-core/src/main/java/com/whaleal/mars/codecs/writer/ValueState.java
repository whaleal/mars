package com.whaleal.mars.codecs.writer;

import com.mongodb.lang.Nullable;

/**
 * @author wh
 */
abstract class ValueState<T>  extends WriteState{
    ValueState(DocumentWriter writer, WriteState previous) {
        super(writer, previous);
    }

    ValueState() {
    }

    @Nullable
    public abstract T value();
}
