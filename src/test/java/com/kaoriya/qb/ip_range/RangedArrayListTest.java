package com.kaoriya.qb.ip_range;

import org.junit.Test;
import static org.junit.Assert.*;

public class RangedArrayListTest
{

    @Test
    public void find()
    {
        RangedItem<Object> o0 = new RangedItem<>(0, 9);
        RangedItem<Object> o1 = new RangedItem<>(10, 19);
        RangedItem<Object> o2 = new RangedItem<>(20, 29);
        RangedItem<Object> o3 = new RangedItem<>(31, 39);
        RangedItem<Object> o5 = new RangedItem<>(50, 59);

        RangedArrayList<Object> list = new RangedArrayList<>();
        list.add(o0);
        list.add(o1);
        list.add(o2);
        list.add(o3);
        list.add(o5);
        list.markModified();

        assertEquals(o0, list.find(5));
        assertEquals(o1, list.find(15));
        assertEquals(o2, list.find(25));
        assertEquals(o3, list.find(35));
        assertNull(list.find(45));
        assertEquals(o5, list.find(55));

        assertNull(list.find(-1));
        assertNull(list.find(30));
        assertNull(list.find(40));
        assertNull(list.find(49));
        assertNull(list.find(60));

        assertEquals(o0, list.find(0));
        assertEquals(o0, list.find(9));
        assertEquals(o1, list.find(10));
        assertEquals(o1, list.find(19));
        assertEquals(o2, list.find(20));
        assertEquals(o2, list.find(29));
        assertEquals(o3, list.find(31));
        assertEquals(o3, list.find(39));
        assertEquals(o5, list.find(50));
        assertEquals(o5, list.find(59));
    }

}
