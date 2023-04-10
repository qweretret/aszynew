package com.ossjk.qlh.subject.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.subject.entity.Course;
import com.ossjk.qlh.subject.vo.Course2VO;
import com.ossjk.qlh.subject.vo.CourseVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: CourseService
 * @Description:  课程-服务类接口
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
public interface ICourseService extends IService<Course> {

    List<CourseVO> selectByName(QueryWrapper<Course> ew);

     List<Course2VO> selectCoursePlan(@Param("ew") QueryWrapper<Course> ew);
}
