package org.edu.ptu.studentmanager.service.impl;

import org.apache.logging.log4j.util.Strings;
import org.edu.ptu.studentmanager.common.dao.Major;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.common.request.MapEditor;
import org.edu.ptu.studentmanager.mapper.CommonMapper;
import org.edu.ptu.studentmanager.mapper.CourseMapper;
import org.edu.ptu.studentmanager.mapper.MajorMapper;
import org.edu.ptu.studentmanager.service.MajorService;
import org.edu.ptu.studentmanager.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static org.edu.ptu.studentmanager.common.enums.CourseType.COMPULSORY;
import static org.edu.ptu.studentmanager.common.enums.Errors.*;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
@Service
public class MajorServiceImpl extends BaseService<Major> implements MajorService {
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private CourseMapper courseMapper;

    @PostConstruct
    public void init() {
        table = TABLE;
        keyColumn = COL_NAME;
        mapper = commonMapper;
        baseMapper = majorMapper;
        columns = getFields(Major.class);
    }

    @Override
    protected Result beforeEdit(Major data, Major record) {
        //判断capacity是否低于当前已选学生数量
        if (!data.getCapacity().equals(record.getCapacity())) {
            int num = commonMapper.countColumn(StudentService.TABLE, StudentService.COL_MAJOR, String.valueOf(data.getId()));
            if (num > data.getCapacity()) return Result.fail(RECORD_LIMITED, "新上限低于已选学生数量");
        }
        return Result.OK;
    }

    @Override
    protected Result beforeDelete(String ids) {
        //判断是否已被学生选择
        if (commonMapper.countColumnIn(StudentService.TABLE, StudentService.COL_MAJOR, ids) > 0)
            return Result.fail(RECORD_OCCUPIED, "该专业已有学生选取");
        commonMapper.deleteColumnIn(TABLE_MAP_COURSE, COL_MAP_MAJOR, ids);
        return Result.OK;
    }

    @Override
    protected Result beforeChooseCourse(MapEditor mapEditor) {
        int count = courseMapper.countInvalidCourseChoice(Strings.join(mapEditor.getIds(), ','),
                COMPULSORY.getValue());
        if (count > 0) return Result.fail(RECORD_LIMITED, "所选课程非必修课");
        return Result.OK;
    }

    @Override
    public Result list() {
        return Result.ok(majorMapper.selectName());
    }

    @Override
    public Result chooseCourse(MapEditor mapEditor) {
        return chooseCourse(mapEditor, TABLE_MAP_COURSE, COL_MAP_MAJOR, COL_MAP);
    }
}
