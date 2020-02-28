package com.github.cjqcn.tinypk.service.internal;

/**
 * 实例锁
 *
 * @author chenjinquan
 */
public interface InstanceIdLock<K extends Comparable> {

    /**
     * 获取唯一的实例锁
     *
     * @param lease 租期
     * @return
     */
    LockInfo<K> lock(long lease);

    /**
     * 续约
     *
     * @param lease 租期
     * @return
     */
    boolean renewal(long lease);


}
