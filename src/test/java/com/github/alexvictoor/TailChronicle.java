package com.github.alexvictoor;

import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.logger.ChronicleLogEvent;
import net.openhft.chronicle.logger.tools.ChroniTool;

import java.io.IOException;
import java.io.StringWriter;

import static net.openhft.chronicle.logger.tools.ChroniTool.asString;

public class TailChronicle {

    public static void main(String[] args) throws IOException {
        String path = System.getProperty("java.io.tmpdir") +  "perf-test-bin-chronicle";
        //path = "c:/work/temp/vanilla-chronicle5";
        System.out.println(path);
        ChroniTool.process(ChronicleQueueBuilder.indexed(path).useCompressedObjectSerializer(true).build(), new ChroniTool.BinaryProcessor() {
            @Override
            public void process(ChronicleLogEvent event) {
                if (event.getThrowable() == null) {
                    StringWriter writer = new StringWriter();
                    System.out.println(asString(event, writer));
                    return;
                }
                System.out.printf("%s|%s|%s|%s|%s - <%s><%s>\n",
                        ChroniTool.DF.format(event.getTimeStamp()),
                        event.getLevel().toString(),
                        event.getThreadName(),
                        event.getLoggerName(),
                        event.getMessage(),
                        event.getArgumentArray().toString(),
                        event.getThrowable().toString()
                );

            }
        }, true, false);
    }

}
