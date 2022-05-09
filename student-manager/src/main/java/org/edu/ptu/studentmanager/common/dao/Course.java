package org.edu.ptu.studentmanager.common.dao;

import lombok.Data;
import org.edu.ptu.studentmanager.common.basic.UniqueDao;
import org.edu.ptu.studentmanager.common.enums.CourseType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@Data
public class Course implements UniqueDao {
    private static final BigDecimal ZERO = new BigDecimal(0);

    private Integer id;
    private String name;
    private Integer type;
    private Integer requiredGrade;
    private BigDecimal academicHour;
    private BigDecimal credit;

    @Override
    public String getKey() {
        return name;
    }

    @Bean
    @Scope
    @Override
    public boolean hasIllegalField() {
        return !StringUtils.hasLength(name) || academicHour == null || credit == null ||
                CourseType.of(type) == null || academicHour.compareTo(ZERO) <= 0 || credit.compareTo(ZERO) <= 0;
    }
}
