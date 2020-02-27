package com.github.cjqcn.tinypk.service.internal;

/**
 * 实例锁
 */
public interface InstanceIdLock<K> {
    LockInfo<K> lock(long lease);

    boolean renewal(long lease);

}
