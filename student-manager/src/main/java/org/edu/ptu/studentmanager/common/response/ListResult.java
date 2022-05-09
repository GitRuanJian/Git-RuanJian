package org.edu.ptu.studentmanager.common.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ListResult extends Result {
    private final long total;

    private ListResult(int code, String message, Object data, long total) {
        super(code, message, data);
        this.total = total;
    }

    public static ListResult ok(Collection<?> collection, long total) {
        return new ListResult(0, null, collection, total);
    }
}
