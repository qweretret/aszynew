package com.ossjk.qlh.trainplan.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.coreability.entity.Coremodel;
import com.ossjk.qlh.coreability.service.ICoremodelService;
import com.ossjk.qlh.subject.entity.Coursetimeplan;

import com.ossjk.qlh.subject.entity.Subject;
import com.ossjk.qlh.subject.service.ISubjectService;
import com.ossjk.qlh.trainplan.entity.Trainingplan;
import com.ossjk.qlh.trainplan.service.ITrainingplanService;
import com.ossjk.qlh.trainplan.vo.CoursetimeplanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: TrainingplanController
 * @Description: Trainingplan-控制器
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */
@Api(tags = "人才培养方案")
@RestController
@RequestMapping("/subject/trainingplan")
public class TrainingplanController extends BaseController {
	@Autowired
	private ITrainingplanService iTrainingplanService;
	@Autowired
	private ICoremodelService iCoremodelService;
	@Autowired
	private ISubjectService iSubjectService;

//	@RequiresPermissions(value = { "03000011" })
	@ApiOperation(value = "查询一个")
//	@RequiresPermissions("")
	@GetMapping("/list")
	public ResponseBean<Trainingplan> getOneByCid(@ApiParam(value = "cid",required = true) @RequestParam(name = "cid",required = true) String cid) {
		QueryWrapper<Trainingplan> queryWrapper = new QueryWrapper<Trainingplan>();
        queryWrapper.eq("cid",cid);

		Trainingplan plan = iTrainingplanService.getOne(queryWrapper);
		//取回专业
		Coremodel cmd = iCoremodelService.getById(cid);
		Subject sbj = iSubjectService.getById(cmd.getSid());

		JSONObject  json = null;
		//第一次进入创建人才培养方案时
		if(plan==null){
			plan = new Trainingplan();
			json = new JSONObject();

			JSONArray step4 = new JSONArray();
			JSONObject step4obj = new JSONObject();
			step4obj.put("sCategories",sbj.getSubtype1());
			step4obj.put("sClass",sbj.getSubtype2());
			step4obj.put("dyhy","");
			step4obj.put("zyzhlb","");
			step4obj.put("zygw","");
			step4obj.put("zyzs","");
			step4.add(step4obj);
			json.put("step4",step4);

			JSONArray step5 = new JSONArray();
			JSONObject step5obj = new JSONObject();
			step5obj.put("rcpymb","");
			step5obj.put("szyq","");
			step5obj.put("zsyq","");
			step5obj.put("nlyq","");
			step5obj.put("byszbd", new JSONArray());
			step5obj.put("zbdgl", new JSONArray());
			step5.add(step5obj);
			json.put("step5",step5);

			JSONObject step6 = new JSONObject();
			JSONArray cors = new JSONArray();
			step6.put("cors",cors);
			step6.put("remarks","");
			json.put("step6",step6);

			JSONArray step8 = new JSONArray();
			JSONObject step8obj = new JSONObject();
			step8obj.put("szdw","");
			step8obj.put("jxss","");
			step8obj.put("jxzy","");
			step8obj.put("jxff","");
			step8obj.put("xxpj","");
			step8obj.put("zlgl","");
			step8.add(step8obj);
			json.put("step8",step8);

			JSONArray step9 = new JSONArray();
			JSONObject step9obj = new JSONObject();
			step9obj.put("requirement","");
			step9.add(step9obj);
			json.put("step9",step9);
		}else{
			 json = (JSONObject) JSONObject.parseObject(plan.getRcplan());
			 JSONArray step4 = 	 json.getJSONArray("step4");
			 JSONObject step4obj = step4.getJSONObject(0);
			 step4obj.put("sCategories",sbj.getSubtype1());
			 step4obj.put("sClass",sbj.getSubtype2());
		}
		//强制同步专业信息
			plan.setCid(cid);
			json.put("cid",cid);
			json.put("subname",sbj.getName());
			json.put("code",sbj.getCode());
			json.put("enterNd",sbj.getStutype());
			json.put("xynx",sbj.getYear());

			plan.setRcplan(json.toJSONString());

		return ResponseBean.Success( plan );
	}

//	@RequiresPermissions(value = { "03000012" })
	@ApiOperation(value = "添加")
//	@RequiresPermissions("")
	@PostMapping("/save")
	public ResponseBean save(@RequestBody Trainingplan record) {
		boolean isOK = iTrainingplanService.save(record);
		if (  isOK  ) {
			//ResponseBean<Trainingplan> bean   = this.getOneByCid(record.getCid());
			return ResponseBean.Success(  );
		} else {
			return ResponseBean.Fail();
		}
	}

// 	@RequiresPermissions(value = { "03000013" })
	@ApiOperation(value = "去编辑")
//	@RequiresPermissions("")
	@GetMapping("/toUpdate")
	public ResponseBean<Trainingplan> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
		Trainingplan trainingplan = iTrainingplanService.getById(id);
		if (ObjectUtil.isNotNull(trainingplan)) {
			return ResponseBean.Success(trainingplan);
		} else {
			return ResponseBean.Fail();
		}
	}


//	@RequiresPermissions(value = { "03000014" })
	@ApiOperation(value = "编辑")
//	@RequiresPermissions("")
	@PutMapping("/update")
	public ResponseBean update(@RequestBody Trainingplan record) {
		if (iTrainingplanService.updateById(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

//	@RequiresPermissions(value = { "03000015" })
	@ApiOperation(value = "删除")
//	@RequiresPermissions("")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "ids") @RequestParam(name = "ids") String[] ids) {
		if (iTrainingplanService.removeByIds(Arrays.asList(ids))) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}
//	@RequiresPermissions(value = { "03000016" })
	@ApiOperation("获取教学进程总体安排")
	@GetMapping("/getCourseTimePlan")
	public ResponseBean<ArrayList<CoursetimeplanVO>> getYear(@ApiParam(value = "cids") @RequestParam(name = "cids")List<String> cids, String grade){
		ResponseBean<ArrayList<CoursetimeplanVO>> coursetimeplans = iTrainingplanService.getCoursetimeplans(cids,grade);
		return coursetimeplans;
	}

	//	@RequiresPermissions(value = { "03000017" })
	@ApiOperation(value = "更新或保存课程时间计划")
	@PutMapping("/updataCourseTimePlan")
	public ResponseBean updataCourseTimePlan(@RequestBody List<Coursetimeplan> record){
		ResponseBean responseBean = iTrainingplanService.updataCourseTimePlan(record);

		return responseBean;
	}



}
