package org.edu.ptu.studentmanager.mapper;

import org.apache.ibatis.annotations.*;
import org.edu.ptu.studentmanager.common.basic.BaseMapper;
import org.edu.ptu.studentmanager.common.dao.Course;
import org.edu.ptu.studentmanager.common.dao.CourseDetail;
import org.edu.ptu.studentmanager.common.request.Condition;

import java.util.List;

/**
 * Created by Lin Chenxiao on 2021-05-24
 **/
public interface CourseMapper extends BaseMapper<Course> {
    @Select("select count(id) from `course` where id in (${param1}) and `type`!=#{type}")
    int countInvalidCourseChoice(String ids, int type);

    @Select("select count(id) from `course` where id in (${param1}) and (`type`!=#{type} or `required_grade`>#{grade})")
    int countInvalidCourseChoiceWithGrade(String ids, int type, int grade);

    @Select("select id,name from `course` where type=#{type}")
    List<Course> selectNameByType(int type);

    @Select("select id,name from `course` where type=#{type} and (`required_grade` is null or `required_grade` <= #{grade})")
    List<Course> selectNameByTypeAndGrade(int type, int grade);

    @Select("select b.id from `map_student_course` as a left join `course` as b on a.course_id = b.id where a.student_id=#{id}")
    List<Integer> selectIdByStudentId(int id);

    @Select("select b.id from `map_major_course` as a left join `course` as b on a.course_id = b.id where a.major_id=#{id}")
    List<Integer> selectIdByMajorId(int id);

    @Select("select count(b.id) from `map_student_course` as a left join `student` as b on a.student_id = b.id where a.course_id=#{id} and b.admission_year>#{year}")
    int countUnqualifiedStudent(int id, int year);

    @Select("select count(b.id) from `map_student_course` as a left join `course` as b on a.course_id = b.id where a.student_id=#{id} and b.required_grade>#{grade}")
    int countUnqualifiedCourse(int id, int grade);

    @Override
    @Select("select * from `course` where id=#{id}")
    Course selectById(int id);

    @Override
    @Select("select * from `course` where ${column}=#{value} order by id limit #{offset},#{limit}")
    List<CourseDetail> select(Condition condition);

    @Override
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Integer.class)
    @Insert("insert into `course`(name,type,required_grade,academic_hour,credit) values(#{name},#{type},#{requiredGrade},#{academicHour},#{credit})")
    int add(Course course);

    @Override
    @Update("update `course` set name=#{name},type=#{type},required_grade=#{requiredGrade},academic_hour=#{academicHour},credit=#{credit} where id = #{id}")
    int update(Course course);
}
