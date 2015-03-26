package com.github.alexvictoor;

import com.google.common.base.Throwables;
import org.HdrHistogram.Histogram;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelWriter {


    public void writeLongs(long nbLongs, int batchSize) {
        try {
            File f = new File("target/filechannel-bench-" + nbLongs + "-" + batchSize + "-.txt");
            f.delete();

            FileChannel fc = new RandomAccessFile(f, "rw").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

            Histogram putHistogram = new Histogram(5);

            long counter = 1;
            long startWriting = System.currentTimeMillis();
            for (; ; ) {
                long beforeWrite = System.nanoTime();
                for (int i = 0; i < batchSize; i++) {
                    buffer.putLong(counter);
                    buffer.flip();
                    fc.write(buffer);
                    buffer.clear();
                    counter++;
                }
                putHistogram.recordValue((System.nanoTime() - beforeWrite)/1000);
                if (counter > nbLongs)
                    break;
            }

            System.out.println("---------------------------------------------------------------------------");
            System.out.println("map 90 percentile " + putHistogram.getValueAtPercentile(0.90));
            System.out.println("mean " + putHistogram.getMean());
            System.out.println(String.format("No Of Message %s , Time %s ms", nbLongs, (System.currentTimeMillis() - startWriting)));

            try {

                FileOutputStream stream = new FileOutputStream("target/filechannel.nano." + nbLongs + "." + batchSize + "-bench", false);
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
        long nbLongs = 10 * 1024 * 1024;


        // warm up
        writer.writeLongs(nbLongs, 2048);
        writer.writeLongs(nbLongs, 2048);
        writer.writeLongs(nbLongs, 2048);
        writer.writeLongs(nbLongs, 2048);

    }

}
