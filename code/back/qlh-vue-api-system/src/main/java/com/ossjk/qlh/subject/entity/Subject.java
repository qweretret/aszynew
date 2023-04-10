package com.ossjk.qlh.subject.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ossjk.core.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright  2022-03-09 QLH. Tech Ltd. All rights reserved.
 * 
 * @Package: com.ossjk.qlh.subject.entity
 * @ClassName: Subject
 * @Description: 专业表-实体类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:52 
 */
@ApiModel(value = "专业表")
@Data
public class Subject extends BaseEntity<Subject> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键", required = true)
	@TableId("id")
	private String id;
	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名", required = true)
	@TableField("name")
	private String name;


	@ApiModelProperty(value = "部门编码", required = false)
	@TableField("dcode")
	private String dcode;

	/**
	 * 代码
	 */
	@ApiModelProperty(value = "代码", required = true)
	@TableField("code")
	private String code;
	/**
	 * 类别
	 */
	@ApiModelProperty(value = "类别", required = true)
	@TableField("type")
	private String type;
	/**
	 * 大类名称
	 */
	@ApiModelProperty(value = "大类名称")
	@TableField("subtype1")
	private String subtype1;
	/**
	 * 二级类名称
	 */
	@ApiModelProperty(value = "二级类名称")
	@TableField("subtype2")
	private String subtype2;
	/**
	 * 设立时间
	 */
	@ApiModelProperty(value = "设立时间")
	@TableField("crdate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
	private Date crdate;
	/**
	 * 学制
	 */
	@ApiModelProperty(value = "学制")
	@TableField("year")
	private Integer year;
	/**
	 * 生源类型
	 */
	@ApiModelProperty(value = "生源类型")
	@TableField("stutype")
	private String stutype;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	@TableField("ramark")
	private String ramark;
	 
	 
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
