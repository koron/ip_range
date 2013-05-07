package com.kaoriya.qb.ip_range;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.ardverk.collection.IntegerKeyAnalyzer;
import org.ardverk.collection.PatriciaTrie;
import org.trie4j.doublearray.DoubleArray;

public class MemApp
{

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static long usedMemory(Runtime runtime) {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static long measureMemory(String label, Runnable runnable)
        throws Exception
    {
        long used = measureMemory(runnable);
        System.out.format("measureMemory: \"%1$s\" used %2$d bytes\n",
                label, used);
        return used;
    }

    public static long measureMemory(Runnable runnable)
        throws Exception
    {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        runtime.gc();
        long memPre = usedMemory(runtime);
        if (runnable != null) {
            runnable.run();
        }
        runtime.gc();
        runtime.gc();
        long memPost = usedMemory(runtime);
        if (runnable != null && runnable instanceof Closeable) {
            closeQuietly((Closeable)runnable);
        }
        System.err.format("> pre=%1$d post=%2$d\n", memPre, memPost);
        return memPost - memPre;
    }

    public static abstract class Base implements Runnable, Closeable
    {
        protected final File file;
        protected Base(File file) {
            this.file = file;
        }
        protected abstract void add(IPv4RangeData data) throws Exception;
        protected abstract void endAdd() throws Exception;
        public void run() {
            DataReader reader = null;
            try {
                reader = new DataReader(new FileInputStream(file));
                while (true) {
                    IPv4RangeData data = reader.read();
                    if (data == null) {
                        break;
                    }
                    add(data);
                }
                endAdd();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeQuietly(reader);
            }
        }
        public abstract void close();
    }

    public static class Null extends Base
    {
        public Null(File file) {
            super(file);
        }
        @Override
        protected void add(IPv4RangeData data) throws Exception {
        }
        @Override
        protected void endAdd() throws Exception {
        }
        @Override
        public void close() {
        }
    }

    public static class BinarySearch extends Null
    {
        protected IntRangeTable<String> table;
        public BinarySearch(File file) {
            super(file);
            this.table = new IntRangeTable();
        }
        @Override
        protected void add(IPv4RangeData data) throws Exception {
            int start = IPv4Integer.valueOf(data.getStart());
            int end = IPv4Integer.valueOf(data.getEnd());
            this.table.add(start, end, null);
        }
        @Override
        public void close() {
            this.table = null;
        }
    }

    public static class BinarySearch2 extends BinarySearch
    {
        protected IntRangeArray<String> array;
        public BinarySearch2(File file) {
            super(file);
            this.array = null;
        }
        @Override
        protected void endAdd() {
            this.array = new IntRangeArray<String>(this.table, String.class);
            this.table = null;
        }
        @Override
        public void close() {
            super.close();
            this.array = null;
        }
    }

    public static class ArdverkPatricia extends Null
    {
        private PatriciaTrie<Integer, TrieData> table;
        public ArdverkPatricia(File file) {
            super(file);
            this.table = new PatriciaTrie(IntegerKeyAnalyzer.INSTANCE);
        }
        @Override
        protected void add(IPv4RangeData data) throws Exception {
            List<CIDR> list = CIDRUtils.toCIDR(data);
            for (CIDR v : list) {
                TrieData td = new TrieData(v, null);
                this.table.put(v.getAddress().intValue(), td);
            }
        }
        @Override
        public void close() {
            this.table = null;
        }
    }

    public static class Trie4JDoubleArray extends Null
    {
        private DoubleArray table;
        private org.trie4j.patricia.simple.PatriciaTrie trie;
        public Trie4JDoubleArray(File file) {
            super(file);
        }
        @Override
        public void run() {
            this.trie = new org.trie4j.patricia.simple.PatriciaTrie();
            super.run();
            this.table = new DoubleArray(trie);
            this.table.trimToSize();
            this.trie = null;
        }
        @Override
        protected void add(IPv4RangeData data) throws Exception {
            List<CIDR> list = CIDRUtils.toCIDR(data);
            for (CIDR v : list) {
                String s = v.toBitsString();
                trie.insert(s);
            }
        }
        @Override
        public void close() {
            this.table = null;
        }
    }

    public static void testNull(File file) throws Exception {
        measureMemory("null#1", new Null(file));
        measureMemory("null#2", new Null(file));
        measureMemory("null#3", new Null(file));
        measureMemory("null#4", new Null(file));
        measureMemory("null#5", new Null(file));
    }

    public static void testBinarySearch(File file) throws Exception {
        measureMemory("BinarySearch#1", new BinarySearch(file));
        measureMemory("BinarySearch#2", new BinarySearch(file));
        measureMemory("BinarySearch#3", new BinarySearch(file));
        measureMemory("BinarySearch#4", new BinarySearch(file));
        measureMemory("BinarySearch#5", new BinarySearch(file));
    }

    public static void testBinarySearch2(File file) throws Exception {
        measureMemory("BinarySearch2#1", new BinarySearch2(file));
        measureMemory("BinarySearch2#2", new BinarySearch2(file));
        measureMemory("BinarySearch2#3", new BinarySearch2(file));
        measureMemory("BinarySearch2#4", new BinarySearch2(file));
        measureMemory("BinarySearch2#5", new BinarySearch2(file));
    }

    public static void testArdverkPatricia(File file) throws Exception {
        measureMemory("ArdverkPatricia#1", new ArdverkPatricia(file));
        measureMemory("ArdverkPatricia#2", new ArdverkPatricia(file));
        measureMemory("ArdverkPatricia#3", new ArdverkPatricia(file));
        measureMemory("ArdverkPatricia#4", new ArdverkPatricia(file));
        measureMemory("ArdverkPatricia#5", new ArdverkPatricia(file));
    }

    public static void testTrie4JDoubleArray(File file) throws Exception {
        measureMemory("Trie4JDoubleArray#1", new Trie4JDoubleArray(file));
        measureMemory("Trie4JDoubleArray#2", new Trie4JDoubleArray(file));
        measureMemory("Trie4JDoubleArray#3", new Trie4JDoubleArray(file));
        measureMemory("Trie4JDoubleArray#4", new Trie4JDoubleArray(file));
        measureMemory("Trie4JDoubleArray#5", new Trie4JDoubleArray(file));
    }

    public static void main2(File file) throws Exception {
        testNull(file);
        testBinarySearch(file);
        testBinarySearch2(file);
        testArdverkPatricia(file);
        testTrie4JDoubleArray(file);
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                main2(new File(args[0]));
            } else {
                System.out.println("Require 1 arg, filename");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
