package com.github.cjqcn.tinypk.service;

import com.github.cjqcn.tinypk.service.util.NumberUtil;
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
public class NumberUtilTest {
    private long t = 1;

    @Benchmark
    public void getAsLong() {
        NumberUtil.to62RadixString(t++);
    }

    @Benchmark
    public void toHexString() {
        Long.toHexString(t++);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // 导入要测试的类
                .include(NumberUtilTest.class.getSimpleName())
                .build();
        new Runner(opt).run();

    }

}
