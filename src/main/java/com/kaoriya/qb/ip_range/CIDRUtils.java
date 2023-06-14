package com.kaoriya.qb.ip_range;

import java.util.List;
import java.util.ArrayList;

public final class CIDRUtils {

    private static void add(List<CIDR> list, int address, int bits) {
        list.add(new CIDR(new IPv4(address), 32 - bits));
    }

    public static List<CIDR> toCIDR(IPv4RangeData data) {
        ArrayList<CIDR> list = new ArrayList<>();
        int min = data.getStart().intValue();
        int max = data.getEnd().intValue();

        int minBits = 0;
        int minMask = 1;
        while (true) {
            while ((min & minMask) == 0) {
                minMask = (minMask << 1) | 1;
                ++minBits;
            }
            if (max < (min | minMask)) {
                break;
            }
            add(list, min, minBits);
            min = (min | minMask) + 1;
        }

        int maxBits = 0;
        int maxMask = 1;
        while (true) {
            while ((max & maxMask) == maxMask) {
                maxMask = (maxMask << 1) | 1;
                ++maxBits;
            }
            max &= ~maxMask;
            if (max < min) {
                break;
            }
            add(list, max, maxBits);
            --max;
        }

        return list;
    }

}
