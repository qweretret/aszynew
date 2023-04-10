package com.ossjk.qlh.coursestandards.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.qlh.coursestandards.entity.Coursestandards;
import com.ossjk.qlh.coursestandards.mapper.CoursestandardsMapper;
import com.ossjk.qlh.coursestandards.service.ICoursestandardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: CoursestandardsServiceImpl
 * @Description: 课程标准编制-服务实现类
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */
@Service
public class CoursestandardsServiceImpl extends ServiceImpl<CoursestandardsMapper, Coursestandards> implements ICoursestandardsService {

    @Autowired
    private ResourceMappersProperties ymlUri;

    public List<Map<String,String>> findIdsByCid(String cid){
        return this.baseMapper.findIdsByCid(cid);
    }

                                     //Mybatis insert默认不取ID，MybatisPlus insert会取ID
    public int insert(Coursestandards c){
        return  this.baseMapper.insert(c);
    }

    @Override
    public boolean removeFile(String csid,Integer idx) {
        Coursestandards coursestandards = this.getById(csid);

        JSONArray arrs = JSONArray.parseArray(coursestandards.getPjbztk());
        JSONObject toRmv = arrs.getJSONObject(idx);
        arrs.remove(toRmv);
        coursestandards.setPjbztk( arrs.toJSONString() );

        boolean b =  updateById(coursestandards);
        //读取物理path
        List<ResourceMappersProperties.ResourceMapper> resourceMapperList = ymlUri.getMappers();
        Map<String, String> uriMap = resourceMapperList.stream().collect(Collectors.toMap(ResourceMappersProperties.ResourceMapper::getUri, ResourceMappersProperties.ResourceMapper::getFile));
        String diskUri = uriMap.get("/statics/zysz");
        File file = new File(diskUri + toRmv.get("url"));
        //存在则删除
        if(file.exists()){
            file.delete();
        }

        return b;

    }

}
