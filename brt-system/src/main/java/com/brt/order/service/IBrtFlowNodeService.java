package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtFlowNode;
import com.brt.order.vo.BrtFlowNodeVo;

import java.util.List;

/**
 * 模板节点Service接口
 *
 * @author Fgn
 * @date 2024-04-30
 */
public interface IBrtFlowNodeService extends IService<BrtFlowNode> {

    /**
     * @description: TODO 分页查询模板节点列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeVo 模板节点
     * @return:
     * @return TableDataInfo<BrtFlowNodeVo>
     **/
    public TableDataInfo<BrtFlowNodeVo> queryBrtFlowNodeList(BrtFlowNodeVo brtFlowNodeVo);

    /**
     * @description: TODO 查询全部模板节点列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeVo 模板节点
     * @return:
     * @return java.util.List<BrtFlowNodeVo>
     **/
    public List<BrtFlowNodeVo> queryBrtFlowNodeAll(BrtFlowNodeVo brtFlowNodeVo);

    /**
     * @description: TODO 根据nodeId查询模板节点
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: nodeId
     * @return:
     * @return BrtFlowNodeVo
     **/
    public BrtFlowNodeVo queryBrtFlowNodeByNodeId(String nodeId);

    /**
     * @description: TODO 新增模板节点
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeVo 模板节点
     * @return:
     * @return int
     **/
    public BrtFlowNodeVo insertBrtFlowNode(BrtFlowNodeVo brtFlowNodeVo);

    /**
     * @description: TODO 修改模板节点
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeVo 模板节点
     * @return:
     * @return int
     **/
    public BrtFlowNodeVo updateBrtFlowNode(BrtFlowNodeVo brtFlowNodeVo);

    /**
     * @description: TODO 批量删除模板节点
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param:  nodeIds 需要删除的模板节点主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtFlowNodeByNodeIds(String[] nodeIds);

    BrtFlowNodeVo getFlowNode(String templateId,String nodeName,String status);

}
