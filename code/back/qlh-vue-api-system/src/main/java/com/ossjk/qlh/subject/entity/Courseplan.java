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
 * @ClassName: Courseplan
 * @Description: 课程安排-实体类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@ApiModel(value = "课程安排")
@Data
public class Courseplan extends BaseEntity<Courseplan> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 专业id
	 */
	@ApiModelProperty(value = "专业id", required = true)
	@TableField("sid")
	private String sid;
	/**
	 * 课程id
	 */
	@ApiModelProperty(value = "课程id", required = true)
	@TableField("csid")
	@DbColumnMapper(tableName="course",columns="name",condition="id")
	private String csid;
	/**
	 * 课程性质[选修,必修]
	 */
	@ApiModelProperty(value = "课程性质[选修,必修]")
	@TableField("csprop")
	private Integer csprop;
	/**
	 * 考核方式
	 */
	@ApiModelProperty(value = "考核方式")
	@TableField("testmode")
	private String testmode;
	/**
	 * 学分
	 */
	@ApiModelProperty(value = "学分")
	@TableField("credit")
	private Integer credit;
	/**
	 * 总学时
	 */
	@ApiModelProperty(value = "总学时")
	@TableField("period")
	private Integer period;
	/**
	 * 理论课总学时
	 */
	@ApiModelProperty(value = "理论课总学时")
	@TableField("theoryperiod")
	private Integer theoryperiod;
	/**
	 * 实践课总学时
	 */
	@ApiModelProperty(value = "实践课总学时")
	@TableField("actperiod")
	private Integer actperiod;
	/**
	 * 课程目标
	 */
	@ApiModelProperty(value = "课程目标")
	@TableField("cstarget")
	private String cstarget;
	/**
	 * 主要内容
	 */
	@ApiModelProperty(value = "主要内容")
	@TableField("cscontent")
	private String cscontent;
	/**
	 * 教学要求
	 */
	@ApiModelProperty(value = "教学要求")
	@TableField("teachneed")
	private String teachneed;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	@TableField("remarks")
	private String remarks;
	 
	 
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
