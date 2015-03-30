package com.github.alexvictoor;

import org.openjdk.jmh.annotations.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class ToStringSerializationBench {

    public static final int SIZE = 1000;
    private Quote[] prices;
    private ByteArrayOutputStream outputStream;

    @Setup
    public void init() throws IOException {
        Random rand = new Random();
        prices = new Quote[SIZE];
        for (int i = 0; i < SIZE; i++) {
            prices[i]
                    = new Quote(
                        "abcdefghi" + rand.nextInt(999),
                        rand.nextDouble(),
                        rand.nextDouble()
            );
        }
        outputStream = new ByteArrayOutputStream();
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public void serializePrices() throws IOException {
        for (Quote price : prices) {
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(price);
            oos.reset();
            outputStream.reset();
        }
    }

    @Benchmark
    @OperationsPerInvocation(SIZE)
    public void toStringPrices() throws IOException {
        for (Quote price : prices) {
            outputStream.write(price.toString().getBytes());
            outputStream.reset();
        }
    }
}
