package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderMaterielRecord;
import com.brt.order.vo.BrtOrderMaterielRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数量记录Mapper接口
 * 
 * @author Fgn
 * @date 2024-06-20
 */
public interface BrtOrderMaterielRecordMapper extends BaseMapper<BrtOrderMaterielRecord> {

    /**
     * @description: TODO 分页查询数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: page
     * @param: brtOrderMaterielRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderMaterielRecordVo> queryBrtOrderMaterielRecordList(Page<?> page, @Param("brtOrderMaterielRecordVo") BrtOrderMaterielRecordVo brtOrderMaterielRecordVo);

    /**
     * @description: TODO 查询数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtOrderMaterielRecordVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderMaterielRecordVo> queryBrtOrderMaterielRecordList(@Param("brtOrderMaterielRecordVo") BrtOrderMaterielRecordVo brtOrderMaterielRecordVo);

    /**
     * @description: TODO 根据recordId查询数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderMaterielRecordVo queryBrtOrderMaterielRecordByRecordId(@Param("RecordId") String recordId);

}
