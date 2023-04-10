package com.ossjk.qlh.subject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.subject.entity.Coursetimeplan;
import com.ossjk.qlh.subject.mapper.CoursetimeplanMapper;
import com.ossjk.qlh.subject.service.ICoursetimeplanService;
import org.springframework.stereotype.Service;

/**
 * Copyright  2022-04-06 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: CoursetimeplanServiceImpl
 * @Description: -服务实现类
 * @author: huang
 * @date:  2022-04-06 19:52:52 
 */
@Service
public class CoursetimeplanServiceImpl extends ServiceImpl<CoursetimeplanMapper, Coursetimeplan> implements ICoursetimeplanService {

    public String[] selectGradeByCid( String cid) {
        return this.baseMapper.selectGradeByCid(cid);
    }

}
