package com.ossjk.qlh.subject.controller;

import java.util.Arrays;
import java.util.List;

import com.ossjk.qlh.subject.vo.Course2VO;
import com.ossjk.qlh.subject.vo.CourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.subject.entity.Course;
import com.ossjk.qlh.subject.service.ICourseService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Copyright 2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: CourseController
 * @Description: Course-控制器
 * @author: wanghaohui
 * @date: 2022-03-09 15:54:53
 */
@Api(tags = "课程管理")
@RestController
@RequestMapping("/subject/course")
public class CourseController extends BaseController {

    @Autowired
    private ICourseService iCourseService;

    @ApiOperation(value = "列表")
    // @RequiresPermissions("")
    @GetMapping("/list")
    public ResponseBean<List<Course>> list(
            @ApiParam(value = "课程代码") @RequestParam(name = "code", required = false) String code,
            @ApiParam(value = "课程名称") @RequestParam(name = "name", required = false) String name,
            @ApiParam(value = "课程类别") @RequestParam(name = "type", required = false) String type,
            @ApiParam(value = "专业id") @RequestParam(name = "subId", required = false) String subId) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>();

        if (!StrUtil.isBlank(code)) {
            queryWrapper.like("code", code);
        }
        if (!StrUtil.isBlank(name)) {
            queryWrapper.like("name", name);
        }
        if (!StrUtil.isBlank(type)) {
            queryWrapper.like("type", type);
        }
        if (!StrUtil.isBlank(subId)) {
            queryWrapper.eq("subjectid", subId);
        }

        return ResponseBean.Success(iCourseService.list( queryWrapper));
    }

    @ApiOperation(value = "列表")
    // @RequiresPermissions("")
    @GetMapping("/list2")
    public ResponseBean<List<Course2VO>> list2(
            @ApiParam(value = "课程代码") @RequestParam(name = "code", required = false) String code,
            @ApiParam(value = "课程名称") @RequestParam(name = "name", required = false) String name,
            @ApiParam(value = "课程类别") @RequestParam(name = "type", required = false) String type,
            @ApiParam(value = "专业id") @RequestParam(name = "subId", required = false) String subId) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>();
        if (!StrUtil.isBlank(code)) {
            queryWrapper.like("code", code);
        }
        if (!StrUtil.isBlank(name)) {
            queryWrapper.like("name", name);
        }
        if (!StrUtil.isBlank(type)) {
            queryWrapper.like("type", type);
        }
        if (!StrUtil.isBlank(subId)) {
            queryWrapper.eq("subjectid", subId);
        }

        return ResponseBean.Success(iCourseService.selectCoursePlan(queryWrapper));
    }


    //感应查询课程
    @ApiOperation(value = "通过名称感应查询课程")
    // @RequiresPermissions("")
    @GetMapping("/list3")
    public   ResponseBean<List<CourseVO>>  inductionName(@ApiParam(value = "课程名称") @RequestParam(name = "name", required = false) String name ){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<Course>();
        if (!StrUtil.isBlank(name)) {
            queryWrapper.like("name", name);
        }
        return ResponseBean.Success(iCourseService.selectByName(queryWrapper));
    }


    @ApiOperation(value = "添加")
    // @RequiresPermissions("")
    @PostMapping("/save")
    public ResponseBean save(@RequestBody Course record) {

        if (iCourseService.save(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }


    @ApiOperation(value = "去编辑")
    // @RequiresPermissions("")
    @GetMapping("/toUpdate")
    public ResponseBean<Course> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
        Course course = iCourseService.getById(id);
        if (ObjectUtil.isNotNull(course)) {
            return ResponseBean.Success(course);
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "编辑")
    // @RequiresPermissions("")
    @PutMapping("/update")
    public ResponseBean update(@RequestBody Course record) {
        if (iCourseService.updateById(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "删除")
    // @RequiresPermissions("")
    @DeleteMapping("/remove")
    public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
        if (iCourseService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }
}
