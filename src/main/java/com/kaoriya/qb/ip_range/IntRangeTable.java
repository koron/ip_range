package com.kaoriya.qb.ip_range;

import java.util.ArrayList;
import java.util.Random;

public final class IntRangeTable<T>
{

    private final ArrayList<IntRangeData<T>> arrayList = new ArrayList();

    private final ArrayList<IntRangeData<Object>> negativeList
        = new ArrayList();

    public IntRangeTable() {
    }

    public void clear() {
        this.arrayList.clear();
        this.negativeList.clear();
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
        this.negativeList.clear();
        int prev = Integer.MIN_VALUE;
        for (IntRangeData<T> item : this.arrayList) {
            int curr = item.getStart();
            if (prev < curr) {
                if (prev < 0 && curr >= 0) {
                    this.negativeList.add(
                            new IntRangeData<Object>(prev, -1, null));
                    this.negativeList.add(
                            new IntRangeData<Object>(0, curr, null));
                } else {
                    this.negativeList.add(
                            new IntRangeData<Object>(prev, curr - 1, null));
                }
            }
            prev = item.getEnd();
            if (prev == Integer.MAX_VALUE) {
                break;
            }
            prev += 1;
        }
        if (prev < Integer.MAX_VALUE) {
            if (prev < 0) {
                this.negativeList.add(new IntRangeData<Object>(prev,
                            -1, null));
                this.negativeList.add(new IntRangeData<Object>(0,
                            0x3FFFFFFF, null));
                this.negativeList.add(new IntRangeData<Object>(0x40000000,
                            Integer.MAX_VALUE, null));
            } else {
                this.negativeList.add(new IntRangeData<Object>(prev,
                            Integer.MAX_VALUE, null));
            }
        }
        // TODO:
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
        //return get(r).get(r);
        int index = r.nextInt(this.arrayList.size());
        return this.arrayList.get(index).get(r);
    }

    public int getNegative(Random r) {
        int start = 1;
        int end = 0;
        while (start > end) {
            int n = r.nextInt(this.arrayList.size() + 1);
            if (n == 0) {
                end = this.arrayList.get(0).getStart() - 1;
                start = end >= 0 ? 0 : Integer.MIN_VALUE;
            } else if (n >= this.arrayList.size()) {
                start = this.arrayList.get(n - 1).getEnd() + 1;
                end = start < 0 ? -1 : Integer.MAX_VALUE;
            } else {
                start = this.arrayList.get(n - 1).getEnd() + 1;
                end = this.arrayList.get(n).getStart() - 1;
            }
        }
        return start + r.nextInt(end - start + 1);
    }

    public int getNegative2(Random r) {
        int index = r.nextInt(this.negativeList.size());
        return this.negativeList.get(index).get(r);
    }
}
