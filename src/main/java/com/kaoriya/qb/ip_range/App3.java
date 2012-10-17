package com.kaoriya.qb.ip_range;

import java.util.List;

import org.trie4j.doublearray.DoubleArray;
import org.trie4j.patricia.simple.PatriciaTrie;

public class App3
{

    static DoubleArray doubleArray;

    static void setup() throws Exception {
        PatriciaTrie trie = new PatriciaTrie();

        DataReader reader = new DataReader(System.in);
        try {
            while (true) {
                IPv4RangeData d = reader.read();
                if (d == null) {
                    break;
                }
                // load to trieTable.
                List<CIDR> list = CIDRUtils.toCIDR(d);
                for (CIDR v : list) {
                    String s = v.toBitsString();
                    //System.out.format("%1$s -> %2$s\n", v.toString(), s);
                    trie.insert(s);
                }
            }
        } finally {
            reader.close();
        }

        doubleArray = new DoubleArray(trie);

        System.gc();
        System.out.println("Waiting 5 seconds");
        Thread.sleep(5000);
    }

    static String toIPv4BitsString(int i) {
        IPv4 ipv4 = new IPv4(i);
        return ipv4.toBitsString();
    }

    static void benchmark() throws Exception {
        int hitCount = 0;
        for (int i = Integer.MIN_VALUE; true; ++i) {
            if (doubleArray.contains3(i)) {
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

    public static void main(String[] args) {
        try {
            setup();
            benchmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
