package com.kaoriya.qb.ip_range;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public final class DataReader implements Closeable
{

    private LineNumberReader reader = null;

    public DataReader(InputStream input) throws IOException
    {
        this.reader = new LineNumberReader(new InputStreamReader(
                new BufferedInputStream(input), "utf-8"));
    }

    public void close() throws IOException
    {
        if (this.reader != null) {
            try {
                this.reader.close();
            } finally {
                this.reader = null;
            }
        }
    }

    public IPv4RangeData read() throws IOException {
        if (this.reader == null) {
            return null;
        }
        while (true) {
            String line = this.reader.readLine();
            if (line == null) {
                close();
                return null;
            }
            IPv4RangeData data = parseAsData(line);
            if (data != null) {
                return parseAsData(line);
            }
        }
    }

    public static IPv4RangeData parseAsData(String str)
    {
        String[] values = str.split("\t", 3);
        if (values.length < 3) {
            return null;
        }
        IPv4 start = IPv4.fromString(values[0]);
        IPv4 end = IPv4.fromString(values[1]);
        if (start == null || end == null) {
            return null;
        }
        return new IPv4RangeData(start, end, values[2]);
    }
}
