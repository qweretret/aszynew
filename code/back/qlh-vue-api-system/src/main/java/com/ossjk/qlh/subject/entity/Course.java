package com.ossjk.qlh.subject.entity;


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
 * @ClassName: Course
 * @Description: 课程-实体类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@ApiModel(value = "课程")
@Data
public class Course extends BaseEntity<Course> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 课程名称
	 */
	@ApiModelProperty(value = "课程名称", required = true)
	@TableField("name")
	private String name;
	/**
	 * 课程代码
	 */
	@ApiModelProperty(value = "课程代码")
	@TableField("code")
	private String code;
	/**
	 * 课程类型
	 */
	@ApiModelProperty(value = "课程类型")
	@TableField("type")
	private String type;
	/**
	 * 课程类别1级
	 */
	@ApiModelProperty(value = "课程类别1级")
	@TableField("level1")
	@DbColumnMapper(tableName="coursetypes",columns="name",condition="code")
	private Integer level1;
	/**
	 * 课程类别2级
	 */
	@ApiModelProperty(value = "课程类别2级")
	@TableField("level2")
	@DbColumnMapper(tableName="coursetypes",columns="name",condition="code")
	private Integer level2;
	/**
	 * 所属专业
	 */
	@ApiModelProperty(value = "所属专业")
	@TableField("subjectid")
	@DbColumnMapper(tableName="subject",columns="name",condition="id")
	private String subjectid;
	 
	 
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
