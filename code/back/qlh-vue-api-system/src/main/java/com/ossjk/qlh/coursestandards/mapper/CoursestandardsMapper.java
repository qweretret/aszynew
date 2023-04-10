package com.ossjk.qlh.coursestandards.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.coursestandards.entity.Coursestandards;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.mapper
 * @ClassName: CoursestandardsMapper
 * @Description: 课程标准编制-Mapper
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */

public interface CoursestandardsMapper extends BaseMapper<Coursestandards> {

      @Select("select id,coursename as name from coursestandards where cid=#{cid}")
      List<Map<String,String>> findIdsByCid(@Param("cid") String cid);
}
