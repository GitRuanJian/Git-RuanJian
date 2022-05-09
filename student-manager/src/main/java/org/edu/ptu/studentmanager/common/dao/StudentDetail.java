package org.edu.ptu.studentmanager.common.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentDetail extends Student {
    private String grade;
    private String major;
    private Integer courseNum;
    private BigDecimal totalCredits;
    private BigDecimal requiredCredits;
}
