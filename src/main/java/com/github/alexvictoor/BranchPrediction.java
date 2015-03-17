package com.github.alexvictoor;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.System.nanoTime;

public class BranchPrediction {


    public static void main(String[] args) throws Exception {

        long naive = 0;
        long optim = 0;

        for (int i = 0; i < 20; i++) {
            long start = nanoTime();
            for (int j = 0; j < 16384; j++) {
                if (j % 2 == 0) {
                    cos(j);
                } else {
                    sin(j);
                }
                /*if ((j & 1) == 0) {
                    cos(j);
                } else {
                    sin(j);
                }*/
            }
            long end = nanoTime();
            naive += (end-start);
            start = nanoTime();
            for (int j = 0; j < 16384; j++) {
                if ((8192 & j) == 0) {
                    cos(j*2);
                    sin(j*2+1);
                }
            }
            end = nanoTime();
            optim += (end-start);
        }

        System.out.println("Naive test: " + naive + " ns");
        System.out.println("Optim test: " + optim + " ns");

    }

}
