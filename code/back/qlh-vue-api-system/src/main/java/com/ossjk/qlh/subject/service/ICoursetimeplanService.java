package com.ossjk.qlh.subject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.subject.entity.Coursetimeplan;

/**
 * Copyright  2022-04-06 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: CoursetimeplanService
 * @Description:  -服务类接口
 * @author: huang
 * @date:  2022-04-06 19:52:52 
 */
public interface ICoursetimeplanService extends IService<Coursetimeplan> {

    String[] selectGradeByCid( String cid);
}
