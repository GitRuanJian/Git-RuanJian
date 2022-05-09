package org.edu.ptu.studentmanager.mapper;

import org.apache.ibatis.annotations.*;

/**
 * Created by Lin Chenxiao on 2021-05-24
 **/
public interface CommonMapper {
    @Select("select count(id) from `${param1}` where id=#{id}")
    int exists(String table, int id);

    @Select("select count(id) from `${param1}` where `${param2}`=#{id}")
    int existsColumnId(String table, String column, int id);

    @Select("select count(id) from `${param1}`")
    long count(String table);

    @Select("select count(id) from `${param1}` where trim(`${param2}`)=#{param3}")
    int countColumn(String table, String column, String data);

    @Select("select count(id) from `${param1}` where `${param2}` in (${param3})")
    int countColumnIn(String table, String column, String in);

    @Delete("delete from `${param1}` where `id` in (${param2})")
    int deleteByIds(String table, String ids);

    @Delete("delete from `${param1}` where `${param2}`=#{id}")
    int deleteByColumnId(String table, String column, int id);

    @Delete("delete from `${param1}` where `${param2}` in (${param3})")
    int deleteColumnIn(String table, String column, String ids);

    @Insert("insert into `${param1}`(${param2}) values${param3}")
    int insertValues(String table, String columns, String values);
}
