package com.ossjk.qlh.subject.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.subject.entity.Coursetypes;
import com.ossjk.qlh.subject.service.ICoursetypesService;
import com.ossjk.qlh.subject.vo.CoursetypesVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
/** 
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: CoursetypesController
 * @Description: Coursetypes-控制器
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@Api(tags = "课程类别")
@RestController
@RequestMapping("/subject/coursetypes")
public class CoursetypesController extends BaseController {
	
	@Autowired
	private ICoursetypesService iCoursetypesService;

	@ApiOperation(value = "列表")
	@RequiresPermissions("102102")
	@GetMapping("/list")
	public ResponseBean<Page<CoursetypesVo>> list(Page page,
												  @ApiParam(value = "类型名称") @RequestParam(name = "name", required = false) String name,
												  @ApiParam(value = "类型code") @RequestParam(name = "code", required = false) String code) {

		QueryWrapper<CoursetypesVo> queryWrapper = new QueryWrapper<CoursetypesVo>();
		//第一级
		queryWrapper.lt("code",100);
        Boolean flag = false;
		if (!StrUtil.isBlank(name)) {
			queryWrapper.like("name", name);
			flag = true;
		}
		if (!StrUtil.isBlank(code)) {
			queryWrapper.like("code", code);
			flag = true;
		}
		if(flag){
			return ResponseBean.Success(iCoursetypesService.page(page, queryWrapper));
		}else{
			return ResponseBean.Success(iCoursetypesService.pageVo(page, queryWrapper));
		}
	}

	@ApiOperation(value = "添加")
	@RequiresPermissions("10210210")
	@PostMapping("/save")
	public ResponseBean save(@RequestBody Coursetypes record) {
		if (iCoursetypesService.save(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "去编辑")
	@RequiresPermissions("102102")
	@GetMapping("/toUpdate")
	public ResponseBean<Coursetypes> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
		Coursetypes coursetypes = iCoursetypesService.getById(id);
		if (ObjectUtil.isNotNull(coursetypes)) {
			return ResponseBean.Success(coursetypes);
		} else {
			return ResponseBean.Fail();
		}
	}


	@ApiOperation(value = "编辑")
	@RequiresPermissions("10210220")
	@PutMapping("/update")
	public ResponseBean update(@RequestBody Coursetypes record) {
		if (iCoursetypesService.updateById(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "删除")
	@RequiresPermissions("10210230")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
		if (iCoursetypesService.removeByIds(Arrays.asList(ids))) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}
}
