package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderExamine;
import com.brt.order.vo.BrtOrderExamineVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单审批Mapper接口
 *
 * @author Fgn
 * @date 2024-05-10
 */
public interface BrtOrderExamineMapper extends BaseMapper<BrtOrderExamine> {

    /**
     * @description: TODO 分页查询订单审批列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: page
     * @param: brtOrderExamineVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderExamineVo> queryBrtOrderExamineList(Page<?> page, @Param("brtOrderExamineVo") BrtOrderExamineVo brtOrderExamineVo);

    /**
     * @description: TODO 查询订单审批列表
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @param: brtOrderExamineVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderExamineVo> queryBrtOrderExamineList(@Param("brtOrderExamineVo") BrtOrderExamineVo brtOrderExamineVo);

    /**
     * @description: TODO 根据examineId查询订单审批
     * @author: Fgn
     * @date: 2024-05-10
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderExamineVo queryBrtOrderExamineByExamineId(@Param("ExamineId") String examineId);

    /**
     * @description: TODO 获取全部订单列表
     * @author: FanGN
     * @date: 00:53 2024/5/20
     * @param:
     * @return:
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    List<Map<String, String>> orderAllList();

    List<BrtOrderExamineVo> auditQuery(@Param("orderId") String orderId,@Param("childId") String childId);

}
