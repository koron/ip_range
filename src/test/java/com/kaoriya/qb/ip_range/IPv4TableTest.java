package com.kaoriya.qb.ip_range;

import org.junit.Assert;
import org.junit.Test;

public class IPv4TableTest
{

    @Test
    public void find()
    {
        String v = new String("foo");
        IPv4Table<String> table = new IPv4Table<>(
                new RangedArray<>(new RangedItem<>(10, 10,
                        new RangedArray<>(new RangedItem<>(20, 20,
                                new RangedArray<>(new RangedItem<>(30, 30,
                                        new RangedArray<>(new RangedItem<>(40, 40,
                                                v)))))))));

        Assert.assertNull(table.find(new IPv4(11, 20, 30, 40)));
        Assert.assertNull(table.find(new IPv4(10, 21, 30, 40)));
        Assert.assertNull(table.find(new IPv4(10, 20, 31, 40)));
        Assert.assertNull(table.find(new IPv4(10, 20, 30, 41)));
        Assert.assertEquals(v, table.find(new IPv4(10, 20, 30, 40)));
    }

}
