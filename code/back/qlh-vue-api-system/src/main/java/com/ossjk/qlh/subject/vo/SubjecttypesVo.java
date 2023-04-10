package com.ossjk.qlh.subject.vo;

import com.ossjk.qlh.subject.entity.Subjecttypes;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjecttypesVo extends Subjecttypes {
    private List<SubjecttypesVo> children = new ArrayList<>();
}
