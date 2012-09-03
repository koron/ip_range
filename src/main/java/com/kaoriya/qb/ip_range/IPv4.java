package com.kaoriya.qb.ip_range;

import java.util.Iterator;

public final class IPv4 implements Iterable<Integer>
{

    private final int[] values = new int[4];

    public IPv4(int a, int b, int c, int d)
    {
        this.values[0] = a;
        this.values[1] = b;
        this.values[2] = c;
        this.values[3] = d;
    }

    public IPv4()
    {
        this(0, 0, 0, 0);
    }

    public int getValue(int index) {
        return this.values[index];
    }

    public void setValue(int index, int value) {
        this.values[index] = value;
    }

    @Override
    public Iterator<Integer>iterator() {
        return new Iterator<Integer>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return this.index < IPv4.this.values.length;
            }
            @Override
            public Integer next() {
                return IPv4.this.values[this.index++];
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

}
