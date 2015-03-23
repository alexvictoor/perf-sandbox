package com.github.alexvictoor;

import com.google.common.base.Throwables;
import org.HdrHistogram.Histogram;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelWriterBlock {


    public void writeLongs(long nbLongs, int batchSize) {
        try {
            File f = new File("c:/work/temp/filechannel-block-bench-" + nbLongs + "-" + batchSize + "-.txt");
            f.delete();

            FileChannel fc = new RandomAccessFile(f, "rw").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * batchSize);

            Histogram putHistogram = new Histogram(5);

            long counter = 1;
            long startWriting = System.currentTimeMillis();
            for (; ; ) {
                long beforeWrite = System.nanoTime();
                for (int i = 0; i < batchSize; i++) {
                    buffer.putLong(counter);
                    counter++;
                }
                fc.write(buffer);
                buffer.clear();
                putHistogram.recordValue(System.nanoTime() - beforeWrite);
                if (counter > nbLongs)
                    break;
            }

            System.out.println("---------------------------------------------------------------------------");
            System.out.println("map 90 percentile " + putHistogram.getValueAtPercentile(0.90));
            System.out.println("mean " + putHistogram.getMean());
            System.out.println(String.format("No Of Message %s , Time %s ms", nbLongs, (System.currentTimeMillis() - startWriting)));

            try {

                FileOutputStream stream = new FileOutputStream("target/filechannelblock.nano." + nbLongs + "." + batchSize + "-bench", false);
                putHistogram.outputPercentileDistribution(new PrintStream(stream, true), 1D);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
    }


    public static void main(String[] args) throws Exception {

        FileChannelWriterBlock writer = new FileChannelWriterBlock();
        long HUNDREDK=100000;
        long nbLongs = HUNDREDK * 10 * 10;

        // warm up
        writer.writeLongs(nbLongs, 2048);
        writer.writeLongs(nbLongs, 2048);
        writer.writeLongs(nbLongs, 2048);
        writer.writeLongs(nbLongs, 2048);

    }

}
