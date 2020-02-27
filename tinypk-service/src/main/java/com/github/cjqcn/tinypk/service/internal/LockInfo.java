package com.github.cjqcn.tinypk.service.internal;

public class LockInfo<K> {
    K key;
    long timestamp;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LockInfo{" +
                "key=" + key +
                ", timestamp=" + timestamp +
                '}';
    }
}