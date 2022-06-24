package com.whaleal.mars.codecs;

import java.time.ZoneId;

import static java.time.ZoneId.of;
import static java.time.ZoneId.systemDefault;

/**
 * This enum is used to determine how JSR 310 dates and times are stored in the database.
 *
 * */
public enum DateStorage {
    UTC {
        @Override
        public ZoneId getZone() {
            return of("UTC");
        }
    },

    SYSTEM_DEFAULT {
        @Override
        public ZoneId getZone() {
            return systemDefault();
        }
    };

    /**
     * @return the ZoneId for this storage type
     */
    public abstract ZoneId getZone();
}
