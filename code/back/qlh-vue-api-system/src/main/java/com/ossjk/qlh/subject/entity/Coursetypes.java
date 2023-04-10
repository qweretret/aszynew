package com.ossjk.qlh.subject.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.core.base.entity.BaseEntity;
import com.ossjk.core.system.annotation.DbColumnMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.entity
 * @ClassName: Coursetypes
 * @Description: 课程类别-实体类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@ApiModel(value = "课程类别")
@Data
public class Coursetypes extends BaseEntity<Coursetypes> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 分类代码
	 */
	@ApiModelProperty(value = "分类代码", required = true)
	@TableField("code")
	private Integer code;
	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称", required = true)
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "创建人")
	@TableField(value = "crer", fill = FieldFill.INSERT)
	@DbColumnMapper(tableName="user",columns="name",condition="id")
	private String crer;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	@TableField(value = "mder", fill = FieldFill.INSERT_UPDATE)
	@DbColumnMapper(tableName="user",columns="name",condition="id")
	// select name as dbColumn_mder from user where id=#{mder}
	private String mder;
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
