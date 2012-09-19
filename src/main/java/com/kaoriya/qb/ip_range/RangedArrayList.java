package com.kaoriya.qb.ip_range;

import java.util.ArrayList;
import java.util.Collections;

public final class RangedArrayList<T>
    extends ArrayList<RangedItem<? extends T>>
{

    private boolean modified = true;

    public RangedItem<? extends T> find(int key)
    {
        assureSort();
        int index = binarySearch(key, 0, this.size() - 1);
        return index >= 0 ? get(index) : null;
    }

    public T findValue(int key) {
        RangedItem<? extends T> item = find(key);
        return item != null ? item.getValue() : null;
    }

    public void markModified()
    {
        this.modified = true;
    }

    private void assureSort()
    {
        if (this.modified) {
            Collections.sort(this);
            this.modified = false;
        }
    }

    private int binarySearch(
            int key,
            int start,
            int end)
    {
        while (start <= end)
        {
            int mid = (start + end + 1) / 2;
            int c = get(mid).compare(key);
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
