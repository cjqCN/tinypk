package com.github.cjqcn.tinypk.service.internal;

import com.github.cjqcn.tinypk.service.util.SnowflakeIdWorker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IdWorkersManager {

    private Map<String/*scope*/, SnowflakeIdWorker> workerMap = new ConcurrentHashMap<>();

}
