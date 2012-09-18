package com.kaoriya.qb.ip_range;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;

public class RangedArrayTest
{

    @Test
    public void find()
    {
        RangedItem o0 = new RangedItem(0, 9);
        RangedItem o1 = new RangedItem(10, 19);
        RangedItem o2 = new RangedItem(20, 29);
        RangedItem o3 = new RangedItem(31, 39);
        RangedItem o5 = new RangedItem(50, 59);
        RangedArray array = new RangedArray(o0, o1, o2, o3, o5);

        Assert.assertEquals(o0, array.find(5));
        Assert.assertEquals(o1, array.find(15));
        Assert.assertEquals(o2, array.find(25));
        Assert.assertEquals(o3, array.find(35));
        Assert.assertNull(array.find(45));
        Assert.assertEquals(o5, array.find(55));

        Assert.assertNull(array.find(-1));
        Assert.assertNull(array.find(30));
        Assert.assertNull(array.find(40));
        Assert.assertNull(array.find(49));
        Assert.assertNull(array.find(60));

        Assert.assertEquals(o0, array.find(0));
        Assert.assertEquals(o0, array.find(9));
        Assert.assertEquals(o1, array.find(10));
        Assert.assertEquals(o1, array.find(19));
        Assert.assertEquals(o2, array.find(20));
        Assert.assertEquals(o2, array.find(29));
        Assert.assertEquals(o3, array.find(31));
        Assert.assertEquals(o3, array.find(39));
        Assert.assertEquals(o5, array.find(50));
        Assert.assertEquals(o5, array.find(59));
    }

    @Test
    public void fromList()
    {
        ArrayList<RangedItem> list = new ArrayList();
        RangedItem o0 = new RangedItem(0, 9);
        RangedItem o1 = new RangedItem(10, 19);
        RangedItem o2 = new RangedItem(20, 29);
        RangedItem o3 = new RangedItem(31, 39);
        RangedItem o5 = new RangedItem(50, 59);
        list.add(o0);
        list.add(o1);
        list.add(o2);
        list.add(o3);
        list.add(o5);

        RangedArray array = new RangedArray(list);
        Assert.assertEquals(o0, array.find(5));
        Assert.assertEquals(o1, array.find(15));
        Assert.assertEquals(o2, array.find(25));
        Assert.assertEquals(o3, array.find(35));
        Assert.assertNull(array.find(45));
        Assert.assertEquals(o5, array.find(55));

        Assert.assertNull(array.find(-1));
        Assert.assertNull(array.find(30));
        Assert.assertNull(array.find(40));
        Assert.assertNull(array.find(49));
        Assert.assertNull(array.find(60));

        Assert.assertEquals(o0, array.find(0));
        Assert.assertEquals(o0, array.find(9));
        Assert.assertEquals(o1, array.find(10));
        Assert.assertEquals(o1, array.find(19));
        Assert.assertEquals(o2, array.find(20));
        Assert.assertEquals(o2, array.find(29));
        Assert.assertEquals(o3, array.find(31));
        Assert.assertEquals(o3, array.find(39));
        Assert.assertEquals(o5, array.find(50));
        Assert.assertEquals(o5, array.find(59));
    }

}
