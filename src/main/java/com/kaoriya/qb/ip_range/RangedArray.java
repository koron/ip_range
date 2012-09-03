package com.kaoriya.qb.ip_range;

import java.util.Arrays;
import java.util.Comparator;

public class RangedArray
{
    private RangedItem[] array;

    public RangedArray(RangedItem... items)
    {
        this.array = Arrays.copyOf(items, items.length);
        sortByRange(this.array);
    }

    public RangedItem find(int key)
    {
        int index = binarySearch(this.array, 0, this.array.length - 1, key);
        return index >= 0 ? this.array[index] : null;
    }

    static private void sortByRange(RangedItem[] array)
    {
        Arrays.sort(array);
    }

    static private int binarySearch(
            RangedItem[] array,
            int start,
            int end,
            int key)
    {
        while (start <= end)
        {
            int mid = (start + end + 1) / 2;
            int c = array[mid].compare(key);
            if (c < 0)
            {
                end = mid - 1;
            }
            else if (c > 0)
            {
                start = mid + 1;
            }
            else
                return mid;
        }
        return -1;
    }

}
