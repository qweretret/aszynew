package com.ossjk.qlh.subject.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.core.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Copyright  2022-04-06 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.entity
 * @ClassName: Coursetimeplan
 * @Description: -实体类
 * @author: huang
 * @date:  2022-04-06 19:52:52 
 */
@ApiModel(value = "")
@Data
public class Coursetimeplan extends BaseEntity<Coursetimeplan> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 课程id
	 */
	@ApiModelProperty(value = "课程id")
	@TableField("cid")
	private String cid;

	/**
	 * 人才培养方案id
	 */
	@ApiModelProperty(value = "年级")
	@TableField("grade")
	private String grade;
	/**
	 * 学年
	 */
	@ApiModelProperty(value = "学年")
	@TableField("year")
	private Integer year;


	/**
	 * 学期
	 */
	@ApiModelProperty(value = "学期")
	@TableField("term")
	private Integer term;
	/**
	 * 理论学时
	 */
	@ApiModelProperty(value = "理论学时")
	@TableField("theorytime")
	private Integer theorytime;
	/**
	 * 理论周数
	 */
	@ApiModelProperty(value = "理论周数")
	@TableField("theoryweek")
	private Integer theoryweek;
	/**
	 * 实践学时
	 */
	@ApiModelProperty(value = "实践学时")
	@TableField("practicetime")
	private Integer practicetime;
	/**
	 * 实践周数
	 */
	@ApiModelProperty(value = "实践周数")
	@TableField("praticeweek")
	private Integer praticeweek;
	 
	 
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
