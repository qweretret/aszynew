package com.ossjk.qlh.subject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.subject.entity.Subjecttypes;
import com.ossjk.qlh.subject.vo.SubjecttypesVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: SubjecttypesService
 * @Description:  专业类别表,维护一个4级树，类别[高职] - [大类名称] - [二级类名称] - [专业名称]-服务类接口
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
public interface ISubjecttypesService extends IService<Subjecttypes> {

    List<SubjecttypesVo> typeList();

    /**
     * 根据id删除子节点（包含自身）
     *
     * @param id
     * @return
     */
    boolean removeChildrenById(Serializable id);

    /**
     * 根据ids删除子节点（包含自身）
     *
     * @param idList
     * @return
     */
    boolean removeChildrenByIds(Collection<? extends Serializable> idList);

    /**
     * 查找所有子节点（包含自身）
     *
     * @param id
     * @return
     */
    List<Subjecttypes> listChildrenById(Serializable id);

    /**
     * 查找所有子节点（不包含自身）
     *
     * @param pid
     * @param parent
     * @return
     */

    /**
     * 根据uid查询部门
     *
     * @param uid
     * @return
     */
    List<Subjecttypes> listByUid(String uid);
}
