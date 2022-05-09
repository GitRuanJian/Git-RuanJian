package org.edu.ptu.studentmanager.service.impl;

import org.edu.ptu.studentmanager.common.dao.Course;
import org.edu.ptu.studentmanager.common.dao.CourseDetail;
import org.edu.ptu.studentmanager.common.dao.Student;
import org.edu.ptu.studentmanager.common.request.IdReq;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.common.enums.CourseType;
import org.edu.ptu.studentmanager.common.response.SelectedCourses;
import org.edu.ptu.studentmanager.mapper.CommonMapper;
import org.edu.ptu.studentmanager.mapper.CourseMapper;
import org.edu.ptu.studentmanager.mapper.StudentMapper;
import org.edu.ptu.studentmanager.service.CourseService;
import org.edu.ptu.studentmanager.service.MajorService;
import org.edu.ptu.studentmanager.service.StudentService;
import org.edu.ptu.studentmanager.utils.GradeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.edu.ptu.studentmanager.common.enums.CourseType.COMPULSORY;
import static org.edu.ptu.studentmanager.common.enums.CourseType.ELECTIVE;
import static org.edu.ptu.studentmanager.common.enums.Errors.*;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@Service
public class CourseServiceImpl extends BaseService<Course> implements CourseService {
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private CourseMapper courseMapper;

    @PostConstruct
    public void init() {
        table = TABLE;
        keyColumn = COL_NAME;
        mapper = commonMapper;
        baseMapper = courseMapper;
        columns = getFields(Course.class);
    }

    @Override
    protected void afterSelect(List<? extends Course> list) {
        CourseType type;
        for (Course s : list) {
            if (s != null && (type = CourseType.of(s.getType())) != null)
                ((CourseDetail)s).setCourseType(type.getDescription());
        }
    }

    @Override
    protected Result beforeEdit(Course course, Course record) {
        //检查类型是否更改
        if (!course.getType().equals(record.getType())) {
            CourseType type = CourseType.of(record.getType());
            if (type == COMPULSORY || type == ELECTIVE) {
                if (commonMapper.existsColumnId(
                        type == COMPULSORY ? MajorService.TABLE_MAP_COURSE : StudentService.TABLE_MAP_COURSE,
                        CourseService.COL_MAP_COURSE, course.getId()) > 0)
                    return Result.fail(RECORD_OCCUPIED, "该课程已被选择，无法更改课程类型");
            }
        }
        //检查年级要求是否更改
        if (course.getRequiredGrade() != null && (record.getRequiredGrade() == null ||
                course.getRequiredGrade() > 1 && course.getRequiredGrade() > record.getRequiredGrade())) {
            int num = courseMapper.countUnqualifiedStudent(course.getId(),
                    GradeUtils.getAdmissionYear(course.getRequiredGrade()));
            if (num > 0) return Result.fail(RECORD_OCCUPIED, "已有低于年纪要求的学生选择该课程，无法更改课程年级要求");
        }
        return Result.OK;
    }

    @Override
    protected Result beforeDelete(String ids) {
        if (commonMapper.countColumnIn(StudentService.TABLE_MAP_COURSE, COL_MAP_COURSE, ids) > 0)
            return Result.fail(RECORD_OCCUPIED, "该课程已有学生选取");
        if (commonMapper.countColumnIn(MajorService.TABLE_MAP_COURSE, COL_MAP_COURSE, ids) > 0)
            return Result.fail(RECORD_OCCUPIED, "该课程已有专业选取");
        return Result.OK;
    }

    @Override
    public Result getCourseType() {
        Map<String, Integer> map = new HashMap<>();
        for (CourseType courseType : CourseType.values()) {
            map.put(courseType.getDescription(), courseType.getValue());
        }
        return Result.ok(map);
    }

    @Override
    public Result getCourseByStudent(IdReq idReq) {
        if (idReq == null || idReq.getId() == null) return Result.fail(INVALID_PARAMS);
        Student student = studentMapper.selectById(idReq.getId());
        if (student == null) return Result.fail(RECORD_NOT_EXISTS);
        SelectedCourses selectedCourses = new SelectedCourses();
        selectedCourses.setCourses(courseMapper.selectNameByTypeAndGrade(ELECTIVE.getValue(),
                GradeUtils.getGrade(student.getAdmissionYear())));
        selectedCourses.setSelected(courseMapper.selectIdByStudentId(idReq.getId()));
        return Result.ok(selectedCourses);
    }

    @Override
    public Result getCourseByMajor(IdReq idReq) {
        if (idReq == null || idReq.getId() == null) return Result.fail(INVALID_PARAMS);
        SelectedCourses selectedCourses = new SelectedCourses();
        selectedCourses.setCourses(courseMapper.selectNameByType(COMPULSORY.getValue()));
        selectedCourses.setSelected(courseMapper.selectIdByMajorId(idReq.getId()));
        return Result.ok(selectedCourses);
    }
}
