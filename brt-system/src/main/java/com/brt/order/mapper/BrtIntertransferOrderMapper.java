package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtIntertransferOrder;
import com.brt.order.vo.BrtIntertransferOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 互转单Mapper接口
 * 
 * @author Fgn
 * @date 2024-05-16
 */
public interface BrtIntertransferOrderMapper extends BaseMapper<BrtIntertransferOrder> {

    /**
     * @description: TODO 分页查询互转单列表
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: page
     * @param: brtIntertransferOrderVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtIntertransferOrderVo> queryBrtIntertransferOrderList(Page<?> page, @Param("brtIntertransferOrderVo") BrtIntertransferOrderVo brtIntertransferOrderVo);

    /**
     * @description: TODO 查询互转单列表
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @param: brtIntertransferOrderVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtIntertransferOrderVo> queryBrtIntertransferOrderList(@Param("brtIntertransferOrderVo") BrtIntertransferOrderVo brtIntertransferOrderVo);

    /**
     * @description: TODO 根据intertransferId查询互转单
     * @author: Fgn
     * @date: 2024-05-16
     * @param:
     * @return:
     * @return Vo
     **/
    BrtIntertransferOrderVo queryBrtIntertransferOrderByIntertransferId(@Param("IntertransferId") String intertransferId);

}
