package com.kaoriya.qb.ip_range;

import java.util.ArrayList;

public final class IntRangeTable<T>
{

    private final ArrayList<IntRangeData<T>> arrayList = new ArrayList();

    public IntRangeTable() {
    }

    public void clear() {
        this.arrayList.clear();
    }

    private int findIndex(int rangeStart, int rangeEnd) {
        int start = 0;
        int end = this.arrayList.size() - 1;
        int hit = 0;
        while (start <= end) {
            int mid = (start + end + 1) / 2;
            IntRangeData<T> c = this.arrayList.get(mid);
            if (rangeEnd < c.getStart()) {
                end = mid - 1;
                hit = mid;
            } else if (rangeStart > c.getEnd()) {
                start = mid + 1;
                hit = start;
            } else {
                return -1;
            }
        }
        return hit;
    }

    private int findIndex(int value) {
        int start = 0;
        int end = this.arrayList.size() - 1;
        while (start <= end) {
            int mid = (start + end + 1) / 2;
            IntRangeData<T> c = this.arrayList.get(mid);
            if (value < c.getStart()) {
                end = mid - 1;
            } else if (value > c.getEnd()) {
                start = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public void add(IntRangeData<T> data) {
        int index = findIndex(data.getStart(), data.getEnd());
        if (index == -1) {
            throw new RuntimeException("Range overwrap: " + data);
        }
        this.arrayList.add(index, data);
    }

    public void add(int start, int end, T data) {
        add(new IntRangeData<T>(start, end, data));
    }

    public T find(int value) {
        IntRangeData<T> data = findRangeData(value);
        return data == null ? null : data.getData();
    }

    public IntRangeData<T> findRangeData(int value) {
        int index = findIndex(value);
        return index < 0 ? null : this.arrayList.get(index);
    }

}
