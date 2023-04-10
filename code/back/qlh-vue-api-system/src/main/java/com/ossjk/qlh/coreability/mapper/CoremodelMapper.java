package com.ossjk.qlh.coreability.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.coreability.entity.Coremodel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.mapper
 * @ClassName: CoremodelMapper
 * @Description: 专业核心能力模型-Mapper
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */
@Mapper
public interface CoremodelMapper extends BaseMapper<Coremodel> {
     //    院领导
    List<String> selectYldUserCore(String dname);
    //    系领导
    List<String> selectXldUserCore(String dname);


}
