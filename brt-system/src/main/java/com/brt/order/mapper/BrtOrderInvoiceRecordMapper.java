package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderInvoiceRecord;
import com.brt.order.vo.BrtOrderInvoiceRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 开票记录Mapper接口
 * 
 * @author Fgn
 * @date 2024-06-19
 */
public interface BrtOrderInvoiceRecordMapper extends BaseMapper<BrtOrderInvoiceRecord> {

    /**
     * @description: TODO 分页查询开票记录列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: page
     * @param: brtOrderInvoiceRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderInvoiceRecordVo> queryBrtOrderInvoiceRecordList(Page<?> page, @Param("brtOrderInvoiceRecordVo") BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo);

    /**
     * @description: TODO 查询开票记录列表
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @param: brtOrderInvoiceRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderInvoiceRecordVo> queryBrtOrderInvoiceRecordList(@Param("brtOrderInvoiceRecordVo") BrtOrderInvoiceRecordVo brtOrderInvoiceRecordVo);

    /**
     * @description: TODO 根据recordId查询开票记录
     * @author: Fgn
     * @date: 2024-06-19
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderInvoiceRecordVo queryBrtOrderInvoiceRecordByRecordId(@Param("RecordId") String recordId);

}
