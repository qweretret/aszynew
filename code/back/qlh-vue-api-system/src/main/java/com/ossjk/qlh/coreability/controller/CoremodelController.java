package com.ossjk.qlh.coreability.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.coreability.entity.Coremodel;
import com.ossjk.qlh.coreability.service.ICoremodelService;
import com.ossjk.qlh.subject.entity.Subject;
import com.ossjk.qlh.subject.service.ISubjectService;
import com.ossjk.qlh.subject.service.ISubjecttypesService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Copyright 2022-03-22 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: CoremodelController
 * @Description: Coremodel-控制器
 * @author: huang
 * @date: 2022-03-22 16:30:01
 */
@Api(tags = "专业核心能力模型")
@RestController
@RequestMapping("/subject/coremodel")
public class CoremodelController extends BaseController {

    @Autowired
    private ICoremodelService iCoremodelService;

    @Autowired
    private ISubjectService iSubjectService;

    @Autowired
    private ISubjecttypesService iSubjecttypesService;

    @Autowired
    private ISystemCommonApi iSystemCommonApi;

    @ApiOperation(value = "列表")
    // @RequiresPermissions("1000080")
    @GetMapping("/list")
    public ResponseBean<Page<Coremodel>> list(
            @ApiParam(value = "sid") @RequestParam(name = "sid", required = false) String sid, Page page,
            HttpServletRequest request) {
        QueryWrapper<Coremodel> queryWrapper = new QueryWrapper<Coremodel>();
        if (StrUtil.isNotBlank(sid)) {
            queryWrapper.eq("sid", sid);
        }
        LoginUser u = iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));
