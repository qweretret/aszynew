package com.ossjk.qlh.subject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ossjk.qlh.subject.dto.FileDto;
import com.ossjk.qlh.subject.entity.Docresource;
import com.ossjk.qlh.subject.vo.DocresourceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.service
 * @ClassName: DocresourceService
 * @Description:  资料库信息-服务类接口
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
public interface IDocresourceService extends IService<Docresource> {

    List<DocresourceVo> inductionName(String name);

    List<Docresource> isExistence(String fileName);
    boolean saveFile(FileDto docresource);

    List<Docresource> selectBySid(  String sid);
    boolean  removeBySubject( String sid , String  id);
}
