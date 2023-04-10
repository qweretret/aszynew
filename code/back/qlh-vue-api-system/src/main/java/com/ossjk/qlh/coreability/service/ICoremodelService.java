package com.ossjk.qlh.coreability.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.coreability.entity.Coremodel;

/**
 * Copyright 2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: CoremodelService
 * @Description: 专业核心能力模型-服务类接口
 * @author: huang
 * @date: 2022-03-22 16:30:01
 */

public interface ICoremodelService extends IService<Coremodel> {

    // 院领导
    List<String> selectYldUserCore(String dname);

    // 系领导
    List<String> selectXldUserCore(String dname);
}
