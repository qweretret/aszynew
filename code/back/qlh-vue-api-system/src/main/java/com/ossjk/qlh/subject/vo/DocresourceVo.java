package com.ossjk.qlh.subject.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ossjk.core.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "DocresourceVo")
@Data
public class DocresourceVo extends BaseEntity<DocresourceVo> {

    @ApiModelProperty(value = "名称", required = true)
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "id")
    @TableField("id")
    private String value;

    @ApiModelProperty(value = "类型", required = true)
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "地址", required = true)
    @TableField("weburl")
    private String weburl;



}
