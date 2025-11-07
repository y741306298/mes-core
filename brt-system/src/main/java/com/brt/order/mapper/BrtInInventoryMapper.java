package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtInInventory;
import com.brt.order.vo.BrtInInventoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库管理Mapper接口
 * 
 * @author Fgn
 * @date 2024-07-11
 */
public interface BrtInInventoryMapper extends BaseMapper<BrtInInventory> {

    /**
     * @description: TODO 分页查询入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: page
     * @param: brtInInventoryVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtInInventoryVo> queryBrtInInventoryList(Page<?> page, @Param("brtInInventoryVo") BrtInInventoryVo brtInInventoryVo);

    /**
     * @description: TODO 查询入库管理列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtInInventoryVo> queryBrtInInventoryList(@Param("brtInInventoryVo") BrtInInventoryVo brtInInventoryVo);

    /**
     * @description: TODO 根据inInventoryId查询入库管理
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    BrtInInventoryVo queryBrtInInventoryByInInventoryId(@Param("InInventoryId") String inInventoryId);

}
