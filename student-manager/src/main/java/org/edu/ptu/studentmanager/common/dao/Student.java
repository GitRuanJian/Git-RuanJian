package org.edu.ptu.studentmanager.common.dao;

import lombok.Data;
import org.edu.ptu.studentmanager.common.basic.UniqueDao;
import org.springframework.util.StringUtils;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@Data
public class Student implements UniqueDao {
    private Integer id;
    private String name;
    private String number;
    private Integer admissionYear;
    private Integer majorId;

    @Override
    public String getKey() {
        return number;
    }

    @Override
    public boolean hasIllegalField() {
        return !StringUtils.hasLength(name) || admissionYear == null || !StringUtils.hasLength(number) || majorId == null;
    }
}
