package com.ossjk.qlh.subject.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "学年-学期")
public class WeekintermDto {

    @ApiModelProperty(value = "学年", required = true)
    private Integer year;

    @ApiModelProperty(value = "第一学期", required = true)
    private Integer one;

    @ApiModelProperty(value = "第二学期", required = true)
    private Integer two;

    @ApiModelProperty(value = "暑假", required = true)
    private Integer three;
}
