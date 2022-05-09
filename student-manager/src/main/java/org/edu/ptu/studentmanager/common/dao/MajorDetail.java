package org.edu.ptu.studentmanager.common.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class MajorDetail extends Major {
    private Integer studentNum;
    private Integer courseNum;
    private BigDecimal totalCredits;
}
