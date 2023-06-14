package com.kaoriya.qb.ip_range;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class CIDRUtilsTest
{

    @Test
    public void toCIDR() {
        ArrayList<CIDR> expected1 = new ArrayList<>();
        expected1.add(CIDR.fromString("192.168.0.0/16"));
        assertEquals(expected1, CIDRUtils.toCIDR(IPv4RangeData.fromString(
                        "192.168.0.0\t192.168.255.255\t")));

        ArrayList<CIDR> expected2 = new ArrayList<>();
        expected2.add(CIDR.fromString("192.168.0.17/32"));
        expected2.add(CIDR.fromString("192.168.0.18/31"));
        assertEquals(expected2, CIDRUtils.toCIDR(IPv4RangeData.fromString(
                        "192.168.0.17\t192.168.0.19\t")));

        ArrayList<CIDR> expected3 = new ArrayList<>();
        expected3.add(CIDR.fromString("192.168.1.17/32"));
        expected3.add(CIDR.fromString("192.168.1.18/31"));
        expected3.add(CIDR.fromString("192.168.1.20/30"));
        expected3.add(CIDR.fromString("192.168.1.24/29"));
        expected3.add(CIDR.fromString("192.168.1.32/27"));
        expected3.add(CIDR.fromString("192.168.1.64/26"));
        expected3.add(CIDR.fromString("192.168.1.128/25"));
        expected3.add(CIDR.fromString("192.168.2.0/23"));
        expected3.add(CIDR.fromString("192.168.4.0/22"));
        expected3.add(CIDR.fromString("192.168.8.0/21"));
        expected3.add(CIDR.fromString("192.168.16.0/20"));
        expected3.add(CIDR.fromString("192.168.32.0/19"));
        expected3.add(CIDR.fromString("192.168.112.26/32"));
        expected3.add(CIDR.fromString("192.168.112.24/31"));
        expected3.add(CIDR.fromString("192.168.112.16/29"));
        expected3.add(CIDR.fromString("192.168.112.0/28"));
        expected3.add(CIDR.fromString("192.168.96.0/20"));
        expected3.add(CIDR.fromString("192.168.64.0/19"));
        assertEquals(expected3, CIDRUtils.toCIDR(IPv4RangeData.fromString(
                        "192.168.1.17\t192.168.112.26\t")));
    }

}
