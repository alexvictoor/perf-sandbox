package com.github.alexvictoor;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.omg.CORBA.TIMEOUT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.cos;
import static java.lang.System.nanoTime;
import static java.util.Collections.synchronizedList;

public class TooManyThreads {

    private static final int NB_TREATMENTS = 100000;

    public static void main(String[] args) throws Exception {
        for (int j = 0; j < 20; j++) {

            int[] data = new int[NB_TREATMENTS];
            for (int i = 0; i < NB_TREATMENTS; i++) {
                data[i] = i;
            }

            List<Double> results = synchronizedList(new ArrayList<Double>());
            ExecutorService executorService = Executors.newFixedThreadPool(42);
            long start = nanoTime();
            for (int task : data) {
                final int value = task;
                executorService.execute(new Runnable() {
                    public void run() {

                        results.add(cos(value));
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
            for (int task : data) {
                results.add(cos(task));
            }

            end = nanoTime();
            System.out.println("single thread test took " + (end-start) + "ns");
            results.clear();
            start = nanoTime();
            IntStream stream = Arrays.stream(data);
            stream.parallel().forEach(value -> results.add(cos(value)));
            end = nanoTime();
            System.out.println("// stream test took     " + (end-start) + "ns");
            results.clear();
            stream = Arrays.stream(data);
            start = nanoTime();
            stream.forEach(
                    value -> results.add(cos(value))
            );
            end = nanoTime();
            System.out.println("stream test took        " + (end-start) + "ns");

        }
    }

}
