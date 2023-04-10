package com.ossjk.qlh.subject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.subject.entity.Course;
import com.ossjk.qlh.subject.mapper.CourseMapper;
import com.ossjk.qlh.subject.service.ICourseService;
import com.ossjk.qlh.subject.vo.Course2VO;
import com.ossjk.qlh.subject.vo.CourseVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: CourseServiceImpl
 * @Description: 课程-服务实现类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    public List<CourseVO> selectByName(QueryWrapper<Course> ew) {
        return this.baseMapper.selectByName(ew);
    }

    public List<Course2VO> selectCoursePlan(@Param("ew") QueryWrapper<Course> ew){
        return this.baseMapper.selectCoursePlan(ew);
    }
}
