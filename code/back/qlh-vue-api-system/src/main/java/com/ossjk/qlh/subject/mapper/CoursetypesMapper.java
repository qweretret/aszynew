package com.ossjk.qlh.subject.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.subject.entity.Coursetypes;
import com.ossjk.qlh.subject.vo.CoursetypesVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.mapper
 * @ClassName: CoursetypesMapper
 * @Description: 课程类别-Mapper
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
public interface CoursetypesMapper extends BaseMapper<Coursetypes> {

    @Select("select * from coursetypes ${ew.customSqlSegment} ")
    Page<CoursetypesVo> pageVO(Page<CoursetypesVo> page, QueryWrapper<CoursetypesVo> ew);

    //查询某节点的子节点
    @Select("SELECT id,name,  code   FROM coursetypes where SUBSTR(code,1,2)  = #{pcode} and  length(code)=4  order by code")
    List<Coursetypes> listChildren(Integer pcode);

}
