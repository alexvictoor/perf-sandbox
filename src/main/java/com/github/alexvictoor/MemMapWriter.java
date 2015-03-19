package com.github.alexvictoor;

import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import org.HdrHistogram.Histogram;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class MemMapWriter {


    public void writeLongs(long nbLongs, long bufferSize) {
        try {
            File f = new File("c:/work/temp/mapped-buff-"+ bufferSize +".txt");
            f.delete();

            FileChannel fc = new RandomAccessFile(f, "rw").getChannel();

            //long bufferSize= 8*1000;
            Histogram bufferHistogram = new Histogram(5);
            Histogram putHistogram = new Histogram(5);
            long beforeMap = System.nanoTime();
            MappedByteBuffer mem = fc.map(FileChannel.MapMode.READ_WRITE, 0, bufferSize);
            long afterMap = System.nanoTime();
            bufferHistogram.recordValue(afterMap-beforeMap);

            int start = 0;
            long counter = 1;
            long startWriting = System.currentTimeMillis();
            for (; ; ) {
                if (!mem.hasRemaining()) {
                    start += mem.position();
                    beforeMap = System.nanoTime();
                    mem = fc.map(FileChannel.MapMode.READ_WRITE, start, bufferSize);
                    afterMap = System.nanoTime();
                    bufferHistogram.recordValue(afterMap-beforeMap);

                }
                long beforePut = System.nanoTime();
                mem.putLong(counter);
                putHistogram.recordValue(System.nanoTime() - beforePut);
                counter++;
                if (counter > nbLongs)
                    break;
            }

            //bufferHistogram.outputPercentileDistribution(System.out, 1D);
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("map 90 percentile " + bufferHistogram.getValueAtPercentile(0.90));
            System.out.println("mean " + bufferHistogram.getMean());
            System.out.println("Buffer size (byte): " + bufferSize);
            System.out.println(String.format("No Of Message %s , Time %s ms", nbLongs, (System.currentTimeMillis() - startWriting)));

            try {
                FileOutputStream stream = new FileOutputStream("target/mmapbuffer." + bufferSize + ".nano.bench", false);
                bufferHistogram.outputPercentileDistribution(new PrintStream(stream, true), 1D);
                stream = new FileOutputStream("target/mmapput." + bufferSize + ".nano.bench", false);
                putHistogram.outputPercentileDistribution(new PrintStream(stream, true), 1D);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
    }


    public static void main(String[] args) throws Exception {

        MemMapWriter writer = new MemMapWriter();
        long HUNDREDK=100000;
        long nbLongs = HUNDREDK * 10 * 10;

        // warm up
        writer.writeLongs(nbLongs, 20000);

        writer.writeLongs(nbLongs, 512);
        writer.writeLongs(nbLongs, 1024);
        writer.writeLongs(nbLongs, 2048);
        writer.writeLongs(nbLongs, 4096);
        writer.writeLongs(nbLongs, 4096 * 4);
        writer.writeLongs(nbLongs, 4096 * 5);
        writer.writeLongs(nbLongs, 4096 * 8);
        writer.writeLongs(nbLongs, 4096 * 16);
        writer.writeLongs(nbLongs, 1024 * 1024 * 64);
        writer.writeLongs(nbLongs, 1024 * 1024 * 100);
        writer.writeLongs(nbLongs, 1024 * 1024 * 120);

    }

}
