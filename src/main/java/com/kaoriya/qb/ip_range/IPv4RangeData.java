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
}
