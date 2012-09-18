package com.kaoriya.qb.ip_range;

import org.junit.Test;
import static org.junit.Assert.*;

public class DataReaderTest
{
    @Test
    public void parseAsData() {
        // Move to IPv4RangeData#fromString test.
        assertEquals(
                new IPv4RangeData(
                    new IPv4(127, 0, 0, 0),
                    new IPv4(127, 0, 0, 255),
                    "foobar"),
                DataReader.parseAsData(
                    "127.0.0.0\t127.0.0.255\tfoobar"));
    }
}
