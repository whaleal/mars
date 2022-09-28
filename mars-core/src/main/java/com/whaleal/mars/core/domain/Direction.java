package com.whaleal.mars.core.domain;

import org.bson.BsonWriter;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;

/**
 * The sort types
 * @author wh
 */
public enum Direction {
    ASC {
        @Override
        public void encode( BsonWriter writer ) {
            writer.writeInt32(1);
        }
    },
    DESC {
        @Override
        public void encode( BsonWriter writer ) {
            writer.writeInt32(-1);
        }
    },
    META {
        @Override
        public void encode( BsonWriter writer ) {
            document(writer, () -> writer.writeString("$meta", "textScore"));
        }
    };

    /**
     * @param writer the writer to use
     */
    public abstract void encode( BsonWriter writer );
}
