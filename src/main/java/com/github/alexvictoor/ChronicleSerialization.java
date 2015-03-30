package com.github.alexvictoor;


import net.openhft.lang.io.ByteBufferBytes;
import net.openhft.lang.io.BytesStore;
import net.openhft.lang.io.DirectStore;
import net.openhft.lang.io.IByteBufferBytes;
import net.openhft.lang.model.Byteable;
import net.openhft.lang.model.DataValueClasses;
import net.openhft.lang.model.constraints.MaxSize;

import java.nio.ByteBuffer;

import static net.openhft.lang.model.DataValueClasses.newDirectInstance;
import static net.openhft.lang.model.DataValueClasses.newDirectReference;
import static net.openhft.lang.model.DataValueClasses.newInstance;

public class ChronicleSerialization {

    public static void main(String[] args) {
        Quote newInstance = newInstance(Quote.class);
        Quote newDirectInstance = newDirectInstance(Quote.class);
        Quote newDirectReference = newDirectReference(Quote.class);

        newInstance.setCode("baba");
        newDirectInstance.setCode("toto");

        IByteBufferBytes byteBufferBytes = ByteBufferBytes.wrap(ByteBuffer.allocate(28));

        ((Byteable) newDirectReference).bytes(byteBufferBytes, 0);

        newDirectReference.setCode("bobou");


    }

    public interface Quote {
        String getCode();
        void setCode(@MaxSize(12) String code);
        double getBid();
        void setBid(double bid);
        double getAsk();
        void setAsk(double ask);
    }
}
