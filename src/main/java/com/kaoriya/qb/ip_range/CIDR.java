package com.kaoriya.qb.ip_range;

public final class CIDR
{
    private final IPv4 address;
    private final int mask;

    public CIDR(IPv4 address, int mask) {
        this.address = address;
        this.mask = mask;
    }

    public IPv4 getAddress() { return this.address; }
    public int getMask() { return this.mask; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(obj instanceof CIDR)) {
            return false;
        }

        CIDR that = (CIDR)obj;
        if ((this.address == null && that.address != null) ||
                (this.address != null && !this.address.equals(that.address))) {
            return false;
        } else if (this.mask != this.mask) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("CIDR{")
            .append("address=").append(this.address).append(",")
            .append("mask=").append(this.mask)
            .append("}");
        return s.toString();
    }

    public static CIDR fromString(String s) {
        String[] values = s.split("/", 2);
        if (values.length != 2) {
            return null;
        }

        IPv4 address = IPv4.fromString(values[0]);
        if (address == null) {
            return null;
        }

        int mask = -1;
        try {
            mask = Integer.parseInt(values[1]);
        } catch (NumberFormatException e) {
            return null;
        }
        if (mask < 0 || mask > 32) {
            return null;
        }

        return new CIDR(address, mask);
    }
}
