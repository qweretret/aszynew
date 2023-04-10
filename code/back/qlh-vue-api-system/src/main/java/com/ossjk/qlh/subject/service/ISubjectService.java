package com.ossjk.qlh.subject.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.subject.entity.Subject;
import com.ossjk.qlh.subject.vo.SubjectVo;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: SubjectService
 * @Description:  专业表-服务类接口
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:52 
 */
public interface ISubjectService extends IService<Subject> {
    List<Subject> selectYldSubByDepart(String dname);
    int updateSubjectByCrer(String crer1, String crer2);
    Page<SubjectVo> pageVo(Page<SubjectVo> page,  Wrapper<SubjectVo> ew);
}
