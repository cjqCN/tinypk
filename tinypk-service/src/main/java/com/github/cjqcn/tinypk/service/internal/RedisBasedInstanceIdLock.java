package com.github.cjqcn.tinypk.service.internal;

import com.github.cjqcn.tinypk.service.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisOperations;

import java.util.UUID;

/**
 * 基于redis的实例锁
 *
 * @author chenjinquan
 */
public abstract class RedisBasedInstanceIdLock<K extends Comparable> implements InstanceIdLock {

    protected final static Logger LOGGER = LoggerFactory.getLogger(InstanceIdLock.class);
    protected static final String DEFAULT_SCOPE = "instance_id_lock";

    protected final String scope;
    protected final RedisOperations<String, String> redisOperations;

    protected final String lockId = UUID.randomUUID().toString();
    protected K instanceId;

    public RedisBasedInstanceIdLock(String scope, RedisOperations<String, String> redisOperations) {
        this.scope = scope;
        this.redisOperations = redisOperations;
    }

    public RedisBasedInstanceIdLock(RedisOperations<String, String> redisOperations) {
        this(DEFAULT_SCOPE, redisOperations);
    }

    @Override
    public LockInfo<K> lock(long lease) {
        final int loopNum = 1024;
        long endTimestamp = TimeUtil.currentTimeSec() + lease;
        LockInfo<K> lockInfo = new LockInfo();
        lockInfo.setTimestamp(endTimestamp);
        for (int i = 0; i < loopNum; i++) {
            K instanceId = randomInstanceId();
            if (!tryLock(instanceId, endTimestamp)) {
                lockInfo.setKey(instanceId);
                this.instanceId = instanceId;
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


    private boolean tryLock(K instanceId, long endTimestamp) {
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


    protected String key(K instanceId) {
        return scope + instanceId;
    }

    protected abstract K randomInstanceId();

}
