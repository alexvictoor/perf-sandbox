package com.github.alexvictoor;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import net.openhft.lang.model.constraints.MaxSize;
import org.HdrHistogram.Histogram;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class LogbackTest {

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

    @Before
    public void setUp() throws Exception {

        fakeDataTxt = Strings.repeat("*", 1500);
        fakeMsgPattern = "Blabla - {} - {}";
    }

    @Test
    public void testWithoutSerialization() throws Exception {
        for (int i=0; i<2; i++)
        for (String loggerName : loggerNames) {
            runTestWithoutSerialization(loggerName);
        }
    }

    private void runTestWithoutSerialization(String loggerName) {
        System.out.println("-------------------------- " + loggerName + " ------------------------------");
        logger = getLogger(loggerName);
        Histogram histogram = new Histogram(5);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 100000; i++) {
            long start = System.nanoTime();
            logger.info(fakeMsgPattern, i, fakeDataTxt);
            histogram.recordValue(System.nanoTime() - start);
        }
        System.out.println(loggerName + ": test took " + stopwatch);
        histogram.outputPercentileDistribution(System.out, 1D);
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
