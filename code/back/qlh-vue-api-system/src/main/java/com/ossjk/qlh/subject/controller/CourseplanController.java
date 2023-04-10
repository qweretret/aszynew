package com.ossjk.qlh.subject.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.api.ISystemCommonApi;
import com.ossjk.core.system.dto.LoginUser;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.subject.entity.Courseplan;
import com.ossjk.qlh.subject.entity.Subject;
import com.ossjk.qlh.subject.service.ICourseplanService;
import com.ossjk.qlh.subject.service.ICoursetimeplanService;
import com.ossjk.qlh.subject.service.ISubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: CourseplanController
 * @Description: Courseplan-控制器
 * @author: wanghaohui
 * @date: 2022-03-09 15:54:53
 */
@Api(tags = "课程安排")
@RestController
@RequestMapping("/subject/courseplan")
public class CourseplanController extends BaseController {

    @Autowired
    private ICourseplanService iCourseplanService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private ISystemCommonApi iSystemCommonApi;

    @Autowired
    private ICoursetimeplanService coursetimeplanService;

    @ApiOperation(value = "列表")
//	@RequiresPermissions("")
    @GetMapping("/list")
    public ResponseBean<Page<Courseplan>> list(@ApiParam(value = "csid",required = false) @RequestParam(name = "csid",required = false) String csid,Page page) {
        QueryWrapper<Courseplan> queryWrapper = new QueryWrapper<Courseplan>();

        if (StrUtil.isNotBlank(csid)) {
            queryWrapper.eq("csid", csid);
        }

        return ResponseBean.Success(iCourseplanService.page(page, queryWrapper));
    }

    @ApiOperation(value = "添加")
//	@RequiresPermissions("")
    @PostMapping("/save")
    public ResponseBean save(@RequestBody Courseplan record) {
        if (iCourseplanService.save(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "去编辑")
//	@RequiresPermissions("")
    @GetMapping("/toUpdate")
    public ResponseBean<Map> toUpdate(@ApiParam(value = "csid") @RequestParam(name = "csid") String csid,
                                      @ApiParam(value = "sid") @RequestParam(name = "sid") String sid  , HttpServletRequest request
    ) {
        LoginUser me = iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));

        QueryWrapper<Courseplan> queryWrapper = new QueryWrapper<Courseplan>();
        if (StrUtil.isNotBlank(csid)) {
            queryWrapper.eq("csid", csid);
        }
        if (StrUtil.isNotBlank(sid)) {
            queryWrapper.eq("sid", sid);
        }
        Courseplan courseplan = iCourseplanService.getOne(queryWrapper);
        if (ObjectUtil.isNull(courseplan)) {
            courseplan = new Courseplan();
            courseplan.setCsid(csid);
            courseplan.setSid(sid);
        }
        Map datas = new HashMap();
        datas.put("data", courseplan);
        //有无更新权限?
        String dcode =  me.getDepartmentCode().iterator().next();
        String role= me.getRoleCode().iterator().next().substring(0,3);
        Subject sb =  subjectService.getById(sid);
        datas.put("isUpdt", false);
        if(sb.getDcode().equals(dcode)){
             //职位
            if(role.equals("jys")){
                datas.put("isUpdt", true);
            }
        }
        if(me.getRoleCode().iterator().next().equals("adm")){
            datas.put("isUpdt", true);
        }

        //备选的年级
        String[] grades = coursetimeplanService.selectGradeByCid(csid);
        if(grades!=null){
            datas.put("grades", grades);
        }

        return ResponseBean.Success(datas);
    }


    @ApiOperation(value = "编辑")
//	@RequiresPermissions("")
    @PutMapping("/update")
    public ResponseBean update(@RequestBody Courseplan record) {
        if (iCourseplanService.updateById(record)) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }

    @ApiOperation(value = "删除")
//	@RequiresPermissions("")
    @DeleteMapping("/remove")
    public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
        if (iCourseplanService.removeByIds(Arrays.asList(ids))) {
            return ResponseBean.Success();
        } else {
            return ResponseBean.Fail();
        }
    }
}
