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

        for (int i = 0; i < 20; i++) {
            long start = nanoTime();
            for (int j = 0; j < 10000; j++) {
                /*if (j % 2 == 0) {
                    cos(j);
                } else {
                    sin(j);
                }*/
                if ((j & 1) == 0) {
                    cos(j);
                } else {
                    sin(j);
                }
            }
            long end = nanoTime();
            System.out.println("Naive test: " + (end-start) + " ns");
            start = nanoTime();
            for (int j = 0; j < 5000; j++) {
                cos(j * 2);
                sin(j * 2 + 1);
            }
            end = nanoTime();
            System.out.println("Optim test: " + (end-start) + " ns");


        }

    }

}
