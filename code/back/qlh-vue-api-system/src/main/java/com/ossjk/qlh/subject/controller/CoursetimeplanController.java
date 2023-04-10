package com.ossjk.qlh.subject.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.subject.entity.Courseplan;
import com.ossjk.qlh.subject.entity.Coursetimeplan;
import com.ossjk.qlh.subject.service.ICourseplanService;
import com.ossjk.qlh.subject.service.ICoursetimeplanService;
import com.ossjk.qlh.subject.service.IWeekintermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Copyright  2022-04-06 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: CoursetimeplanController
 * @Description: -控制器
 * @author: huang
 * @date:  2022-04-06 19:52:52 
 */
@Api(tags = "课程时间安排")
@RestController
@RequestMapping("/subject/coursetimeplan")
public class CoursetimeplanController extends BaseController {
	
	@Autowired
	private ICoursetimeplanService iCoursetimeplanService;
	@Autowired
	private ICourseplanService iCourseplanService;
	@Autowired
	private IWeekintermService iWeekintermService;

	@ApiOperation(value = "列表")
//	@RequiresPermissions("")
	@GetMapping("/list")
	public ResponseBean<Map> list(@ApiParam(value = "课程id") @RequestParam(name = "cid") String cid,
								  @ApiParam(value = "年级",required = false) @RequestParam(name = "grade",required = false)Integer grade,
								  @ApiParam(value = "专业ID",required = true) @RequestParam(name = "sid",required = true) String sid
								  ) {

		Map data = new HashMap<>();

		//取回一个课程计划
		QueryWrapper<Courseplan> queryWrapper2 = new QueryWrapper<Courseplan>();
		if (StrUtil.isNotBlank(cid)) {
			queryWrapper2.eq("csid", cid);
		}
		if (StrUtil.isNotBlank(sid)) {
			queryWrapper2.eq("sid", sid);
		}

		data.put("courseplan",iCourseplanService.getOne(queryWrapper2));

		//第二个请求学期周的数据
		if (ObjectUtil.isEmpty( grade)) {
			//默认    如果当前<9月,本年  否则  明年
			java.util.Calendar cldr = java.util.Calendar.getInstance();
			long now = cldr.getTimeInMillis();
			cldr.set(Calendar.MONTH, 8);
			cldr.set(Calendar.DATE, 1);

			grade = cldr.get(Calendar.YEAR);
			if (now > cldr.getTimeInMillis()) {  //如果当前>9月,  明年
				grade  += 1;
			}
		}

		data.put("weekTerms",iWeekintermService.selectAllYear(grade,sid));

		//第三个请求的数据
		QueryWrapper<Coursetimeplan> queryWrapper = new QueryWrapper<Coursetimeplan>();
		if (StrUtil.isNotBlank(cid)){
			queryWrapper.eq("cid",cid).eq("grade",grade);
		}
		queryWrapper.orderByAsc("year");
		queryWrapper.orderByAsc("term");
		data.put("courseTMplan",iCoursetimeplanService.list(queryWrapper));

		return ResponseBean.Success(data);
	}

	@ApiOperation(value = "添加")
//	@RequiresPermissions("")
	@PostMapping("/save")
	public ResponseBean save(@RequestBody List<Coursetimeplan> record) {

		if (iCoursetimeplanService.saveBatch(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "去编辑")
//	@RequiresPermissions("")
	@GetMapping("/toUpdate")
	public ResponseBean<Coursetimeplan> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
		Coursetimeplan coursetimeplan = iCoursetimeplanService.getById(id);
		if (ObjectUtil.isNotNull(coursetimeplan)) {
			return ResponseBean.Success(coursetimeplan);
		} else {
			return ResponseBean.Fail();
		}
	}


	@ApiOperation(value = "编辑")
//	@RequiresPermissions("")
	@PutMapping("/update")
	public ResponseBean update(@RequestBody List<Coursetimeplan> record) {
		System.out.println(record);
		if (iCoursetimeplanService.updateBatchById(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "删除")
//	@RequiresPermissions("")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
		if (iCoursetimeplanService.removeByIds(Arrays.asList(ids))) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}
}
