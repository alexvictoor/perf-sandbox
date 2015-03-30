package com.github.alexvictoor;

import com.google.common.base.Stopwatch;
import org.HdrHistogram.Histogram;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

import static org.slf4j.LoggerFactory.getLogger;

public class LogbackTestWithSerialization {

    public static final int SIZE = 1024;
    public static final int SIZE_MASK = SIZE - 1;
    private Quote[] quotes;

    private Logger logger;
    private String[] loggerNames
            = new String[]{
                "FILE-ALONE",
                "BIN-CHRONICLE-ALONE"
            };

    public static void main(String[] args) {

        new LogbackTestWithSerialization().bench();
    }

    public void bench() {

        Random rand = new Random();
        quotes = new Quote[SIZE];
        for (int i = 0; i < SIZE; i++) {
            quotes[i]
                    = new Quote(
                    "abcdefghi" + rand.nextInt(999),
                    rand.nextDouble(),
                    rand.nextDouble()
            );
        }

        for (int i=0; i<2; i++)
            for (String loggerName : loggerNames) {
                runTestWithSerialization(loggerName, true);
            }
        for (String loggerName : loggerNames) {
            runTestWithSerialization(loggerName, false);
        }}

    private void runTestWithSerialization(String loggerName, boolean dryRun) {
        System.out.println("-------------------------- " + loggerName + " ------------------------------");
        logger = getLogger(loggerName);
        Histogram histogram = new Histogram(5);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 100000; i++) {
            long start = System.nanoTime();
            logger.info("New quote received - {}", quotes[i & SIZE_MASK]);
            histogram.recordValue((System.nanoTime() - start)/1000);
        }
        System.out.println(loggerName + ": test took " + stopwatch);
        if (!dryRun) {
            try {
                FileOutputStream stream = new FileOutputStream("target/" + loggerName + ".serial.vs.tostring.micro.bench", false);
                histogram.outputPercentileDistribution(new PrintStream(stream, true), 1D);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-------------------------- end test for " + loggerName + " ------------------------------");
    }

/*
    public interface StockPrice {
        String getCode();
        void setCode(@MaxSize(15) String code);
        double getBid();
        void setBid(double bid);
        double getAsk();
        void setAsk(double ask);
    }
    */
}
