package com.github.alexvictoor;

import java.nio.ByteBuffer;

public class SimpleMemoryLayout {


    //public static final int NB_PAIRS = 10000;
    public static final int ARRAY_SIZE = 1000000;
    public static final int NB_ARRAY = 50;
    public static final int NB_RUN = 5;


    public static void main(String[] args) throws Exception {

        long[][] longs = new long[NB_ARRAY][ARRAY_SIZE];

        // populate array
        for (int i = 0; i < NB_ARRAY; i++) {
            for (int j = 0; j < ARRAY_SIZE; j++) {
                longs[i][j] = i+j;
            }
        }
        for (int run = 0; run < NB_RUN; run++) {

            long start = System.nanoTime();
            long result = 0;
            for (int i = 0; i < NB_ARRAY; i++)

                for (int j = 0; j < ARRAY_SIZE; j++)

                    result += longs[i][j];


            long end = System.nanoTime();
            System.out.println("memory layout friendly run through:   " + (end - start));

            start = System.nanoTime();
            result = 0;
            for (int j = 0; j < ARRAY_SIZE; j++) {
                for (int i = 0; i < NB_ARRAY; i++) {
                    result += longs[i][j];
                }
            }
            end = System.nanoTime();
            System.out.println("memory layout unfriendly run through: " + (end - start));
        }

    }
}
