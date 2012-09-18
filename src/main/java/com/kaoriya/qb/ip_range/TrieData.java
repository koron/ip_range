package com.kaoriya.qb.ip_range;

public final class TrieData
{
    private final CIDR cidr;

    private final String data;

    public TrieData(CIDR cidr, String data) {
        this.cidr = cidr;
        this.data = data;
    }

    public CIDR getCIDR() { return this.cidr; }
    public String getData() { return this.data; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(obj instanceof TrieData)) {
            return false;
        }

        TrieData that = (TrieData)obj;
        if ((this.cidr == null && that.cidr != null) ||
                (this.cidr != null && !this.cidr.equals(that.cidr))) {
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
        s.append("TrieData{")
            .append("cidr=").append(this.cidr).append(",")
            .append("data=").append(this.data)
            .append("}");
        return s.toString();
    }
}
