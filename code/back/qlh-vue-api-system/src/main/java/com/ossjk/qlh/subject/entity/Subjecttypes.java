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
 * @ClassName: Subjecttypes
 * @Description: 专业类别表,维护一个4级树，类别[高职] - [大类名称] - [二级类名称] - [专业名称]-实体类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@ApiModel(value = "专业类别表,维护一个4级树，类别[高职] - [大类名称] - [二级类名称] - [专业名称]")
@Data
public class Subjecttypes extends BaseEntity<Subjecttypes> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称", required = true)
	@TableField("name")
	private String name;
	/**
	 * 父id
	 */
	@ApiModelProperty(value = "父id")
	@TableField("pid")
	private String pid;
	/**
	 * 级
	 */
	@ApiModelProperty(value = "级", required = true)
	@TableField("level")
	private Integer level;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序", required = true)
	@TableField("idex")
	private Integer idex;
	 
	 
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
