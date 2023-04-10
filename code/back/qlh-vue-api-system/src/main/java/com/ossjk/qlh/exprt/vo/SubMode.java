package com.ossjk.qlh.exprt.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SubMode implements Serializable {

   private String txt;
   private List<SubMode> chld = new ArrayList<>();

   public void addChld(SubMode children){
      chld.add(children);
   }

}
