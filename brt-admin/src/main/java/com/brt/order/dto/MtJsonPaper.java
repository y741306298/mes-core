package com.brt.order.dto;

import java.util.List;
import lombok.Data;

@Data
public class MtJsonPaper {

    private Integer width;

    private Integer height;

    private List<Integer> margin;
}