//        String code = u.getRoleCode().toString().replace("[", "").replace("]", "").substring(0, 7);
//        QueryWrapper<Subject> queryWrapper2 = new QueryWrapper<Subject>();
//        String bkOrGz = code.substring(0, 2);
//        String ldOrTe = code.substring(4, 6);
//        if (bkOrGz.equals("ad")) {
//            return ResponseBean.Success(iCoremodelService.page(page, queryWrapper));
//        }
//        switch (bkOrGz) {
//        case "gz":
//            queryWrapper2.eq("type", "高职");
//            getSubject(queryWrapper2, ldOrTe);
//            break;
//        case "bk":
//            queryWrapper2.eq("type", "本科");
//            getSubject(queryWrapper2, ldOrTe);
//            break;
//        case "no":
//            queryWrapper2.eq("id", "0");
//            break;
//        default:
//            break;
//        }
//       / Object[] ids = iSubjectService.list(queryWrapper2.select("id")).toArray();
//        queryWrapper.in("sid", ids);
        return ResponseBean.Success(iCoremodelService.page(page, queryWrapper));
    }

    private void getSubject(QueryWrapper<Subject> queryWrapper2, String ldOrTe) {
        switch (ldOrTe) {
        case "ji":
            queryWrapper2.in("subtype1", "信息工程类 ", "计算机类");
            break;
        case "tm":
            queryWrapper2.eq("subtype1", "土木类");
            break;
        case "hg":
            queryWrapper2.eq("subtype1", "化工类");
            break;
        case "gl":
            queryWrapper2.eq("subtype1", "管理运营类");
            break;
        case "ys":
            queryWrapper2.eq("subtype1", "艺术类");
            break;
        }
    }

    /**
     * zcmodellist 权限查看
     *
     * @param sid sid
     * @param page 页面
     * @return {@link ResponseBean}<{@link Page}<{@link Coremodel}>>
     */

    @ApiOperation(value = "列表")
    // @RequiresPermissions("1000080")
    @GetMapping("/zcmodel/list")
    public ResponseBean<Page<Coremodel>> zcmodeLlist(
            @ApiParam(value = "sid") @RequestParam(name = "sid", required = false) String sid, Page page) {
        QueryWrapper<Coremodel> queryWrapper = new QueryWrapper<Coremodel>();
        queryWrapper.select("zcmodel");
        if (StrUtil.isNotBlank(sid)) {
            queryWrapper.eq("sid", sid);
        } else {
            return ResponseBean.Fail();
        }
        return ResponseBean.Success(iCoremodelService.page(page, queryWrapper));
    }

    /**
     * otrmodellist
     *
     * @param sid sid
     * @param page 页面
     * @return {@link ResponseBean}<{@link Page}<{@link Coremodel}>>
     */
    @ApiOperation(value = "列表")
    // @RequiresPermissions("1000080")
    @GetMapping("/otrmodel/list")
    public ResponseBean<Page<Coremodel>> otrmodeLlist(
            @ApiParam(value = "sid") @RequestParam(name = "sid", required = false) String sid, Page page) {
        QueryWrapper<Coremodel> queryWrapper = new QueryWrapper<Coremodel>();
        queryWrapper.select("otrmodel");
        if (StrUtil.isNotBlank(sid)) {
            queryWrapper.eq("sid", sid);
        } else {
            return ResponseBean.Fail();
        }
        return ResponseBean.Success(iCoremodelService.page(page, queryWrapper));
    }

    /**
     * 通过sid
     *
     * @param sid sid
     * @param grade 年级
     * @return {@link ResponseBean}<{@link Coremodel}>
     */
    @ApiOperation(value = "获取单条数据")
    // @RequiresPermissions("1000080")
    @GetMapping("/getBySid")
    public ResponseBean<Coremodel> getBySid(@ApiParam(value = "sid") @RequestParam(name = "sid") String sid,
            @ApiParam(value = "grade") @RequestParam(name = "grade") String grade) {
        QueryWrapper<Coremodel> queryWrapper = new QueryWrapper<Coremodel>();
        queryWrapper.eq("sid", sid);
        queryWrapper.eq("grade", grade);

        return ResponseBean.Success(iCoremodelService.getOne(queryWrapper));
    }

    @ApiOperation(value = "添加")
    // @RequiresPermissions("10000802")
    @PostMapping("/save")
    public ResponseBean save(@RequestBody Coremodel record) {
        QueryWrapper<Coremodel> wrapper = new QueryWrapper<Coremodel>().eq("sid", record.getSid());
        List<Coremodel> list = iCoremodelService.list(wrapper);
        String grade = record.getGrade();
        for (Coremodel coremodel : list) {
            if (coremodel.getGrade().equals(grade)) {
                return ResponseBean.Fail("添加年级重复");
            }
        }
        if (iCoremodelService.save(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    /**
     * 更新
     *
     * @param id id
     * @return {@link ResponseBean}<{@link Coremodel}>
     */
    @ApiOperation(value = "去编辑")
    // @RequiresPermissions("1000080")
    @GetMapping("/toUpdate")
    public ResponseBean<Coremodel> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
        Coremodel coremodel = iCoremodelService.getById(id);
        if (ObjectUtil.isNotNull(coremodel)) {
            return ResponseBean.Success(coremodel);
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "编辑")
    // @RequiresPermissions("1000080")
    @PutMapping("/update")
    public ResponseBean update(@RequestBody Coremodel record) {
        if (iCoremodelService.updateById(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "删除")
    // @RequiresPermissions("10000803")
    @DeleteMapping("/remove")
    public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
        if (iCoremodelService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @Autowired
    private ResourceMappersProperties ymlUri;

    /**
     * 导入
     *
     * @param fnm 文件名
     * @return {@link ResponseBean}<{@link R}>
     */
    @ApiOperation(value = "去确认导入")
    // @RequiresPermissions("")
    @GetMapping("/toImport")
    public ResponseBean toImport(@ApiParam(value = "fnm") @RequestParam(name = "fnm") String fnm) {
        // 读取yuml的配置数据
        List<ResourceMappersProperties.ResourceMapper> resourceMapperList = ymlUri.getMappers();
        Map<String, String> uriMap = resourceMapperList.stream().collect(Collectors.toMap(
                ResourceMappersProperties.ResourceMapper::getUri, ResourceMappersProperties.ResourceMapper::getFile));
        System.out.println(fnm);
        // List<Student> students = iStudentService10.parseStudent(new
        // File(uriMap.get("/statics/excels"), fnm));
        if (true) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }

    }
}
