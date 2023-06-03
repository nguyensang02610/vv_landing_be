package com.vvlanding.utils;

import java.util.HashMap;
import java.util.Map;

public class Constant {

    public static Map<String,Object> res(String message, boolean bl, Object data){
        Map<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("success",bl);
        map.put("data",data);
        return map;
    }

    public static Map<String,Object> res(String message, boolean bl){
        Map<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("success",bl);
        return map;
    }
}
