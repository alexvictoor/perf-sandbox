package com.github.alexvictoor;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;

public class Quote implements Externalizable {

    String code;
    double bid;
    double ask;

    public Quote() {
    }

    public Quote(String code, double bid, double ask) {
        this.code = code;
        this.bid = bid;
        this.ask = ask;
    }



    @Override
    public String toString() {
        return "StockPrice{" +
                "code='" + code + '\'' +
                ", bid=" + bid +
                ", ask=" + ask +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(28);
        buffer.put(code.getBytes());
        buffer.putDouble(bid);
        buffer.putDouble(ask);
        buffer.flip();
        out.write(buffer.array());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        byte[] bytes = new byte[28];
        in.readFully(bytes);
        code = new String(bytes, 0, 12);
        ByteBuffer buffer = ByteBuffer.wrap(bytes, 12, 16);
        bid  = buffer.getDouble();
        ask = buffer.getDouble();
    }
}
