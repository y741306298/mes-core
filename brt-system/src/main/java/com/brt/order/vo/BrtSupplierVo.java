package com.brt.order.vo;

import com.brt.order.domain.BrtSupplier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtSupplierVo extends BrtSupplier {

    /**
     * 搜索关键字
     */
    private String keyWords;

    /**
     * 查询日期类型
     */
    private String dateType;

    /**
     * 查询日期范围
     */
    private List<String> createTimes;

    private String[] ids;
}
