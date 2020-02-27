package com.github.cjqcn.tinypk.service.util;

public final class TimeUtil {

    public static final long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static final long currentTimeSec() {
        return currentTimeMillis() / 1000L;
    }

}
