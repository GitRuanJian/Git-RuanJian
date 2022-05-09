package org.edu.ptu.studentmanager.common.basic;

import org.edu.ptu.studentmanager.common.request.Condition;

import java.util.List;
/**
 * Created by Lin Chenxiao on 2021-05-24
 **/
public interface BaseMapper<T> {
    T selectById(int id);

    List<? extends T> select(Condition condition);

    int add(T data);

    int update(T data);
}
