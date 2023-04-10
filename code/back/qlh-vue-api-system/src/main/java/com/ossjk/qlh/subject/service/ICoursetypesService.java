package com.ossjk.qlh.subject.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.subject.entity.Coursetypes;
import com.ossjk.qlh.subject.vo.CoursetypesVo;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: CoursetypesService
 * @Description:  课程类别-服务类接口
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
public interface ICoursetypesService extends IService<Coursetypes> {

    Page<CoursetypesVo> pageVo(Page<CoursetypesVo> page, QueryWrapper<CoursetypesVo> queryWrapper);
    List<Coursetypes> listChildren(Integer pcode);
    Page<CoursetypesVo> page(Page<CoursetypesVo> page, QueryWrapper<CoursetypesVo> queryWrapper);
}
