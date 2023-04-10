package com.ossjk.qlh.subject.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.ossjk.qlh.subject.entity.Subjecttypes;
import com.ossjk.qlh.subject.mapper.SubjecttypesMapper;
import com.ossjk.qlh.subject.service.ISubjecttypesService;
import com.ossjk.qlh.subject.vo.SubjecttypesVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: SubjecttypesServiceImpl
 * @Description: 专业类别表,维护一个4级树，类别[高职] - [大类名称] - [二级类名称] - [专业名称]-服务实现类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@Service
public class SubjecttypesServiceImpl extends ServiceImpl<SubjecttypesMapper, Subjecttypes> implements ISubjecttypesService {

    @Override
    public List<SubjecttypesVo> typeList() {
        Map<String,SubjecttypesVo> typeTree = new HashMap<>();
         List<Subjecttypes> list = this.baseMapper.selectList(new QueryWrapper<Subjecttypes>());
//         .orderByAsc("level","idex")
         for(Subjecttypes item : list){
             SubjecttypesVo temp = new SubjecttypesVo();
             BeanUtils.copyProperties(item, temp);
             typeTree.put(item.getId(),temp);
         }
//         list = null;
         typeTree.entrySet().forEach(item->{
             SubjecttypesVo type = item.getValue();
             SubjecttypesVo typevo = typeTree.get(type.getPid());
             if(typevo != null){
                 typevo.getChildren().add(type);
             }
         });
         List<SubjecttypesVo> rel = new ArrayList<>();
        typeTree.entrySet().forEach(item->{
            if(item.getValue().getLevel() != null && item.getValue().getLevel() == 0){
                rel.add(item.getValue());
            }
        });
        return rel;
    }


    @Override
    public boolean removeChildrenById(Serializable id) {
        List<Subjecttypes> subjecttypes = listChildrenById(id);
        List<String> ids = subjecttypes.stream().map((t) -> {
            return t.getId();
        }).collect(Collectors.toList());
        return SqlHelper.retBool(baseMapper.deleteBatchIds(ids));
    }

    @Override
    public boolean removeChildrenByIds(Collection<? extends Serializable> idList) {
        if (ObjectUtil.isNotEmpty(idList)) {
            for (Serializable id : idList) {
                if (!this.removeById(id)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Subjecttypes> listChildrenById(Serializable id) {
        List<Subjecttypes> subjecttypes = null;
        Subjecttypes psubjecttypes = baseMapper.selectById(id);
        if (ObjectUtil.isNotEmpty(psubjecttypes)) {
            subjecttypes = new ArrayList();
            subjecttypes.add(psubjecttypes);
            listChildrenByPid(psubjecttypes.getId(), subjecttypes);

        }
        return subjecttypes;
    }


    private List<Subjecttypes> listChildrenByPid(Serializable pid, List<Subjecttypes> parent) {
        if (ObjectUtil.isEmpty(parent)) {
            parent = new ArrayList<Subjecttypes>();
        }
        QueryWrapper<Subjecttypes> queryWrapper = new QueryWrapper();
        List<Subjecttypes> children = baseMapper.selectList(queryWrapper.eq("pid", pid));
        if (ObjectUtil.isNotEmpty(children)) {
            parent.addAll(children);
            for (Subjecttypes subjecttypes : children) {
                listChildrenByPid(subjecttypes.getId(), parent);
            }
        }

        return parent;
    }

    @Override
    public List<Subjecttypes> listByUid(String uid) {
        return null;
    }
}
