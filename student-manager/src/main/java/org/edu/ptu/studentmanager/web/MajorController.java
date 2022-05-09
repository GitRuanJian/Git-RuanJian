package org.edu.ptu.studentmanager.web;

import org.edu.ptu.studentmanager.common.dao.Major;
import org.edu.ptu.studentmanager.common.request.Condition;
import org.edu.ptu.studentmanager.common.request.IdCollection;
import org.edu.ptu.studentmanager.common.request.MapEditor;
import org.edu.ptu.studentmanager.common.response.Result;
import org.edu.ptu.studentmanager.service.MajorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Lin Chenxiao on 2021-05-24
 **/
@RestController
@RequestMapping("/j/maj")
public class MajorController {
    @Resource
    private MajorService majorService;

    @PostMapping("/name")
    public Result listNames() {
        return majorService.list();
    }

    @PostMapping("/list")
    public Result list(Condition condition) {
        return majorService.list(condition);
    }

    @PostMapping("/add")
    public Result add(Major major) {
        return majorService.add(major);
    }

    @PostMapping("/edit")
    public Result edit(Major major) {
        return majorService.edit(major);
    }

    @PostMapping("/del")
    public Result del(IdCollection idCollection) {
        return majorService.delete(idCollection);
    }

    @PostMapping("/cc")
    public Result chooseCourse(MapEditor mapEditor) {
        return majorService.chooseCourse(mapEditor);
    }
}
