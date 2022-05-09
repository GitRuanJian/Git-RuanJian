package org.edu.ptu.studentmanager.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
public enum CourseType {
    COMPULSORY(0, "必修课"),
    ELECTIVE(1, "选修课");

    private final int value;
    private final String description;

    CourseType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static CourseType of(Integer value) {
        if (value == null) return null;
        for (CourseType courseType : values()) {
            if (courseType.value == value) return courseType;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
