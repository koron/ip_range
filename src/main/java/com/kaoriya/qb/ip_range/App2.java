package com.kaoriya.qb.ip_range;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import darts.DoubleArrayTrie;
import org.ardverk.collection.IntegerKeyAnalyzer;
import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.Trie;
import org.trie4j.doublearray.DoubleArray;
import org.trie4j.doublearray.TailDoubleArray;

public class App2
{
    public static final int INTERVAL = 30;

    static IntRangeTable<String> rangeTable;
    static Trie<Integer, TrieData> trieTable;
    static DoubleArray doubleArray;
    static TailDoubleArray tailDoubleArray;
    static DoubleArrayTrie doubleArrayTrie;

    static void setup() throws Exception {
        rangeTable = new IntRangeTable<String>();
        trieTable = new PatriciaTrie(IntegerKeyAnalyzer.INSTANCE);
        org.trie4j.patricia.simple.PatriciaTrie trie =
            new org.trie4j.patricia.simple.PatriciaTrie();
        ArrayList<String> bitStrList = new ArrayList();

        System.out.println("Loading data");
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
                    String bitStr = v.toBitsString();
                    trie.insert(bitStr);
                    bitStrList.add(bitStr);
                }
            }
        } finally {
            reader.close();
        }

        System.out.println("Intializing pattern generator");
        rangeTable.updateNegativeList();

        System.out.println("Building da1");
        doubleArray = new DoubleArray(trie);
        System.out.println("Building da2");
        tailDoubleArray = new TailDoubleArray(trie);
        System.out.println("Building da3");
        doubleArrayTrie = new DoubleArrayTrie();
        doubleArrayTrie.build(bitStrList);

        System.out.println("Garbage collecting");
        System.gc();
        System.out.println("Waiting 5 seconds");
        Thread.sleep(5000);
    }

    interface Finder {
        String find(int n);
    }

    static class RangeTableFinder implements Finder {
        public String find(int n) {
            return rangeTable.find(IPv4Integer.valueOf(n));
        }
    }

    static class TrieTableFinder implements Finder {
        public String find(int n) {
            TrieData td = trieTable.selectNearValue(n);
            if (td == null) {
                return null;
            }
            return td.getCIDR().match(n) ? td.getData() : null;
        }
    }

    static class DoubleArrayFinder implements Finder {
        public String find(int n) {
            if (doubleArray.contains3(n)) {
                return "";
            } else {
                return null;
            }
        }
    }

    static class TailDoubleArrayFinder implements Finder {
        public String find(int n) {
            if (tailDoubleArray.contains3(n)) {
                return "";
            } else {
                return null;
            }
        }
    }

    static class DoubleArrayTrieFinder implements Finder {
        public String find(int n) {
            List<Integer> list = doubleArrayTrie.commonPrefixSearch(
                    new IPv4(n).toBitsString());
            return list.size() > 0 ? "" : null;
            /*
            if (doubleArrayTrie.contains3(n)) {
                return "";
            } else {
                return null;
            }
            */
        }
    }

    static int getNextValidInt(Random r) {
        return IPv4Integer.valueOf(rangeTable.getPositive(r));
    }

    static int getNextInvalidInt(Random r) {
        return IPv4Integer.valueOf(rangeTable.getNegative2(r));
    }

    static void negativeBench(String name, Finder finder, long seed) {
        System.out.format("%0$s (negative) running\n", name);
        Random r = new Random(seed);
        long end = System.currentTimeMillis() + INTERVAL * 1000;
        long count = 0;
        while (System.currentTimeMillis() < end) {
            int n = getNextInvalidInt(r);
            String v = finder.find(n);
            if (v != null) {
                throw new RuntimeException("found for " + n);
            }
            ++count;
        }
        double qps = (double)count / INTERVAL;
        System.out.format("%0$s result:\n", name);
        System.out.format("  qps=%0$.2f\n", qps);
    }

    static void negativeBenchRange() {
        negativeBench("range", new RangeTableFinder(), 1);
    }

    static void negativeBenchTrie() {
        negativeBench("trie", new TrieTableFinder(), 1);
    }

    static void negativeBenchDoubleArray() {
        negativeBench("double_array", new DoubleArrayFinder(), 1);
    }

    static void negativeBenchTailDoubleArray() {
        negativeBench("tail_da", new TailDoubleArrayFinder(), 1);
    }

    static void negativeBenchDoubleArrayTrie() {
        negativeBench("darts", new DoubleArrayTrieFinder(), 1);
    }

    static void positiveBench(String name, Finder finder, long seed) {
        System.out.format("%0$s (positive) running\n", name);
        Random r = new Random(seed);
        long end = System.currentTimeMillis() + INTERVAL * 1000;
        long count = 0;
        while (System.currentTimeMillis() < end) {
            int n = getNextValidInt(r);
            String v = finder.find(n);
            if (v == null) {
                throw new RuntimeException("not find for " + n);
            }
            ++count;
        }
        double qps = (double)count / INTERVAL;
        System.out.format("%0$s result:\n", name);
        System.out.format("  qps=%0$.2f\n", qps);
    }

    static void positiveBenchRange() {
        positiveBench("range", new RangeTableFinder(), 1);
    }

    static void positiveBenchTrie() {
        positiveBench("trie", new TrieTableFinder(), 1);
    }

    static void positiveBenchDoubleArray() {
        positiveBench("double_array", new DoubleArrayFinder(), 1);
    }

    static void positiveBenchTailDoubleArray() {
        positiveBench("tail_da", new TailDoubleArrayFinder(), 1);
    }

    static void positiveBenchDoubleArrayTrie() {
        positiveBench("darts", new DoubleArrayTrieFinder(), 1);
    }

    static void positiveBenchCount(
            String name,
            Finder finder,
            int countMax,
            long seed)
    {
        System.out.format("%0$s (count positive) running\n", name);
        Random r = new Random(seed);
        long count = 0;
        long start = System.currentTimeMillis();
        while (count < countMax) {
            int n = getNextValidInt(r);
            /*
            String v = finder.find(n);
            if (v == null) {
                throw new RuntimeException("not find for " + n);
            }
            */
            ++count;
        }
        long duration = System.currentTimeMillis() - start;
        System.out.format("duration=%0$.3f\n", duration / 1000f);
    }

    static void negativeBenchCount(
            String name,
            Finder finder,
            int countMax,
            long seed)
    {
        System.out.format("%0$s (count negative) running\n", name);
        Random r = new Random(seed);
        long count = 0;
        long start = System.currentTimeMillis();
        while (count < countMax) {
            int n = getNextInvalidInt(r);
            /*
            String v = finder.find(n);
            if (v != null) {
                throw new RuntimeException("found for " + n);
            }
            */
            ++count;
        }
        long duration = System.currentTimeMillis() - start;
        System.out.format("duration=%0$.3f\n", duration / 1000f);
    }

    static void benchmark() {
        benchmark1();
    }

    static void benchmark1() {
        positiveBenchRange();
        negativeBenchRange();
        positiveBenchTrie();
        negativeBenchTrie();
        positiveBenchDoubleArray();
        negativeBenchDoubleArray();
        positiveBenchTailDoubleArray();
        negativeBenchTailDoubleArray();
    }

    static void benchmark2() {
        DoubleArrayFinder finder = new DoubleArrayFinder();
        positiveBenchCount("double_array", finder, 1000000, 1);
        negativeBenchCount("double_array", finder, 1000000, 1);
    }

    static void benchmark3() {
        positiveBenchTailDoubleArray();
        negativeBenchTailDoubleArray();
    }

    static void benchmark4() {
        positiveBenchDoubleArrayTrie();
        negativeBenchDoubleArrayTrie();
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
