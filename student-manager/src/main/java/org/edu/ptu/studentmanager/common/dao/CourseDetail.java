package org.edu.ptu.studentmanager.common.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseDetail extends Course {
    private String courseType;
}
