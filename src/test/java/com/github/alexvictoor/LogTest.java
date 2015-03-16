package com.github.alexvictoor;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import net.openhft.lang.io.ByteBufferBytes;
import net.openhft.lang.io.IByteBufferBytes;
import net.openhft.lang.model.Byteable;
import net.openhft.lang.model.DataValueClasses;
import net.openhft.lang.model.constraints.MaxSize;
import org.HdrHistogram.Histogram;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class LogTest {

    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    /**
     * binary chronicle 300 ms
     * classic 600 ms
     * text chronicle 900 ms
     * @throws Exception
     */
    @Test
    public void testName() throws Exception {
        for (int i = 0; i < 1000; i++) {

            logger.info("hello " + System.currentTimeMillis() + " " + i);
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 100000; i++) {

            logger.info("hello " + System.currentTimeMillis() + " " + i);
        }
        System.out.println("test took " + stopwatch);
    }

    @Test
    public void testObject() throws Exception {
        for (int i = 0; i < 1000; i++) {

            logger.info("hello {}", System.currentTimeMillis());
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 100000; i++) {

            logger.info("hello {}", System.currentTimeMillis());
        }
        System.out.println("test took " + stopwatch);
    }

    /**
     * 1800ms ref classic
     * 1933ms bin
     * 2596ms text
     *
     * 1385ms text chronicle
     * 1500ms bin chronicle
     *
     *
     * messageCapacity 32768
     * 1826ms text
     *
     *
     * @throws Exception
     */
    @Test
    public void testBigMsg() throws Exception {
        String raw = Strings.repeat("*", 1500);
        for (int i = 0; i < 1000; i++) {

            logger.info(raw+i);
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 100000; i++) {
            logger.info(raw+i);
        }
        System.out.println("test took " + stopwatch);
    }


    /**
     * 1076ms txt
     * 864ms bin
     * 996ms classic
     *
     * avec {}
     * 1183ms classic
     * 773ms bin
     * 1200ms txt
     *
     * @throws Exception
     */
    @Test
    public void testMediumMsg() throws Exception {
        String raw = Strings.repeat("*", 500) + " {}";
        for (int i = 0; i < 1000; i++) {

            logger.info(raw, i);
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 100000; i++) {

            logger.info(raw, i);
        }
        System.out.println("test took " + stopwatch);
    }


    /**
     * 3sec file 15 micro  - 2 sec 10micro
     * 1.1sec bin - 8micro
     * 2.7sec text - 17micro
     *
     *
     * binary
     * test took 1,178 s
     * 99% 8063
     * mean 11463.99939
     * max 7811519
     *
     * text
     * test took 1,893 s
     * 99% 10995
     * mean 18429.53823
     * max 44658943
     *
     * classic
     * test took 2,093 s
     * 99% 10996
     * mean 20445.76146
     * max 9698431@throws Exception
     */
    @Test
    public void testLastMsg() throws Exception {
        String raw = Strings.repeat("*", 1500) + " {}";
        for (int i = 0; i < 1000; i++) {

            logger.info(raw, i);
        }
        Histogram histogram = new Histogram(5);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 100000; i++) {
            long start = System.nanoTime();
            logger.info(raw, i);
            histogram.recordValue(System.nanoTime()-start);
        }
        System.out.println("test took " + stopwatch);
        System.out.println("99% " + histogram.getValueAtPercentile(0.99));
        System.out.println("mean " + histogram.getMean());
        System.out.println("max " + histogram.getMaxValue());
    }

    /**
     * CLASSIC
     * test took 1,516 s
     * 99% 7330
     * mean 14020.35669
     * max 25210623
     *
     * text
     * test took 1,774 s
     * 99% 7330
     * mean 16121.1746
     * max 8627647
     *
     * bin
     * test took 638,0 ms
     * 99% 1466
     * mean 5362.1666
     * max 18594559
     *
     * size ser 31
     * size txt 57
     *
     * @throws Exception
     */
    @Test
    public void testBytesMarshable() throws Exception {
        StockPrice stockPrice = DataValueClasses.newDirectInstance(StockPrice.class);
        //IByteBufferBytes byteBufferBytes = ByteBufferBytes.wrap(ByteBuffer.allocate(1024));
        //((Byteable) stockPrice).bytes(byteBufferBytes, 0);
        stockPrice.setCode("gle");
        stockPrice.setBid(50.123);
        stockPrice.setAsk(50.345);
        System.out.println("price: "+stockPrice);

        System.out.println("size ser " + ((Byteable) stockPrice).maxSize());
        System.out.println("size txt " + stockPrice.toString().getBytes().length);

        //String raw = Strings.repeat("*", 1500) + " {}";
        for (int i = 0; i < 1000; i++) {
            stockPrice.setBid(50+i);
            stockPrice.setAsk(51+i);
            logger.info("input {}", stockPrice);
        }
        Histogram histogram = new Histogram(5);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 100000; i++) {
            stockPrice.setBid(50+i);
            stockPrice.setAsk(51+i);
            long start = System.nanoTime();
            logger.info("input {}", stockPrice);
            histogram.recordValue(System.nanoTime()-start);
        }
        System.out.println("test took " + stopwatch);
        System.out.println("99% " + histogram.getValueAtPercentile(0.99));
        System.out.println("mean " + histogram.getMean());
        System.out.println("max " + histogram.getMaxValue());

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
