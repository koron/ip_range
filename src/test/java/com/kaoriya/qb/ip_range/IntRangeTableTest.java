package com.kaoriya.qb.ip_range;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntRangeTableTest
{

    @Test
    public void findEmpty() {
        IntRangeTable<Integer> table = new IntRangeTable<>();
        assertNull(null, table.find(50));
        assertNull(null, table.find(150));
        assertNull(null, table.find(250));
        assertNull(null, table.find(350));
        assertNull(null, table.find(450));
    }

    @Test
    public void addOrder() {
        IntRangeTable<Integer> table1 = new IntRangeTable<>();
        table1.add(100, 199, 1111);
        table1.add(300, 399, 3333);
        table1.add(500, 599, 5555);
        assertEquals(Integer.valueOf(1111), table1.find(150));
        assertEquals(Integer.valueOf(3333), table1.find(350));
        assertEquals(Integer.valueOf(5555), table1.find(550));

        IntRangeTable<Integer> table2 = new IntRangeTable<>();
        table2.add(500, 599, 5555);
        table2.add(300, 399, 3333);
        table2.add(100, 199, 1111);
        assertEquals(Integer.valueOf(1111), table2.find(150));
        assertEquals(Integer.valueOf(3333), table2.find(350));
        assertEquals(Integer.valueOf(5555), table2.find(550));

        IntRangeTable<Integer> table3 = new IntRangeTable<>();
        table3.add(500, 599, 5555);
        table3.add(100, 199, 1111);
        table3.add(300, 399, 3333);
        assertEquals(Integer.valueOf(1111), table3.find(150));
        assertEquals(Integer.valueOf(3333), table3.find(350));
        assertEquals(Integer.valueOf(5555), table3.find(550));
    }

}
