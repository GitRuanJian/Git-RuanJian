package com.example.demo.common;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String message;
    private Object data;

    public static Result ok(Object data){
        Result r = new Result();
        r.setData(data);
        return r;
    }

    public static Result fail(String message){
        Result r = new Result();
        r.setCode(1);
        r.setMessage(message);
        return r;
    }
}
