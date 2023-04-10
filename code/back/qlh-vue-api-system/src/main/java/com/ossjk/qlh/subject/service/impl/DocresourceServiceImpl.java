package com.ossjk.qlh.subject.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ossjk.config.mvc.ResourceMappersProperties;
import com.ossjk.qlh.subject.dto.FileDto;
import com.ossjk.qlh.subject.entity.Docresource;
import com.ossjk.qlh.subject.mapper.DocresourceMapper;
import com.ossjk.qlh.subject.service.IDocresourceService;
import com.ossjk.qlh.subject.vo.DocresourceVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.subject.service.impl
 * @ClassName: DocresourceServiceImpl
 * @Description: 资料库信息-服务实现类
 * @author: wanghaohui
 * @date: 2022-03-09 15:54:53
 */
@Service
public class DocresourceServiceImpl extends ServiceImpl<DocresourceMapper, Docresource> implements IDocresourceService {


    @Autowired
    private ResourceMappersProperties ymlUri;

    @Override
    public List<DocresourceVo> inductionName(String name) {


        return this.baseMapper.inductionName("%" + name + "%");
    }

    @Override
    public List<Docresource> isExistence(String fileName) {
        QueryWrapper<Docresource> wrapper = new QueryWrapper<>();
        wrapper.eq("name", fileName);
        return this.baseMapper.selectList(wrapper);

    }

    @Override
    @Transactional
    public boolean saveFile(FileDto dto) {
        int flag =0;
                       //类型,1现有的、2传新的                                    "1">外链  "2">附件
        if(  dto.getVo().getId()!=null  ){
            //写入join table
            flag += this.baseMapper.insertJoinTable(dto.getVo().getId(),dto.getSid());
            return flag== 1  ;
        }else{
            //新添加
            Docresource doc = new Docresource();
            try {
                BeanUtils.copyProperties( doc,dto.getVo() );
                         //mybatis-plus 会返回新插入的id
                flag = this.baseMapper.insert(doc);
                flag +=  this.baseMapper.insertJoinTable(doc.getId(),dto.getSid());

                return flag== 2  ;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public List<Docresource> selectBySid(  String sid) {
       return baseMapper.selectBySid(sid);
    }

    @Override
    @Transactional
    public boolean removeBySubject(  String sid,String id){
        //删连接信息
        Integer cnt = baseMapper.deleteBySid(sid,id);

        if(cnt ==1 ){
            //有没有其他专业在用该文件
            cnt =baseMapper.countDocInuse(id);
            //确认是最后一个引用
            if( cnt == 0  ){
                //取回附件 准备删除
                Docresource doc = baseMapper.selectById(id);
                //主表删除
                cnt =  baseMapper.deleteById(id);

                //删除物理文件           2是附件
                if(cnt == 1 && doc.getType()==2 ){ //文件
                    //读取物理path
                    List<ResourceMappersProperties.ResourceMapper> resourceMapperList = ymlUri.getMappers();
                    Map<String, String> uriMap = resourceMapperList.stream().collect(Collectors.toMap(ResourceMappersProperties.ResourceMapper::getUri, ResourceMappersProperties.ResourceMapper::getFile));
                    //解析给前端回显
                    int panfu = doc.getWeburl().lastIndexOf("/"); //盘符
                    String mth = doc.getWeburl().substring(0,panfu); //月份文件夹
                    mth = mth.substring(mth.length()-6);
                    String fname = doc.getWeburl().substring(panfu+1);
                    File file = new File(uriMap.get("/statics/zysz")+"/"+mth,fname);
                    if(file.exists()){
                        file.delete();
                    }
                    return true;
                }
            }
        }

        return cnt == 1;
    }
}
