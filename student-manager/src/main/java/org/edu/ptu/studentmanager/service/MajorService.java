package org.edu.ptu.studentmanager.service;

import org.edu.ptu.studentmanager.common.dao.Major;
import org.edu.ptu.studentmanager.common.request.Condition;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.common.request.MapEditor;
import org.edu.ptu.studentmanager.common.request.IdCollection;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
public interface MajorService {
    String TABLE = "major";
    String COL_NAME = "name";
    String TABLE_MAP_COURSE = "map_major_course";
    String COL_MAP_MAJOR = "major_id";
    String COL_MAP = "major_id,course_id";

    Result list();
    Result list(Condition condition);
    Result add(Major major);
    Result edit(Major major);
    Result delete(IdCollection ids);
    Result chooseCourse(MapEditor mapEditor);
}
