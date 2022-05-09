package org.edu.ptu.studentmanager.common.response;

import lombok.Data;
import org.edu.ptu.studentmanager.common.dao.Course;

import java.util.List;

/**
 * Created by Lin Chenxiao on 2021-05-28
 **/
@Data
public class SelectedCourses {
    private List<Integer> selected;
    private List<Course> courses;
}
