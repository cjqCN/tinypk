package com.github.cjqcn.tinypk.service.internal;

import com.github.cjqcn.tinypk.service.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisOperations;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RedisBasedInstanceIdLock implements InstanceIdLock<Long> {

    private final static Logger LOGGER = LoggerFactory.getLogger(InstanceIdLock.class);

    private static final long DEFAULT_OFFSET = 0;
    private static final long DEFAULT_LIMIT = 0;
    private static final String DEFAULT_SCOPE = "instance_id_lock";

    private final long offset;
    private final long limit;
    private final String scope;
    private final RedisOperations<String, String> redisOperations;

    private final String lockId = UUID.randomUUID().toString();
    private Long instanceId;

    public RedisBasedInstanceIdLock(long offset, long limit, String scope, RedisOperations<String, String> redisOperations) {
        this.offset = offset;
        this.limit = limit;
        this.scope = scope;
        this.redisOperations = redisOperations;
    }

    public RedisBasedInstanceIdLock(String scope, RedisOperations<String, String> redisOperations) {
        this(DEFAULT_OFFSET, DEFAULT_LIMIT, scope, redisOperations);
    }

    public RedisBasedInstanceIdLock(RedisOperations<String, String> redisOperations) {
        this(DEFAULT_OFFSET, DEFAULT_LIMIT, DEFAULT_SCOPE, redisOperations);
    }

    @Override
    public LockInfo<Long> lock(long lease) {
        final int loopNum = 1024;
        long endTimestamp = TimeUtil.currentTimeSec() + lease;
        LockInfo<Long> lockInfo = new LockInfo();
        lockInfo.setTimestamp(endTimestamp);
        for (int i = 0; i < loopNum; i++) {
            long instanceId = ThreadLocalRandom.current().nextLong(limit - offset) + offset;
            if (!tryLock(instanceId, endTimestamp)) {
                lockInfo.setKey(instanceId);
                return lockInfo;
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean renewal(long lease) {
        if (instanceId == null) {
            throw new IllegalStateException("should lock first");
        }
        String key = key(instanceId);
        String content = redisOperations.opsForValue().get(key);
        if (content == null) {
            return tryLock(instanceId, TimeUtil.currentTimeSec() + lease);
        }
        String[] data = content.split("\\.");
        if (!lockId.equals(data[0])) {
            return false;
        }
        String newLockContent = lockId + "." + (TimeUtil.currentTimeSec() + lease);
        redisOperations.opsForValue().set(key, newLockContent);
        LOGGER.info("instance_id:{} renewal success", instanceId);
        return true;
    }


    private boolean tryLock(long instanceId, long endTimestamp) {
        String key = key(instanceId);
        String content = lockId + "." + endTimestamp;
        boolean lock = redisOperations.opsForValue().setIfAbsent(key, content);
        if (lock) {
            LOGGER.info("instance_id:{} lock success", instanceId);
            return true;
        }
        String verifyContent = redisOperations.opsForValue().get(key);
        if (TimeUtil.currentTimeSec() > Long.parseLong(verifyContent.split("\\.")[1]) + 3) {
            LOGGER.info("instance_id:{} del, because it's expire", instanceId);
            redisOperations.delete(key);
        }
        return false;
    }


    private String key(long instanceId) {
        return scope + instanceId;
    }

}
