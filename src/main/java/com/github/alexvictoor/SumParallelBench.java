package com.github.alexvictoor;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.openjdk.jmh.annotations.Level.Invocation;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static org.openjdk.jmh.annotations.Scope.Benchmark;

@State(Benchmark)
@OutputTimeUnit(MILLISECONDS)
@BenchmarkMode(AverageTime)
public class SumParallelBench {

    public static final int SIZE = 10 * 1024 * 1024;
    private IntStream stream;

    @Setup(Invocation)
    public void init() {
        Random rand = new Random();
        // random stream of 0 and 1
        stream = rand.ints(SIZE, 0, 2);
    }

    @Benchmark
    public Integer sumValues() {
        return stream.sum();
    }

    @Benchmark
    public Integer sumValuesInParallel() {
        return stream.parallel().sum();
    }

}
