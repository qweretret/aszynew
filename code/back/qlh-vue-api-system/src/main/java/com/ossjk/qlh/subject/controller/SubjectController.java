package com.ossjk.qlh.subject.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.config.mvc.WxminipappProperties;
import com.ossjk.config.mvc.XxxProperties;
import com.ossjk.qlh.system.entity.Department;
import com.ossjk.qlh.system.service.IDepartmentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.subject.entity.Subject;
import com.ossjk.qlh.subject.service.ISubjectService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Copyright 2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: SubjectController
 * @Description: Subject-控制器
 * @author: wanghaohui
 * @date: 2022-03-09 15:54:52
 */
@Api(tags = "专业设置")
@RestController
@RequestMapping("/subject/subject")
public class SubjectController extends BaseController {

    @Autowired
    private ISubjectService iSubjectService;

    @Autowired
    private ISystemCommonApi iSystemCommonApi;

    @Autowired
    private ResourceMappersProperties ymlUri;

    @Autowired
    private XxxProperties xProperties;

    @Autowired
    private WxminipappProperties wxminipappProperties;

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "列表")
    @RequiresPermissions("102101")
    @GetMapping("/list")
    public ResponseBean<Page<Subject>> list(Page page,@ApiParam(value = "专业名称") @RequestParam(name = "name",required = false) String name, HttpServletRequest request) {
        //取得当前用户
        LoginUser u = iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));
        //当前用户角色
        String role =  u.getRoleCode().iterator().next();
        //当前用户所在部门
        String dept =   u.getDepartmentCode().iterator().next();

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<Subject>();
        if(StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }

        return ResponseBean.Success(iSubjectService.page(page, queryWrapper));
    }


    @ApiOperation(value = "全部列表")
    @RequiresPermissions("102101")
    @GetMapping("/allList")
    public ResponseBean<List<Subject>> list() {
        return ResponseBean.Success(iSubjectService.list());
    }

    @ApiOperation(value = "添加")
    @RequiresPermissions("10210102")
    @PostMapping("/save")
    public ResponseBean save(@RequestBody Subject record,HttpServletRequest request) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<Subject>();
        String type = record.getType();
        String subType1 = record.getSubtype1();
        String subType2 = record.getSubtype2();
        String name = record.getName();

        //部门
        LoginUser me = iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));
        record.setDcode( me.getDepartmentCode().iterator().next() );

        if (StrUtil.isNotBlank(type)) {
            queryWrapper.eq("type", type);
        }
        if (StrUtil.isNotBlank(subType1)) {
            queryWrapper.eq("subtype1", subType1);
        }
        if (StrUtil.isNotBlank(subType2)) {
            queryWrapper.eq("subtype2", subType2);
        }
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.eq("name", name);
        }
        if (ObjectUtil.isNotEmpty(iSubjectService.getOne(queryWrapper))) {
            return ResponseBean.Fail("专业已添加，请重新选择！");
        }
        if (iSubjectService.save(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "去编辑")
    @RequiresPermissions("102101")
    @GetMapping("/toUpdate")
    public ResponseBean<Map> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {

        //加载备选的教研室
        List<Department> depts =    this.departmentService.selectAllJys();
        Subject subject = iSubjectService.getById(id);

        Map datas = new HashMap();
        datas.put("data",subject);
        datas.put("depts",  depts );

        if (ObjectUtil.isNotNull(subject)) {
            return ResponseBean.Success(datas);
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "查询全部教研室")

    @GetMapping("/allJys")
    public ResponseBean<List<Department> > allJys() {

        //加载备选的教研室
        List<Department> depts =    this.departmentService.selectAllJys();

        if (ObjectUtil.isNotNull(depts)) {
            return ResponseBean.Success(depts);
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "编辑")
    @RequiresPermissions("10210102")
    @PutMapping("/update")
    public ResponseBean update(@RequestBody Subject record) {
        if (iSubjectService.updateById(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "删除")
    @RequiresPermissions("10210103")
    @DeleteMapping("/remove")
    public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
        if (iSubjectService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }
}
