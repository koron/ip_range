package com.kaoriya.qb.ip_range;

import java.util.List;

import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.Trie;
import org.ardverk.collection.IntegerKeyAnalyzer;
import java.util.Random;

public class App2
{
    public static final int INTERVAL = 30;

    static IntRangeTable<String> rangeTable;
    static Trie<Integer, TrieData> trieTable;

    static void setup() throws Exception {
        rangeTable = new IntRangeTable<String>();
        trieTable = new PatriciaTrie(IntegerKeyAnalyzer.INSTANCE);

        DataReader reader = new DataReader(System.in);
        try {
            while (true) {
                IPv4RangeData d = reader.read();
                if (d == null) {
                    break;
                }
                // load to rangeTable.
                int start = IPv4Integer.valueOf(d.getStart());
                int end = IPv4Integer.valueOf(d.getEnd());
                rangeTable.add(start, end, d.getData());
                // load to trieTable.
                List<CIDR> list = CIDRUtils.toCIDR(d);
                for (CIDR v : list) {
                    TrieData td = new TrieData(v, d.getData());
                    trieTable.put(v.getAddress().intValue(), td);
                }
            }
        } finally {
            reader.close();
        }

        System.gc();
        System.out.println("Waiting 5 seconds");
        Thread.sleep(5000);
    }

    interface Finder {
        String find(int n);
    }

    static int getNextValidInt(Random r) {
        return IPv4Integer.valueOf(rangeTable.get(r).get(r));
    }

    static void positiveBench(String name, Finder finder, long seed) {
        System.out.format("%0$s running\n", name);
        Random r = new Random(seed);
        long end = System.currentTimeMillis() + INTERVAL * 1000;
        long count = 0;
        while (System.currentTimeMillis() < end) {
            int n = getNextValidInt(r);
            String v = finder.find(n);
            if (v == null) {
                throw new RuntimeException("not find" + n);
            }
            ++count;
        }
        double qps = (double)count / INTERVAL;
        System.out.format("%0$s result:\n", name);
        System.out.format("  qps=%0$.2f\n", qps);
    }

    static void positiveBenchRange() {
        positiveBench("range", new Finder() {
            public String find(int n) {
                return rangeTable.find(IPv4Integer.valueOf(n));
            }
        }, 1);
    }

    static void positiveBenchTrie() {
        positiveBench("trie", new Finder() {
            public String find(int n) {
                TrieData td = trieTable.selectNearValue(n);
                if (td == null) {
                    return null;
                }
                return td.getCIDR().match(n) ? td.getData() : null;
            }
        }, 1);
    }

    static void benchmark() {
        positiveBenchRange();
        positiveBenchTrie();
    }

    public static void main(String[] args) {
        try {
            setup();
            benchmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}