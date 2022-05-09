package org.edu.ptu.studentmanager.web;

import lombok.extern.slf4j.Slf4j;
import org.edu.ptu.studentmanager.common.response.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.edu.ptu.studentmanager.common.enums.Errors.INTERNAL_ERROR;

/**
 * Created by Lin Chenxiao on 2021-05-25
 **/
@Slf4j
@RestControllerAdvice
public class ExAdviceController {
    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e) {
        e.printStackTrace();
        return Result.fail(INTERNAL_ERROR);
    }
}
