package com.ossjk.qlh.subject.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.core.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.entity
 * @ClassName: Weekinterm
 * @Description: 学期教学周-实体类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@ApiModel(value = "学期教学周")
@Data
public class Weekinterm extends BaseEntity<Weekinterm> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 学年
	 */
	@ApiModelProperty(value = "学年", required = true)
	@TableField("year")
	private Integer year;
	/**
	 * 学期
	 */
	@ApiModelProperty(value = "学期", required = true)
	@TableField("term")
	private Integer term;
	/**
	 * 周数
	 */
	@ApiModelProperty(value = "周数", required = true)
	@TableField("weeks")
	private Integer weeks;
	 
	 
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
