package com.vvlanding.data;

public class Meta {
    int page;
    int size;
    long total;

    public void setSize(int size) {
        this.size = size;
    }

    public void setPage(int page) {
        this.page = page;
    }



    public int getSize() {
        return size;
    }

    public int getPage() {
        return page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
