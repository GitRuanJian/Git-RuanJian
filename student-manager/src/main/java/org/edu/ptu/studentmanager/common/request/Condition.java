package org.edu.ptu.studentmanager.common.request;

import lombok.Data;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@Data
public class Condition {
    private String column;
    private String value;
    private Integer offset;
    private Integer limit;
}
