package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtPriceSheetOrderRecord;
import com.brt.order.vo.BrtPriceSheetOrderRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报价单_报价记录Mapper接口
 * 
 * @author Fgn
 * @date 2024-07-13
 */
public interface BrtPriceSheetOrderRecordMapper extends BaseMapper<BrtPriceSheetOrderRecord> {

    /**
     * @description: TODO 分页查询报价单_报价记录列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: page
     * @param: brtPriceSheetOrderRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtPriceSheetOrderRecordVo> queryBrtPriceSheetOrderRecordList(Page<?> page, @Param("brtPriceSheetOrderRecordVo") BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo);

    /**
     * @description: TODO 查询报价单_报价记录列表
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @param: brtPriceSheetOrderRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtPriceSheetOrderRecordVo> queryBrtPriceSheetOrderRecordList(@Param("brtPriceSheetOrderRecordVo") BrtPriceSheetOrderRecordVo brtPriceSheetOrderRecordVo);

    /**
     * @description: TODO 根据recordId查询报价单_报价记录
     * @author: Fgn
     * @date: 2024-07-13
     * @param:
     * @return:
     * @return Vo
     **/
    BrtPriceSheetOrderRecordVo queryBrtPriceSheetOrderRecordByRecordId(@Param("RecordId") String recordId);

}
