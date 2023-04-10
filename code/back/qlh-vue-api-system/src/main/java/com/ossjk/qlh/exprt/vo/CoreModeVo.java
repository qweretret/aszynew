package com.ossjk.qlh.exprt.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//核心能力模型导出VO
@Data
public class CoreModeVo implements Serializable {

    private String name;

    //第一个子表    职业+主要工作任务
    private  List<SubMode>  first = new ArrayList<>();

    //第二个子表    主要工作任务-工作内容-技能点+知识点
    private  List<SubMode>  second= new ArrayList<>();

    //第三个子表   所属课程+学时
    private  List<SubMode2>  third= new ArrayList<>();

    //第4个子表    课程+总学时+理论+实训
    private  List<SubMode2>  fouth= new ArrayList<>();
}
