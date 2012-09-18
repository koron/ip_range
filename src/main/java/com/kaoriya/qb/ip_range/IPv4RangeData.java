package com.kaoriya.qb.ip_range;

public final class IPv4RangeData {

    private IPv4 start;
    private IPv4 end;
    private String data;

    public IPv4RangeData(IPv4 start, IPv4 end, String data) {
        this.start = start;
        this.end = end;
        this.data = data;
    }

    public IPv4 getStart() { return this.start; }
    public IPv4 getEnd() { return this.end; }
    public String getData() { return this.data; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(obj instanceof IPv4RangeData)) {
            return false;
        }

        IPv4RangeData that = (IPv4RangeData)obj;
        if ((this.start == null && that.start != null) ||
                (this.start != null && !this.start.equals(that.start))) {
            return false;
        } else if ((this.end == null && that.end != null) ||
                (this.end != null && !this.end.equals(that.end))) {
            return false;
        } else if ((this.data == null && that.data != null) ||
                (this.data != null && !this.data.equals(that.data))) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("IPv4RangeData{")
            .append("start=").append(this.start).append(",")
            .append("end=").append(this.end).append(",")
            .append("data=").append(this.data)
            .append("}");
        return s.toString();
    }

    public static IPv4RangeData fromString(String str)
    {
        String[] values = str.split("\t", 3);
        if (values.length < 3) {
            return null;
        }
        IPv4 start = IPv4.fromString(values[0]);
        IPv4 end = IPv4.fromString(values[1]);
        if (start == null || end == null) {
            return null;
        }
        return new IPv4RangeData(start, end, values[2]);
    }
}
