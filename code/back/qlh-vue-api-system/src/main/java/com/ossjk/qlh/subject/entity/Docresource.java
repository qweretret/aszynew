package com.ossjk.qlh.subject.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @ClassName: Docresource
 * @Description: 资料库信息-实体类
 * @author: wanghaohui
 * @date:  2022-03-09 15:54:53 
 */
@ApiModel(value = "资料库信息")
@Data
public class Docresource extends BaseEntity<Docresource> {

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
	 * 网址
	 */
	@ApiModelProperty(value = "网址")
	@TableField("weburl")
	private String weburl;
	/**
	 * 附件
	 */
	@ApiModelProperty(value = "type")
	@TableField("type")
	private Integer type;
	/**
	 * 序号
	 */
	@ApiModelProperty(value = "序号")
	@TableField("idex")
	private Integer idex;

	@ApiModelProperty(value = "创建时间")
	@TableField(value = "crtm", fill = FieldFill.INSERT)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
	protected Date crtm;
	 
	@Override
	protected Serializable pkVal() {
				return id;
	}
 

	 
}
