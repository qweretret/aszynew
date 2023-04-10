package com.ossjk.qlh.subject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.subject.entity.Subjecttypes;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.mapper
 * @ClassName: SubjecttypesMapper
 * @Description: 专业类别表,维护一个4级树，类别[高职] - [大类名称] - [二级类名称] - [专业名称]-Mapper
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
public interface SubjecttypesMapper extends BaseMapper<Subjecttypes> {

//    List<Subjecttypes> selectByUid(@Param("uid")String uid);

}
