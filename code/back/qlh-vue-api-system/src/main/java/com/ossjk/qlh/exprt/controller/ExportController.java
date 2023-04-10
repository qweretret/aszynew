package com.ossjk.qlh.exprt.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ossjk.core.base.controller.BaseController;
import com.ossjk.core.vo.ResponseBean;
import com.ossjk.qlh.exprt.service.IExportService;
import com.ossjk.qlh.exprt.vo.CourseStandVo;
import com.ossjk.qlh.exprt.vo.TrainplanDocVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.subject.controller
 * @ClassName: TrainingplanController
 * @Description: Trainingplan-控制器
 * @author: Rick
 * @date: 2022-03-22 16:30:01
 */
@Api(tags = "导出文件专用类")
@RestController
@RequestMapping("/export")
public class ExportController extends BaseController {

    @Autowired
    private IExportService iExportService;

    @ApiOperation(value = "专业核心能力模型")
    //@RequiresPermissions("")
    @GetMapping("/getCmd")
    public ResponseBean getCoreMode(@ApiParam(value = "id") @RequestParam(name = "id") String id,
                                    @ApiParam(value = "fn") @RequestParam(name = "fn") String fn,
                                    HttpServletRequest request, HttpServletResponse response) {
		/*
		      SpringMVC通过代理设计模式代理   Servlet API  封装表单参数:
              String id = request.getParameter("id");
		 */
        //String id = request.getParameter("id");
        //String fn = request.getParameter("fn");

        Workbook wb = iExportService.getCoreModeVo(fn, id);
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fn);
        response.setCharacterEncoding("UTF-8");
        try {
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            wb.close();
            return ResponseBean.Success();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseBean.Fail();
    }

    @ApiOperation(value = "导出课程标准")
    //@RequiresPermissions("")
    @GetMapping("/getCrsDoc")
    public ResponseBean<CourseStandVo> getCoursestand(@ApiParam(value = "课程id") @RequestParam(name = "id") String id,
                                                      HttpServletResponse response) {

        XWPFDocument ctv = iExportService.getCourseStandVo( id );
        response.setHeader("content-Type", "application/vnd.ms-word");
        response.setHeader("Content-Disposition", "attachment;filename=f.doc");
        response.setCharacterEncoding("UTF-8");

        try {
            OutputStream out = response.getOutputStream();
            ctv.write(out);
            out.flush();
            out.close();
            ctv.close();
            return ResponseBean.Success();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseBean.Fail();
    }

    @ApiOperation(value = "人才培养  导出完整方案")
    //@RequiresPermissions("")
    @GetMapping("/getAllTpv")
    public void getAllTrainplan(@ApiParam(value = "id") @RequestParam(name = "id") String id,
                             @ApiParam(value = "cid") @RequestParam(name = "cid") String cid,
                             HttpServletResponse response) {
        File zipFile = iExportService.getTrainplan(id, cid);

        response.setHeader("content-Type", "application/vnd.ms-word");
        response.setHeader("Content-Disposition", "attachment;filename=f.doc");
        response.setCharacterEncoding("UTF-8");

        try {
            if (zipFile.exists()) {
                InputStream in = new FileInputStream(zipFile);
                OutputStream out = response.getOutputStream();

                //把本地文件发送给浏览器    缓存8k
                byte[] buf = new byte[8192];

                int len = 0;

                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }

                out.flush();
                out.close();
                in.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "导出人才培养方案")
    //@RequiresPermissions("")
    @GetMapping("/getTrainplan")
    public ResponseBean<TrainplanDocVo> getPersonnel(@ApiParam(value = "id") @RequestParam(name = "id") String id,
                                                  HttpServletResponse response) {
        System.out.println(id);
        XWPFDocument ctv = iExportService.getTrainplanWord(id);
        response.setHeader("content-Type","application/vnd.ms-word");
        response.setHeader("Content-Disposition","attachment;filename=f.doc");
        response.setCharacterEncoding("UTF-8");

        try {
            OutputStream out = response.getOutputStream();
            ctv.write(out);
            out.flush();
            out.close();
            ctv.close();
            return ResponseBean.Success( );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseBean.Fail();
    }

    @ApiOperation(value = "教学课程周安排表")
    //@RequiresPermissions("")
    @GetMapping("/getTimeplan")
    public ResponseBean getTimeplan() {

        iExportService.getTimeplan();

        if (ObjectUtil.isNotNull(1)) {
            return ResponseBean.Success(1);
        } else {
            return ResponseBean.Fail();
        }
    }


}
