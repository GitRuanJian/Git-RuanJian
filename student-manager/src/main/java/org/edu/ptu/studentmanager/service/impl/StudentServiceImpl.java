package org.edu.ptu.studentmanager.service.impl;

import org.apache.logging.log4j.util.Strings;
import org.edu.ptu.studentmanager.common.dao.Major;
import org.edu.ptu.studentmanager.common.dao.Student;
import org.edu.ptu.studentmanager.common.dao.StudentDetail;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.common.request.MapEditor;
import org.edu.ptu.studentmanager.mapper.CommonMapper;
import org.edu.ptu.studentmanager.mapper.CourseMapper;
import org.edu.ptu.studentmanager.mapper.MajorMapper;
import org.edu.ptu.studentmanager.mapper.StudentMapper;
import org.edu.ptu.studentmanager.service.StudentService;
import org.edu.ptu.studentmanager.utils.GradeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.util.*;

import static org.edu.ptu.studentmanager.common.enums.CourseType.ELECTIVE;
import static org.edu.ptu.studentmanager.common.enums.Errors.*;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@Service
@Transactional
public class StudentServiceImpl extends BaseService<Student> implements StudentService {
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private CourseMapper courseMapper;

    @PostConstruct
    public void init() {
        table = TABLE;
        keyColumn = COL_NUMBER;
        mapper = commonMapper;
        baseMapper = studentMapper;
        columns = getFields(Student.class);
    }

    @Override
    protected void afterSelect(List<? extends Student> list) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        for (Student s : list) {
            if (s != null && s.getAdmissionYear() != null)
                ((StudentDetail)s).setGrade(GradeUtils.getGradeText(year, month, s.getAdmissionYear()));
        }
    }

    @Override
    protected Result beforeAdd(Student data) {
        Result result = checkCapacity(data);
        if (result.isSuccess()) return Result.OK;
        return result;
    }

    @Override
    protected Result beforeEdit(Student data, Student record) {
        //判断是否转系
        if (!data.getMajorId().equals(record.getMajorId())) {
            Result result = checkCapacity(data);
            if (!result.isSuccess()) return result;
        }
        //判断是否拖后入学年份
        if (data.getAdmissionYear() > record.getAdmissionYear()) {
            int num = courseMapper.countUnqualifiedCourse(data.getId(),
                    GradeUtils.getGrade(data.getAdmissionYear()));
            if (num > 0) return Result.fail(RECORD_OCCUPIED, "更改入学时间将产生不满足年级要求的选课，请先检查已选课程");
        }
        return Result.OK;
    }

    @Override
    protected Result beforeDelete(String ids) {
        commonMapper.deleteColumnIn(TABLE_MAP_COURSE, COL_MAP_STUDENT, ids);
        return Result.OK;
    }

    @Override
    public Result chooseCourse(MapEditor mapEditor) {
        return chooseCourse(mapEditor, TABLE_MAP_COURSE, COL_MAP_STUDENT, COL_MAP);
    }

    @Override
    protected Result beforeChooseCourse(MapEditor mapEditor) {
        Student student = studentMapper.selectById(mapEditor.getReceptorId());
        if (student == null) return Result.fail(RECORD_NOT_EXISTS);
        int count = courseMapper.countInvalidCourseChoiceWithGrade(Strings.join(mapEditor.getIds(), ','),
                ELECTIVE.getValue(), GradeUtils.getGrade(student.getAdmissionYear()));
        if (count > 0) return Result.fail(RECORD_LIMITED, "所选课程非选修课或超出学生年级");
        return Result.OK;
    }

    private Result checkCapacity(Student student) {
        Major major = majorMapper.selectById(student.getMajorId());
        if (major == null) return Result.fail(RECORD_NOT_EXISTS, "专业不存在");
        int max = major.getCapacity();
        long num = commonMapper.countColumn(TABLE, COL_MAJOR, String.valueOf(student.getMajorId()));
        if (num >= max) return Result.fail(RECORD_LIMITED, "该专业学生已满");
        return Result.OK;
    }
}
