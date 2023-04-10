package com.ossjk.qlh.subject.dto;

import com.ossjk.qlh.subject.vo.DocresourceVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileDto {

    @ApiModelProperty(value = "类型,1是外链的 或 选择了现有的附件、2 传新的附件、3:课程标准附件", required = true)
    private Integer type;

    @ApiModelProperty(value = "专业ID", required = true)
    private String sid;

    private DocresourceVo vo;

}
