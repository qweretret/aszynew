package com.ossjk.qlh.coursestandards.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.coursestandards.entity.Coursestandards;
import com.ossjk.qlh.coursestandards.service.ICoursestandardsService;
import com.ossjk.qlh.coursestandards.vo.UpLoadPjbztkVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: CoursestandardsController
 * @Description: Coursestandards-控制器
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */
@Api(tags = "课程标准编制")
@RestController
@RequestMapping("/subject/coursestandards")
@Slf4j
public class CoursestandardsController extends BaseController {
	
	@Autowired
	private ICoursestandardsService iCoursestandardsService;

	@ApiOperation(value = "删除一个附件")
//	@RequiresPermissions("")
	@DeleteMapping("/removePjbztk")
	public ResponseBean removePjbztk(@ApiParam(value = "课程标准id",required = true) @RequestParam(name = "id") String id,
									 @ApiParam(value = "序号",required = true) @RequestParam(name = "flag") Integer flag) {

		if(iCoursestandardsService.removeFile(id,flag))
			return ResponseBean.Success();
		else
			return ResponseBean.Fail();
	}

	@ApiOperation(value = "保存all课程附件")
//	@RequiresPermissions("")
	@PostMapping("/updateAllPjbztk")
	public ResponseBean updateAllPjbztk(@RequestBody @ApiParam(value = "课程标准,传id和pjbztk") Coursestandards dto ) {

		Coursestandards coursestandards = iCoursestandardsService.getById(dto.getId());
		coursestandards.setPjbztk(dto.getPjbztk());

		if (iCoursestandardsService.updateById(coursestandards))
			return ResponseBean.Success();
		else
			return ResponseBean.Fail();

	}

	@ApiOperation(value = "新增一个附件")
//	@RequiresPermissions("")
	@GetMapping("/updatePjbztk")
	public ResponseBean updatePjbztk(@ApiParam(value = "upLoadPjbztkVo") UpLoadPjbztkVo upLoadPjbztkVo,
									 @ApiParam(value = "id") String id) {
		boolean flag;
		Coursestandards coursestandards1 = new Coursestandards();
		coursestandards1.setId(id);
		Coursestandards coursestandards2 = iCoursestandardsService.getById(id);
		//原字符段为空
		if (coursestandards2.getPjbztk() == null || "".equals(coursestandards2.getPjbztk())) {
			String pjbztk = "[" + JSON.toJSONString(upLoadPjbztkVo) + "]";
			coursestandards1.setPjbztk(pjbztk);
			flag = iCoursestandardsService.updateById(coursestandards1);
			//原字段非空
		} else {
			List<UpLoadPjbztkVo> upLoadPjbztkVos = JSON.parseArray(coursestandards2.getPjbztk(), UpLoadPjbztkVo.class);
			upLoadPjbztkVos.add(upLoadPjbztkVo);
			String pjbztks = JSON.toJSONString(upLoadPjbztkVos);
			coursestandards1.setPjbztk(pjbztks);
			flag = iCoursestandardsService.updateById(coursestandards1);
		}
		if (flag)
			return ResponseBean.Success();
		else
			return ResponseBean.Fail();

	}

	@ApiOperation(value = "课程标准-附件列表")
//	@RequiresPermissions("")
	@GetMapping("/listPjbztkVo")
	public ResponseBean<String> listPjbztkVo( @ApiParam(value = "id") @RequestParam("id") String id) {
		Coursestandards cs = iCoursestandardsService.getById(id);
		return ResponseBean.Success(cs.getPjbztk());

	}


	@ApiOperation(value = "列表")
//	@RequiresPermissions("")
	@GetMapping("/list")
	public ResponseBean<List<Coursestandards>> list(@ApiParam(value = "cid") @RequestParam(name = "cid",required = false) String cid,
													@ApiParam(value = "id") @RequestParam(name = "id",required = false) String id,
													@ApiParam(value = "coursename") @RequestParam(name = "coursename",required = false) String coursename,
													Coursestandards coursestandards) {
		QueryWrapper<Coursestandards> queryWrapper = new QueryWrapper<Coursestandards>();

		if (StrUtil.isNotBlank(coursestandards.getCoursename()))
			queryWrapper.like("coursename", "%" + coursestandards.getCoursename() + "%");
		if (ObjectUtil.isNotEmpty(coursestandards.getAbtype()))
			queryWrapper.eq("abtype",   coursestandards.getAbtype()  );
		if(StrUtil.isNotBlank(cid)){
			queryWrapper.eq("cid",cid);
		}
//		if(StrUtil.isNotBlank(coursename)){
//			queryWrapper.eq("coursename",coursename);
//		}
		if(StrUtil.isNotBlank(id)){
			queryWrapper.eq("id",id);
		}
		return ResponseBean.Success(iCoursestandardsService.list(queryWrapper));
	}

	@ApiOperation(value = "添加")
//	@RequiresPermissions("")
	@PostMapping("/save")
	public ResponseBean save(@RequestBody Coursestandards record) {
		if (iCoursestandardsService.insert(record)==1) {
			return ResponseBean.Success(record.getId());
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "去编辑")
//	@RequiresPermissions("")
	@GetMapping("/toUpdate")
	public ResponseBean<Coursestandards> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
		Coursestandards coursestandards = iCoursestandardsService.getById(id);
		if (ObjectUtil.isNotNull(coursestandards)) {
			return ResponseBean.Success(coursestandards);
		} else {
			return ResponseBean.Fail();
		}
	}


	@ApiOperation(value = "编辑")
//	@RequiresPermissions("")
	@PutMapping("/update")
	public ResponseBean update(@RequestBody Coursestandards record) {
		if (iCoursestandardsService.updateById(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "删除")
//	@RequiresPermissions("")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
		if (iCoursestandardsService.removeByIds(Arrays.asList(ids))) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}
}
