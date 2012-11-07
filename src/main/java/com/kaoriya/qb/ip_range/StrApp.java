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

    public static void main(File hostFile, File urlFile)
        throws Exception
    {
        PatriciaTrie<String, Boolean> trie100 =
            newPatriciaTrie(hostFile, 100);

        List<String> urlList = loadLines(urlFile);

        int countMatch = 0;
        int countUnmatch = 0;
        long start = System.nanoTime();
        for (int i = 0; i < 1000; ++i) {
            for (String item : urlList) {
                Map.Entry<String, Boolean> entry = trie100.selectNear(item);
                if (!item.startsWith(entry.getKey())) {
                    ++countUnmatch;
                } else {
                    ++countMatch;
                }
            }
        }
        long end = System.nanoTime();
        long duration = end - start;

        System.out.format("  match=%1$d unmatch=%2$d total=%3$d\n",
                countMatch, countUnmatch, countMatch + countUnmatch);

        System.out.format("  duration: %1$d.%2$03ds (%3$d nano)\n",
                duration / 1000000000,
                (duration % 1000000000) / 1000000,
                duration);
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
