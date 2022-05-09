package org.edu.ptu.studentmanager.mapper;

import org.apache.ibatis.annotations.*;
import org.edu.ptu.studentmanager.common.basic.BaseMapper;
import org.edu.ptu.studentmanager.common.dao.Major;
import org.edu.ptu.studentmanager.common.dao.Student;
import org.edu.ptu.studentmanager.common.dao.StudentDetail;
import org.edu.ptu.studentmanager.common.request.Condition;

import java.util.List;

/**
 * Created by Lin Chenxiao on 2021-05-23
 **/
public interface StudentMapper extends BaseMapper<Student> {
    @Select("select * from `student`")
    List<StudentDetail> select1();

    @Select("select * from `student` where ${ids}")
    List<Student> select2(String ids);

    @Override
    @Select("select * from `student` where id=#{id}")
    Student selectById(int id);

    @Override
    @Select("select id, name, number, admission_year, major_id, major, required_credits, sum(course_num) as course_num, sum(total_academic_hour) as total_academic_hour, sum(total_credits) as total_credits from ((select a.id, a.name, a.number, a.admission_year, a.major_id, c.name as major, c.required_credits, count(f.id) as course_num, sum(f.academic_hour) as total_academic_hour, sum(f.credit) as total_credits from `student` as a left join `major` as c on a.major_id = c.id left join `map_major_course` as d on a.major_id=d.major_id left join `course` as f on d.course_id = f.id group by a.id) union all (select a.id, a.name, a.number, a.admission_year, a.major_id, c.name as major, c.required_credits, count(f.id) as course_num, sum(f.academic_hour) as total_academic_hour, sum(f.credit) as total_credits from `student` as a left join `major` as c on a.major_id = c.id left join `map_student_course` as e on e.student_id = a.id left join `course` as f on e.course_id = f.id group by a.id)) as t where ${column}=#{value} group by id order by id limit #{offset},#{limit}")
    List<StudentDetail> select(Condition condition);

    @Override
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Integer.class)
    @Insert("insert into `student`(name,number,admission_year,major_id) values(#{name},#{number},#{admissionYear},#{majorId})")
    int add(Student student);

    @Override
    @Update("update `student` set name=#{name},number=#{number},admission_year=#{admissionYear},major_id=#{majorId} where id = #{id}")
    int update(Student student);
}
