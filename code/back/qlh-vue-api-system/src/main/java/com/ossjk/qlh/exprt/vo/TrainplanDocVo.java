package com.ossjk.qlh.exprt.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//职业院校专业人才培养方案 Word版
@Data
public class TrainplanDocVo implements Serializable {

    //step1

    //专业名称
    private String subname;
    //专业代码
    private  String code;
    //学年
    private  String year;

    //step2

    //入学要求
    private  String enterNd;

    //step3

    //修业年限
    private String xynx;

    //step4

    //职业面向
    private List<String[]> Step4 = new ArrayList<>();

    public void addStep4(String[] element){
        Step4.add(element);
    }



    //step5

    //人才培养目标
    private  String rcpymb;
    //培训规格-素质要求
    private  String szyq;
    //培训规格-知识要求
    private String zsyq;
    //培训规格-能力要求
    private String nlyq;
    //指标点标题
    private String[] titles;
    //毕业生能力要求指标点
    private List<SubMode> byszbd = new ArrayList<SubMode>();

    public void addByszbdChild(SubMode element){
        byszbd.add(element);
    }

    //指标点关联矩阵
    private Map<String,List<String[]>> zbdgl = new HashMap<>();
    public void putZbdglChild(String key,List<String[]> value){
        zbdgl.put(key,value);
    }


    //step6 课程设置及要求
    private List<String[]> step6 = new ArrayList<>();;

    public void addStep6(String[] element){
        step6.add(element);
    }


    //step7 教学进程总体安排备注
    private String step7;

    //step8

    //实施保障-师资队伍
    private String szdw;
    //实施保障-教学设施
    private String jxss;
    //实施保障-教学资源
    private String jxzy;
    //实施保障-教学方法
    private String jxff;
    //实施保障-学习评价
    private String xxpj;
    //实施保障-质量管理
    private String zlgl;

    //step9

    //毕业要求
    private String requirement;

    //step10

    //附录
    private List<String[]> appendix = new ArrayList<>();;






}
