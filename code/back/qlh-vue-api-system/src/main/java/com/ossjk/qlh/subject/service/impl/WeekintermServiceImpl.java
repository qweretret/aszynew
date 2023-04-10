package com.ossjk.qlh.subject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.qlh.subject.entity.Subject;
import com.ossjk.qlh.subject.entity.Weekinterm;
import com.ossjk.qlh.subject.mapper.WeekintermMapper;
import com.ossjk.qlh.subject.service.ISubjectService;
import com.ossjk.qlh.subject.service.IWeekintermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: WeekintermServiceImpl
 * @Description: 学期教学周-服务实现类
 * @author: wanghaohui
 * @date: 2022-03-09 15:54:53
 */
@Service
public class WeekintermServiceImpl extends ServiceImpl<WeekintermMapper, Weekinterm> implements IWeekintermService {

    @Autowired
    private ISubjectService iSubjectService;

    @Override
    public List<Weekinterm> selectByYear(Integer year) {
        return this.baseMapper.selectList(new QueryWrapper<Weekinterm>().eq("year", year).orderByAsc("term"));
    }

    @Override
    public  List<Weekinterm> selectAllYear(Integer grade,String sid){
        List<Weekinterm> allYears = new ArrayList<>();
        List<Weekinterm> wtms = null;
        Weekinterm wtmp= null;

        Subject sub =   iSubjectService.getById(sid);
        int num =sub.getYear();

        for (int i = 0; i < num; i++) {
            wtms =  this.selectByYear(grade+i);
              if(wtms!=null && wtms.size()>0 ){
                  for (Weekinterm wt :wtms  ) {
                      allYears.add(wt);
                  }
              }else {
                  //创建新的
                  for (int j = 1; j <= 3; j++) {
                      wtmp = new Weekinterm();
                      wtmp.setYear(grade+i);
                      wtmp.setTerm(j);
                      wtmp.setWeeks(3);
                      this.baseMapper.insert(wtmp);
                      allYears.add(wtmp);
                  }
              }
        }

      return  allYears;
    }

    @Override
    public Boolean removeByYears(Integer[] year) {
        for (Integer item : year) {
            QueryWrapper<Weekinterm> queryWrapper = new QueryWrapper<Weekinterm>();
            queryWrapper.eq("year",item);
            if(this.baseMapper.delete(queryWrapper)==0){
                return false;
            }
        }
        return true;
    }

//    @Override
//    public Boolean updateByTerm(List<Weekinterm> weekintermList) {
//        weekintermList.stream().forEach(item->{
//            if(this.baseMapper.update(item) == 0)
//        });
//    }
}
