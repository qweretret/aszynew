package com.ossjk.qlh.exprt.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ossjk.qlh.exprt.vo.SubMode;
import com.ossjk.qlh.exprt.vo.TrainplanDocVo;
import com.ossjk.qlh.trainplan.entity.Trainingplan;
import com.ossjk.qlh.trainplan.service.ITrainingplanService;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TrainPlanDocUtil {

    @Autowired
    public ITrainingplanService iTrainingplanService;

    public XWPFDocument generateDoc(String id) {

        //取得封装后的java 对象模型
        TrainplanDocVo vo = generateTrainplanDocVo(id);
        
        //在内存中创建word doc
        XWPFDocument xwpfDocument = new XWPFDocument();

        XWPFParagraph xwpfParagraph = checkXWPFParagraph(xwpfDocument, 0);

        //设置方案标题
        XWPFRun xwpfRun = checkXWPFRun(xwpfParagraph, 0);
        xwpfRun.setText("\r职业院校专业人才培养方案\r");


        //设置step1标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun =  checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040一、专业名称及代码");

        //设置step1内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040专业名称："+vo.getSubname()
                +"\r\040\040\040\040专业代码："+vo.getCode());

        //设置step2标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040二、入学要求");

        //设置step2内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        String enterNd = vo.getEnterNd().replaceAll("\"", "");
        xwpfRun.setText("\040\040\040\040"+enterNd.substring(1,enterNd.length()-1));

        //设置step3标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040三、修业年限");

        //设置step3内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040"+vo.getXynx()+"年");

        //设置step4标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040四、职业面向");

        //设置step4内容-表格
        List<String[]> step4 = vo.getStep4();
        int rows;
        int cols = 6;
        if(step4!=null && step4.size()>0){
            rows = step4.size() + 1;
        }else {
            rows = 1;
        }
        //word的表格渲染
        XWPFTable table = xwpfDocument.createTable(rows, cols);
        table.setTableAlignment(TableRowAlign.CENTER);
        //设置表头
        checkTableRun(table, 0, 0,0,null).setText("所属专业大类（代码）");
        checkTableRun(table, 0, 1,0,null).setText("所属专业类（代码）");
        checkTableRun(table, 0, 2,0,null).setText("对应行业（代码）");
        checkTableRun(table, 0, 3,0,null).setText("主要职业类别（代码）");
        checkTableRun(table, 0, 4,0,null).setText("主要岗位群或技术领域举例");
        checkTableRun(table, 0, 5,0,null).setText("职业资格证书和职业技能等级证书举例");
        //设置表体
        if(rows>1){
            for(int i=1;i<rows;i++){
                for(int j = 0;j<cols;j++){//这里还有”代码“没有写进去
                    checkTableRun(table,i,j,1,null).setText(step4.get(i-1)[j]);
                }
            }
        }

        //设置step5标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040五、培养目标与培养规格");

        //设置step5内容
        //（一）培养目标
        //小标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（一）培养目标");
        //内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040"+vo.getRcpymb());

        //（二）培养规格
        //小标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（二）培养规格");
        //内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        StringBuilder content = new StringBuilder("\040\040\040\040毕业生能力要求：");
        content.append("\r\040\040\040\040素质要求：").append(vo.getSzyq()).append("\r\040\040\040\040知识要求：")
                .append(vo.getZsyq()).append("\r\040\040\040\040能力要求：").append(vo.getNlyq());
        xwpfRun.setText(content.toString());

        //（三）毕业生能力要求指标点
        //小标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（三）毕业生能力要求指标点");
        //内容-表格
        List<SubMode> byszbd = vo.getByszbd();
        //计算行数列数
        rows = 1;
        cols = 2;
        if(byszbd.size()>0){
            for(SubMode s:byszbd){
                if(s.getChld().size()>0)
                    rows+=s.getChld().size();
            }
            if(rows<byszbd.size()+1){
                rows = byszbd.size();
            }
        }
        table = xwpfDocument.createTable(rows, cols);
        table.setTableAlignment(TableRowAlign.CENTER);
        //设置表头
        checkTableRun(table,0,0,0,"5000").setText("毕业生能力要求");
        checkTableRun(table,0,1,0,"5000").setText("毕业生能力要求指标点");
        //设置表体
        if(rows>1){
            int index = 1;//当前指针，跳过第一行表头
            int lastIndex = 1;//记录上一个起始点指针
            for(SubMode subMode:byszbd){
                if(subMode.getChld().size()>0){
                    checkTableRun(table,index,0,1,"5000").setText(subMode.getTxt());
                    lastIndex = index;
                    List<SubMode> children = subMode.getChld();
                    for (int i = 0; i < children.size(); i++) {
                        checkTableRun(table,index,1,1,"5000").setText(children.get(i).getTxt());
                        index++;
                    }
                }
                mergeCellsByCol(table,0,lastIndex,index-1);
            }

        }

        //（四）课程体系与毕业生能力指标点关联矩阵
        //小标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（四）课程体系与毕业生能力指标点关联矩阵");
        //内容-表格
        //计算行列
        Map<String, List<String[]>> zbdgl = vo.getZbdgl();
        rows = 2;
        cols = 2;
        rows+=zbdgl.size();
        if(vo.getTitles()!=null && vo.getTitles().length>0){
            cols+=vo.getTitles().length;
        }else {
            cols++;
        }
        table = xwpfDocument.createTable(rows,cols);
        table.setTableAlignment(TableRowAlign.CENTER);
        //设置表头
        checkTableRun(table,0,0,0,"2000").setText("课程性质名");
        checkTableRun(table,0,1,0,"2000").setText("课程名称");
        StringBuilder temp = new StringBuilder(vo.getSubname());
        temp.append("-").append(vo.getYear()).append("年").append("\n").append("毕业生能力要求指标点");
        String width = 6000/vo.getTitles().length + "";
        checkTableRun(table,0,2,0,width).setText(temp.toString());
        if(vo.getTitles()!=null && vo.getTitles().length>0){
            for (int i = 0; i < vo.getTitles().length; i++) {
                checkTableRun(table,0,i+2,404,width);//第三个参数不选中类型，只用于设置单元格
                checkTableRun(table,1,i+2,0,width).setText(vo.getTitles()[i]);
            }
        }
        //表头合并处理
        mergeCellsByCol(table,0,0,1);
        mergeCellsByCol(table,1,0,1);
        mergeCellsByRow(table,0,2,cols-1);

        //设置表体
        if(rows>2){
            int index = 2;//当前指针，跳过前两行表头
            int lastIndex = 2;//记录上一个起始点指针
            for(String k:zbdgl.keySet()){
                List<String[]> strings = zbdgl.get(k);
                checkTableRun(table,index,0,1,"2000").setText(k);
                lastIndex = index;
                for(int i=0;i<strings.size();i++){
                    checkTableRun(table,index,1,1,"2000").setText(strings.get(i)[0]);
                    for(int j=1;j<strings.get(i).length;j++){
                        checkTableRun(table,index,j+1,1,width).setText(strings.get(i)[j]);
                    }
                    index++;
                }
                mergeCellsByCol(table,1,lastIndex,index-1);
            }
        }

        //设置step6标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040六、课程设置及要求");

        //设置step6内容-表格
        List<String[]> step6 = vo.getStep6();
        rows = 1;
        cols = 6;
        if(step6!=null && step6.size()>0){
            rows += step6.size();
        }
        table = xwpfDocument.createTable(rows, cols);
        table.setTableAlignment(TableRowAlign.CENTER);
        //设置表头
        checkTableRun(table, 0, 0,0,null).setText("课程性质");
        checkTableRun(table, 0, 1,0,null).setText("课程");
        checkTableRun(table, 0, 2,0,null).setText("课程目标");
        checkTableRun(table, 0, 3,0,null).setText("主要内容");
        checkTableRun(table, 0, 4,0,null).setText("教学要求");
        checkTableRun(table, 0, 5,0,null).setText("备注");
        //设置表体
        if(rows>1){
            for(int i=1;i<rows;i++){
                for(int j = 0;j<cols;j++){
                   checkTableRun(table,i,j,1,null).setText(step6.get(i-1)[j]);
                }
            }
        }

        //设置step7标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040七、教学进程总体安排");

        //设置step7内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040详情见附件：教学进程总体安排\r\040\040\040\040教学进程总体安排备注："+vo.getStep7());

        //设置step8标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040八、实施保障");

        //设置step8内容
        //小标题（一）师资队伍
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（一）师资队伍");
        //内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040"+vo.getSzdw());

        //小标题（二）教学设施
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（二）教学设施");
        //内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040"+vo.getJxss());

        //小标题（三）教学资源
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（三）教学资源");
        //内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040"+vo.getJxzy());

        //小标题（四）教学方法
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（四）教学方法");
        //内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040"+vo.getJxff());

        //小标题（五）学习评价
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（五）学习评价");
        //内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040"+vo.getXxpj());

        //小标题（六）质量管理
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,3);
        xwpfRun.setText("\040\040\040\040（六）质量管理");
        //内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040"+vo.getZlgl());

        //设置step9标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040九、毕业要求");
        //设置step9内容
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,2);
        xwpfRun.setText("\040\040\040\040"+vo.getRequirement());

        //设置step10标题
        xwpfParagraph = checkXWPFParagraph(xwpfDocument,0);
        xwpfRun = checkXWPFRun(xwpfParagraph,1);
        xwpfRun.setText("\040\040\040\040十、附录");


        //返回doc对象
        return xwpfDocument;
    }
    

    public TrainplanDocVo generateTrainplanDocVo(String id)  {
        //获取字段
        Trainingplan trainingplan = iTrainingplanService.getById(id);
        JSONObject jsonObject = JSON.parseObject(trainingplan.getRcplan());

        //创建存储对象
        TrainplanDocVo vo = new TrainplanDocVo();

        //封装step1
        vo.setCode(jsonObject.getString("code"));
        vo.setSubname(jsonObject.getString("subname"));
        vo.setYear(jsonObject.getString("year"));

        //封装step2
        vo.setEnterNd(jsonObject.getString("enterNd"));

        //封装step3
        vo.setXynx(jsonObject.getString("xynx"));

        //封装step4
        JSONArray step4 = jsonObject.getJSONArray("step4");
        if(step4 !=null ){
            for(int i = 0;i<step4.size();i++){
                String[] row = new String[6];
                JSONObject objectTemp = (JSONObject) step4.get(i);
                row[0] = objectTemp.getString("sCategories");
                row[1] = objectTemp.getString("sClass");
                row[2] = objectTemp.getString("dyhy");
                row[3] = objectTemp.getString("zyzhlb");
                row[4] = objectTemp.getString("zygw");
                row[5] = objectTemp.getString("zyzs");
                vo.addStep4(row);
            }
        }

        //封装step5
        JSONObject step5 = (JSONObject) jsonObject.getJSONArray("step5").get(0);
        vo.setNlyq(step5.getString("nlyq"));
        vo.setRcpymb(step5.getString("rcpymb"));
        vo.setSzyq(step5.getString("szyq"));
        vo.setZsyq(step5.getString("zsyq"));
        String[] titles = step5.getObject("titles", String[].class);
        vo.setTitles(titles);
        //解析毕业生能力要求指标点
        JSONArray byszbds = step5.getJSONArray("byszbd");
        for(int i=0;i<byszbds.size();i++){
            JSONObject temp = (JSONObject) byszbds.get(i);
            String name = temp.getString("name");
            SubMode subMode1 = new SubMode();
            subMode1.setTxt(name);
            JSONArray jsonChildren = temp.getJSONArray("children");
            if(jsonChildren !=null && jsonChildren.size()>0){
                List<SubMode> children  = new ArrayList<>();
                for(int j = 0;j<jsonChildren.size();j++){
                    JSONObject childTemp = (JSONObject) jsonChildren.get(j);
                    String childName = childTemp.getString("name");
                    SubMode subMode2 = new SubMode();
                    subMode2.setTxt((i+1)+"-"+(j+1)+childName);
                    children.add(subMode2);
                }
                subMode1.setChld(children);
            }
            vo.addByszbdChild(subMode1);
        }
        //解析指标点关联矩阵
        JSONArray zbdgl = step5.getJSONArray("zbdgl");
        for(int i=0;i<zbdgl.size();i++){
            String key = ((JSONObject)zbdgl.get(i)).getString("name");
            List<String[]> value = new ArrayList<>();
            JSONArray children = ((JSONObject) zbdgl.get(i)).getJSONArray("children");
            for(int j=0;j<children.size();j++){
                String[] valueElement = new String[1+titles.length];
                String name = ((JSONObject) children.get(j)).getString("name");
                valueElement[0] = name;
                JSONArray checked = ((JSONObject) children.get(j)).getJSONArray("checked");
                for(int k=0;k<checked.size();k++){
                    if((checked.getString(k).equals("true"))){
                        valueElement[k+1] = "√";
                    }else {
                        valueElement[k+1] = "";
                    }
                }
                value.add(valueElement);
            }
            vo.putZbdglChild(key,value);
        }

        //封装step6
        JSONArray step6 = jsonObject.getJSONArray("step6");
        if(step6 !=null ){
            for(int i = 0;i<step6.size();i++){
                String[] row = new String[6];
                JSONObject objectTemp = (JSONObject) step6.get(i);
                row[0] = objectTemp.getString("dbColumn_level1")+"-"+objectTemp.getString("dbColumn_level2");
                row[1] = objectTemp.getString("name");
                row[2] = objectTemp.getString("cstarget");
                row[3] = objectTemp.getString("cscontent");
                row[4] = objectTemp.getString("teachneed");
                row[5] = objectTemp.getString("remarks");
                vo.addStep6(row);
            }
        }

        //封装step7
        vo.setStep7(jsonObject.getString("step7"));

        //封装step8
        JSONObject step8 = (JSONObject) jsonObject.getJSONArray("step8").get(0);
        vo.setXxpj(step8.getString("xxpj"));
        vo.setSzdw(step8.getString("szdw"));
        vo.setJxss(step8.getString("jxss"));
        vo.setZlgl(step8.getString("zlgl"));
        vo.setJxzy(step8.getString("jxzy"));
        vo.setJxff(step8.getString("jxff"));

        //封装step9
        vo.setRequirement(((JSONObject) jsonObject.getJSONArray("step9").get(0)).getString("requirement"));

        return vo;


    }
    
    public XWPFParagraph checkXWPFParagraph(XWPFDocument document, int type){
        XWPFParagraph pgph = document.createParagraph();
        if(type == 0){//不作任何操作，直接创建一个段落
        }else if(type == 1){//设置居中对齐
            pgph.setAlignment(ParagraphAlignment.CENTER);
        }else if(type == 2){//设置首行缩进
            pgph.setAlignment(ParagraphAlignment.LEFT);
            pgph.setFirstLineIndent(500);
        }
        return pgph;
    }

    //文本样式选择
    public XWPFRun checkXWPFRun (XWPFParagraph pgph, int type){
        XWPFRun run = pgph.createRun();
        //设置方案标题
        if(type == 0){
            //设置段落居中
            pgph.setAlignment(ParagraphAlignment.CENTER);
            run.setFontSize(22);
            run.setFontFamily("方正小标宋简体");
        }else if(type == 1){//设置step标题
            run.setFontSize(16);
            run.setFontFamily("黑体");
        }else if(type ==2){ //设置step内容
            run.setFontSize(12);
            run.setFontFamily("方正仿宋简体");
        }else if(type == 3){//设置小标题如（一）
            run.setFontSize(16);
            run.setFontFamily("楷体_GB2312");
            run.setBold(true);
        }

        return run;
    }

    //获取对应表格位置的XWPFRun
    public XWPFRun checkTableRun(XWPFTable table , int row, int col, int type,String width){

        XWPFRun xwpfRun = null;
        XWPFTableCell cell = table.getRow(row).getCell(col);
        XWPFParagraph pgph = cell.getParagraphs().get(0);
        pgph.setAlignment(ParagraphAlignment.CENTER);
        cell.getCTTc().addNewTcPr().addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中

        if(type == 0){//step4表表头
            xwpfRun = checkXWPFRun(pgph, 2);
            xwpfRun.setBold(true);
        }else if(type == 1){//step4表表体
            xwpfRun = checkXWPFRun(pgph, 2);
        }
        if(width !=null ){//如果非空，则设置单元格宽度
            CTTcPr tcpr = cell.getCTTc().addNewTcPr();
            CTTblWidth cellw = tcpr.addNewTcW();
            cellw.setType(STTblWidth.DXA);
            cellw.setW(new BigInteger(width));
        }
        return xwpfRun;
    }
    //合并单元格，按列合并行
    public  void mergeCellsByCol(XWPFTable table, int col, int begin, int end) {
        for (int i = begin; i <= end; i++) {
            XWPFTableCell cell = table.getRow(i).getCell(col);
            if (i == begin) {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }
    //合并单元格，按行合并列
    public  void mergeCellsByRow(XWPFTable table, int row, int begin, int end) {
        for (int i = begin; i <= end; i++) {
            XWPFTableCell cell = table.getRow(row).getCell(i);
            if (i == begin) {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }
}
