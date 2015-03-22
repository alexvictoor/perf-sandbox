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


    public void writeLongs(long nbLongs, long bufferSize, long batchSize) {
        try {
            File f = new File("c:/work/temp/mapped-buff-"+ bufferSize +"-"+ batchSize +".txt");
            f.delete();

            FileChannel fc = new RandomAccessFile(f, "rw").getChannel();
            Histogram histogram = new Histogram(5);
            MappedByteBuffer mem = null; //fc.map(FileChannel.MapMode.READ_WRITE, 0, bufferSize);

            int start = 0;
            long counter = 1;
            long startWriting = System.currentTimeMillis();
            for (; ; ) {
                long beforeBatch = System.nanoTime();
                for (int i = 0; i < batchSize; i++) {
                    if (mem == null) {
                        mem = fc.map(FileChannel.MapMode.READ_WRITE, start, bufferSize);
                    }
                    if (!mem.hasRemaining()) {
                        start += mem.position();
                        mem = fc.map(FileChannel.MapMode.READ_WRITE, start, bufferSize);
                    }
                    mem.putLong(counter);
                    counter++;
                }
                histogram.recordValue(System.nanoTime() - beforeBatch);
                if (counter > nbLongs)
                    break;
            }

            System.out.println("---------------------------------------------------------------------------");
            System.out.println("map 90 percentile " + histogram.getValueAtPercentile(0.90));
            System.out.println("mean " + histogram.getMean());
            System.out.println("Buffer size (byte): " + bufferSize);
            System.out.println(String.format("No Of Message %s , Time %s ms", nbLongs, (System.currentTimeMillis() - startWriting)));

            try {
                FileOutputStream stream = new FileOutputStream("target/mmapbuffer." + bufferSize + "." + batchSize + ".nano.bench", false);
                histogram.outputPercentileDistribution(new PrintStream(stream, true), 1D);
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
        writer.writeLongs(nbLongs, 20000, 2048);

        writer.writeLongs(nbLongs, 512, 2048);
        writer.writeLongs(nbLongs, 1024, 2048);
        writer.writeLongs(nbLongs, 2048, 2048);
        writer.writeLongs(nbLongs, 4096, 2048);
        writer.writeLongs(nbLongs, 4096 * 4, 2048);
        writer.writeLongs(nbLongs, 4096 * 5, 2048);
        writer.writeLongs(nbLongs, 4096 * 8, 2048);
        writer.writeLongs(nbLongs, 4096 * 16, 2048);
        writer.writeLongs(nbLongs, 1024 * 1024 * 64, 2048);
        writer.writeLongs(nbLongs, 1024 * 1024 * 100, 2048);
        writer.writeLongs(nbLongs, 1024 * 1024 * 120, 2048);

    }

}
