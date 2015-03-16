package com.github.alexvictoor;

import java.nio.ByteBuffer;

public class MemoryLayout {


    //public static final int NB_PAIRS = 10000;
    public static final int NB_PAIRS = 1000000;
    public static final int NB_RUN = 10;

    public static void main(String[] args) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * 2 * NB_PAIRS);
        Long[] longObjects = new Long[2 * NB_PAIRS];
        long[] longs = new long[2 * NB_PAIRS];
        for (int i = 0; i < NB_PAIRS; i++) {
            Long firstLong = System.nanoTime();
            buffer.putLong(firstLong);
            longObjects[i*2] = firstLong;
            longs[i*2] = firstLong;
            Long secondLong = System.nanoTime() + i;
            buffer.putLong(secondLong);
            longObjects[i*2+1] = secondLong;
            longs[i*2+1] = secondLong;
        }


        long fakeResult = 0L;
        long start = System.nanoTime();
        for (int i = 0; i < NB_RUN; i++) {
            buffer.flip();
            for (int j = 0; j < NB_PAIRS; j++) {
                fakeResult = buffer.getLong() + buffer.getLong();
            }

        }
        System.out.println(NB_PAIRS*NB_RUN + " pairs of long added in " + (System.nanoTime() - start) + "ns");
        start = System.nanoTime();
        for (int i = 0; i < NB_RUN; i++) {
            for (int j = 0; j < NB_PAIRS; j++) {
                fakeResult = longObjects[j*2] + longObjects[j*2+1];
            }
        }
        System.out.println(NB_PAIRS*NB_RUN + " pairs of Long added in " + (System.nanoTime() - start) + "ns");
        start = System.nanoTime();
        for (int i = 0; i < NB_RUN; i++) {
            for (int j = 0; j < NB_PAIRS; j++) {
                fakeResult = longs[j*2] + longs[j*2+1];
            }
        }
        System.out.println(NB_PAIRS*NB_RUN + " pairs of long added in " + (System.nanoTime() - start) + "ns");
    }
}
