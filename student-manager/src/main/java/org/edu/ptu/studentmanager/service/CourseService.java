package org.edu.ptu.studentmanager.service;

import org.edu.ptu.studentmanager.common.dao.Course;
import org.edu.ptu.studentmanager.common.request.Condition;
import org.edu.ptu.studentmanager.common.request.IdReq;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.common.request.IdCollection;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
public interface CourseService {
    String TABLE = "major";
    String COL_NAME = "name";
    String COL_MAP_COURSE = "course_id";

    Result list(Condition condition);
    Result add(Course course);
    Result edit(Course course);
    Result delete(IdCollection ids);
    Result getCourseType();
    Result getCourseByStudent(IdReq idReq);
    Result getCourseByMajor(IdReq idReq);
}
