package com.github.alexvictoor;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.omg.CORBA.TIMEOUT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.cos;
import static java.lang.System.nanoTime;
import static java.util.Collections.synchronizedList;

public class TooManyThreads {

    private static final int NB_TREATMENTS = 100000;

    public static void main(String[] args) throws Exception {
        for (int j = 0; j < 20; j++) {


            final List<Double> results = synchronizedList(new ArrayList<Double>());
            ExecutorService executorService = Executors.newFixedThreadPool(42);
            long start = nanoTime();
            for (int i = 0; i < NB_TREATMENTS; i++) {
                final int currentValue = i;
                executorService.execute(new Runnable() {
                    public void run() {

                        results.add(cos(currentValue));
                    }
                });

            }
            executorService.shutdown();
            executorService.awaitTermination(20, TimeUnit.SECONDS);
            long end = nanoTime();
            System.out.println("threadpool test took    " + (end-start) + "ns");
            executorService.shutdownNow();
            results.clear();

            start = nanoTime();
            for (int i = 0; i < NB_TREATMENTS; i++) {

                results.add(cos(i));
            }

            end = nanoTime();
            System.out.println("single thread test took " + (end-start) + "ns");

        }
    }

}
