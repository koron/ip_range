package com.kaoriya.qb.ip_range;

import org.junit.Assert;
import org.junit.Test;

public class RangedItemTest
{

    @Test
    public void value()
    {
        RangedItem item = new RangedItem(10, 12);
        Assert.assertNull(item.getValue());

        Object v1 = new Object();
        item.setValue(v1);
        Assert.assertEquals(v1, item.getValue());
    }

    @Test
    public void compare()
    {
        RangedItem item = new RangedItem(100, 102);
        Assert.assertEquals(-1, item.compare(0));
        Assert.assertEquals(-1, item.compare(98));
        Assert.assertEquals(-1, item.compare(99));
        Assert.assertEquals(0, item.compare(100));
        Assert.assertEquals(0, item.compare(101));
        Assert.assertEquals(0, item.compare(102));
        Assert.assertEquals(1, item.compare(103));
        Assert.assertEquals(1, item.compare(104));
        Assert.assertEquals(1, item.compare(199));
    }

    @Test
    public void compare_2()
    {
        RangedItem item = new RangedItem(100, 100);
        Assert.assertEquals(-1, item.compare(99));
        Assert.assertEquals(0, item.compare(100));
        Assert.assertEquals(1, item.compare(101));
    }

    @Test
    public void compareTo()
    {
        RangedItem o0 = new RangedItem(10, 19);
        RangedItem o1 = new RangedItem(10, 19);
        RangedItem o2 = new RangedItem(20, 29);
        RangedItem o3 = new RangedItem(10, 29);
        RangedItem o4 = new RangedItem(5, 15);
        RangedItem o5 = new RangedItem(15, 25);

        Assert.assertEquals( 0, o0.compareTo(o1));
        Assert.assertEquals(-1, o0.compareTo(o2));
        Assert.assertEquals( 1, o2.compareTo(o0));
        Assert.assertEquals(-1, o0.compareTo(o3));
        Assert.assertEquals( 1, o3.compareTo(o0));
        Assert.assertEquals( 1, o2.compareTo(o3));
        Assert.assertEquals(-1, o3.compareTo(o2));
        Assert.assertEquals( 1, o0.compareTo(o4));
        Assert.assertEquals(-1, o0.compareTo(o5));
    }

}
