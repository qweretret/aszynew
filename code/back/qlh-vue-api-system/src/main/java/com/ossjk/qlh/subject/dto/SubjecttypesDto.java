package com.ossjk.qlh.subject.dto;

import com.ossjk.qlh.subject.entity.Subjecttypes;
import lombok.Data;

import java.util.List;

@Data
public class SubjecttypesDto extends Subjecttypes{
    private List<Subjecttypes> father;
}
