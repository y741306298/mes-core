package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOutInventoryMateriel;
import com.brt.order.vo.BrtOutInventoryMaterielVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库单详情 入库单关联物料Mapper接口
 *
 * @author Fgn
 * @date 2024-07-11
 */
public interface BrtOutInventoryMaterielMapper extends BaseMapper<BrtOutInventoryMateriel> {

    /**
     * @description: TODO 分页查询入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: page
     * @param: brtOutInventoryMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOutInventoryMaterielVo> queryBrtOutInventoryMaterielList(Page<?> page, @Param("brtOutInventoryMaterielVo") BrtOutInventoryMaterielVo brtOutInventoryMaterielVo);

    /**
     * @description: TODO 查询入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtOutInventoryMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOutInventoryMaterielVo> queryBrtOutInventoryMaterielList(@Param("brtOutInventoryMaterielVo") BrtOutInventoryMaterielVo brtOutInventoryMaterielVo);

    /**
     * @description: TODO 根据outInventoryMaterielId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOutInventoryMaterielVo queryBrtOutInventoryMaterielByOutInventoryMaterielId(@Param("OutInventoryMaterielId") String outInventoryMaterielId);

    /**
     * @description: TODO 根据outInventoryId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    List<BrtOutInventoryMaterielVo> queryByOutInventoryId(@Param("outInventoryId") String outInventoryId);


    /**
     * @description: TODO 根据outInventoryId删除入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    void deleteByOutInventoryId(@Param("inventoryIds") String [] inventoryIds);

}
