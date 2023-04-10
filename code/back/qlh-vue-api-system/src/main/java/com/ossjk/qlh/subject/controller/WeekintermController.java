package com.ossjk.qlh.subject.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.subject.dto.WeekintermDto;
import com.ossjk.qlh.subject.entity.Weekinterm;
import com.ossjk.qlh.subject.service.IWeekintermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: WeekintermController
 * @Description: Weekinterm-控制器
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@Api(tags = "学期教学周")
@RestController
@RequestMapping("/subject/weekinterm")
public class WeekintermController extends BaseController {
	
	@Autowired
	private IWeekintermService iWeekintermService;

	@ApiOperation(value = "列表")
//	@RequiresPermissions("")
	@GetMapping("/list")
	public ResponseBean<Page<Weekinterm>> list(Page page) {
		QueryWrapper<Weekinterm> queryWrapper = new QueryWrapper<Weekinterm>();
		queryWrapper.orderByDesc("year");
		queryWrapper.orderByAsc("term");
		return ResponseBean.Success(iWeekintermService.page(page,queryWrapper));
	}


	@ApiOperation(value = "添加")
//	@RequiresPermissions("")
	@PostMapping("/save")
	public ResponseBean save(@RequestBody WeekintermDto weekintermDto) {


//		List<Weekinterm> weekinterms=new ArrayList<Weekinterm>();
//		int[] years={record.getWeeks1(),record.getWeeks2(),record.getWeeks3()};
//		for (int i = 0; i < 3; i++) {
//			QueryWrapper<Weekinterm> queryWrapper=new QueryWrapper<Weekinterm>();
//			queryWrapper.eq("year",record.getYear());
//			queryWrapper.eq("term",String.valueOf(i+1));
//			Weekinterm weekinterm=iWeekintermService.getOne(queryWrapper);
//			weekinterm.setWeeks(years[i]);
//			weekinterms.add(weekinterm);
//		}

		List<Weekinterm> list = new ArrayList<>();
		Weekinterm weekinterm1 = new Weekinterm();
		Integer year = weekintermDto.getYear();
		if(iWeekintermService.selectByYear(year).size()==3){
			return ResponseBean.Fail("学年重复，请重新输入！");
		}
		weekinterm1.setYear(year);
		weekinterm1.setTerm(1);
		weekinterm1.setWeeks(weekintermDto.getOne());
		list.add(weekinterm1);
		Weekinterm weekinterm2 = new Weekinterm();
		weekinterm2.setYear(year);
		weekinterm2.setTerm(2);
		weekinterm2.setWeeks(weekintermDto.getTwo());
		list.add(weekinterm2);
		Weekinterm weekinterm3 = new Weekinterm();
		weekinterm3.setYear(year);
		weekinterm3.setTerm(3);
		weekinterm3.setWeeks(weekintermDto.getThree());
		list.add(weekinterm3);
		System.out.println( list.size() );

		if (iWeekintermService.saveBatch(list)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "去编辑")
//	@RequiresPermissions("")
	@GetMapping("/toUpdate")
	public ResponseBean<List<Weekinterm>> toUpdate(@ApiParam(value = "year") @RequestParam(name = "year") Integer year) {
		List<Weekinterm> weekinterms = iWeekintermService.selectByYear(year);
		if (ObjectUtil.isNotNull(weekinterms)) {
			return ResponseBean.Success(weekinterms);
		} else {
			return ResponseBean.Fail();
		}
	}


	@ApiOperation(value = "编辑")
//	@RequiresPermissions("")
	@PutMapping("/update")
	public ResponseBean update(@RequestBody WeekintermDto weekintermDto) {

		QueryWrapper<Weekinterm> queryWrapper1 = new QueryWrapper<Weekinterm>();
		QueryWrapper<Weekinterm> queryWrapper2 = new QueryWrapper<Weekinterm>();
		QueryWrapper<Weekinterm> queryWrapper3 = new QueryWrapper<Weekinterm>();
		queryWrapper1.eq("year",weekintermDto.getYear());
		queryWrapper2.eq("year",weekintermDto.getYear());
		queryWrapper3.eq("year",weekintermDto.getYear());
		queryWrapper1.eq("term",1);
		queryWrapper2.eq("term",2);
		queryWrapper3.eq("term",3);
		Weekinterm weekinterm1 = new Weekinterm();
		Weekinterm weekinterm2 = new Weekinterm();
		Weekinterm weekinterm3 = new Weekinterm();
		weekinterm1.setWeeks(weekintermDto.getOne());
		weekinterm2.setWeeks(weekintermDto.getTwo());
		weekinterm3.setWeeks(weekintermDto.getThree());

		if (!iWeekintermService.update(weekinterm1,queryWrapper1)) {
			return ResponseBean.Fail();
		}
		if (!iWeekintermService.update(weekinterm2,queryWrapper2)) {
			return ResponseBean.Fail();
		}
		if (!iWeekintermService.update(weekinterm3,queryWrapper3)) {
			return ResponseBean.Fail();
		}
		return ResponseBean.Success();
	}

	@ApiOperation(value = "删除")
//	@RequiresPermissions("")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "years") @RequestParam(name = "years") Integer[] years) {
		if (iWeekintermService.removeByYears(years)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

}
