package com.kaoriya.qb.ip_range;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.ardverk.collection.StringKeyAnalyzer;
import org.ardverk.collection.PatriciaTrie;
import org.trie4j.doublearray.DoubleArray;

public class StrApp
{

    private static BufferedReader newReader(File file) throws IOException
    {
        FileInputStream stream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        return new BufferedReader(reader);
    }

    private static PatriciaTrie<String, Boolean> newPatriciaTrie(
            File hostFile, int percent)
        throws Exception
    {
        Random r = new Random(0);
        PatriciaTrie<String, Boolean> trie =
            new PatriciaTrie(StringKeyAnalyzer.INSTANCE);
        BufferedReader reader = newReader(hostFile);
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else if (r.nextInt(100) >= percent) {
                    continue;
                }
                trie.put(line, Boolean.TRUE);
            }
        } finally {
            reader.close();
        }
        return trie;
    }

    private static void loadKeys(File file, Matcher matcher, int percent)
        throws Exception
    {
        Random r = new Random(0);
        BufferedReader reader = newReader(file);
        try {
            matcher.addStart();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else if (r.nextInt(100) >= percent) {
                    continue;
                }
                matcher.add(line);
            }
            matcher.addEnd();
        } finally {
            reader.close();
        }
    }

    private static List<String> loadLines(File urlFile) throws Exception {
        ArrayList<String> list = new ArrayList(100000);
        BufferedReader reader = newReader(urlFile);
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                list.add(line);
            }
        } finally {
            reader.close();
        }
        return list;
    }

    public static abstract class Matcher {
        protected final String name;
        protected Matcher(String name) {
            this.name = name;
        }
        public final String getName() { return this.name; }
        public void addStart() throws Exception {}
        public abstract void add(String s) throws Exception;
        public void addEnd() throws Exception {}
        public abstract boolean match(String s) throws Exception;
    }

    public static void run(
            File hostFile,
            File urlFile,
            Matcher matcher,
            int percentage,
            int loop)
        throws Exception
    {
        System.out.format("%1$s: running (expected_ratio:%2$d loop:%3$d)\n",
                matcher.getName(), percentage, loop);
        loadKeys(hostFile, matcher, percentage);
        List<String> urlList = loadLines(urlFile);

        int countMatch = 0;
        int countUnmatch = 0;
        long start = System.nanoTime();
        for (int i = 0; i < loop; ++i) {
            for (String item : urlList) {
                if (matcher.match(item)) {
                    ++countMatch;
                } else {
                    ++countUnmatch;
                }
            }
        }
        long end = System.nanoTime();
        long duration = end - start;

        System.out.format("%1$s: ratio:%2$.2f%% duration:%3$.6fs\n",
                matcher.getName(),
                countMatch * 100d / (countMatch + countUnmatch),
                duration / 1000000000d);
    }

    public static class PatriciaTrieMatcher extends Matcher {
        private final PatriciaTrie<String, Boolean> trie;
        public PatriciaTrieMatcher() {
            super("PatriciaTrie");
            this.trie = new PatriciaTrie(StringKeyAnalyzer.INSTANCE);
        }
        @Override
        public void add(String s) throws Exception {
            this.trie.put(s, Boolean.TRUE);
        }
        @Override
        public boolean match(String s) throws Exception {
            Map.Entry<String, Boolean> entry = this.trie.selectNear(s);
            return s.startsWith(entry.getKey());
        }
    }

    public static class DoubleArrayMatcher extends Matcher {
        private DoubleArray table = null;
        private org.trie4j.patricia.simple.PatriciaTrie trie = null;
        public DoubleArrayMatcher() {
            super("DoubleArray");
        }
        @Override
        public void addStart() throws Exception {
            this.trie = new org.trie4j.patricia.simple.PatriciaTrie();
        }
        @Override
        public void add(String s) throws Exception {
            this.trie.insert(s);
        }
        @Override
        public void addEnd() throws Exception {
            this.table = new DoubleArray(trie);
            this.trie = null;
        }
        @Override
        public boolean match(String s) throws Exception {
            return this.table.contains2(s);
        }
    }

    public static void main(File hostFile, File urlFile)
        throws Exception
    {
        run(hostFile, urlFile, new PatriciaTrieMatcher(), 100, 100);
        run(hostFile, urlFile, new DoubleArrayMatcher(), 100, 100);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Require 2 args, filenames");
            return;
        }
        try {
            main(new File(args[0]), new File(args[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
