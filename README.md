# tinypk
轻量级分布式 primary-key 生成器

## 压测
### JMH 
`com.github.cjqcn.tinypk.service.SnowflakeIdWorkerTest`

```bash
# JMH version: 1.19
# VM version: JDK 1.8.0_212, VM 25.212-b10
# VM invoker: C:\Program Files\Java\jdk1.8.0_212\jre\bin\java.exe
# Warmup: 5 iterations, 1 s each
# Measurement: 10 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.github.cjqcn.tinypk.service.SnowflakeIdWorkerTest.getAsLong

# Run progress: 0.00% complete, ETA 00:00:15
# Fork: 1 of 1
# Warmup Iteration   1: 4093934.538 ops/s
# Warmup Iteration   2: 4098657.533 ops/s
# Warmup Iteration   3: 4096093.298 ops/s
# Warmup Iteration   4: 4096351.233 ops/s
# Warmup Iteration   5: 4100997.989 ops/s
Iteration   1: 4091916.676 ops/s
Iteration   2: 4094630.259 ops/s
Iteration   3: 4100010.809 ops/s
Iteration   4: 4091994.583 ops/s
Iteration   5: 4096246.673 ops/s
Iteration   6: 4100334.347 ops/s
Iteration   7: 4095963.582 ops/s
Iteration   8: 4087775.347 ops/s
Iteration   9: 4095845.332 ops/s
Iteration  10: 4095977.495 ops/s

Result "com.github.cjqcn.tinypk.service.SnowflakeIdWorkerTest.getAsLong":
  4095069.510 ±(99.9%) 5713.577 ops/s [Average]
  (min, avg, max) = (4087775.347, 4095069.510, 4100334.347), stdev = 3779.178
  CI (99.9%): [4089355.933, 4100783.088] (assumes normal distribution)


# Run complete. Total time: 00:00:16

Benchmark                         Mode  Cnt        Score      Error  Units
SnowflakeIdWorkerTest.getAsLong  thrpt   10  4095069.510 ± 5713.577  ops/s
```
理论值：4096000 ops/s, 测试值: 4095069.510 ± 5713.577 ops/s, 基本能跑满。

### http
````bash
$ ab -n 10000000  -c 64 -k http://127.0.0.1:8080/id/long
This is ApacheBench, Version 2.3 <$Revision: 1843412 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking 127.0.0.1 (be patient)
Completed 1000000 requests
Completed 2000000 requests
Completed 3000000 requests
Completed 4000000 requests
Completed 5000000 requests
Completed 6000000 requests
Completed 7000000 requests
Completed 8000000 requests
Completed 9000000 requests
Completed 10000000 requests
Finished 10000000 requests


Server Software:
Server Hostname:        127.0.0.1
Server Port:            8080

Document Path:          /id/long
Document Length:        18 bytes

Concurrency Level:      64
Time taken for tests:   82.232 seconds
Complete requests:      10000000
Failed requests:        0
Keep-Alive requests:    10000000
Total transferred:      1220000000 bytes
HTML transferred:       180000000 bytes
Requests per second:    121607.19 [#/sec] (mean)
Time per request:       0.526 [ms] (mean)
Time per request:       0.008 [ms] (mean, across all concurrent requests)
Transfer rate:          14488.36 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:     0    1   0.5      1      73
Waiting:        0    1   0.5      1      73
Total:          0    1   0.5      1      73

Percentage of the requests served within a certain time (ms)
  50%      1
  66%      1
  75%      1
  80%      1
  90%      1
  95%      1
  98%      1
  99%      1
 100%     73 (longest request)
````