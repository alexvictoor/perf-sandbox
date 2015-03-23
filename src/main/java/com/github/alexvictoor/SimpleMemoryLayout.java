package com.github.alexvictoor;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class SimpleMemoryLayout {


    public static final int ARRAY_SIZE = 1000000;
    public static final int NB_ARRAY = 50;
    public long[][] longs;


    @Setup
    public void setUp() {
        longs = new long[NB_ARRAY][ARRAY_SIZE];

        // populate array
        for (int i = 0; i < NB_ARRAY; i++) {
            for (int j = 0; j < ARRAY_SIZE; j++) {
                longs[i][j] = i+j;
            }
        }
    }



    @Benchmark
    public long friendlyRunThrough() {
        long result = 0;
        for (int i = 0; i < NB_ARRAY; i++)

            for (int j = 0; j < ARRAY_SIZE; j++)

                result += longs[i][j];



        return result;
    }

    @Benchmark
    public long unfriendlyRunThrough() {
        long result = 0;
        for (int j = 0; j < ARRAY_SIZE; j++) {
            for (int i = 0; i < NB_ARRAY; i++) {
                result += longs[i][j];
            }
        }
        return result;
    }
}
