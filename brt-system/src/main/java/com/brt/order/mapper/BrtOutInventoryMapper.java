package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOutInventory;
import com.brt.order.vo.BrtOutInventoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库管理Mapper接口
 *
 * @author Fgn
 * @date 2024-07-11
 */
public interface BrtOutInventoryMapper extends BaseMapper<BrtOutInventory> {

    /**
     * @description: TODO 分页查询入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: page
     * @param: brtOutInventoryVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOutInventoryVo> queryBrtOutInventoryList(Page<?> page, @Param("brtOutInventoryVo") BrtOutInventoryVo brtOutInventoryVo);

    /**
     * @description: TODO 查询入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOutInventoryVo> queryBrtOutInventoryList(@Param("brtOutInventoryVo") BrtOutInventoryVo brtOutInventoryVo);

    /**
     * @description: TODO 根据outInventoryId查询入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOutInventoryVo queryBrtOutInventoryByOutInventoryId(@Param("OutInventoryId") String outInventoryId);

}
