package com.github.cjqcn.tinypk.service;

import com.github.cjqcn.tinypk.service.util.SnowflakeIdWorker;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
public class SnowflakeIdWorkerTest {
    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1L);

    @Benchmark
    public void getAsLong() {
        snowflakeIdWorker.nextId();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // 导入要测试的类
                .include(SnowflakeIdWorkerTest.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }


}
