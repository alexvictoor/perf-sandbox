package com.github.alexvictoor;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import net.openhft.lang.model.constraints.MaxSize;
import org.HdrHistogram.Histogram;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static org.slf4j.LoggerFactory.getLogger;

public class LogbackTestWithoutSerialization {

    private Logger logger;
    private String fakeDataTxt;
    private String fakeMsgPattern;
    private String[] loggerNames
            = new String[]{
                "FILE-ALONE",
                "BIN-CHRONICLE-ALONE",
                "TXT-CHRONICLE-ALONE",
                "FILE-FILE",
                "FILE-BIN",
                "FILE-TXT",
                "BIN-BIN"
            };

    public static void main(String[] args) {

        new LogbackTestWithoutSerialization().bench();
    }

    public void bench() {
        fakeDataTxt = Strings.repeat("*", 1500);
        fakeMsgPattern = "Blabla - {} - {}";

        for (int i=0; i<2; i++)
            for (String loggerName : loggerNames) {
                runTestWithoutSerialization(loggerName, true);
            }
        for (String loggerName : loggerNames) {
            runTestWithoutSerialization(loggerName, false);
        }}

    private void runTestWithoutSerialization(String loggerName, boolean dryRun) {
        System.out.println("-------------------------- " + loggerName + " ------------------------------");
        logger = getLogger(loggerName);
        Histogram histogram = new Histogram(5);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 100000; i++) {
            long start = System.nanoTime();
            logger.info(fakeMsgPattern, i, fakeDataTxt);
            histogram.recordValue((System.nanoTime() - start)/1000);
        }
        System.out.println(loggerName + ": test took " + stopwatch);
        if (!dryRun) {
            try {
                FileOutputStream stream = new FileOutputStream(loggerName + ".micro.bench", false);
                histogram.outputPercentileDistribution(new PrintStream(stream, true), 1D);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-------------------------- end test for " + loggerName + " ------------------------------");
    }


    public interface StockPrice {
        String getCode();
        void setCode(@MaxSize(15) String code);
        double getBid();
        void setBid(double bid);
        double getAsk();
        void setAsk(double ask);
    }
}
