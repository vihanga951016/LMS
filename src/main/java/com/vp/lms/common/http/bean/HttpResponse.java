package com.vp.lms.common.http.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vp.lms.common.ApplicationConstant;
import lombok.Data;

import java.util.Date;

@Data
public class HttpResponse<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Asia/Colombo")
    private Date timeStamp;
    private String message;
    private T data;

    public HttpResponse<T> responseOk(T data){
        message = ApplicationConstant.RESPONSE_OK;
        this.data = data;
        return this;
    }

    public HttpResponse<T> responseOk(String msg, T data){
        message = msg;
        this.data = data;
        return this;
    }

    public HttpResponse<T> responseFail(T data){
        message = (String) data;
        this.data = data;
        return this;
    }

    public HttpResponse() {
    }

    public HttpResponse(String message, T data){
        this.message = message;
        this.data = data;
    }
}
