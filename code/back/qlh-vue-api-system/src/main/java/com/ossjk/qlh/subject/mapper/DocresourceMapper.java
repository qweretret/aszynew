package com.ossjk.qlh.subject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ossjk.qlh.subject.entity.Docresource;
import com.ossjk.qlh.subject.vo.DocresourceVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.mapper
 * @ClassName: DocresourceMapper
 * @Description: 资料库信息-Mapper
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
public interface DocresourceMapper extends BaseMapper<Docresource> {

    @Select("select id,name as value ,type,weburl from docresource where name like #{name}")
    List<DocresourceVo> inductionName(String name);

    @Insert("insert into  doc2subject(docid,sid) value (#{docid},#{sid})")
    Integer insertJoinTable(@Param("docid") String docid, @Param("sid")String sid);

    @Select("select * from docresource d left join doc2subject ds on ds.docid =d.id  where ds.sid = #{sid}")
    List<Docresource> selectBySid(@Param("sid") String sid);

    @Delete("delete from  doc2subject where sid = #{sid} and  docid = #{id}")
    Integer deleteBySid(  @Param("sid")String sid, @Param("id")String id);

    @Select("select count(sid) from doc2subject where docid = #{id}")
    Integer countDocInuse(    @Param("id")String id);
}
