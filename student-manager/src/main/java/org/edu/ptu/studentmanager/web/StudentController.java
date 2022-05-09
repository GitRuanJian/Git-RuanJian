package org.edu.ptu.studentmanager.web;

import org.edu.ptu.studentmanager.common.dao.Student;
import org.edu.ptu.studentmanager.common.request.Condition;
import org.edu.ptu.studentmanager.common.request.IdCollection;
import org.edu.ptu.studentmanager.common.request.MapEditor;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.service.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Lin Chenxiao on 2021-05-24
 **/
@RestController
@RequestMapping("/j/stu")
public class StudentController {
    @Resource
    private StudentService studentService;

    @PostMapping("/list")
    public Result list(Condition condition) {
        return studentService.list(condition);
    }

    @PostMapping("/add")
    public Result add(Student student) {
        return studentService.add(student);
    }

    @PostMapping("/edit")
    public Result edit(Student student) {
        return studentService.edit(student);
    }

    @PostMapping("/del")
    public Result del(IdCollection idCollection) {
        return studentService.delete(idCollection);
    }

    @PostMapping("/cc")
    public Result chooseCourse(MapEditor mapEditor) {
        return studentService.chooseCourse(mapEditor);
    }
}
