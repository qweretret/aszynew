package com.ossjk.qlh.coreability.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.coreability.entity.Coremodel;
import com.ossjk.qlh.coreability.mapper.CoremodelMapper;
import com.ossjk.qlh.coreability.service.ICoremodelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: CoremodelServiceImpl
 * @Description: 专业核心能力模型-服务实现类
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */

@Service
public class CoremodelServiceImpl extends ServiceImpl<CoremodelMapper, Coremodel> implements ICoremodelService {
    @Autowired
    private CoremodelMapper coremodelMapper;

    @Override
    public List<String> selectYldUserCore(String dname) {
        return coremodelMapper.selectYldUserCore(dname);
    }

    @Override
    public List<String> selectXldUserCore(String dname) {
        return coremodelMapper.selectXldUserCore(dname);
    }
}
