package com.tazine.http;

import java.io.Serializable;

/**
 * Created by lina on 2018/2/5.
 *
 * @author frank
 * @since 1.0.0
 */
public class ApiResponse<T> implements Serializable {

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String errMsg;

    /**
     * 业务数据
     */
    private T data;

    public static <T> ApiResponse<T> buildSuccess(T data){
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setSuccess(true);
        return response;
    }

    public static <T> ApiResponse<T> buildSuccess(){
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        return response;
    }

    public static <T> ApiResponse<T> buildFailure(){
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        return response;
    }

    public static <T> ApiResponse<T> buildFailure(String errMsg){
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(0);
        response.setErrMsg(errMsg);
        return response;
    }


    public static <T> ApiResponse<T> buildFailure(int errorCode, String errMsg){
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setCode(errorCode);
        response.setErrMsg(errMsg);
        return response;
    }





    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
