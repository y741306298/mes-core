package com.brt.order.dto;

import java.util.List;
import lombok.Data;

@Data
public class MtJsonPageImage {

    private String name;

    private Integer rotate;

    private List<Integer> rect;

    private String extendInfo;
}
