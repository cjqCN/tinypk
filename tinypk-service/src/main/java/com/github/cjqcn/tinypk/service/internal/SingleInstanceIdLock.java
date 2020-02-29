package com.github.cjqcn.tinypk.service.internal;

import com.github.cjqcn.tinypk.service.util.TimeUtil;

public class SingleInstanceIdLock implements InstanceIdLock<Long> {
    @Override
    public LockInfo<Long> lock(long lease) {
        LockInfo<Long> longLockInfo = new LockInfo<>();
        longLockInfo.setKey(1L);
        longLockInfo.setTimestamp(TimeUtil.currentTimeSec() + lease);
        return longLockInfo;
    }

    @Override
    public boolean renewal(long lease) {
        return true;
    }
}
