package com.brt.order.vo;

import com.brt.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BrtMaterielRecordVo extends BaseEntity {
    private String type;
    private String orderNo;
    private BigDecimal num;
    private BigDecimal residueNum;
    private String userName;
    private String userId;
    private String materielId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date timeStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date timeEnd;

}
