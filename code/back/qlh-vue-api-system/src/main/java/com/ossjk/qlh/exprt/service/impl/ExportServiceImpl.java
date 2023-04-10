package com.ossjk.qlh.exprt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.myUtil.StringUtil;
import com.ossjk.qlh.coreability.entity.Coremodel;
import com.ossjk.qlh.coreability.service.ICoremodelService;
import com.ossjk.qlh.coursestandards.entity.Coursestandards;
import com.ossjk.qlh.exprt.service.IExportService;
import com.ossjk.qlh.coursestandards.service.ICoursestandardsService;
import com.ossjk.qlh.exprt.service.TrainPlanDocUtil;
import com.ossjk.qlh.exprt.vo.*;
import com.ossjk.qlh.subject.entity.Subject;
import com.ossjk.qlh.subject.entity.Weekinterm;
import com.ossjk.qlh.subject.service.ISubjectService;
import com.ossjk.qlh.subject.service.IWeekintermService;
import com.ossjk.qlh.trainplan.service.ITrainingplanService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: TrainingplanServiceImpl
 * @Description: 集中-服务实现类
 * @author: Rick
 * @date: 2022-03-22 16:30:01
 */
@Service
public class ExportServiceImpl implements IExportService {

    @Autowired
    private ITrainingplanService iTrainingplanService;
    @Autowired
    private ICoremodelService iCoremodelService;
    @Autowired
    private ICoursestandardsService iCoursestandardsService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IWeekintermService weekintermService;
    @Autowired
    private TrainPlanDocUtil trainplanUtil;
    @Autowired
    private ResourceMappersProperties ymlUri;
    //人才培养方案
    @Override
    public File getTrainplan(String tpid, String cid) {
        // 读取yuml的配置数据
        List<ResourceMappersProperties.ResourceMapper> resourceMapperList = ymlUri.getMappers();
        Map<String, String> uriMap = resourceMapperList.stream().
                collect(Collectors.toMap(ResourceMappersProperties.ResourceMapper::getUri, ResourceMappersProperties.ResourceMapper::getFile));

        //抽象文件路径
        File rootfile = new File(uriMap.get("/statics/zysztmp"));
        if (!rootfile.exists()) {
            rootfile.mkdir();
        }
        // 导出all人才培养方案
        //1、搜索临时文件夹，删除1天以前的，再先创建一个临时文件夹
        //获取所有文件路径
        File[] files = rootfile.listFiles();
        for (File file : files) {
            //是否为目录
            if (file.isDirectory()) {
                //删除1天以前的
                if (System.currentTimeMillis() - file.lastModified() > 1000 * 60 * 60 * 24) {
                    file.delete();
                }
            }
        }

        File tmpRoot = new File(rootfile, System.currentTimeMillis() + "");
        if (tmpRoot.mkdir()) {
            //2、写入coreModel
            Workbook wb = this.getCoreModeVo("核心能力模型.xls", cid);
            try {
                FileOutputStream out = new FileOutputStream(new File(tmpRoot, "核心能力模型.xls"));
                wb.write(out);
                out.flush();
                out.close();
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //3、循环写入CourseStandard
            List<Map<String, String>> csids = this.iCoursestandardsService.findIdsByCid(cid);
            if (csids != null)
                for (Map<String, String> csid : csids) {
                    XWPFDocument ctv = this.getCourseStandVo(csid.get("id"));
                    try {
                        FileOutputStream out = new FileOutputStream(new File(tmpRoot, csid.get("name") + "-课程标准.doc"));
                        ctv.write(out);
                        out.flush();
                        out.close();
                        ctv.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            //4、写入TrainplanExcel

            //5、写入TrainplanWord


            //6、压缩读取返回
            File[] fs = tmpRoot.listFiles();
            try {
                File zipFile = new File(tmpRoot, "tranplan.zip");
                StringUtil.zipMultipleFiles(zipFile, fs);

                //返回
                return zipFile;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;

    }

    @Override
    public TrainplanXlsVo getTrainplanExcelVo(String id) {
        return null;
    }

    @Override
    public XWPFDocument getTrainplanWord(String id) {
        return this.trainplanUtil.generateDoc(id);//    getpersonnelVo(id);
    }

    //取得核心能力模型
    @Override
    public Workbook getCoreModeVo(String fn, String id) {

        CoreModeVo cmv = genCoreMode(id);

        //导出Excel
        //1.在内存中创建一个excel文件
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFCellStyle styleTitle = getCoreModeHSSFCellStyle(excel, 1);
        HSSFCellStyle styleBar = getCoreModeHSSFCellStyle(excel, 2);
        HSSFCellStyle styleCntCenter = getCoreModeHSSFCellStyle(excel, 4);
        HSSFCellStyle styleCntTop = getCoreModeHSSFCellStyle(excel, 5);

        //2.创建工作簿
        HSSFSheet sheet = excel.createSheet(cmv.getName());
        //设置列宽
        sheet.setColumnWidth(0, (int) ((30 + 0.72) * 256));
        sheet.setColumnWidth(1, (int) ((50 + 0.72) * 256));
        sheet.setColumnWidth(2, (int) ((8 + 0.72) * 256));
        sheet.setColumnWidth(3, (int) ((40 + 0.72) * 256));
        sheet.setColumnWidth(4, (int) ((40 + 0.72) * 256));
        sheet.setColumnWidth(5, (int) ((40 + 0.72) * 256));
        sheet.setColumnWidth(6, (int) ((40 + 0.72) * 256));
        sheet.setColumnWidth(7, (int) ((8 + 0.72) * 256));
        sheet.setColumnWidth(8, (int) ((30 + 0.72) * 256));
        sheet.setColumnWidth(9, (int) ((30 + 0.72) * 256));
        sheet.setColumnWidth(10, (int) ((8 + 0.72) * 256));
        sheet.setColumnWidth(11, (int) ((30 + 0.72) * 256));
        sheet.setColumnWidth(12, (int) ((20 + 0.72) * 256));
        sheet.setColumnWidth(13, (int) ((20 + 0.72) * 256));
        sheet.setColumnWidth(14, (int) ((20 + 0.72) * 256));

        //3.创建标题行
        HSSFRow titlerRow = sheet.createRow(0);
        titlerRow.setHeightInPoints(55);
        //当前操作的单元格
        HSSFCell cell = titlerRow.createCell(0);

        cell.setCellStyle(styleTitle);
        cell.setCellValue(cmv.getName());

        //第一行合并单元格
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$O$1"));
        // 第二行
        HSSFRow scndRow = sheet.createRow(1);
        scndRow.setHeightInPoints(35);

        //  tab1
        createCoreModelTab1(cmv, styleBar, styleCntTop, sheet, scndRow);
        //  tab2
        createCoreModelTab2(cmv, styleBar, styleCntTop, sheet, scndRow);
        //  tab3+tab4
        return createCoreModelTab34(cmv, styleBar, styleCntCenter, excel, scndRow);

    }

    //取得课程标准
    @Override
    public XWPFDocument getCourseStandVo(String id) {
        //数据
        CourseStandVo csvo = genCourseStand(id);
        //内存中生成数据
        XWPFDocument document = new XWPFDocument();
        //设置边距
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        //添加标题
        XWPFParagraph title1pgph = document.createParagraph();

        XWPFRun titleRun = getWordRun(document, 0);
        titleRun.setText("课程标准");
        titleRun.addBreak();    //换行
//        titleRun.addBreak();
        titleRun.setText("《" + csvo.getSubjectName() + " - " + csvo.getName() + "》");
        titleRun.addBreak();

        titleRun = getWordRun(document, 1);
        titleRun.setText("一、课程概述");
//        titleRun.addBreak();
        XWPFRun contentRun = getWordRun(document, 2);
        contentRun.setText(csvo.getSubjectName() + "《" + csvo.getName() + "》课程标准");
//        contentRun.addBreak();
//        contentRun.addBreak();

        titleRun = getWordRun(document, 3);
        titleRun.setText("1.课程基本信息");
//        titleRun.addBreak();
        contentRun = getWordRun(document, 2);
        contentRun.setText("  课程名称：" + csvo.getName() + "  课程代码：" + csvo.getCode());
        contentRun.addBreak();
        contentRun.setText("  教学单位：" + csvo.getCompany() + "  制定时间：" + csvo.getZdtm());
        contentRun.addBreak();
        contentRun.setText("  课程学分：" + csvo.getCredit() + "  课程学时：" + csvo.getPeriod());
        contentRun.addBreak();
        contentRun.setText("  课程类型：" + csvo.getType() + "  专业代码：" + csvo.getSubjectCode());
        contentRun.addBreak();
//        contentRun.addBreak();
        contentRun.setText("  授课对象：" + csvo.getObject());
        contentRun.addBreak();
        contentRun.setText("  先修课程：" + csvo.getBeforeCourse());
        contentRun.addBreak();
        contentRun.setText("  后续课程：" + csvo.getFollowCourse());
//        contentRun.addBreak();
//        contentRun.addBreak();

        titleRun = getWordRun(document, 3);
        titleRun.setText("2. 课程概述");
//        titleRun.addBreak();

        contentRun = getWordRun(document, 2);
//        contentRun.setText("  后续课程：" + csvo.getDesc());
        contentRun.setText( csvo.getDesc());
//        contentRun.addBreak();
        contentRun.addBreak();

        contentRun = getWordRun(document, 4);
        contentRun.setText("表1：专业对应的职业标准及工作任务");
        contentRun.addBreak();

        //数据表格
        XWPFTable table1 = document.createTable(csvo.getCoretab1().size() + 1, 2);
        //===========表格表头行===============
        getTableRun(table1, 0, 0, "2800", true).setText("职业标准");
        getTableRun(table1, 0, 1, "5800", true).setText("工作任务");
        //灌装数据
        List<SubMode> tab1Data = csvo.getCoretab1();
        SubMode cm = null;
        for (int i = 0; i < tab1Data.size(); i++) {
            cm = tab1Data.get(i);
            getTableRun(table1, i + 1, 0, "2800", false).setText(cm.getTxt());
            if (cm.getChld().size() > 0) {
                getTableRun(table1, i + 1, 1, "5800", false).setText(cm.getChld().get(0).getTxt());
            }
        }

        titleRun = getWordRun(document, 1);
        titleRun.addBreak();
        titleRun.setText("二、 本课程的能力标准（选自专业计划的能力表）");
        titleRun.addBreak();

        contentRun = getWordRun(document, 3);
        contentRun.setText("合格完成本课程学习后，学生应当具有以下知识目标、操作技能和职业素养： ");
        contentRun.addBreak();

        titleRun = getWordRun(document, 5);
        titleRun.setText("1. 知识目标");
        contentRun = getWordRun(document, 2);
        contentRun.setText("   " + csvo.getTarget());
//        contentRun.addBreak();
//        contentRun.addBreak();

        titleRun.addBreak();
        titleRun = getWordRun(document, 5);
        titleRun.setText("2. 操作技能 ");
        contentRun = getWordRun(document, 2);
        contentRun.setText("   " + csvo.getSkill());
//        contentRun.addBreak();
//        contentRun.addBreak();

        titleRun.addBreak();
        titleRun = getWordRun(document, 5);
        titleRun.setText("3. 职业素养");
        contentRun = getWordRun(document, 2);
        contentRun.setText("   " + csvo.getAccomplishment());
//        contentRun.addBreak();
//        contentRun.addBreak();

        contentRun = getWordRun(document, 4);
        contentRun.setText("表2：能力表（选自专业计划）");
        contentRun.addBreak();

        //数据表格 tab2                                     //计算数据有多少行？
        List<SubMode> list = csvo.getCoretab2();
        XWPFTable table2 = document.createTable(calTab2Rows(list) + 1, 4);
        //===========表格表头行===============
        titleRun = getTableRun(table2, 0, 0, "1500", true);
        titleRun.setText("职业功能");
        titleRun.addBreak();
        titleRun.setText("（工作任务）");

        getTableRun(table2, 0, 1, "1600", true).setText("工作内容");
        getTableRun(table2, 0, 2, "2000", true).setText("能力元素");
        getTableRun(table2, 0, 3, "3500", true).setText("相关知识");

        List<SubMode> list2 = null, list3 = null, list4 = null;
        SubMode floor1 = null, floor2 = null, floor3 = null;
        int bfList2Rows = 0, bfList3Rows = 0, bfList4Rows = 0;
        int nowRow = 1;
        for (int i = 0; i < list.size(); i++) {
            floor1 = list.get(i);
            if (floor1.getChld().size() == 0) {
                //没孩子算当前的1行
                getTableRun(table2, nowRow, 0, "1500", false).setText(floor1.getTxt());
                getTableRun(table2, nowRow, 1, "1600", false).setText("");
                getTableRun(table2, nowRow, 2, "2000", false).setText("");
                getTableRun(table2, nowRow, 3, "3500", false).setText("");
                nowRow++;
            } else {
                //有孩子继续往下数
                list2 = floor1.getChld();
                bfList2Rows = nowRow;
                for (int j = 0; j < list2.size(); j++) {
                    floor2 = list2.get(j);
                    if (floor2.getChld().size() == 0) {
                        getTableRun(table2, nowRow, 0, "1500", false).setText(floor1.getTxt());
                        getTableRun(table2, nowRow, 1, "1600", false).setText(floor2.getTxt());
                        getTableRun(table2, nowRow, 2, "2000", false).setText("");
                        getTableRun(table2, nowRow, 3, "3500", false).setText("");
                        nowRow++;
                    } else {
                        list3 = floor2.getChld();
                        bfList3Rows = nowRow;
                        for (int k = 0; k < list3.size(); k++) {
                            floor3 = list3.get(k);
                            if (floor3.getChld().size() == 0) {
                                getTableRun(table2, nowRow, 0, "1500", false).setText(floor1.getTxt());
                                getTableRun(table2, nowRow, 1, "1600", false).setText(floor2.getTxt());
                                getTableRun(table2, nowRow, 2, "2000", false).setText(floor3.getTxt());
                                getTableRun(table2, nowRow, 3, "3500", false).setText("");
                                nowRow++;
                            } else {
                                //第4级不再往下
                                list4 = floor3.getChld();
                                bfList4Rows = nowRow;
                             //   System.out.println( "bfList4Rows  =  "  +bfList4Rows);
                                for (int h = 0; h < list4.size(); h++) {
                                    getTableRun(table2, nowRow, 0, "1500", false).setText(floor1.getTxt());
                                    getTableRun(table2, nowRow, 1, "1600", false).setText(floor2.getTxt());
                                    getTableRun(table2, nowRow, 2, "2000", false).setText(floor3.getTxt());
                                    getTableRun(table2, nowRow, 3, "3500", false).setText(list4.get(h).getTxt());

                                    //第3级合并
                                   // System.out.println("col = "+2+"   , "+bfList4Rows+" - "+nowRow);
                                    //最后一行
                                    if (h == list4.size() - 1) {
                                         mergeCellsVertically(table2,2, bfList4Rows  ,nowRow);
                                    }
                                    nowRow++;
                                }
                            }
                            //第2级合并
                            if (k == list3.size() - 1) {
                                  mergeCellsVertically(table2,1, bfList3Rows  ,nowRow-1);
                            }
                        }
                    }
                    //第1级合并
                    if (j == list2.size() - 1) {
                         mergeCellsVertically(table2,0, bfList2Rows  ,nowRow-1);
                    }
                }
            }

        }

        //数据表格 tab3                   //计算数据有多少行？
        List<SubMode2> tab3 = csvo.getCourseJg();

        titleRun = getWordRun(document, 1);
        titleRun.addBreak();
        titleRun.addBreak();
        titleRun.setText("三、课程结构");
        titleRun.addBreak();
        contentRun = getWordRun(document, 4);
        contentRun.setText("表3：课程结构一览表");

        XWPFTable table3 = document.createTable(tab3.size() + 1, 2);
        //===========表格表头行===============
        titleRun = getTableRun(table3, 0, 0, "5600", true);
        titleRun.setText("课程模块名称");
        titleRun.addBreak();
        titleRun.setText("（可以是工作内容）");
        getTableRun(table3, 0, 1, "3000", true).setText("学时（含实训） ");
        //灌装数据
        SubMode2 cm2 = null;
        for (int i = 0; i < tab3.size(); i++) {
            cm2 = tab3.get(i);
            getTableRun(table3, i + 1, 0, "5600", false).setText(cm2.getVal1());
            getTableRun(table3, i + 1, 1, "3000", false).setText(cm2.getVal2());
        }

        //数据表格 tab4                   //计算数据有多少行？
        List<SubMode2> tab4 = csvo.getStudyList();
        titleRun = getWordRun(document, 1);
        titleRun.addBreak();
        titleRun.addBreak();
        titleRun.setText("四、课程模块内容描述及测评要求");
        titleRun.addBreak();
        contentRun = getWordRun(document, 4);
        contentRun.setText("表4：学习产出及测评要求一览表");
        titleRun.addBreak();
        XWPFTable table4 = document.createTable(tab4.size() + 1, 4);
        //===========表格表头行===============模块名称 	学习内容（范围） 	学习产出
        //（评价标准） 	测评方法
        //（工具、场所）
        getTableRun(table4, 0, 0, "1800", true).setText("模块名称");
        titleRun = getTableRun(table4, 0, 1, "2200", true);

        titleRun.setText("学习内容");
        titleRun.addBreak();
        titleRun.setText("（范围）");

        titleRun = getTableRun(table4, 0, 2, "2500", true);
        titleRun.setText("学习产出");
        titleRun.addBreak();
        titleRun.setText("（评价标准）");
        titleRun = getTableRun(table4, 0, 3, "2100", true);
        titleRun.setText("测评方法");
        titleRun.addBreak();
        titleRun.setText("（工具、场所）");
        //灌装数据
        for (int i = 0; i < tab4.size(); i++) {
            cm2 = tab4.get(i);
            getTableRun(table4, i + 1, 0, "1800", false).setText(cm2.getVal1());
            getTableRun(table4, i + 1, 1, "2200", false).setText(cm2.getVal2());
            getTableRun(table4, i + 1, 2, "2500", false).setText(cm2.getVal3());
            getTableRun(table4, i + 1, 3, "2100", false).setText(cm2.getVal4());
        }


        contentRun.addBreak();

        titleRun = getWordRun(document, 5);
        titleRun.addBreak();
        titleRun.addBreak();
        titleRun.setText("  本课程的达标要求：");
        titleRun.addBreak();
        titleRun.addBreak();

        titleRun.setText("    1.必须通过的模块：");
        titleRun = getWordRun(document, 2);
        titleRun.setText(csvo.getMustPass());
        titleRun.addBreak();

        titleRun = getWordRun(document, 5);
        titleRun.setText("    2.可以选修的模块：");
        titleRun = getWordRun(document, 2);
        titleRun.setText(csvo.getChoosePass());
        titleRun.addBreak();

        titleRun = getWordRun(document, 5);
        titleRun.setText("    3.本课程合格标准：");
        titleRun = getWordRun(document, 2);
        titleRun.setText(csvo.getPassStandard());
        titleRun.addBreak();
        titleRun.addBreak();

        titleRun = getWordRun(document, 1);
        titleRun.addBreak();
        titleRun.addBreak();
        titleRun.setText("五、本课程的实施建议");
        titleRun.addBreak();

        titleRun = getWordRun(document, 5);
        titleRun.setText(" 1. 师资队伍");
        titleRun.addBreak();
        titleRun = getWordRun(document, 2);
        titleRun.setText("   " + csvo.getTeacherTeam());
        titleRun.addBreak();

        titleRun = getWordRun(document, 5);
        titleRun.setText(" 2. 教学设置");
        titleRun.addBreak();
        titleRun = getWordRun(document, 2);
        titleRun.setText("   " + csvo.getFacilities());
        titleRun.addBreak();

        titleRun = getWordRun(document, 5);
        titleRun.setText(" 3. 教学资源");
        titleRun.addBreak();
        titleRun = getWordRun(document, 2);
        titleRun.setText("   " + csvo.getResources());
        titleRun.addBreak();

        titleRun = getWordRun(document, 5);
        titleRun.setText(" 4. 教学建议");
        titleRun.addBreak();
        titleRun = getWordRun(document, 2);
        titleRun.setText("   " + csvo.getSuggest());
        titleRun.addBreak();

        //开始写入
        /*try {
            FileOutputStream out = new FileOutputStream(new File("D:/imgs","courseStand.docx"));
            document.write(out);
            out.flush();
            out.close();
            document.close();
            System.out.println("课程标准doc done");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }*/

        return document;
    }


    //教学进程总体安排excel导出
    @Override
    public void getTimeplan() {
        //1.在内存中创建一个excel文件
        HSSFWorkbook excel = new HSSFWorkbook();
        //2.创建工作簿
        HSSFSheet sheet = excel.createSheet("测试");
        //3.第一部分标题
        for (int i = 0; i < 3; i++) {
            HSSFRow row = sheet.createRow(i);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("课程性质");
            cell = row.createCell(1);
            cell.setCellValue("课程编码");
            cell = row.createCell(2);
            cell.setCellValue("课程名称");
            cell = row.createCell(3);
            cell.setCellValue("课程性质");
            cell = row.createCell(4);
            cell.setCellValue("考核方式");
            cell = row.createCell(5);
            cell.setCellValue("课程总学时");
        }
        //获取1-3行
        HSSFRow oneRow = sheet.getRow(0);
        HSSFRow twoRow = sheet.getRow(1);
        HSSFRow threeRow = sheet.getRow(2);
        //第二部分标题
        for (int i = 6; i < 9; i++) {
            HSSFCell cell = oneRow.createCell(i);
            cell.setCellValue("课程学时");
        }
        HSSFCell cell1 = twoRow.createCell(6);
        cell1.setCellValue("总学时");
        cell1 = twoRow.createCell(7);
        cell1.setCellValue("理论学时");
        cell1 = twoRow.createCell(8);
        cell1.setCellValue("实践学时");
        HSSFCell cell2 = threeRow.createCell(6);
        cell2.setCellValue("总学时");
        cell2 = threeRow.createCell(7);
        cell2.setCellValue("理论学时");
        cell2 = threeRow.createCell(8);
        cell2.setCellValue("实践学时");

        //首先先合并前两部分行列
        for (int i = 0; i < 6; i++) {
            sheet.addMergedRegion(new CellRangeAddress(0, 2, i, i));
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 8));
        for (int i = 6; i < 9; i++) {
            sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
        }

        //4.画学期周表头
        Integer currentYear = 2022;
        QueryWrapper<Weekinterm> queryWrapper = new QueryWrapper<Weekinterm>();
        queryWrapper.between("year", currentYear, currentYear + 3);
        queryWrapper.orderByAsc("year");
        queryWrapper.orderByAsc("term");
        List<Weekinterm> list = weekintermService.list(queryWrapper);
        int temp = 9;
        for (Weekinterm item : list) {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 3; i++) {
                    HSSFRow row = sheet.getRow(i);
                    HSSFCell cell = row.createCell(temp);
                    if (i == 0) {
                        cell.setCellValue(item.getYear());
                    } else if (i == 1) {
//                        String tempStr = (item.getTerm() == 1) ? "第一学期(" : (item.getTerm() == 2) ? "第二学期(" : "暑假(" + item.getWeeks() + ")";
                        StringBuffer tempStr = new StringBuffer();
                        if (item.getTerm() == 1) {
                            tempStr.append("第一学期(");
                        } else if (item.getTerm() == 2) {
                            tempStr.append("第二学期(");
                        } else {
                            tempStr.append("暑假(");
                        }
                        tempStr.append(item.getWeeks() + ")");
                        cell.setCellValue(tempStr.toString());
                    } else {
                        if (j == 0) {
                            cell.setCellValue("理论课学时");
                        } else {
                            cell.setCellValue("实践课学时");
                        }
                    }
                }
                temp++;
            }
        }
        ;
        //合并行列
        for (int i = 9; i < 28; i += 6) {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i, i + 5));
        }
        for (int i = 9; i < 28; i += 2) {
            sheet.addMergedRegion(new CellRangeAddress(1, 1, i, i + 1));
        }
        String json = "";
        //5.获取/转化表单数据
        //静态数据（别点开，很长）
        {
            json = "[\n" +
                    "    {\n" +
                    "        \"cid\":\"0e75a088b222800eec10c8c06b5b3be7\",\n" +
                    "        \"csprop\":\"专业课-专业基础课\",\n" +
                    "        \"name\":\"测试小课程1\",\n" +
                    "        \"testmode\":\"考试\",\n" +
                    "        \"cstarget\":\"课程目标\",\n" +
                    "        \"cscontent\":\"主要内容\",\n" +
                    "        \"teachneed\":\"教学要求\",\n" +
                    "        \"remarks\":\"备注\",\n" +
                    "        \"period\":22,\n" +
                    "        \"credit\":12,\n" +
                    "        \"theoryperiod\":20,\n" +
                    "        \"actperiod\":8,\n" +
                    "        \"testtype\":\"理论\",\n" +
                    "        \"code\":\"11\",\n" +
                    "        \"arr1\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr2\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr3\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"cid\":\"0e75a088b222800eec10c8c06b5b3be7\",\n" +
                    "        \"csprop\":\"专业课-专业基础课\",\n" +
                    "        \"name\":\"测试小课程2\",\n" +
                    "        \"testmode\":\"考试\",\n" +
                    "        \"cstarget\":\"课程目标\",\n" +
                    "        \"cscontent\":\"主要内容\",\n" +
                    "        \"teachneed\":\"教学要求\",\n" +
                    "        \"remarks\":\"备注\",\n" +
                    "        \"period\":22,\n" +
                    "        \"credit\":12,\n" +
                    "        \"theoryperiod\":20,\n" +
                    "        \"actperiod\":8,\n" +
                    "        \"testtype\":\"理论\",\n" +
                    "        \"code\":\"11\",\n" +
                    "        \"arr1\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr2\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr3\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"cid\":\"0e75a088b222800eec10c8c06b5b3be7\",\n" +
                    "        \"csprop\":\"专业课-专业基础课\",\n" +
                    "        \"name\":\"测试小课程3\",\n" +
                    "        \"testmode\":\"考试\",\n" +
                    "        \"cstarget\":\"课程目标\",\n" +
                    "        \"cscontent\":\"主要内容\",\n" +
                    "        \"teachneed\":\"教学要求\",\n" +
                    "        \"remarks\":\"备注\",\n" +
                    "        \"period\":22,\n" +
                    "        \"credit\":12,\n" +
                    "        \"theoryperiod\":20,\n" +
                    "        \"actperiod\":8,\n" +
                    "        \"testtype\":\"理论\",\n" +
                    "        \"code\":\"11\",\n" +
                    "        \"arr1\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr2\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr3\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"cid\":\"0e75a088b222800eec10c8c06b5b3be7\",\n" +
                    "        \"csprop\":\"必修课-专业必修课\",\n" +
                    "        \"name\":\"小课程1\",\n" +
                    "        \"testmode\":\"考试\",\n" +
                    "        \"cstarget\":\"课程目标\",\n" +
                    "        \"cscontent\":\"主要内容\",\n" +
                    "        \"teachneed\":\"教学要求\",\n" +
                    "        \"remarks\":\"备注\",\n" +
                    "        \"period\":22,\n" +
                    "        \"credit\":12,\n" +
                    "        \"theoryperiod\":20,\n" +
                    "        \"actperiod\":8,\n" +
                    "        \"testtype\":\"理论\",\n" +
                    "        \"code\":\"11\",\n" +
                    "        \"arr1\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr2\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr3\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"cid\":\"0e75a088b222800eec10c8c06b5b3be7\",\n" +
                    "        \"csprop\":\"必修课-专业必修课\",\n" +
                    "        \"name\":\"小课程2\",\n" +
                    "        \"testmode\":\"考试\",\n" +
                    "        \"cstarget\":\"课程目标\",\n" +
                    "        \"cscontent\":\"主要内容\",\n" +
                    "        \"teachneed\":\"教学要求\",\n" +
                    "        \"remarks\":\"备注\",\n" +
                    "        \"period\":22,\n" +
                    "        \"credit\":12,\n" +
                    "        \"theoryperiod\":20,\n" +
                    "        \"actperiod\":8,\n" +
                    "        \"testtype\":\"理论\",\n" +
                    "        \"code\":\"11\",\n" +
                    "        \"arr1\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr2\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr3\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "{\n" +
                    "        \"cid\":\"0e75a088b222800eec10c8c06b5b3be7\",\n" +
                    "        \"csprop\":\"必修课-专业必修课\",\n" +
                    "        \"name\":\"小课程2\",\n" +
                    "        \"testmode\":\"考试\",\n" +
                    "        \"cstarget\":\"课程目标\",\n" +
                    "        \"cscontent\":\"主要内容\",\n" +
                    "        \"teachneed\":\"教学要求\",\n" +
                    "        \"remarks\":\"备注\",\n" +
                    "        \"period\":22,\n" +
                    "        \"credit\":12,\n" +
                    "        \"theoryperiod\":20,\n" +
                    "        \"actperiod\":8,\n" +
                    "        \"testtype\":\"理论\",\n" +
                    "        \"code\":\"11\",\n" +
                    "        \"arr1\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr2\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr3\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "{\n" +
                    "        \"cid\":\"0e75a088b222800eec10c8c06b5b3be7\",\n" +
                    "        \"csprop\":\"选修课-专业选修课\",\n" +
                    "        \"name\":\"小课程2\",\n" +
                    "        \"testmode\":\"考试\",\n" +
                    "        \"cstarget\":\"课程目标\",\n" +
                    "        \"cscontent\":\"主要内容\",\n" +
                    "        \"teachneed\":\"教学要求\",\n" +
                    "        \"remarks\":\"备注\",\n" +
                    "        \"period\":22,\n" +
                    "        \"credit\":12,\n" +
                    "        \"theoryperiod\":20,\n" +
                    "        \"actperiod\":8,\n" +
                    "        \"testtype\":\"理论\",\n" +
                    "        \"code\":\"11\",\n" +
                    "        \"arr1\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr2\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr3\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ]\n" +
                    "    },\n" +
                    "{\n" +
                    "        \"cid\":\"0e75a088b222800eec10c8c06b5b3be7\",\n" +
                    "        \"csprop\":\"选修课-专业选修课\",\n" +
                    "        \"name\":\"小课程2\",\n" +
                    "        \"testmode\":\"考试\",\n" +
                    "        \"cstarget\":\"课程目标\",\n" +
                    "        \"cscontent\":\"主要内容\",\n" +
                    "        \"teachneed\":\"教学要求\",\n" +
                    "        \"remarks\":\"备注\",\n" +
                    "        \"period\":22,\n" +
                    "        \"credit\":12,\n" +
                    "        \"theoryperiod\":20,\n" +
                    "        \"actperiod\":8,\n" +
                    "        \"testtype\":\"理论\",\n" +
                    "        \"code\":\"11\",\n" +
                    "        \"arr1\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr2\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ],\n" +
                    "        \"arr3\":[\n" +
                    "            \"12\",\n" +
                    "            \"12\",\n" +
                    "            \"13\",\n" +
                    "            \"13\",\n" +
                    "            \"5\",\n" +
                    "            \"5\"\n" +
                    "        ]\n" +
                    "    }\n" +
                    "]";
        }

        JSONArray jsonArray = JSONArray.parseArray(json);
        String type = jsonArray.getJSONObject(0).getString("csprop");
        int sMegerRow = 3;  //合并行数开始行
        int eMegerRow = 2;  //合并行数结束行
        //开始循环数据进行渲染
        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            HSSFRow row = sheet.createRow(3 + i);
            row.createCell(0).setCellValue(jsonObject.getString("csprop"));
            row.createCell(1).setCellValue(jsonObject.getInteger("code"));
            row.createCell(2).setCellValue(jsonObject.getString("name"));
            row.createCell(3).setCellValue(jsonObject.getString("testtype"));
            row.createCell(4).setCellValue(jsonObject.getString("testmode"));
            row.createCell(5).setCellValue(jsonObject.getInteger("credit"));
            row.createCell(6).setCellValue(jsonObject.getInteger("period"));
            row.createCell(7).setCellValue(jsonObject.getInteger("theoryperiod"));
            row.createCell(8).setCellValue(jsonObject.getInteger("actperiod"));

            JSONArray arr1 = jsonObject.getJSONArray("arr1");
            for (int j = 0; j < arr1.size(); j++) {
                row.createCell(9 + j).setCellValue(arr1.getInteger(j));
            }
            JSONArray arr2 = jsonObject.getJSONArray("arr2");
            for (int j = 0; j < arr1.size(); j++) {
                row.createCell(15 + j).setCellValue(arr1.getInteger(j));
            }
            JSONArray arr3 = jsonObject.getJSONArray("arr3");
            for (int j = 0; j < arr1.size(); j++) {
                row.createCell(21 + j).setCellValue(arr1.getInteger(j));
            }
            //判断合并
            if (!jsonObject.getString("csprop").equals(type)) {
                sheet.addMergedRegion(new CellRangeAddress(sMegerRow, eMegerRow, 0, 0));
                type = jsonObject.getString("csprop");
                sMegerRow = ++eMegerRow;
            } else {
                eMegerRow++;
            }
            //处理最后一个分类的合并
            if (i == jsonArray.size() - 1) {
                sheet.addMergedRegion(new CellRangeAddress(sMegerRow, eMegerRow, 0, 0));
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("d://test.xls"));
            excel.write(fileOutputStream);
            excel.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////工厂方法 生成课程标准  =====>begin
    private XWPFRun getWordRun(XWPFDocument document, int type) {
        XWPFParagraph pgph = document.createParagraph();
        XWPFRun run = pgph.createRun();
        //主title的样式
        if (type == 0) {
            //设置段落居中
            pgph.setAlignment(ParagraphAlignment.CENTER);
            run.setBold(true);//加粗
            run.setFontSize(16);//字体大小
            run.setFontFamily("宋体");
            run.setTextPosition(40);//行间距
        } else if (type == 1) {
            pgph.setAlignment(ParagraphAlignment.LEFT);
            run.setBold(true);//加粗
            run.setTextPosition(30);
            run.setFontSize(14);
        } else if (type == 2) {   //标准内容
            pgph.setAlignment(ParagraphAlignment.LEFT);
            run.setTextPosition(20);
//            pgph.setFirstLineIndent(300); //首行缩进
            run.setFontFamily("仿宋");
            run.setFontSize(10);
        } else if (type == 3) {
            pgph.setFirstLineIndent(300); //首行缩进
            pgph.setAlignment(ParagraphAlignment.LEFT);
            run.setTextPosition(20);
            run.setFontFamily("仿宋");
            run.setFontSize(12);
        } else if (type == 4) {
            pgph.setAlignment(ParagraphAlignment.CENTER);
            run.setFontFamily("仿宋");
            run.setFontSize(10);
        } else if (type == 5) {
            pgph.setAlignment(ParagraphAlignment.LEFT);
            run.setBold(true);//加粗
            run.setFontFamily("仿宋");
            run.setFontSize(12);
        }

        return run;
    }

    private XWPFRun getTableRun(XWPFTable table, int row, int col, String width, Boolean isTitle) {

            XWPFTableRow rowX = table.getRow(row);//创建的的一行一列的表格，获取第一行
           // System.out.println("table.size =  "+table.getRows().size());
         //   System.out.println("row = [ "+row+","+col+" ],rowX = "+rowX);

            XWPFTableCell cell = rowX.getCell(col);
            XWPFParagraph pgph = cell.getParagraphs().get(0);
            XWPFRun run = pgph.createRun();
            run.setFontFamily("仿宋");
            if (isTitle) {
                rowX.setHeight(400);//设置当前行行高
                run.setFontSize(12);
                run.setBold(true);//加粗
                pgph.setAlignment(ParagraphAlignment.CENTER);
            } else {
                rowX.setHeight(300);//设置当前行行高
                run.setFontSize(10);
            }
            CTTc cttc = cell.getCTTc();
            CTTcPr ctPr = cttc.addNewTcPr();
            ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中
            CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
            tblWidth.setW(new BigInteger(width));//设置列宽度
            tblWidth.setType(STTblWidth.DXA);
            return run;

    }

    private void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    private CourseStandVo genCourseStand(String id) {  //生成课程标准

        //还原成json
        Coursestandards cv = this.iCoursestandardsService.getById(id);
        JSONObject courseStand = JSON.parseObject(cv.getCourest());

        //封装数据
        CourseStandVo vo = new CourseStandVo();

        JSONObject one = courseStand.getJSONObject("one");
        if (one != null) {
            String sbjid = cv.getSubjectid();
            Subject subject = this.subjectService.getById(sbjid);

            vo.setSubjectName(subject.getName() + "-" + cv.getCoursename());

            //vo.setSubjectName(one.getString("subjectName"));
            vo.setName(one.getString("name"));
            vo.setCode(one.getString("code"));
            vo.setCompany(one.getString("company"));
            vo.setZdtm(one.getString("zdtm"));
            vo.setCredit(one.getString("credit"));
            vo.setPeriod(one.getString("period"));
            vo.setActperiod(one.getString("actperiod"));
            vo.setType(one.getString("type"));
            vo.setSubjectCode(one.getString("subjectCode"));
            vo.setObject(one.getString("object"));
            vo.setBeforeCourse(one.getString("beforeCourse"));
            vo.setFollowCourse(one.getString("followCourse"));

            CoreModeVo cmd = genCoreMode(cv.getCid());

            vo.setCoretab1(cmd.getFirst());
            vo.setCoretab2(cmd.getSecond());
        }

        one = courseStand.getJSONObject("two");
        if (one != null) {
            vo.setDesc(one.getString("desc"));
            one = courseStand.getJSONObject("three");
            if (one != null) {
                vo.setTarget(one.getString("target"));
                vo.setSkill(one.getString("skill"));
                vo.setAccomplishment(one.getString("accomplishment"));
            }
        }



        //课程结构
        JSONArray editFourDatas = courseStand.getJSONArray("four");
        if (editFourDatas != null) {
            List<SubMode2> studyList = vo.getStudyList();

            JSONObject four = null;
            SubMode2 fourMode = null;
            //1.可以选修的模块
            StringBuilder str1 = new StringBuilder();
            // StringBuffer
            //2.必须通过的模块
            StringBuilder str2= new StringBuilder();
            if (editFourDatas != null)
                for (int i = 0; i < editFourDatas.size(); i++) {
                    four = editFourDatas.getJSONObject(i);
                    //处理  -课程结构
                    fourMode = new SubMode2();
                    fourMode.setVal1(tx2Str(four.get("name")));
                    fourMode.setVal2(tx2Str(four.get("time")));
                    vo.getCourseJg().add(fourMode);

                    //处理 -学习产出及测评要求一览表
                    // ↓↓ 死BUG 大爷的浪费我1天时间 ↓↓
                    fourMode = new SubMode2();
                    fourMode.setVal1(tx2Str(four.get("name")));
                    fourMode.setVal2(tx2Str(four.get("studyContent")));
                    fourMode.setVal3(tx2Str(four.get("studyProduce")));
                    fourMode.setVal4(tx2Str(four.get("testMethod")));
                    vo.getStudyList().add(fourMode);

                    if (tx2Str(four.get("type")).equals("可以选修的模块")) {
                        str1.append(fourMode.getVal1());
                        str1.append("、");
                    } else if (tx2Str(four.get("type")).equals("必须通过的模块")) {
                        str2.append(fourMode.getVal1());
                        str2.append("、");
                    }
                }

            if (str1.length() >= 2) {
                str1 = new StringBuilder(str1.substring(0, str1.length() - 1));
            }
            if (str2.length() >= 2) {
                str2 = new StringBuilder(str2.substring(0, str2.length() - 1));
            }
            //可以选修的模块：
            vo.setChoosePass(str1.toString());
            //必须通过的模块：
            vo.setMustPass(str2.toString());
        }

        //本课程合格标准
        one = courseStand.getJSONObject("five");
        if (one != null) {
            vo.setPassStandard(one.getString("desc"));
            one = courseStand.getJSONObject("six");
            vo.setTeacherTeam(one.getString("teacherTeam"));
            vo.setFacilities(one.getString("facilities"));
            vo.setResources(one.getString("resources"));
            vo.setSuggest(one.getString("suggest"));
        }
        return vo;
    }
    ////////////////////////////////////工厂方法  生成课程标准 end <====

    private Workbook createCoreModelTab34(CoreModeVo cmv, HSSFCellStyle styleBar, HSSFCellStyle styleCnt, HSSFWorkbook excel, HSSFRow scndRow) {
        HSSFSheet sheet = excel.getSheet(cmv.getName());
        HSSFCell cell;
        cell = scndRow.createCell(8);
        cell.setCellStyle(styleBar);
        cell.setCellValue("所属课程");
        cell = scndRow.createCell(9);
        cell.setCellStyle(styleBar);
        cell.setCellValue("课时");
        //获取数据
        List<SubMode2> mList = cmv.getThird();
        SubMode2 mTmp = null;
        HSSFRow tmpRow = null;
        HSSFCell tmpCel = null;
        int maxRow = 2;
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                mTmp = mList.get(i);
                tmpRow = this.getRow(sheet, maxRow);
                tmpCel = this.getCell(tmpRow, 8);
                tmpCel.setCellStyle(styleCnt);
                tmpCel.setCellValue(mTmp.getVal1());
                tmpCel = this.getCell(tmpRow, 9);
                tmpCel.setCellStyle(styleCnt);
                tmpCel.setCellValue(mTmp.getVal2());
                maxRow++;
            }
        }
        //  tab4
        mList = cmv.getFouth();
        //title
        cell = scndRow.createCell(11);
        cell.setCellValue(cmv.getName());
        cell.setCellStyle(styleBar);
        HSSFCellStyle title2Style = this.getCoreModeHSSFCellStyle(excel, 3);
        //子title
        tmpRow = this.getRow(sheet, 2);
        cell = tmpRow.createCell(11);
        cell.setCellValue("课程");
        cell.setCellStyle(title2Style);
        cell = tmpRow.createCell(12);
        cell.setCellValue("总学时");
        cell.setCellStyle(title2Style);
        cell = tmpRow.createCell(13);
        cell.setCellValue("理论");
        cell.setCellStyle(title2Style);
        cell = tmpRow.createCell(14);
        cell.setCellValue("实训");
        cell.setCellStyle(title2Style);

        sheet.addMergedRegion(new CellRangeAddress(1, 1, (short) 11, (short) 14));
        //合并后补边框
        RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(1, 1, (short) 11, (short) 14), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(1, 1, (short) 14, (short) 14), sheet);

        maxRow = 3;
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                mTmp = mList.get(i);
                tmpRow = this.getRow(sheet, maxRow);
                tmpCel = this.getCell(tmpRow, 11);
                if (i == mList.size() - 1) {
                    tmpCel.setCellStyle(title2Style);
                } else {
                    tmpCel.setCellStyle(styleCnt);
                }
                tmpCel.setCellValue(mTmp.getVal1());
                tmpCel = this.getCell(tmpRow, 12);
                if (i == mList.size() - 1) {
                    tmpCel.setCellStyle(title2Style);
                } else {
                    tmpCel.setCellStyle(styleCnt);
                }
                tmpCel.setCellValue(mTmp.getVal2());
                tmpCel = this.getCell(tmpRow, 13);
                if (i == mList.size() - 1) {
                    tmpCel.setCellStyle(title2Style);
                } else {
                    tmpCel.setCellStyle(styleCnt);
                }
                tmpCel.setCellValue(mTmp.getVal3());
                tmpCel = this.getCell(tmpRow, 14);
                if (i == mList.size() - 1) {
                    tmpCel.setCellStyle(title2Style);
                } else {
                    tmpCel.setCellStyle(styleCnt);
                }
                tmpCel.setCellValue(mTmp.getVal4());
                maxRow++;
            }
        }
        return excel;
    }

    private void createCoreModelTab2(CoreModeVo cmv, HSSFCellStyle styleBar, HSSFCellStyle styleCnt, HSSFSheet sheet, HSSFRow scndRow) {
        HSSFRow tmpRow = null;
        HSSFCell tmpCel = null;
        SubMode mTmp = null, mChldTmp = null;
        int maxRow = 1;
        tmpRow = this.getRow(sheet, maxRow);
        tmpCel = scndRow.createCell(3);
        tmpCel.setCellStyle(styleBar);
        tmpCel.setCellValue("主要工作任务");

        tmpCel = scndRow.createCell(4);
        tmpCel.setCellStyle(styleBar);
        tmpCel.setCellValue("工作内容");

        tmpCel = scndRow.createCell(5);
        tmpCel.setCellStyle(styleBar);
        tmpCel.setCellValue("技能点");

        tmpCel = scndRow.createCell(6);
        tmpCel.setCellStyle(styleBar);
        tmpCel.setCellValue("知识点");

        //获取数据
        List<SubMode> m2List = cmv.getSecond();
        List<SubMode> chld2 = null, chld3 = null, chld4 = null;
        //进去显示-子行 前的行号
        int inRow2 = 0;
        int inRow3 = 0;
        int inRow4 = 0;
        maxRow++;

        if (m2List != null && m2List.size() > 0) {
            for (int i = 0; i < m2List.size(); i++) {
                mTmp = m2List.get(i);

                tmpRow = this.getRow(sheet, maxRow);
                tmpCel = this.getCell(tmpRow, 3);
                tmpCel.setCellStyle(styleCnt);
                tmpCel.setCellValue(mTmp.getTxt());

                chld2 = mTmp.getChld();
                //可能写多行
                if (chld2 != null && chld2.size() > 0) {
                    inRow2 = maxRow;
                    for (int j = 0; j < chld2.size(); j++) {
                        mChldTmp = chld2.get(j);

                        tmpRow = this.getRow(sheet, maxRow);
                        tmpCel = this.getCell(tmpRow, 4);
                        tmpCel.setCellStyle(styleCnt);
                        tmpCel.setCellValue(mChldTmp.getTxt());

                        chld3 = mChldTmp.getChld();
                        //可能写多行
                        if (chld3 != null && chld3.size() > 0) {
                            inRow3 = maxRow;
                            for (int k = 0; k < chld3.size(); k++) {
                                mChldTmp = chld3.get(k);

                                tmpRow = this.getRow(sheet, maxRow);
                                tmpCel = this.getCell(tmpRow, 5);
                                tmpCel.setCellStyle(styleCnt);
                                tmpCel.setCellValue(mChldTmp.getTxt());

                                chld4 = mChldTmp.getChld();
                                //可能写多行
                                if (chld4 != null && chld4.size() > 0) {
                                    inRow4 = maxRow;
                                    for (int n = 0; n < chld4.size(); n++) {
                                        mChldTmp = chld4.get(n);

                                        tmpRow = this.getRow(sheet, maxRow);
                                        tmpCel = this.getCell(tmpRow, 6);
                                        tmpCel.setCellStyle(styleCnt);
                                        tmpCel.setCellValue(mChldTmp.getTxt());

                                        maxRow++;
                                    }
                                    //计算合并行
                                    if (maxRow - inRow4 > 1) {
                                        sheet.addMergedRegion(new CellRangeAddress(inRow4, maxRow - 1, (short) 5, (short) 5));
                                    }
                                } else {
                                    //补半个边框
                                    tmpCel = this.getCell(tmpRow, 3);
                                    // 第一个错误 tmpCel = this.getCell(tmpRow, 1);
                                    tmpCel.setCellStyle(styleCnt);
                                    tmpCel.setCellValue("");
                                    maxRow++;
                                }
                            }
                            //计算合并行
                            if (maxRow - inRow3 > 1) {
                                sheet.addMergedRegion(new CellRangeAddress(inRow3, maxRow - 1, (short) 4, (short) 4));
                            }
                        } else {
                            //补半个边框
                            tmpCel = this.getCell(tmpRow, 4);
                            tmpCel.setCellStyle(styleCnt);
                            tmpCel.setCellValue("");
                            maxRow++;
                        }
                    }
                    //计算合并行
                    if (maxRow - inRow2 > 1) {
                        sheet.addMergedRegion(new CellRangeAddress(inRow2, maxRow - 1, (short) 3, (short) 3));
                    }
                } else {
                    //补半个边框
                    tmpCel = this.getCell(tmpRow, 5);
                    tmpCel.setCellStyle(styleCnt);
                    tmpCel.setCellValue("");
                    maxRow++;
                }
            }
            //补合并后的左边框
            RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(2, maxRow - 1, (short) 3, (short) 3), sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(2, maxRow - 1, (short) 4, (short) 4), sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(2, maxRow - 1, (short) 5, (short) 6), sheet);
            //补合并后的下边框
            RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(maxRow - 1, maxRow - 1, (short) 3, (short) 6), sheet);
        }
    }

    private void createCoreModelTab1(CoreModeVo cmv, HSSFCellStyle styleBar, HSSFCellStyle styleTop, HSSFSheet sheet, HSSFRow scndRow) {

        HSSFCell cell = scndRow.createCell(0);
        cell.setCellStyle(styleBar);
        cell.setCellValue("职业");

        cell = scndRow.createCell(1);
        cell.setCellStyle(styleBar);
        cell.setCellValue("主要工作任务");

        //获取数据
        List<SubMode> m1List = cmv.getFirst();

        // 迭代中处理模型数据的临时变量
        SubMode mTmp = null, mChldTmp = null;
        int maxRow = 2;
        HSSFRow tmpRow = null;
        HSSFCell tmpCel = null;
        List<SubMode> chld = null;
        //进去显示-子行 前的行号
        int inRow = 0;

        if (m1List != null && m1List.size() > 0) {
            for (int i = 0; i < m1List.size(); i++) {
                mTmp = m1List.get(i);

                tmpRow = this.getRow(sheet, maxRow);
                tmpRow.setHeightInPoints(50);
                tmpCel = this.getCell(tmpRow, 0);
                tmpCel.setCellStyle(styleTop);
                tmpCel.setCellValue(mTmp.getTxt());
                chld = mTmp.getChld();

                //可能写多行
                if (chld != null && chld.size() > 0) {
                    inRow = maxRow;
                    for (int j = 0; j < chld.size(); j++) {
                        mChldTmp = chld.get(j);
                        tmpRow = this.getRow(sheet, maxRow);
                        tmpCel = this.getCell(tmpRow, 1);
                        tmpCel.setCellStyle(styleTop);
                        tmpCel.setCellValue(mChldTmp.getTxt());
                        maxRow++;
                    }
                    //计算合并行
                    if (maxRow - inRow > 1) {
                        sheet.addMergedRegion(new CellRangeAddress(inRow, maxRow - 1, (short) 0, (short) 0));
                    }
                } else {
                    //补半个边框
                    tmpCel = this.getCell(tmpRow, 1);
                    tmpCel.setCellStyle(styleTop);
                    tmpCel.setCellValue("");
                    maxRow++;
                }
            }
        }
    }

    private CoreModeVo genCoreMode(String id) {
        //数据库取一个
        Coremodel cm = iCoremodelService.getById(id);
        Subject sbjt = this.subjectService.getById(cm.getSid());
        //还原成json
        JSONArray coremodel = JSON.parseArray(cm.getCoremodel());
        //coremodel  ------>   cmv
        CoreModeVo cmv = new CoreModeVo();
        cmv.setName(sbjt.getName() + "-" + cm.getGrade());

        //临时变量
        JSONObject f1 = null, f2 = null, f3 = null, f4 = null, f5 = null, f6 = null, f7 = null, f8 = null;
        JSONArray f1Children = null, f2Children = null, f3Children = null, f4Children = null, f5Children = null, f6Children = null;
        SubMode sub1 = null;
        SubMode sub3 = null;
        SubMode sub4 = null;
        SubMode sub5 = null;
        SubMode sub6 = null;
        SubMode2 sub7 = null;

        if (coremodel != null)
            //处理第1层
            for (int i = 0; i < coremodel.size(); i++) {
                f1 = coremodel.getJSONObject(i);

                f1Children = f1.getJSONArray("children");
                if (f1Children != null) {
                    sub1 = new SubMode();
                    sub1.setTxt(tx2Str(f1.get("name")));
                    //处理第2层
                    for (int y = 0; y < f1Children.size(); y++) {
                        //掠过第2层
                        f2 = f1Children.getJSONObject(y);
                        f2Children = f2.getJSONArray("children");
                        if (f2Children != null) {
                            //处理第3层
                            for (int j = 0; j < f2Children.size(); j++) {
                                f3 = f2Children.getJSONObject(j);
                                sub3 = new SubMode();
                                sub3.setTxt(tx2Str(f3.get("name")));
                                sub1.getChld().add(sub3);

                                f3Children = f3.getJSONArray("children");
                                if (f3Children != null)
                                    //处理第4层
                                    for (int k = 0; k < f3Children.size(); k++) {
                                        f4 = f3Children.getJSONObject(k);
                                        sub4 = new SubMode();
                                        sub4.setTxt(tx2Str(f4.get("name")));
                                        sub3.getChld().add(sub4);

                                        f4Children = f4.getJSONArray("children");
                                        if (f4Children != null)
                                            //处理第5层
                                            for (int n = 0; n < f4Children.size(); n++) {
                                                f5 = f4Children.getJSONObject(n);
                                                sub5 = new SubMode();
                                                sub5.setTxt(tx2Str(f5.get("name")));
                                                sub4.getChld().add(sub5);

                                                f5Children = f5.getJSONArray("children");
                                                if (f5Children != null)
                                                    //处理第6层
                                                    for (int m = 0; m < f5Children.size(); m++) {
                                                        f6 = f5Children.getJSONObject(m);
                                                        sub6 = new SubMode();
                                                        sub6.setTxt(tx2Str(f6.get("name")));
                                                        sub5.getChld().add(sub6);

                                                        f6Children = f6.getJSONArray("children");
                                                        if (f6Children != null)
                                                            //处理第7层+8层
                                                            for (int g = 0; g < f6Children.size(); g++) {
                                                                f7 = f6Children.getJSONObject(g);
                                                                f8 = f7.getJSONArray("children").getJSONObject(0);
                                                                //处理tab3
                                                                sub7 = new SubMode2();
                                                                sub7.setVal1(tx2Str(f7.get("name")));
                                                                sub7.setVal2(tx2Str(f8.get("name")));
                                                                cmv.getThird().add(sub7);

                                                                //处理tab4
//                                                        sub7 = new SubMode2();
//                                                        sub7.setVal1(tx2Str(f7.get("name")));
//                                                        sub7.setVal2(tx2IntStr(f8.get("all")));
//                                                        sub7.setVal3((Integer.parseInt(tx2IntStr(f8.get("all"))) - Integer.parseInt(tx2IntStr(f8.get("sx")))) + "");
//                                                        sub7.setVal4(tx2IntStr(f8.get("sx")));
//                                                        cmv.getFouth().add(sub7);
                                                                sub7 = new SubMode2();
                                                                sub7.setVal1(tx2Str(f7.get("name")));
                                                                sub7.setVal2(tx2IntStr(f7.get("all")));
                                                                sub7.setVal3((Integer.parseInt(tx2IntStr(f7.get("all"))) - Integer.parseInt(tx2IntStr(f7.get("sx")))) + "");
                                                                sub7.setVal4(tx2IntStr(f7.get("sx")));
                                                                cmv.getFouth().add(sub7);
                                                            }
                                                    }
                                            }
                                    }
                                //处理tab2
                                cmv.getSecond().add(sub3);
                            }
                        }
                    }
                }
                //处理tab1
                //第一个子表
                cmv.getFirst().add(sub1);
            }

        //计算tab4汇总最后一行
        int v2 = 0, v3 = 0, v4 = 0;
        for (SubMode2 sm : cmv.getFouth()) {
            v2 += Integer.parseInt(sm.getVal2());
            v3 += Integer.parseInt(sm.getVal3());
            v4 += Integer.parseInt(sm.getVal4());
        }
        sub7 = new SubMode2();
        sub7.setVal1("总课时");
        sub7.setVal2(v2 + "");
        sub7.setVal3(v3 + "");
        sub7.setVal4(v4 + "");
        cmv.getFouth().add(sub7);

        //封装
        return cmv;
    }

    private int calTab2Rows(List<SubMode> list) {
        int rows = 0;
        List<SubMode> list2, list3 = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getChld().size() == 0) {
                //没孩子算当前的1行
                rows++;
            } else {
                //有孩子继续往下数
                list2 = list.get(i).getChld();
                for (int j = 0; j < list2.size(); j++) {
                    if (list2.get(j).getChld().size() == 0) {
                        rows++;
                    } else {
                        list3 = list2.get(j).getChld();
                        for (int k = 0; k < list3.size(); k++) {
                            if (list3.get(k).getChld().size() == 0) {
                                rows++;
                            } else {
                                //第4级不再往下
                                rows += list3.get(k).getChld().size();
                            }
                        }
                    }
                }
            }
        }
        return rows;
    }

    //工具1
    private String tx2Str(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    //工具2
    private String tx2IntStr(Object obj) {
        if (obj == null) {
            return "0";
        } else {
            return obj.toString();
        }
    }

    //Excel格式工具类
    private HSSFCellStyle getCoreModeHSSFCellStyle(HSSFWorkbook excel, int type) {
        //样式
        HSSFCellStyle styleTitle = excel.createCellStyle();
        HSSFCellStyle styleBar = excel.createCellStyle();
        HSSFCellStyle styleCntTop = excel.createCellStyle();
        HSSFCellStyle styleCntCenter = excel.createCellStyle();
        HSSFCellStyle styleTitle2 = excel.createCellStyle();

        HSSFFont font26 = excel.createFont();
        font26.setFontName("宋体");
        font26.setFontHeightInPoints((short) 26);//设置字体大小
        //水平居中
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitle.setFont(font26);

        HSSFFont font2 = excel.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 12);//设置字体大小
        font2.setBold(true);//粗体显示
        styleBar.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        styleBar.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleBar.setFont(font2);
        styleBar.setAlignment(HorizontalAlignment.CENTER);
        styleBar.setVerticalAlignment(VerticalAlignment.CENTER);

        HSSFFont font3 = excel.createFont();
        font3.setFontName("宋体");
        font3.setFontHeightInPoints((short) 12);//设置字体大小
        styleCntTop.setFont(font3);
        styleCntTop.setWrapText(true);  //自动换行
        styleCntTop.setVerticalAlignment(VerticalAlignment.TOP);
        styleCntTop.setAlignment(HorizontalAlignment.LEFT);

        styleCntCenter.setFont(font3);
        styleCntCenter.setWrapText(true);  //自动换行
        styleCntCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        styleCntCenter.setAlignment(HorizontalAlignment.CENTER);

        styleTitle2.setFont(font2);
        styleTitle2.setBorderBottom(BorderStyle.THIN); //下边框
        styleTitle2.setBorderLeft(BorderStyle.THIN);//左边框
        styleTitle2.setBorderTop(BorderStyle.THIN);//上边框
        styleTitle2.setBorderRight(BorderStyle.THIN);//右边框
        styleTitle2.setAlignment(HorizontalAlignment.CENTER);
        styleTitle2.setVerticalAlignment(VerticalAlignment.CENTER);

        styleBar.setBorderBottom(BorderStyle.THIN); //下边框
        styleBar.setBorderLeft(BorderStyle.THIN);//左边框
        styleBar.setBorderTop(BorderStyle.THIN);//上边框
        styleBar.setBorderRight(BorderStyle.THIN);//右边框

        styleCntTop.setBorderBottom(BorderStyle.THIN); //下边框
        styleCntTop.setBorderLeft(BorderStyle.THIN);//左边框
        styleCntTop.setBorderTop(BorderStyle.THIN);//上边框
        styleCntTop.setBorderRight(BorderStyle.THIN);//右边框
        styleCntTop.setAlignment(HorizontalAlignment.CENTER);
        styleCntTop.setVerticalAlignment(VerticalAlignment.CENTER);

        styleCntCenter.setBorderBottom(BorderStyle.THIN); //下边框
        styleCntCenter.setBorderLeft(BorderStyle.THIN);//左边框
        styleCntCenter.setBorderTop(BorderStyle.THIN);//上边框
        styleCntCenter.setBorderRight(BorderStyle.THIN);//右边框
        styleCntCenter.setAlignment(HorizontalAlignment.CENTER);
        styleCntCenter.setVerticalAlignment(VerticalAlignment.CENTER);

        if (type == 1) {
            return styleTitle;
        } else if (type == 2) {
            return styleBar;
        } else if (type == 3) {
            return styleTitle2;
        } else if (type == 4) {
            return styleCntCenter;
        } else {
            return styleCntTop;
        }
    }

    private HSSFRow getRow(HSSFSheet sheet, int rownum) {
        if (sheet.getRow(rownum) == null) {
            return sheet.createRow(rownum);
        } else {
            return sheet.getRow(rownum);
        }
    }

    private HSSFCell getCell(HSSFRow row, int cellnum) {
        if (row.getCell(cellnum) == null) {
            return row.createCell(cellnum);
        } else {
            return row.getCell(cellnum);
        }
    }
}
