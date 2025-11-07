package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtOrderChildProcess;
import com.brt.order.vo.BrtOrderChildProcessVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单子流程Mapper接口
 * 
 * @author Fgn
 * @date 2024-06-21
 */
public interface BrtOrderChildProcessMapper extends BaseMapper<BrtOrderChildProcess> {

    /**
     * @description: TODO 分页查询订单子流程列表
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: page
     * @param: brtOrderChildProcessVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtOrderChildProcessVo> queryBrtOrderChildProcessList(Page<?> page, @Param("brtOrderChildProcessVo") BrtOrderChildProcessVo brtOrderChildProcessVo);

    /**
     * @description: TODO 查询订单子流程列表
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @param: brtOrderChildProcessVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtOrderChildProcessVo> queryBrtOrderChildProcessList(@Param("brtOrderChildProcessVo") BrtOrderChildProcessVo brtOrderChildProcessVo);

    /**
     * @description: TODO 根据childId查询订单子流程
     * @author: Fgn
     * @date: 2024-06-21
     * @param:
     * @return:
     * @return Vo
     **/
    BrtOrderChildProcessVo queryBrtOrderChildProcessByChildId(@Param("ChildId") String childId);

}
