package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtPriceSheetMateriel;
import com.brt.order.vo.BrtPriceSheetMaterielVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数量记录Mapper接口
 *
 * @author Fgn
 * @date 2024-06-20
 */
public interface BrtPriceSheetMaterielMapper extends BaseMapper<BrtPriceSheetMateriel> {

    /**
     * @description: TODO 分页查询数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: page
     * @param: brtPriceSheetMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtPriceSheetMaterielVo> queryBrtPriceSheetMaterielList(Page<?> page, @Param("brtPriceSheetMaterielVo") BrtPriceSheetMaterielVo brtPriceSheetMaterielVo);

    /**
     * @description: TODO 查询数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtPriceSheetMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtPriceSheetMaterielVo> queryBrtPriceSheetMaterielList(@Param("brtPriceSheetMaterielVo") BrtPriceSheetMaterielVo brtPriceSheetMaterielVo);

    /**
     * @description: TODO 根据recordId查询数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return Vo
     **/
    BrtPriceSheetMaterielVo queryBrtPriceSheetMaterielByRecordId(@Param("RecordId") String recordId);

}
