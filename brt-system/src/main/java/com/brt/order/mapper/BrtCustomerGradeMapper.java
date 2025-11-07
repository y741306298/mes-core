package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtCustomerGrade;
import com.brt.order.vo.BrtCustomerGradeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户等级Mapper接口
 * 
 * @author Fgn
 * @date 2024-04-27
 */
public interface BrtCustomerGradeMapper extends BaseMapper<BrtCustomerGrade> {

    /**
     * @description: TODO 分页查询客户等级列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: page
     * @param: brtCustomerGradeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtCustomerGradeVo> queryBrtCustomerGradeList(Page<?> page, @Param("brtCustomerGradeVo") BrtCustomerGradeVo brtCustomerGradeVo);

    /**
     * @description: TODO 查询客户等级列表
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @param: brtCustomerGradeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtCustomerGradeVo> queryBrtCustomerGradeList(@Param("brtCustomerGradeVo") BrtCustomerGradeVo brtCustomerGradeVo);

    /**
     * @description: TODO 根据gradeId查询客户等级
     * @author: Fgn
     * @date: 2024-04-27
     * @param:
     * @return:
     * @return Vo
     **/
    BrtCustomerGradeVo queryBrtCustomerGradeByGradeId(@Param("GradeId") String gradeId);

}
