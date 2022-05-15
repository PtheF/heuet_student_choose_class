package cn.chl.robClass.controller.entity;

public class ResponseData<T> {
    private boolean success;

    private T data;

    private String authorization;

    public String getAuthorization() {
        return authorization;
    }

    public ResponseData auth(String authorization) {
        this.authorization = authorization;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResponseData setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseData setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> ResponseData<T> ok(T data){
        return new ResponseData<T>().setSuccess(true).setData(data);
    }

    public static <T> ResponseData<T> fail(T data){
        return new ResponseData<T>().setSuccess(false).setData(data);
    }
}
