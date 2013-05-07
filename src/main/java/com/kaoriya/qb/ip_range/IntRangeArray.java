package com.kaoriya.qb.ip_range;

import java.lang.reflect.Array;

public final class IntRangeArray<T>
{
    final int length;
    final int[] rangeArray;
    final T[] valueArray;

    public IntRangeArray(IntRangeTable<T> table, Class<T> elementType)
    {
        this.length = table.arrayList.size();
        this.rangeArray = new int[this.length * 2];
        this.valueArray = (T[])Array.newInstance(elementType, this.length);
        // initialize array.
        for (int i = 0; i < this.length; ++i) {
            IntRangeData<T> d = table.arrayList.get(i);
            this.rangeArray[i * 2 + 0] = d.getStart();
            this.rangeArray[i * 2 + 1] = d.getEnd();
            this.valueArray[i] = d.getData();
        }
    }

    public T find(int value)
    {
        int index = findIndex(value);
        return index < 0 ? null : this.valueArray[index];
    }

    private int findIndex(int value)
    {
        int start = 0;
        int end = this.length - 1;
        while (start <= end) {
            int mid = (start + end + 1) / 2;
            if (value < this.rangeArray[mid * 2]) {
                end = mid - 1;
            } else if (value > this.rangeArray[mid * 2 + 1]) {
                start = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
