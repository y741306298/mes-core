package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtMarketMateriel;
import com.brt.order.vo.BrtMarketMaterielVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数量记录Mapper接口
 *
 * @author Fgn
 * @date 2024-06-20
 */
public interface BrtMarketMaterielMapper extends BaseMapper<BrtMarketMateriel> {

    /**
     * @description: TODO 分页查询数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: page
     * @param: brtMarketMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtMarketMaterielVo> queryBrtMarketMaterielList(Page<?> page, @Param("brtMarketMaterielVo") BrtMarketMaterielVo brtMarketMaterielVo);

    /**
     * @description: TODO 查询数量记录列表
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @param: brtMarketMaterielVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtMarketMaterielVo> queryBrtMarketMaterielList(@Param("brtMarketMaterielVo") BrtMarketMaterielVo brtMarketMaterielVo);

    /**
     * @description: TODO 根据recordId查询数量记录
     * @author: Fgn
     * @date: 2024-06-20
     * @param:
     * @return:
     * @return Vo
     **/
    BrtMarketMaterielVo queryBrtMarketMaterielByRecordId(@Param("RecordId") String recordId);

}
