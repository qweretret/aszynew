package com.ossjk.qlh.subject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.subject.entity.Weekinterm;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: WeekintermService
 * @Description:  学期教学周-服务类接口
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
public interface IWeekintermService extends IService<Weekinterm> {

    List<Weekinterm> selectByYear(Integer year);

    List<Weekinterm> selectAllYear(Integer grade,String sid);

    Boolean removeByYears(Integer[] year);

//    Boolean updateByTerm(List<Weekinterm> weekintermList);
}
