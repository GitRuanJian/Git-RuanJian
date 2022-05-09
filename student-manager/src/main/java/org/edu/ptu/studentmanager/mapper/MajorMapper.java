package org.edu.ptu.studentmanager.mapper;

import org.apache.ibatis.annotations.*;
import org.edu.ptu.studentmanager.common.basic.BaseMapper;
import org.edu.ptu.studentmanager.common.dao.Major;
import org.edu.ptu.studentmanager.common.dao.MajorDetail;
import org.edu.ptu.studentmanager.common.request.Condition;

import java.util.List;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
public interface MajorMapper extends BaseMapper<Major> {
    @Select("select id,name from `major`")
    List<Major> selectName();

    @Override
    @Select("select * from `major` where id=#{id}")
    Major selectById(int id);

    @Override
    @Select("select * from (select a.id, a.name, a.capacity, a.required_credits, count(b.id) as student_num from `major` as a left join `student` as b on b.major_id = a.id group by a.id order by a.id) as f left join (select c.id, count(e.id) as course_num, sum(e.credit) as total_credits from `major` as c left join `map_major_course` as d on c.id = d.major_id left join `course` as e on d.course_id = e.id group by c.id) as g on f.id = g.id where ${column}=#{value} limit #{offset},#{limit}")
    List<MajorDetail> select(Condition condition);

    @Override
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Integer.class)
    @Insert("insert into `major`(name,capacity,required_credits) values(#{name},#{capacity},#{requiredCredits})")
    int add(Major major);

    @Override
    @Update("update `major` set name=#{name},capacity=#{capacity},required_credits=#{requiredCredits} where id = #{id}")
    int update(Major major);
}
