package org.edu.ptu.studentmanager.web;

import org.edu.ptu.studentmanager.common.dao.Course;
import org.edu.ptu.studentmanager.common.request.Condition;
import org.edu.ptu.studentmanager.common.request.IdCollection;
import org.edu.ptu.studentmanager.common.request.IdReq;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.mapper.CourseMapper;
import org.edu.ptu.studentmanager.mapper.StudentMapper;
import org.edu.ptu.studentmanager.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Lin Chenxiao on 2021-05-24
 **/
@RestController
@RequestMapping("/j/cou")
public class CourseController {
    @Resource
    private CourseService courseService;
    @Resource
    private StudentMapper studentMapper;

    @GetMapping("/t")
    public Result test(String t) { return Result.ok(studentMapper.select2(t)); }

    @PostMapping("/lsc")
    public Result listIdByStudent(IdReq id) {
        return courseService.getCourseByStudent(id);
    }

    @PostMapping("/lsm")
    public Result listIdByMajor(IdReq id) {
        return courseService.getCourseByMajor(id);
    }

    @PostMapping("/list")
    public Result list(Condition condition) {
        return courseService.list(condition);
    }

    @PostMapping("/add")
    public Result add(Course course) {
        return courseService.add(course);
    }

    @PostMapping("/edit")
    public Result edit(Course course) {
        return courseService.edit(course);
    }

    @PostMapping("/del")
    public Result del(IdCollection collection) {
        return courseService.delete(collection);
    }

    @PostMapping("/type")
    public Result getType() {
        return courseService.getCourseType();
    }
}
