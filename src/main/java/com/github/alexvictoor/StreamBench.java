package com.github.alexvictoor;


import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StreamBench {

    public static final int SIZE = 1024 * 1024;
    private List<Integer> data;

    @Setup
    public void init() {
        Random rand = new Random();
        data = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            data.add(rand.nextInt(100) - 50);
        }
    }

    @Benchmark
    public Map<Boolean, List<Integer>> filterNegativeValuesInParallel() {
        return data.parallelStream().collect(Collectors.groupingBy(x -> x > 0));
    }

    @Benchmark
    public Map<Boolean, List<Integer>> filterNegativeValuesSingleThread() {
        return data.stream().collect(Collectors.groupingBy(x -> x > 0));
    }

}
