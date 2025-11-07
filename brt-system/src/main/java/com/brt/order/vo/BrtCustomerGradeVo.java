package com.brt.order.vo;

import com.brt.order.domain.BrtCustomerGrade;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 客户等级Vo对象 brt_customer_grade
 *
 * @author Fgn
 * @date 2024-04-27
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BrtCustomerGradeVo extends BrtCustomerGrade {

}
