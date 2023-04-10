package com.ossjk.qlh.subject.controller;



import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.system.annotation.LogOperation;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.subject.entity.Subjecttypes;
import com.ossjk.qlh.subject.service.ISubjecttypesService;
import com.ossjk.qlh.subject.vo.SubjecttypesVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: SubjecttypesController
 * @Description: Subjecttypes-控制器
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@Api(tags = "专业类别管理")
@RestController
@RequestMapping("/subject/subjecttypes")
public class SubjecttypesController extends BaseController {

	@Autowired
	private ISubjecttypesService iSubjecttypesService;
//	@Autowired
//	private ISubjecttypesService subjecttypesService;
//	@Autowired
//	private ISystemCommonApi iSystemCommonApi;

	@LogOperation("列表")
	@ApiOperation(value = "列表")
	@RequiresPermissions(value = { "102105" })
	@GetMapping("/list")
	public ResponseBean<List<Subjecttypes>> list() {
		return ResponseBean.Success(iSubjecttypesService.list(new QueryWrapper<Subjecttypes>().orderByAsc("level","idex")));
	}

	@LogOperation("类型列表")
	@ApiOperation(value = "类型列表")
 	@RequiresPermissions(value = { "102105" })
	@GetMapping("/typeList")
	public ResponseBean<List<SubjecttypesVo>> typeList() {
		return ResponseBean.Success(iSubjecttypesService.typeList()
		);
	}

	@LogOperation("添加")
	@ApiOperation(value = "添加")
 	@RequiresPermissions(value = { "1021050000" })
	@PostMapping("/save")
	public ResponseBean save(@RequestBody Subjecttypes recode) {
		// 防止部门编码重复
//		Subjecttypes subjecttypes = iSubjecttypesService.getOne(new QueryWrapper<Subjecttypes>().eq("code", recode.getCode()));
//		if (ObjectUtil.isNotEmpty(subjecttypes)) {
//			return ResponseBean.Fail("部门标识	重复");
//		}
		if (iSubjecttypesService.save(recode)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@LogOperation("去编辑")
	@ApiOperation(value = "去编辑")
//	@RequiresPermissions(value = { "000000050010" })
	@GetMapping("/toUpdate")
	public ResponseBean<Subjecttypes> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
		Subjecttypes subjecttypes = iSubjecttypesService.getById(id);
		if (ObjectUtil.isNotEmpty(subjecttypes)) {
			return ResponseBean.Success(subjecttypes);
		} else {
			return ResponseBean.Fail();
		}
	}

	@LogOperation("编辑")
	@ApiOperation(value = "编辑")
 	@RequiresPermissions(value = { "1021050000" })
	@PutMapping("/update")
	public ResponseBean update(@RequestBody Subjecttypes recode) {
		if (iSubjecttypesService.updateById(recode)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@LogOperation("删除")
	@ApiOperation(value = "删除")
 	@RequiresPermissions(value = { "1021050005" })
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String ids) {
		if (iSubjecttypesService.removeChildrenById(ids)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

//	@ApiOperation(value = "部门员工列表")
//	@GetMapping("/deptEmps")
//	public ResponseBean<Map> lists(@ApiParam(value = "部门名称")    @RequestParam(name = "dept",required = false)   String dept, HttpServletRequest request) {
//		//封装数据
//		Map data = new HashMap();
//		//  没参数普通与员工默认自己本月的，主管  ：本部门本月的
//		//自己
//		LoginUser usr =iSystemCommonApi.refreshTokenCacheLoginUser(this.getToekn(request));
//		String role = usr.getRoleCode().iterator().next();
//		String roleFlag = role.substring(0,2);
//		List<UserinDeptVo> usrs = new ArrayList<>();
//
//		if("mg".equals(roleFlag) ||"ad".equals(roleFlag)   ) {  //部门负责人
//			//部门内的员工导航
//			Page page2 = new Page();
//			page2.setSize(100L);
//
//			if (roleFlag.equals("ad")) { // 总经办：boss
//				//出现部门导航   二级部门
//				data.put("dpts", departmentService.list(new QueryWrapper<Department>().orderByAsc("sort")));
//			}
//			data.put("emps", iUserService.pageVo(page2, new QueryWrapper<User>().eq("d.name", usr.getDname())));
//
//			//本部门
//			if (StrUtil.isNotBlank(dept)) {
//				if (!"靖凯开源".equals(dept)) {
//					// 加载员工s
//					usrs = iUserService.selectVoByDptName(dept);
//				} else {
//					usrs = iUserService.selectVoByDptName();
//				}
//			} else {
//				if (roleFlag.equals("ad")) { // 总经办：boss
//					usrs = iUserService.selectVoByDptName();
//				}else{
//					usrs = iUserService.selectVoByDptName(usr.getDname());
//				}
//			}
//			UserinDeptVo all = new UserinDeptVo();
//			all.setId("全部人");
//			all.setName("全部人");
//			usrs.add(0, all);
//
//			data.put("emps", usrs);
//		}
//
//		return ResponseBean.Success(data);
//	}



}
