package org.edu.ptu.studentmanager.common.response;

import lombok.Data;
import org.edu.ptu.studentmanager.common.enums.Errors;

@Data
public class Result {
    private final int code;
    private final String message;
    private final Object data;
    public static final Result OK = new Result(0, null, null);

    protected Result() {
        code = 0;
        message = null;
        data = null;
    }

    protected Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public static Result ok(Object data) {
        return new Result(0, null, data);
    }

    public static Result fail(Errors errors) {
        return new Result(errors.getCode(), errors.getMessage(), null);
    }

    public static Result fail(Errors errors, String message) {
        return new Result(errors.getCode(), message, null);
    }
}
