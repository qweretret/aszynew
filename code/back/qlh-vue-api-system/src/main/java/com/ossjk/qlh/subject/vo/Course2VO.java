package com.ossjk.qlh.subject.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.core.base.entity.BaseEntity;
import com.ossjk.core.system.annotation.DbColumnMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** 
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.entity
 * @ClassName: Course
 * @Description: 课程-实体类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@ApiModel(value = "课程VO2")
@Data
public class Course2VO extends BaseEntity<Course2VO> {

	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;

	@ApiModelProperty(value = "所属专业")
	@TableField("subjectid")
	@DbColumnMapper(tableName="subject",columns="name",condition="id")
	private String subjectid;

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

	@ApiModelProperty(value = "课程性质[选修,必修]")
	@TableField("csprop")
	private Integer csprop;

	@ApiModelProperty(value = "考核方式")
	@TableField("testmode")
	private String testmode;

	@ApiModelProperty(value = "学分")
	@TableField("credit")
	private Integer credit;

	@ApiModelProperty(value = "总学时")
	@TableField("period")
	private Integer period;

	@ApiModelProperty(value = "理论课学时")
	@TableField("theoryperiod")
	private Integer theoryperiod;

	@ApiModelProperty(value = "课程目标")
	@TableField("cstarget")
	private String cstarget;

	@ApiModelProperty(value = "主要内容")
	@TableField("cscontent")
	private String cscontent;

	@ApiModelProperty(value = "教学要求")
	@TableField("teachneed")
	private String teachneed;

	@ApiModelProperty(value = "level1")
	@TableField("level1")
	@DbColumnMapper(tableName="coursetypes",columns="name",condition="code")
	private String level1;

	@ApiModelProperty(value = "level2")
	@DbColumnMapper(tableName="coursetypes",columns="name",condition="code")
	@TableField("level2")
	private String level2;


}
