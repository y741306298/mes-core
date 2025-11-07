package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtInInventoryMateriel;
import com.brt.order.vo.BrtInInventoryMaterielVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库单详情 入库单关联物料Mapper接口
 *
 * @author Fgn
 * @date 2024-07-11
 */
public interface BrtInInventoryMaterielMapper extends BaseMapper<BrtInInventoryMateriel> {

    /**
     * @description: TODO 分页查询入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: page
     * @param: brtInInventoryMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtInInventoryMaterielVo> queryBrtInInventoryMaterielList(Page<?> page, @Param("brtInInventoryMaterielVo") BrtInInventoryMaterielVo brtInInventoryMaterielVo);

    /**
     * @description: TODO 查询入库单详情 入库单关联物料列表
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @param: brtInInventoryMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtInInventoryMaterielVo> queryBrtInInventoryMaterielList(@Param("brtInInventoryMaterielVo") BrtInInventoryMaterielVo brtInInventoryMaterielVo);

    /**
     * @description: TODO 根据inInventoryMaterielId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    BrtInInventoryMaterielVo queryBrtInInventoryMaterielByInInventoryMaterielId(@Param("InInventoryMaterielId") String inInventoryMaterielId);

    /**
     * @description: TODO 根据inInventoryId查询入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    List<BrtInInventoryMaterielVo> queryByInInventoryId(@Param("inInventoryId") String inInventoryId);


    /**
     * @description: TODO 根据inInventoryId删除入库单详情 入库单关联物料
     * @author: Fgn
     * @date: 2024-07-11
     * @param:
     * @return:
     * @return Vo
     **/
    void deleteByInInventoryId(@Param("inventoryIds") String [] inInventoryIds);

}
