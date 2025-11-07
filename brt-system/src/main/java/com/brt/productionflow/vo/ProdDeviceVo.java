package com.brt.productionflow.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.brt.productionflow.domain.ProdDevice;
import com.brt.productionflow.domain.ProdDeviceHistory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产设备视图对象
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProdDeviceVo extends ProdDevice {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<ProdDeviceHistory> history = new ArrayList<>();
}
