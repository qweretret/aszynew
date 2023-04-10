package com.ossjk.qlh.subject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.subject.entity.Coursetimeplan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Copyright  2022-04-06 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.mapper
 * @ClassName: CoursetimeplanMapper
 * @Description: -Mapper
 * @author: huang
 * @date:  2022-04-06 19:52:52 
 */
public interface CoursetimeplanMapper extends BaseMapper<Coursetimeplan> {


    @Select("select  distinct grade  from coursetimeplan where  cid = #{cid} order  by grade desc")
    String[] selectGradeByCid(@Param("cid") String cid);
}
