package com.vvlanding.data;

import java.util.List;

public class Resp {
    Object data;
    int statusCode;
    String msg;
    Object included;
    Boolean success;
    Meta meta = new Meta();
    List list;

    int page;
    int size;
    long total;

    public void setSize(int size) {
        this.size = size;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
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

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Object getIncluded() {
        return included;
    }
    public void setIncluded(Object included) {
        this.included = included;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public  Boolean  getSuccess(){ return success; };
    public void  setSuccess(boolean success) { this.success = success;  }
    public void statusCode(int i) { }


    public void setMetaRepo(int page, int size, int total) {
        Meta newMeta = new Meta();
        newMeta.setPage(page);
        newMeta.setSize(size);
        newMeta.setTotal(total);
        setMeta(newMeta);
    }
}
