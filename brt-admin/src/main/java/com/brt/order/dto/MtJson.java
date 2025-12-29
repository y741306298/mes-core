package com.brt.order.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class MtJson {

    private MtJsonDtp dtp;

    private MtJsonPaper paper;

    private Map<String, Object> imageName;

    private List<MtJsonPage> pages;
}
