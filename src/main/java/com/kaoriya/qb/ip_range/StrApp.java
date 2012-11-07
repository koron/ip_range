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
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else if (r.nextInt(100) >= percent) {
                    continue;
                }
                matcher.add(line);
            }
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
        public abstract void add(String s) throws Exception;
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
        System.out.format("%1$s: loading #1 (ratio:%2$d loop:%3$d)\n",
                matcher.getName(), percentage, loop);
        loadKeys(hostFile, matcher, percentage);

        System.out.format("%1$s: loading #2\n", matcher.getName());
        List<String> urlList = loadLines(urlFile);

        System.out.format("%1$s: matching #2\n", matcher.getName());
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

        System.out.format("%4$s: match=%1$d unmatch=%2$d total=%3$d\n",
                countMatch, countUnmatch, countMatch + countUnmatch,
                matcher.getName());

        System.out.format("%4$s: duration: %1$d.%2$03ds (%3$d nano)\n",
                duration / 1000000000,
                (duration % 1000000000) / 1000000,
                duration,
                matcher.getName());
    }

    public static class PatriciaTrieMatcher extends Matcher {
        private final PatriciaTrie<String, Boolean> trie;
        PatriciaTrieMatcher() {
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

    public static void main(File hostFile, File urlFile)
        throws Exception
    {
        run(hostFile, urlFile, new PatriciaTrieMatcher(), 100, 100);
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
