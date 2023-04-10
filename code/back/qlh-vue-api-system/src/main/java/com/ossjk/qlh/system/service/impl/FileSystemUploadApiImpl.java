package com.ossjk.qlh.system.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.ossjk.myUtil.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.config.mvc.ResourceMappersProperties.ResourceMapper;
import com.ossjk.core.constant.Constant;
import com.ossjk.core.system.api.IUploadCommonApi;
import com.ossjk.core.system.dto.UploadDto;
import com.ossjk.core.vo.ResponseBean;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import static cn.afterturn.easypoi.excel.entity.enmus.CellValueType.Date;

/**
 * 
 * Copyright © 2021 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.system.service.impl
 * @ClassName: FileSystemUploadApiImpl
 * @Description: 文件系统上传Api实现类
 * @author: chair
 * @date: 2021年7月24日 下午4:30:52
 */
@Service("fileSystem")
public class FileSystemUploadApiImpl implements IUploadCommonApi {
	@Autowired
	private ResourceMappersProperties mappersProperties;
	@Autowired
	private HttpServletRequest request;

	@Override
	public ResponseBean upload(UploadDto uploadDto) {

		List<ResourceMapper> resourceMappers = mappersProperties.getMappers();
		Optional<ResourceMapper> optional = resourceMappers.stream().filter(t -> {

			return StrUtil.equals(t.getUri(), uploadDto.getUri());
		}).findFirst();

		if (!optional.isPresent()) {
			return ResponseBean.Fail("uri:" + uploadDto.getUri() + ",填写错误。请联系后端人员。");
		}
		// 过滤mappersProperties
		String uuid = RandomUtil.randomUUID();
		//按月份存储3
		String mthPath[] = StringUtil.smt8.format(new Date()).split("-");
		String fileName = null;
		String ext =null;

		//取得后缀
		String orgNm = uploadDto.getOriginalFilename();
		if(orgNm.lastIndexOf(".")>0){
				ext = orgNm.substring(orgNm.lastIndexOf(".")) ;
		}else{
				ext =".file";
		}

		fileName = uuid + "-" +  mthPath[1] +ext ;

		ResourceMapper resourceMapper = optional.get();
		String rootPath = resourceMapper.getFile();

		//月份文件夹
		File folder = new File(rootPath,mthPath[0]);
		if(!folder.exists()){
			folder.mkdirs();
		}
		// 保存到文件里面
		FileUtil.writeFromStream(uploadDto.getInputStream(), new File(rootPath+"/"+mthPath[0],  fileName));

		// 传回前端准备保存到db里面
		String url = uploadDto.getUri() +"/"+mthPath[0]+ "/" + fileName;
		url = getLocalUrl(request) + url;
		return ResponseBean.Success(Constant.RESPONSE_MSG_SUCCESS, url);
	}

	public String getLocalUrl(HttpServletRequest request) {
		String url = "";
		url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		return url;
	}

}
