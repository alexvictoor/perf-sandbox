package com.github.alexvictoor;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class QuoteTest {

    @Test
    public void should_find_back_data_after_serialization_deserialization() throws IOException, ClassNotFoundException {
        // given
        Quote quote = new Quote("FR0000120271", 46.575, 46.590);
        // when
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(quote);
        oos.flush();
        oos.close();
        byte[] bytes = outputStream.toByteArray();
        // then
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        Quote newQuote = (Quote) ois.readObject();
        assertEquals(quote.code, newQuote.code);
        assertEquals(quote.bid, newQuote.bid, 0.000001);
        assertEquals(quote.ask, newQuote.ask, 0.000001);
    }

}