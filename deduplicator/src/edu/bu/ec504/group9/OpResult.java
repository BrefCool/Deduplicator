package edu.bu.ec504.group9;

public class OpResult {
    public Integer errorCode;
    public String message;

    OpResult() {
        errorCode = 0;
        message = "";
    }

    OpResult(Integer code, String msg) {
        errorCode = code;
        message = msg;
    }

    public void setErrorCode(Integer code) {
        errorCode = code;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
