package com.vvlanding.utils;

import com.vvlanding.data.Resp;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

public class ListData {
    public Resp ListData(List listData, Pageable pageable){
        List<Object> list;
        int start;
        int end;
        Resp resp = new Resp();
        if (listData.size() <= 10) {
            start = (int) PageRequest.of(0,10).getOffset();
            end = (start + 10) > listData.size() ? listData.size() : 10;
            resp.setMetaRepo(1, 10, listData.size());
        } else {
            start = (int) PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()).getOffset();
            end = Math.min((start + pageable.getPageSize()), listData.size());
            resp.setMetaRepo(pageable.getPageNumber() + 1,pageable.getPageSize(),listData.size());
        }
        list = listData.subList(start,end);
        resp.setList(list);
        return resp;
    }
}
