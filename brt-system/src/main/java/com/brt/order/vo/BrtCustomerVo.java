package com.brt.order.vo;

import com.brt.common.annotation.Excel;
import com.brt.common.annotation.Excels;
import com.brt.common.core.domain.entity.SysUser;
import com.brt.order.domain.BrtCustomer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 客户信息Vo对象 brt_customer
 *
 * @author Fgn
 * @date 2024-04-27
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtCustomerVo extends BrtCustomer {

//    // 客户类型
//    @Excels({
//            @Excel(name = "类型名称",targetAttr = "typeName",type = Excel.Type.EXPORT)
//    })
//    private BrtCustomerTypeVo typeVo;

//    // 类型名称
//    @Excel(name = "类型名称",type = Excel.Type.IMPORT)
//    private String typeName;

//    // 客户等级
//    @Excels({
//            @Excel(name = "等级名称",targetAttr = "gradeName",type = Excel.Type.EXPORT)
//    })
//    private BrtCustomerGradeVo gradeVo;

//    // 等级名称
//    @Excel(name = "等级名称",type = Excel.Type.IMPORT)
//    private String gradeName;

    // 业务员
    @Excels({
            @Excel(name = "业务员",targetAttr = "userName",type = Excel.Type.EXPORT)
    })
    private SysUser user;

    // 业务员名称
    @Excel(name = "业务员",type = Excel.Type.IMPORT)
    private String userName;

    private String createTimeStart;
    private String createTimeEnd;

    private String[] ids;

}
