package com.ossjk.qlh.subject.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.subject.entity.Course;
import com.ossjk.qlh.subject.vo.Course2VO;
import com.ossjk.qlh.subject.vo.CourseVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.mapper
 * @ClassName: CourseMapper
 * @Description: 课程-Mapper
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
public interface CourseMapper extends BaseMapper<Course> {

      @Select("select CONCAT(name,'[',code,']') as name,c.id,c.name as name2,c.code,type,level1,level2 from course c where c.id in " +
              " (select max(id) from course group by name,code) ${ew.customSqlSegment}" )
      List<CourseVO> selectByName(@Param("ew") QueryWrapper<Course> ew);

      @Select("select c.id,c.subjectid,c.name,c.code,c.type,c.level1,c.level2,cp.csprop,cp.testmode,cp.credit,cp.period,cp.theoryperiod, cp.cstarget,cp.cscontent,cp.teachneed\n" +
              " from course c left join courseplan cp on c.id = cp.csid ${ew.customSqlSegment}" )
      List<Course2VO> selectCoursePlan(@Param("ew") QueryWrapper<Course> ew);


}
