package com.ossjk.qlh.exprt.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//课程标准导出VO
@Data
public class CourseStandVo implements Serializable {
    
    //专业名称
    private String subjectName;
    //课程名称
    private  String name;
    //课程代码
    private String code;
    //教学单位
    private String company;
    //制定时间
    private String zdtm;
    //课程学分
    private  String credit;
    //课程学时
    private  String period;
    //实践学时
    private  String  actperiod;
    //课程类型
    private  String type;
    //专业代码
    private String subjectCode;
    //授课对象
    private  String object;
    //先修课程
    private  String beforeCourse;
    //后续课程
    private  String followCourse;

    //////////////////////////////////////////2课程概述
    private  String  desc;
    //表1：专业对应的职业标准及工作任务 	[专业核心能力模型first(1,3)]
    private List<SubMode> coretab1 = new ArrayList<SubMode>();

    /////////////////二、 本课程的能力标准
    //1. 知识目标
    private String  target;
    //2. 操作技能
    private String  skill;
    //3. 职业素养
    String  accomplishment;
    //能力表（选自专业计划）[专业核心能力模型second(3,4,5,6)]
    private  List<SubMode> coretab2 = new ArrayList<SubMode>();

    //三、课程结构
    private List<SubMode2> courseJg = new ArrayList<SubMode2>();

    //四、课程模块内容描述及测评要求
    //    学习产出及测评要求一览表   后面  [学习内容][学习产出 ][测评方法 ]使用Br换行
    private List<SubMode2> studyList = new ArrayList<SubMode2>();

    //本课程的达标要求
    //1.必须通过的模块：
    private  String  mustPass;
    //2.可以选修的模块：
    private  String choosePass;
    // 3.本课程合格标准：
    private String passStandard;


    ////////////////////////////五、本课程的实施建议
    //1. 师资队伍
    private String  teacherTeam;
    //2. 教学设施
    private String  facilities;
    //3. 教学资源
    private  String  resources;
    //4. 教学建议
    private String  suggest;


}
