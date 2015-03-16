package com.github.alexvictoor;

import com.google.common.base.Throwables;
import org.HdrHistogram.Histogram;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MemMapFile {


    public void writeFile(long bufferSize) {
        try {
            File f = new File("c:/work/temp/mapped-size-"+ bufferSize +".txt");
            f.delete();

            FileChannel fc = new RandomAccessFile(f, "rw").getChannel();
            MappedByteBuffer mem = fc.map(FileChannel.MapMode.READ_WRITE, 0, bufferSize);
            mem.putLong(42);
            mem.putLong(Long.MAX_VALUE);
            mem.putLong(Long.MIN_VALUE);
        } catch (IOException ex) {
            throw Throwables.propagate(ex);
        }
    }


    public static void main(String[] args) throws Exception {

        MemMapFile writer = new MemMapFile();
        writer.writeFile(800);
        writer.writeFile(4000);
        writer.writeFile(8000);


    }

}
