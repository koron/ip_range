package com.kaoriya.qb.ip_range;

public final class IPv4Table<T>
{

    private final RangedArray<RangedArray<RangedArray<RangedArray<T> > > >
        table;

    public IPv4Table(
        RangedArray<RangedArray<RangedArray<RangedArray<T> > > > table)
    {
        this.table = table;
    }

    public T find(IPv4 ipv4)
    {
        RangedItem<RangedArray<RangedArray<RangedArray<T> > > > v1 =
            this.table.find(ipv4.getValue(0));
        if (v1 == null) {
            return null;
        }

        RangedItem<RangedArray<RangedArray<T> > > v2 =
            v1.getValue().find(ipv4.getValue(1));
        if (v2 == null) {
            return null;
        }

        RangedItem<RangedArray<T> > v3 = v2.getValue().find(ipv4.getValue(2));
        if (v3 == null) {
            return null;
        }

        RangedItem<T> v4 = v3.getValue().find(ipv4.getValue(3));
        if (v4 == null) {
            return null;
        }

        return v4.getValue();
    }

}
