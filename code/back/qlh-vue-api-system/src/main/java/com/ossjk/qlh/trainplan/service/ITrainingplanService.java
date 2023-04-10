package com.ossjk.qlh.trainplan.service;


import java.util.ArrayList;
import java.util.List;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: TrainingplanService
 * @Description:  人才培养方案-服务类接口
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */
import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.subject.entity.Coursetimeplan;
import com.ossjk.qlh.trainplan.entity.Trainingplan;
import com.ossjk.qlh.trainplan.vo.CoursetimeplanVO;


public interface ITrainingplanService extends IService<Trainingplan> {
    public ResponseBean<ArrayList<CoursetimeplanVO>> getCoursetimeplans(List<String> cids, String grade);

    public ResponseBean updataCourseTimePlan(List<Coursetimeplan> record);

}
