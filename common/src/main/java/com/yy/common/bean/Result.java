package com.yy.common.bean;


import com.yy.common.constant.ResponseCode;

public class Result<T> {


    private Integer code;

    private T data;

    private String msg;

    private long timestamp;

    public Result() {
        initParam(ResponseCode.SUCCESS.getCode(),null,ResponseCode.SUCCESS.getMsg());
    }

    public Result(T data) {
        initParam(ResponseCode.SUCCESS.getCode(),data,ResponseCode.SUCCESS.getMsg());
    }

    public Result(Integer code, String msg) {
        initParam(code,null,msg);
    }

    public Result(Integer code, T data, String msg) {
        initParam(code,data,msg);
    }

    private void initParam(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
