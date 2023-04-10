package com.ossjk.qlh.trainplan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.subject.entity.Coursetimeplan;
import com.ossjk.qlh.subject.service.ICoursetimeplanService;

import com.ossjk.qlh.trainplan.entity.Trainingplan;
import com.ossjk.qlh.trainplan.mapper.TrainingplanMapper;
import com.ossjk.qlh.trainplan.service.ITrainingplanService;
import com.ossjk.qlh.trainplan.vo.CoursetimeplanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: TrainingplanServiceImpl
 * @Description: 人才培养方案-服务实现类
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */
@Service

public class TrainingplanServiceImpl extends ServiceImpl<TrainingplanMapper, Trainingplan> implements ITrainingplanService {


    @Autowired
    private ICoursetimeplanService iCoursetimeplanService;

    @Override
    public ResponseBean<ArrayList<CoursetimeplanVO>> getCoursetimeplans(List<String> cids, String grade) {

        Iterator<String> cidsIterator = cids.iterator();
        ArrayList<CoursetimeplanVO> coursetimeplanVOS = new ArrayList<>();
        while (cidsIterator.hasNext()) {
            //查询cid返回数组
            List<Coursetimeplan> coursetimeplans = iCoursetimeplanService.list(new QueryWrapper<Coursetimeplan>().
                    eq("cid", cidsIterator.next()).eq("grade",grade).orderByAsc("year").orderByAsc("term"));

            //遍历cid数组,放入intList数组
            ArrayList<Integer> integers = new ArrayList<>();
            Iterator<Coursetimeplan> coursetimeplanIterator = coursetimeplans.iterator();
            CoursetimeplanVO coursetimeplanVO = new CoursetimeplanVO();
            //如果数据重复或者为空默认值404
            if (coursetimeplans.size() != 9) {
                coursetimeplanVOS.add(coursetimeplanVO);
                continue;
            }
            while (coursetimeplanIterator.hasNext()) {
                Coursetimeplan next = coursetimeplanIterator.next();
                integers.add(next.getTheorytime());
                integers.add(next.getPracticetime());
            }
            //向CoursetimeplanVO封装数据
            Iterator<Integer> integerIterator = integers.iterator();
            coursetimeplanVO.setFirstYearFirstTermTheorytime(integerIterator.next());
            coursetimeplanVO.setFirstYearFirstTermPracticetime(integerIterator.next());
            coursetimeplanVO.setFirstYearSecondTermTheorytime(integerIterator.next());
            coursetimeplanVO.setFirstYearSecondTermPracticetime(integerIterator.next());
            coursetimeplanVO.setFirstYearThirdTermTheorytime(integerIterator.next());
            coursetimeplanVO.setFirstYearThirdTermPracticetime(integerIterator.next());

            coursetimeplanVO.setSecondYearFirstTermTheorytime(integerIterator.next());
            coursetimeplanVO.setSecondYearFirstTermPracticetime(integerIterator.next());
            coursetimeplanVO.setSecondYearSecondTermTheorytime(integerIterator.next());
            coursetimeplanVO.setSecondYearSecondTermPracticetime(integerIterator.next());
            coursetimeplanVO.setSecondYearThirdTermTheorytime(integerIterator.next());
            coursetimeplanVO.setSecondYearThirdTermPracticetime(integerIterator.next());

            coursetimeplanVO.setThirdYearFirstTermTheorytime(integerIterator.next());
            coursetimeplanVO.setThirdYearFirstTermPracticetime(integerIterator.next());
            coursetimeplanVO.setThirdYearSecondTermTheorytime(integerIterator.next());
            coursetimeplanVO.setThirdYearSecondTermPracticetime(integerIterator.next());
            coursetimeplanVO.setThirdYearThirdTermTheorytime(integerIterator.next());
            coursetimeplanVO.setThirdYearThirdTermPracticetime(integerIterator.next());
            coursetimeplanVOS.add(coursetimeplanVO);
        }
        ResponseBean<ArrayList<CoursetimeplanVO>> success = ResponseBean.Success(coursetimeplanVOS);
        return success;
    }

    @Override
    public ResponseBean updataCourseTimePlan( List<Coursetimeplan> record) {

        //查询数据库是否已经有数据
        List<Coursetimeplan> grade = iCoursetimeplanService.list(new QueryWrapper<Coursetimeplan>()
                .eq("grade", record.get(0).getGrade())
                .eq("cid",record.get(0).getCid()));

        //如果有就更新
        if (grade.size()!=0){
            iCoursetimeplanService.updateBatchById(record);
            return ResponseBean.Success("更新成功");
        }

        //没有进行插入操作
        iCoursetimeplanService.saveBatch(record);
        return ResponseBean.Success("保存成功");

    }


}
