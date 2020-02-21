package com.fanggu.stone.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicResponse<T> {
    private int resultCode;
    private String msg;
    private T result;

    public BasicResponse() {
    }

    public BasicResponse(int resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public BasicResponse(int resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
