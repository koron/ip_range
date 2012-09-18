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
            IPv4RangeData data = IPv4RangeData.fromString(line);
            if (data != null) {
                return data;
            }
        }
    }

    public static IPv4RangeData parseAsData(String str)
    {
        return IPv4RangeData.fromString(str);
    }
}
