package com.kaoriya.qb.ip_range;

public class RangedItem<T>
{

    private int start;

    private int end;

    private T value;

    public T getValue() { return this.value; }

    public void setValue(T value) { this.value = value; }

    public RangedItem(int start, int end, T value)
    {
        if (start < end)
        {
            this.start = start;
            this.end = end;
        }
        else
        {
            this.start = end;
            this.end = start;
        }
        this.value = value;
    }

    public RangedItem(int start, int end)
    {
        this(start, end, null);
    }

    public int compare(int pivot)
    {
        if (pivot < this.start)
            return -1;
        else if (pivot > this.end)
            return 1;
        else
            return 0;
    }

}
