package com.kaoriya.qb.ip_range;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args)
    {
        try {
            DataReader r = new DataReader(System.in);
            while (true) {
                IPv4RangeData d = r.read();
                if (d == null) {
                    break;
                }
                List<CIDR> list = CIDRUtils.toCIDR(d);
                System.out.println(d.toString());
                for (CIDR v : list) {
                    System.out.println("    " + v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
