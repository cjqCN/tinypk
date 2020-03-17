package com.github.cjqcn.tinypk.service;

import com.github.cjqcn.tinypk.service.internal.InstanceIdLock;
import com.github.cjqcn.tinypk.service.internal.LockInfo;
import com.github.cjqcn.tinypk.service.internal.SingleInstanceIdLock;
import com.github.cjqcn.tinypk.service.util.NumberUtil;
import com.github.cjqcn.tinypk.service.util.SnowflakeIdWorker;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class PKServiceImpl implements PKService, InitializingBean {
    public static PKService instance;

    private static final long ONE_DAY_SEC = 3600 * 24L;
    private static final long LEASE = ONE_DAY_SEC;
    private static ScheduledExecutorService freshExecutor = new ScheduledThreadPoolExecutor(1);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private InstanceIdLock<Long> instanceIdLock = new SingleInstanceIdLock();
    private volatile SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public long getAsLong() {
        return snowflakeIdWorker.nextId();
    }

    @Override
    public String getAsShortString() {
        long id = getAsLong();
        return NumberUtil.to62RadixString(id);
    }

    @Override
    public long getAsLong(String scope) {
        return getAsLong();
    }

    @Override
    public String getAsShortString(String scope) {
        return getAsShortString();
    }

    @Override
    public void afterPropertiesSet() {
        initIdWorker();
        freshExecutor.schedule(() -> {
            if (!instanceIdLock.renewal(LEASE)) {
                initIdWorker();
            }
        }, 18, TimeUnit.HOURS);
        instance = this;
    }

    private void initIdWorker() {
        LockInfo<Long> lockInfo = instanceIdLock.lock(LEASE);
        snowflakeIdWorker = new SnowflakeIdWorker(lockInfo.getKey());
    }
}
