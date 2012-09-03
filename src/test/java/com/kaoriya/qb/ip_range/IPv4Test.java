package com.kaoriya.qb.ip_range;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

public class IPv4Test
{

    @Test
    public void getValue()
    {
        IPv4 v = new IPv4(10, 20, 30, 40);
        Assert.assertEquals(10, v.getValue(0));
        Assert.assertEquals(20, v.getValue(1));
        Assert.assertEquals(30, v.getValue(2));
        Assert.assertEquals(40, v.getValue(3));
    }

    @Test
    public void setValue()
    {
        IPv4 v = new IPv4();
        v.setValue(1, 20);
        v.setValue(3, 40);
        Assert.assertEquals( 0, v.getValue(0));
        Assert.assertEquals(20, v.getValue(1));
        Assert.assertEquals( 0, v.getValue(2));
        Assert.assertEquals(40, v.getValue(3));
    }

    @Test
    public void iterator()
    {
        IPv4 v = new IPv4(10, 20, 30, 40);
        Iterator<Integer> iter = v.iterator();

        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals(10, iter.next().intValue());
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals(20, iter.next().intValue());
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals(30, iter.next().intValue());
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals(40, iter.next().intValue());
        Assert.assertFalse(iter.hasNext());
    }

}
