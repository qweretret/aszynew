package com.ossjk.qlh.subject.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.subject.entity.Subject;
import com.ossjk.qlh.subject.mapper.SubjectMapper;
import com.ossjk.qlh.subject.service.ISubjectService;
import com.ossjk.qlh.subject.vo.SubjectVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: SubjectServiceImpl
 * @Description: 专业表-服务实现类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:52 
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements ISubjectService {
    @Resource
    private SubjectMapper subjectMapper;
    @Override
    public List<Subject> selectYldSubByDepart(String dname) {
        return subjectMapper.selectYldSubByDepart(dname);
    }

    @Override
    public int updateSubjectByCrer(String crer1, String crer2) {
        return subjectMapper.updateSubjectByCrer(crer1,crer2);

    }

    @Override
    public Page<SubjectVo> pageVo(Page<SubjectVo> page, Wrapper<SubjectVo> ew){
        return this.subjectMapper.pageVo(page,ew);
    }
}
