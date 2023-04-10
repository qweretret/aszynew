package com.ossjk.qlh.coreability.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.core.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Copyright  2022-03-22 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.entity
 * @ClassName: Coremodel
 * @Description: 专业核心能力模型-实体类
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */
@ApiModel(value = "专业核心能力模型")
@Data
public class Coremodel extends BaseEntity<Coremodel> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 年级
	 */
	@ApiModelProperty(value = "年级")
	@TableField("grade")
	private String grade;
	/**
	 * 状态[0.编写，1,审批通过，2，未通过]
	 */
	@ApiModelProperty(value = "状态[0.编写，1,审批通过，2，未通过]")
	@TableField("state")
	private Integer state;
	/**
	 * 专业id
	 */
	@ApiModelProperty(value = "专业id")
	@TableField("sid")
	private String sid;
	/**
	 * 核心能力模型
	 */
	@ApiModelProperty(value = "核心能力模型")
	@TableField("coremodel")
	private String coremodel;
	/**
	 * 支撑能力模型
	 */
	@ApiModelProperty(value = "支撑能力模型")
	@TableField("zcmodel")
	private String zcmodel;
	/**
	 * 其他能力模型
	 */
	@ApiModelProperty(value = "其他能力模型")
	@TableField("otrmodel")
	private String otrmodel;
	 
	 
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
