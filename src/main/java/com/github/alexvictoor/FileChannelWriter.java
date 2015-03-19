package com.github.alexvictoor;

import com.google.common.base.Throwables;
import org.HdrHistogram.Histogram;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelWriter {


    public void writeLongs(long nbLongs) {
        try {
            File f = new File("c:/work/temp/filechannel-bench.txt");
            f.delete();

            FileChannel fc = new RandomAccessFile(f, "rw").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

            Histogram putHistogram = new Histogram(5);

            int start = 0;
            long counter = 1;
            long startWriting = System.currentTimeMillis();
            for (; ; ) {
                long beforeWrite = System.nanoTime();
                buffer.putLong(0, counter);
                fc.write(buffer);
                putHistogram.recordValue(System.nanoTime() - beforeWrite);
                counter++;
                if (counter > nbLongs)
                    break;
            }

            //bufferHistogram.outputPercentileDistribution(System.out, 1D);
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("map 90 percentile " + putHistogram.getValueAtPercentile(0.90));
            System.out.println("mean " + putHistogram.getMean());
            System.out.println(String.format("No Of Message %s , Time %s ms", nbLongs, (System.currentTimeMillis() - startWriting)));

            try {

                FileOutputStream stream = new FileOutputStream("target/filechannel.nano.bench", false);
                putHistogram.outputPercentileDistribution(new PrintStream(stream, true), 1D);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
    }


    public static void main(String[] args) throws Exception {

        FileChannelWriter writer = new FileChannelWriter();
        long HUNDREDK=100000;
        long nbLongs = HUNDREDK * 10 * 10;

        // warm up
        writer.writeLongs(nbLongs);
        writer.writeLongs(nbLongs);

    }

}