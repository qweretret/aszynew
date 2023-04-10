package com.ossjk.qlh.exprt.service;

import com.ossjk.qlh.exprt.vo.TrainplanXlsVo;
import com.ossjk.qlh.exprt.vo.TrainplanDocVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.subject.service
 * @Description: 导出文件-服务类接口
 * @author: huang
 * @date: 2022-03-22 16:30:01
 */

public interface IExportService {

    //1.导出核心能力模型
    Workbook getCoreModeVo(String fn, String id);

    //2.全部导出人才培养方案
    File getTrainplan(String tpid, String cid);

    TrainplanXlsVo getTrainplanExcelVo(String id);

    XWPFDocument getTrainplanWord(String id);

    //3.导出课程标准
    XWPFDocument getCourseStandVo(String id);

    void getTimeplan();

}
