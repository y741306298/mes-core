package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtCheck;
import com.brt.order.vo.BrtCheckAchievementVo;
import com.brt.order.vo.BrtCheckVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 账单Mapper接口
 *
 * @author Fgn
 * @date 2024-05-15
 */
public interface BrtCheckMapper extends BaseMapper<BrtCheck> {

    /**
     * @description: TODO 分页查询账单列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: page
     * @param: brtCheckVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtCheckVo> queryBrtCheckList(Page<?> page, @Param("brtCheckVo") BrtCheckVo brtCheckVo);

    /**
     * @description: TODO 查询账单列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtCheckVo> queryBrtCheckList(@Param("brtCheckVo") BrtCheckVo brtCheckVo);

    /**
     * @description: TODO 根据checkId查询账单
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @return:
     * @return Vo
     **/
    BrtCheckVo queryBrtCheckByCheckId(@Param("CheckId") String checkId);

    /**
     * @description: TODO 业绩统计
     * @author: FanGN
     * @date: 14:41 2024/5/20
     * @param:
     * @param year
     * @param orderType
     * @return:
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> achievement(@Param("year")String year,@Param("orderType") String orderType,@Param("month")String month);

    /**
     * 销售业绩导出
     * @param year
     * @param orderType
     * @param month
     * @return
     */
    List<BrtCheckAchievementVo> exportAchievement(@Param("year")String year, @Param("orderType") String orderType, @Param("month")String month);


    /**
     * @description: TODO 账户业绩统计
     * @author: FanGN
     * @date: 15:34 2024/5/20
     * @param:
     * @param year
     * @param orderType
     * @param month
     * @return:
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> accountAchievement(@Param("year")String year,@Param("orderType") String orderType,@Param("month") String month);
}
