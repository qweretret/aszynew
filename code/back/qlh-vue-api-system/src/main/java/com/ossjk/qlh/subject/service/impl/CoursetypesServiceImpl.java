package com.ossjk.qlh.subject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.subject.entity.Coursetypes;
import com.ossjk.qlh.subject.mapper.CoursetypesMapper;
import com.ossjk.qlh.subject.service.ICoursetypesService;
import com.ossjk.qlh.subject.vo.CoursetypesVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: CoursetypesServiceImpl
 * @Description: 课程类别-服务实现类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@Service
public class CoursetypesServiceImpl extends ServiceImpl<CoursetypesMapper, Coursetypes> implements ICoursetypesService {


    @Override
    public Page<CoursetypesVo> pageVo(Page<CoursetypesVo> page, QueryWrapper<CoursetypesVo> queryWrapper){
        // 只查根节点

       // queryWrapper.lt("code",1000);
        queryWrapper.orderByAsc("code");
        page =  this.baseMapper.pageVO(page,queryWrapper);

        //加载子节点
        if(page.getRecords()!=null) {
            Coursetypes tmp = null;
            for (CoursetypesVo ct : page.getRecords()) {
                List<Coursetypes> children = this.listChildren(ct.getCode());
                if (children != null && children.size() > 0) {
                    ct.setChildren(children);
                }
            }
        }
        return page;
    }
    @Override
    public Page<CoursetypesVo> page(Page<CoursetypesVo> page, QueryWrapper<CoursetypesVo> queryWrapper){
        // 只查根节点
        queryWrapper.orderByAsc("code");
        page =  this.baseMapper.pageVO(page,queryWrapper);

        //加载子节点
        if(page.getRecords()!=null) {
            Coursetypes tmp = null;
            for (CoursetypesVo ct : page.getRecords()) {
                List<Coursetypes> children = this.listChildren(ct.getCode());
                if (children != null && children.size() > 0) {
                    ct.setChildren(children);
                }
            }
        }
        return page;
    }

    @Override
    public  List<Coursetypes> listChildren(Integer pcode){
        return this.baseMapper.listChildren(pcode);
    }

}
