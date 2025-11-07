package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderCollectionRecord;
import com.brt.order.vo.BrtOrderCollectionRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收款记录Mapper接口
 * 
 * @author Fgn
 * @date 2024-06-20
 */
public interface BrtOrderCollectionRecordMapper extends BaseMapper<BrtOrderCollectionRecord> {

    /**
     * @description: TODO 分页查询收款记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: page
     * @param: brtOrderCollectionRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderCollectionRecordVo> queryBrtOrderCollectionRecordList(Page<?> page, @Param("brtOrderCollectionRecordVo") BrtOrderCollectionRecordVo brtOrderCollectionRecordVo);

    /**
     * @description: TODO 查询收款记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderCollectionRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderCollectionRecordVo> queryBrtOrderCollectionRecordList(@Param("brtOrderCollectionRecordVo") BrtOrderCollectionRecordVo brtOrderCollectionRecordVo);

    /**
     * @description: TODO 根据recordId查询收款记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderCollectionRecordVo queryBrtOrderCollectionRecordByRecordId(@Param("RecordId") String recordId);

}
