package com.ossjk.qlh.coursestandards.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.coursestandards.entity.Coursestandards;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: CoursestandardsService
 * @Description:  课程标准编制-服务类接口
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */
public interface ICoursestandardsService extends IService<Coursestandards> {

    List<Map<String,String>> findIdsByCid(String cid);
    int insert(Coursestandards c);

    boolean removeFile(String csid,Integer idx);
}
