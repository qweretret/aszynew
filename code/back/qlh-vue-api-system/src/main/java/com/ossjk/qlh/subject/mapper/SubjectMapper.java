package com.ossjk.qlh.subject.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ossjk.qlh.subject.entity.Subject;
import com.ossjk.qlh.subject.vo.SubjectVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.mapper
 * @ClassName: SubjectMapper
 * @Description: 专业表-Mapper
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:52 
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    List<Subject> selectYldSubByDepart(String dname);

    int updateSubjectByCrer(String crer1, String crer2);

    @Select("select s.*,u.dcode from subject s left join v_user u on s.crer = u.id  ${ew.customSqlSegment} ")
    Page<SubjectVo> pageVo(Page<SubjectVo> page,@Param(Constants.WRAPPER) Wrapper<SubjectVo> ew);

}
