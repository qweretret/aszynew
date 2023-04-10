package com.ossjk.qlh.trainplan.entity;

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
 * @ClassName: Trainingplan
 * @Description: 人才培养方案-实体类
 * @author: huang
 * @date:  2022-03-22 16:30:01 
 */
@ApiModel(value = "人才培养方案")
@Data
public class Trainingplan extends BaseEntity<Trainingplan> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 核心模型id
	 */
	@ApiModelProperty(value = "核心模型id")
	@TableField("cid")
	private String cid;
	/**
	 * 人才培养方案
	 */
	@ApiModelProperty(value = "人才培养方案")
	@TableField("rcplan")
	private String rcplan;
	/**
	 * 专家意见
	 */
	@ApiModelProperty(value = "专家意见")
	@TableField("adv")
	private String adv;
	 
	 
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
