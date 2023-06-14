package com.kaoriya.qb.ip_range;

import java.util.ArrayList;
import java.util.Random;

public final class IntRangeTable<T>
{
    final ArrayList<IntRangeData<T>> arrayList = new ArrayList<>();

    private final ArrayList<IntRangeData<T>> negativeList = new ArrayList<>();

    private final ArrayList<IntRangeData<T>> positiveList = new ArrayList<>();

    public IntRangeTable() {
    }

    public void clear() {
        this.arrayList.clear();
        this.negativeList.clear();
        this.positiveList.clear();
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

    public void updateNegativeList() {
        invertRange(this.arrayList, this.negativeList);
        invertRange(this.negativeList, this.positiveList);
    }

    public T find(int value) {
        IntRangeData<T> data = findRangeData(value);
        return data == null ? null : data.getData();
    }

    public IntRangeData<T> findRangeData(int value) {
        int index = findIndex(value);
        return index < 0 ? null : this.arrayList.get(index);
    }

    public IntRangeData<T> get(Random r) {
        return this.arrayList.get(r.nextInt(this.arrayList.size()));
    }

    public int getPositive(Random r) {
        int index = r.nextInt(this.positiveList.size());
        return this.positiveList.get(index).get(r);
    }

    public int getNegative2(Random r) {
        int index = r.nextInt(this.negativeList.size());
        return this.negativeList.get(index).get(r);
    }

    public void invertRange(
            ArrayList<IntRangeData<T>> input,
            ArrayList<IntRangeData<T>> output)
    {
        output.clear();

        int prev = Integer.MIN_VALUE;
        for (IntRangeData item : input) {
            int curr = item.getStart();
            if (prev < curr) {
                addRangeData(output, prev, curr - 1);
            }
            prev = item.getEnd();
            if (prev == Integer.MAX_VALUE) {
                break;
            }
            prev += 1;
        }

        if (prev < Integer.MAX_VALUE) {
            addRangeData(output, prev, Integer.MAX_VALUE);
        }
    }

    private void addRangeData(
            ArrayList<IntRangeData<T>> list,
            int start,
            int end)
    {
        while (((long)end - (long)start + 1L) >= 0x80000000L) {
            int mid = start + 0x7FFFFFFF;
            list.add(new IntRangeData<T>(start, mid - 1, null));
            start = mid;
        }
        list.add(new IntRangeData<T>(start, end, null));
    }

}
