package com.github.alexvictoor;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
//@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class BranchBench {

    public static final int SIZE = 256 * 1024;
    private int[] data;

    @Setup
    public void init() {
        Random rand = new Random();
        data = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            data[i] = rand.nextInt(100) - 50;
        }
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int customAbs() {
        int sum = 0;
        for (int x : data) {
            if (x < 0) {
                sum -= x;
            } else {
                sum += x;
            }
        }
        return sum;
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public int mathAbs() {
        int sum = 0;
        for (int x : data) {
            sum += Math.abs(x);
        }
        return sum;
    }
}
