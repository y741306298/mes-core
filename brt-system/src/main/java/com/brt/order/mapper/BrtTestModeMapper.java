package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtTestMode;
import com.brt.order.vo.BrtTestModeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试方式管理Mapper接口
 * 
 * @author Fgn
 * @date 2024-05-09
 */
public interface BrtTestModeMapper extends BaseMapper<BrtTestMode> {

    /**
     * @description: TODO 分页查询测试方式管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: page
     * @param: brtTestModeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtTestModeVo> queryBrtTestModeList(Page<?> page, @Param("brtTestModeVo") BrtTestModeVo brtTestModeVo);

    /**
     * @description: TODO 查询测试方式管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtTestModeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtTestModeVo> queryBrtTestModeList(@Param("brtTestModeVo") BrtTestModeVo brtTestModeVo);

    /**
     * @description: TODO 根据modeId查询测试方式管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @return:
     * @return Vo
     **/
    BrtTestModeVo queryBrtTestModeByModeId(@Param("ModeId") String modeId);

}
