package com.kaoriya.qb.ip_range;

import java.io.IOException;
import java.util.List;

import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.Trie;
import org.ardverk.collection.IntegerKeyAnalyzer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void read(DataReader r, Trie<Integer,TrieData> trie)
        throws IOException
    {
        while (true) {
            IPv4RangeData d = r.read();
            if (d == null) {
                break;
            }
            List<CIDR> list = CIDRUtils.toCIDR(d);
            for (CIDR v : list) {
                TrieData td = new TrieData(v, d.getData());
                trie.put(v.getAddress().intValue(), td);
            }
        }
    }

    public static String find(Trie<Integer,TrieData> trie, int value)
    {
        TrieData td = trie.selectValue(value);
        if (td == null) {
            return null;
        }
        return td.getCIDR().match(value) ? td.getData() : null;
    }

    public static void func1() throws IOException
    {
        Trie<Integer,TrieData> trie = new PatriciaTrie<>(
                IntegerKeyAnalyzer.INSTANCE);

        DataReader reader = new DataReader(System.in);
        try {
            read(reader, trie);
        } finally {
            reader.close();
        }

        int hitCount = 0;
        for (int i = Integer.MIN_VALUE; true; ++i) {
            String data = find(trie, i);
            if (data != null) {
                ++hitCount;
            }
            if ((i & 0xffffff) == 0) {
                System.out.println("curr=" + ((i >> 24) & 0xff));
            }
            if (i == Integer.MAX_VALUE) {
                break;
            }
        }
        System.out.println("hitCount=" + hitCount);
    }

    public static String find1a(Trie<Integer,TrieData> trie, int value)
    {
        TrieData td = trie.selectNearValue(value);
        if (td == null) {
            return null;
        }
        return td.getCIDR().match(value) ? td.getData() : null;
    }

    public static void func1a() throws IOException
    {
        Trie<Integer,TrieData> trie = new PatriciaTrie<>(
                IntegerKeyAnalyzer.INSTANCE);

        DataReader reader = new DataReader(System.in);
        try {
            read(reader, trie);
        } finally {
            reader.close();
        }

        int hitCount = 0;
        for (int i = Integer.MIN_VALUE; true; ++i) {
            String data = find1a(trie, i);
            if (data != null) {
                ++hitCount;
            }
            if ((i & 0xffffff) == 0) {
                System.out.println("curr=" + ((i >> 24) & 0xff));
            }
            if (i == Integer.MAX_VALUE) {
                break;
            }
        }
        System.out.println("hitCount=" + hitCount);
    }

    public static void read(DataReader r, IntRangeTable<String> table)
        throws IOException
    {
        while (true) {
            IPv4RangeData d = r.read();
            if (d == null) {
                break;
            }
            int start = IPv4Integer.valueOf(d.getStart());
            int end = IPv4Integer.valueOf(d.getEnd());
            table.add(start, end, d.getData());
        }
    }

    public static String find(IntRangeTable<String> table, int value)
    {
        return table.find(IPv4Integer.valueOf(value));
    }

    public static void func2() throws IOException
    {
        IntRangeTable<String> table = new IntRangeTable<>();

        DataReader reader = new DataReader(System.in);
        try {
            read(reader, table);
        } finally {
            reader.close();
        }

        int hitCount = 0;
        for (int i = Integer.MIN_VALUE; true; ++i) {
            String data = find(table, i);
            if (data != null) {
                ++hitCount;
            }
            if ((i & 0xffffff) == 0) {
                System.out.println("curr=" + ((i >> 24) & 0xff));
            }
            if (i == Integer.MAX_VALUE) {
                break;
            }
        }
        System.out.println("hitCount=" + hitCount);
    }

    public static void read(
            DataReader r,
            Trie<Integer,TrieData> trie,
            IntRangeTable<String> table)
        throws IOException
    {
        while (true) {
            IPv4RangeData d = r.read();
            if (d == null) {
                break;
            }

            List<CIDR> list = CIDRUtils.toCIDR(d);
            for (CIDR v : list) {
                TrieData td = new TrieData(v, d.getData());
                trie.put(v.getAddress().intValue(), td);
            }

            int start = IPv4Integer.valueOf(d.getStart());
            int end = IPv4Integer.valueOf(d.getEnd());
            table.add(start, end, d.getData());
        }
    }

    public static String find(
            Trie<Integer,TrieData> trie,
            IntRangeTable<String> table,
            int value)
    {
        String v1 = find(trie, value);
        String v2 = find(table, value);
        if (v1 != v2) {
            throw new RuntimeException("not match: value=" + value + " trie="
                    + v1 + " table=" + v2);
        }
        return v1;
    }


    public static void func3() throws IOException
    {
        IntRangeTable<String> table = new IntRangeTable<>();
        Trie<Integer,TrieData> trie = new PatriciaTrie<>(IntegerKeyAnalyzer.INSTANCE);

        DataReader reader = new DataReader(System.in);
        try {
            read(reader, trie, table);
        } finally {
            reader.close();
        }

        int hitCount = 0;
        for (int i = Integer.MIN_VALUE; true; ++i) {
            String data = find(trie, table, i);
            if (data != null) {
                ++hitCount;
            }
            if ((i & 0xffffff) == 0) {
                System.out.println("curr=" + ((i >> 24) & 0xff));
            }
            if (i == Integer.MAX_VALUE) {
                break;
            }
        }
        System.out.println("hitCount=" + hitCount);
    }

    public static void main(String[] args)
    {
        try {
            Thread.sleep(10000);
            long start = System.currentTimeMillis();
            func2();
            long time = System.currentTimeMillis() - start;
            System.out.format("%1$d secs\n", time / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
