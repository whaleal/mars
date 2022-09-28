package com.whaleal.mars.core.domain;

import org.bson.BsonWriter;

import static com.whaleal.mars.core.aggregation.codecs.ExpressionHelper.document;

/**
 * The sort types
 * @author wh
 */
public enum Direction {
    ASC(1) {
        @Override
        public void encode( BsonWriter writer ) {
            writer.writeInt32(1);
        }
    },
    DESC(-1) {
        @Override
        public void encode( BsonWriter writer ) {
            writer.writeInt32(-1);
        }
    },
    META("$meta"){
        @Override
        public void encode( BsonWriter writer ) {
            document(writer, () -> writer.writeString("$meta", "textScore"));
        }
    };

    private final Object direction;


    Direction(Object o) {
        direction = o;
    }

    /**
     * 创建 IndexDirection
     * @param value
     * @return
     */
    public static Direction fromValue( Object value) {

        if (Double.class.equals(value.getClass())) {
            value = (double) value > 0 ? 1 : -1;
        }
        for (Direction direction : values()) {
            if (direction.direction.equals(value)) {

                return direction;
            }
        }
        throw new IllegalArgumentException("No enum value found for " + value);
    }

    /**
     * @param writer the writer to use
     */
    public abstract void encode( BsonWriter writer );
}
