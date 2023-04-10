package com.ossjk.qlh.subject.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.coursestandards.entity.Coursestandards;
import com.ossjk.qlh.coursestandards.service.ICoursestandardsService;
import com.ossjk.qlh.subject.dto.FileDto;
import com.ossjk.qlh.subject.entity.Docresource;
import com.ossjk.qlh.subject.service.IDocresourceService;
import com.ossjk.qlh.subject.vo.DocresourceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: DocresourceController
 * @Description: Docresource-控制器
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@Api(tags = "资料库信息")
@RestController
@RequestMapping("/subject/docresource")
public class DocresourceController extends BaseController {
	
	@Autowired
	private IDocresourceService iDocresourceService;

	@Autowired
	private ICoursestandardsService coursestandardsService;

	@Autowired
	private ResourceMappersProperties ymlUri;

	@ApiOperation(value = "列表")
//	@RequiresPermissions("")
	@GetMapping("/list")
	public ResponseBean<Page<Docresource>> listByName(Page page,
												@ApiParam(value = "name",required = false) @RequestParam(name = "name",required = false)  String name) {
		QueryWrapper<Docresource> queryWrapper = new QueryWrapper<Docresource>();
		if (StrUtil.isNotBlank(name))
			queryWrapper.like("name", "%" + name + "%");
		return ResponseBean.Success(iDocresourceService.page(page,queryWrapper));
	}

	@ApiOperation(value = "列表BySubject")
//	@RequiresPermissions("")
	@GetMapping("/list2")
	public ResponseBean<List<Docresource>> listBySubject( @ApiParam(value = "subid") @RequestParam(name = "subid") String subid) {
		return ResponseBean.Success(iDocresourceService.selectBySid(subid));
	}

	@ApiOperation(value = "感应查询")
//	@RequiresPermissions("")
	@GetMapping("/intnName")
	public ResponseBean<List<DocresourceVo>> list2(@ApiParam(value = "name",required = true) @RequestParam(name = "name",required = true) String name) {
		return ResponseBean.Success(iDocresourceService.inductionName(name));
	}

	@ApiOperation(value = "添加记录到数据库")
//	@RequiresPermissions("")
	@PostMapping("/save")
	public ResponseBean save(@RequestBody FileDto record, HttpServletRequest request) throws IOException {
		boolean flag = false;
		// getType(e = "类型,1是外链的 或 选择了现有的附件、2 传新的附件、3:课程标准附件
		if(record.getType()==3){
			//取回一个
			Coursestandards csdOrg = this.coursestandardsService.getById(  record.getSid() );
			String pjbztk = csdOrg.getPjbztk();
			JSONArray arr = null;
			if(pjbztk == null){ //第一次
				arr = new JSONArray();
			}else{
				arr = JSONArray.parseArray(pjbztk);
			}
			arr.add(JSONObject.toJSON(record.getVo()));

			csdOrg.setPjbztk(arr.toJSONString());
			this.coursestandardsService.updateById(csdOrg);

		}else{
			 flag = this.iDocresourceService.saveFile(record);
		}


		if (flag) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}




	public String upload(String fileData,String filename, HttpServletRequest request) throws IOException {

		if(StrUtil.isNotBlank(fileData)){
			String encode = fileData.split(",")[1];
			byte[] decode = Base64.getDecoder().decode(encode.getBytes(StandardCharsets.UTF_8));
			List<ResourceMappersProperties.ResourceMapper> resourceMapperList = ymlUri.getMappers();
			Map<String, String> uriMap = resourceMapperList.stream().collect(Collectors.toMap(ResourceMappersProperties.ResourceMapper::getUri, ResourceMappersProperties.ResourceMapper::getFile));
			String rootpath = uriMap.get("/statics/accessories");
			String UUID = java.util.UUID.randomUUID().toString();
			String type = filename.substring(filename.lastIndexOf(".") + 1);
			String aPath =   UUID + "." + type;
			String url = rootpath +   aPath;
			File file = new File(rootpath);

			if (!file.exists()) {
				file.mkdirs();
			}

			OutputStream outputStream = new FileOutputStream(new File(rootpath, aPath));
			outputStream.write(decode);
			outputStream.close();
			return "/"+url;
		}
		return "";
	}

	@ApiOperation(value = "去编辑")
//	@RequiresPermissions("0101010")
	@GetMapping("/toUpdate")
	public ResponseBean<Docresource> toUpdate(@ApiParam(value = "id") @RequestParam(name = "id") String id) {
		Docresource docresource = iDocresourceService.getById(id);
		if (ObjectUtil.isNotNull(docresource)) {
			return ResponseBean.Success(docresource);
		} else {
			return ResponseBean.Fail();
		}
	}


	@ApiOperation(value = "编辑")
//	@RequiresPermissions("")
	@PutMapping("/update")
	public ResponseBean update(@RequestBody Docresource record) {
		if (iDocresourceService.updateById(record)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}

	@ApiOperation(value = "删除")
//	@RequiresPermissions("")
	@DeleteMapping("/remove")
	public ResponseBean remove(@ApiParam(value = "专业ID" ,required = true) @RequestParam(name = "sid") String sid,
							   @ApiParam(value = "附件id",required = true) @RequestParam(name = "id") String id ) {

		if (iDocresourceService.removeBySubject(  sid ,   id)) {
			return ResponseBean.Success();
		} else {
			return ResponseBean.Fail();
		}
	}
}
