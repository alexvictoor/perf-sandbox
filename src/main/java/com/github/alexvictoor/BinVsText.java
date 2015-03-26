package com.github.alexvictoor;


import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

public class BinVsText {

    public static void main(String[] args) throws Exception {

        Date now = new Date();
        byte[] nowStringBytes = "Mar 18 00:22:47,618".getBytes();
        long ms = now.getTime();
        byte[] nowBinBytes = longToBytes(ms);
        System.out.println(now+ " "+ Charset.defaultCharset());
        System.out.println("binary " + toHexString(nowBinBytes) + " " + nowBinBytes.length );
        System.out.println("string " + toHexString(nowStringBytes) + " " + nowStringBytes.length );

        byte[] priceStringBytes = "Isin=FR0000120271 Bid=46.575 Ask=46.590".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.put("FR0000120271".getBytes());
        buffer.putDouble(46.575);
        buffer.putDouble(46.590);
        buffer.flip();
        byte[] priceBinBytes = buffer.array();
        System.out.println("binary " + toHexString(priceBinBytes) + " " + priceBinBytes.length );
        System.out.println("string " + toHexString(priceStringBytes) + " " + priceStringBytes.length );

    }

    public static String toHexString(byte[] bytes) {
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 3];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j*3] = hexArray[v/16];
            hexChars[j*3 + 1] = hexArray[v%16];
            hexChars[j*3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
}
