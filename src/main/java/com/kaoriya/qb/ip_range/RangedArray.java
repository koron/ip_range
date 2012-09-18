package com.kaoriya.qb.ip_range;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RangedArray<T>
{
    private final RangedItem<T>[] array;

    public RangedArray(RangedItem<T>... items)
    {
        this.array = Arrays.copyOf(items, items.length);
        sortByRange(this.array);
    }

    public RangedArray(List<RangedItem<T>> list)
    {
        this.array = list.toArray(new RangedItem[list.size()]);
        sortByRange(this.array);
    }

    public RangedItem<T> find(int key)
    {
        int index = binarySearch(this.array, 0, this.array.length - 1, key);
        return index >= 0 ? this.array[index] : null;
    }

    private void sortByRange(RangedItem<T>[] array)
    {
        Arrays.sort(array);
    }

    private int binarySearch(
            RangedItem<T>[] array,
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
