package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtFlowNode;
import com.brt.order.vo.BrtFlowNodeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模板节点Mapper接口
 *
 * @author Fgn
 * @date 2024-04-30
 */
public interface BrtFlowNodeMapper extends BaseMapper<BrtFlowNode> {

    /**
     * @description: TODO 分页查询模板节点列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: page
     * @param: brtFlowNodeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtFlowNodeVo> queryBrtFlowNodeList(Page<?> page, @Param("brtFlowNodeVo") BrtFlowNodeVo brtFlowNodeVo);

    /**
     * @description: TODO 查询模板节点列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtFlowNodeVo> queryBrtFlowNodeList(@Param("brtFlowNodeVo") BrtFlowNodeVo brtFlowNodeVo);

    /**
     * @description: TODO 根据nodeId查询模板节点
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @return:
     * @return Vo
     **/
    BrtFlowNodeVo queryBrtFlowNodeByNodeId(@Param("NodeId") String nodeId);

    /**
     * @description: TODO 根据模板ID查询节点列表
     * @author: FanGN
     * @date: 09:50 2024/4/30
     * @param:
     * @param templateId
     * @return:
     * @return java.util.List<com.brt.order.vo.BrtFlowNodeVo>
     **/
    List<BrtFlowNodeVo> queryBrtFlowNodeListByTemplateId(@Param("templateId") String templateId);

    /**
     * 获取流程节点
     * @return
     */
    BrtFlowNodeVo queryFlownode(@Param("templateId")String templateId,@Param("nodeName")String nodeName,@Param("status")String status);

}
