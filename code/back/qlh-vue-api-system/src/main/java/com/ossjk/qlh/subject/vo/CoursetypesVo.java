package com.ossjk.qlh.subject.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ossjk.qlh.subject.entity.Coursetypes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "课程Vo")
@Data
public class CoursetypesVo extends Coursetypes {

    @ApiModelProperty(value = "子课程类型", required = true)
    @TableField("children")
    private List<Coursetypes> children;


}
