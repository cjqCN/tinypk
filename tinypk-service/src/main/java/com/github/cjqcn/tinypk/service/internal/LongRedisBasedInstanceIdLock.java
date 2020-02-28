package com.github.cjqcn.tinypk.service.internal;

import org.springframework.data.redis.core.RedisOperations;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author chenjinquan
 */
public class LongRedisBasedInstanceIdLock extends RedisBasedInstanceIdLock<Long> {

    private static final int DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 1024;

    private final int offset;
    private final int limit;

    public LongRedisBasedInstanceIdLock(String scope, RedisOperations<String, String> redisOperations, int offset, int limit) {
        super(scope, redisOperations);
        this.offset = offset;
        this.limit = limit;
    }

    public LongRedisBasedInstanceIdLock(RedisOperations<String, String> redisOperations) {
        this(DEFAULT_SCOPE, redisOperations, DEFAULT_OFFSET, DEFAULT_LIMIT);
    }

    @Override
    protected Long randomInstanceId() {
        return ThreadLocalRandom.current().nextLong(offset, limit);
    }


}
