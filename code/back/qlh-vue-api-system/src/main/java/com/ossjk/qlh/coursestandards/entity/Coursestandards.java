package com.ossjk.qlh.coursestandards.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.core.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Copyright  2022-03-31 QLH. Tech Ltd. All rights reserved.
 *
 * @Package: com.ossjk.qlh.xueli.entity
 * @ClassName: Coursestandards
 * @Description: 课程标准编制-实体类
 * @author: Rick.yang
 * @date:  2022-03-31 11:14:56
 */
@ApiModel(value = "课程标准编制")
@Data
public class Coursestandards extends BaseEntity<Coursestandards> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 核心能力id
	 */
	@ApiModelProperty(value = "核心能力id")
	@TableField("cid")
	private String cid;
	/**
	 * 专业核心能力
	 */
	@ApiModelProperty(value = "专业能力")
	@TableField("courest")
	private String courest;

	/**
	 * 评价标准及题库
	 */
	@ApiModelProperty(value = "附件")
	@TableField("pjbztk")
	private String pjbztk;
	/**
	 * 专家意见
	 */
	@ApiModelProperty(value = "专家意见")
	@TableField("adv")
	private String adv;
	/**
	 * 所属专业id
	 */
	@ApiModelProperty(value = "所属专业id")
	@TableField("subjectid")
	private String subjectid;
	/**
	 * 课程名称
	 */
	@ApiModelProperty(value = "课程名称")
	@TableField("coursename")
	private String coursename;


	/**
	 * 课程能力类型
	 */
	@ApiModelProperty(value = "课程能力类型")
	@TableField("abtype")
	private Integer abtype;
	 
	 
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
