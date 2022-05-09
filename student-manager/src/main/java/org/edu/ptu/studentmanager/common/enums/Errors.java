package org.edu.ptu.studentmanager.common.enums;

public enum Errors {
    INVALID_PARAMS(1, "传入参数不合法"),
    REPEAT_DATA(2, "数据重复"),
    RECORD_NOT_EXISTS(3, "记录不存在"),
    RECORD_LIMITED(4, "数据限制"),
    RECORD_OCCUPIED(5, "数据被占用"),
    INTERNAL_ERROR(6, "服务器发生错误");

    private final int code;
    private final String message;

    Errors(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
