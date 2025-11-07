package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtPriceSheetOrderDetailsRecord;
import com.brt.order.vo.BrtPriceSheetOrderDetailsRecordVo;
import com.brt.order.vo.BrtPriceSheetOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报价记录详情Mapper接口
 *
 * @author Fgn
 * @date 2024-07-13
 */
public interface BrtPriceSheetOrderDetailsRecordMapper extends BaseMapper<BrtPriceSheetOrderDetailsRecord> {

    /**
     * @description: TODO 分页查询报价记录详情列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: page
     * @param: brtPriceSheetOrderDetailsRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtPriceSheetOrderDetailsRecordVo> queryBrtPriceSheetOrderDetailsRecordList(Page<?> page, @Param("brtPriceSheetOrderDetailsRecordVo") BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo);

    /**
     * @description: TODO 查询报价记录详情列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderDetailsRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtPriceSheetOrderDetailsRecordVo> queryBrtPriceSheetOrderDetailsRecordList(@Param("brtPriceSheetOrderDetailsRecordVo") BrtPriceSheetOrderDetailsRecordVo brtPriceSheetOrderDetailsRecordVo);

    /**
     * @description: TODO 根据detailsId查询报价记录详情
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @return:
     * @return Vo
     **/
    BrtPriceSheetOrderDetailsRecordVo queryBrtPriceSheetOrderDetailsRecordByDetailsId(@Param("DetailsId") String detailsId);

}
