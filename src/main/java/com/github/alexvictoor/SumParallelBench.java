package com.github.alexvictoor;


import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class SumParallelBench {

    public static final int SIZE = 10 * 1024 * 1024;
    private int[] data;

    @Setup
    public void init() {
        Random rand = new Random();
        data = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            data[i] = rand.nextInt();
        }
    }

    @Benchmark
    public Integer sumValues() {
        IntStream stream = stream(data);
        return stream.sum();
    }

    @Benchmark
    public Integer sumValuesInParallel() {
        IntStream stream = stream(data);
        return stream.parallel().sum();
    }

}
