package org.edu.ptu.studentmanager.service;

import org.edu.ptu.studentmanager.common.dao.Student;
import org.edu.ptu.studentmanager.common.request.Condition;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.common.request.IdCollection;
import org.edu.ptu.studentmanager.common.request.MapEditor;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
public interface StudentService {
    String TABLE = "student";
    String TABLE_MAP_COURSE = "map_student_course";
    String COL_NUMBER = "number";
    String COL_MAJOR = "major_id";
    String COL_MAP_STUDENT = "student_id";
    String COL_MAP = "student_id,course_id";

    Result list(Condition condition);
    Result add(Student student);
    Result edit(Student student);
    Result delete(IdCollection ids);
    Result chooseCourse(MapEditor mapEditor);
}
