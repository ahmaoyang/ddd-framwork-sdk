/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.testing;

import org.junit.Assert;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

/**
 * 你需要配置{@code log4j2.xml}，以便把测试的日志输出到{@code logs/app.log}
 */
public class LogAssert {
    private static final String logFile = "logs/app.log";
    private static RandomAccessFile reader = null;

    private LogAssert() {
    }


    public static synchronized void assertNotContains(String... events) throws IOException {
        try {
            assertContains(events);
            Assert.fail();
        } catch (AssertionError ignored) {
            // 就该出现该异常
        }
    }

    public static synchronized void assertContains(String... events) throws IOException {
        Set<String> expectedEvents = new HashSet<>(events.length);
        for (String event : events) {
            expectedEvents.add(event);
        }
        Set<String> foundEvents = new HashSet<>(events.length);

        if (reader == null) {
            reader = new RandomAccessFile(logFile, "r");
        }
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                // EOF
                break;
            }

            line = new String(line.getBytes("ISO-8859-1"), "utf-8");
            for (String event : events) {
                if (line.contains(event)) {
                    foundEvents.add(event);
                }
            }
        }

        // 差集
        expectedEvents.removeAll(foundEvents);
        if (!expectedEvents.isEmpty()) {
            Assert.fail(String.format("%d events not found: %s", expectedEvents.size(), expectedEvents.toString()));
        }
    }
}
