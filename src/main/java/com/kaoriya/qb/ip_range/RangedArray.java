package com.kaoriya.qb.ip_range;

import java.util.Arrays;
import java.util.Comparator;

public class RangedArray
{
    private RangedItem[] array;

    public RangedArray(RangedItem... items)
    {
        // TODO: write test.
        this.array = Arrays.copyOf(items, items.length);
        sortByRange(this.array);
    }

    static private void sortByRange(RangedItem[] array)
    {
        Arrays.sort(array);
    }
}
