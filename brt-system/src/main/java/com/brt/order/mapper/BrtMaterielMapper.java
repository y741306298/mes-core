package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtMateriel;
import com.brt.order.vo.BrtInInventoryMaterielVo;
import com.brt.order.vo.BrtMaterielRecordVo;
import com.brt.order.vo.BrtMaterielVo;
import com.brt.order.vo.BrtOutInventoryMaterielVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料信息Mapper接口
 *
 * @author Fgn
 * @date 2024-05-07
 */
public interface BrtMaterielMapper extends BaseMapper<BrtMateriel> {

    /**
     * @description: TODO 分页查询物料信息列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: page
     * @param: brtMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtMaterielVo> queryBrtMaterielList(Page<?> page, @Param("brtMaterielVo") BrtMaterielVo brtMaterielVo);

    /**
     * @description: TODO 查询物料信息列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtMaterielVo> queryBrtMaterielList(@Param("brtMaterielVo") BrtMaterielVo brtMaterielVo);

    /**
     * @description: TODO 根据materielId查询物料信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @return:
     * @return Vo
     **/
    BrtMaterielVo queryBrtMaterielByMaterielId(@Param("MaterielId") String materielId);

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    BrtMaterielVo queryByName(@Param("name")String name);

    /**
     * 修改开库存数量
     * @param inInventoryMaterielVo
     */
    void addNum(BrtInInventoryMaterielVo inInventoryMaterielVo);

    void reduceNum(BrtOutInventoryMaterielVo outInventoryMaterielVo);

    /**
     * 查询出入库记录
     * @param materielRecordVo
     * @return
     */
    Page<BrtMaterielRecordVo> selectRecord(Page<?> page, @Param("materielRecordVo")BrtMaterielRecordVo materielRecordVo);

}
