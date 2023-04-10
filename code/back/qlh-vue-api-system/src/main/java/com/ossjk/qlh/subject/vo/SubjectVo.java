package com.ossjk.qlh.subject.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ossjk.qlh.subject.entity.Subject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "专业Vo")
@Data
public class SubjectVo extends Subject {

    @ApiModelProperty(value = " 部门code", required = false)
    @TableField("dcode")
    private String dcode;

}
