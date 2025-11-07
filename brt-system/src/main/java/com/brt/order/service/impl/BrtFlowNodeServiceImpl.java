package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtFlowNode;
import com.brt.order.domain.BrtFlowNodeField;
import com.brt.order.service.IBrtFlowNodeFieldService;
import com.brt.order.vo.BrtFlowNodeFieldVo;
import com.brt.order.vo.BrtFlowNodeVo;
import com.brt.order.mapper.BrtFlowNodeMapper;
import com.brt.order.service.IBrtFlowNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 模板节点Service业务层处理
 *
 * @author Fgn
 * @date 2024-04-30
 */
@Service
public class BrtFlowNodeServiceImpl extends ServiceImpl<BrtFlowNodeMapper, BrtFlowNode> implements IBrtFlowNodeService {

    @Autowired
    private IBrtFlowNodeFieldService flowNodeFieldService;

    @Override
    public TableDataInfo<BrtFlowNodeVo> queryBrtFlowNodeList(BrtFlowNodeVo brtFlowNodeVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtFlowNodeList(PageUtils.buildPage(), brtFlowNodeVo));
    }

    @Override
    public List<BrtFlowNodeVo> queryBrtFlowNodeAll(BrtFlowNodeVo brtFlowNodeVo) {
        return this.baseMapper.queryBrtFlowNodeList(brtFlowNodeVo);
    }

    @Override
    public BrtFlowNodeVo queryBrtFlowNodeByNodeId(String nodeId) {
        return this.baseMapper.queryBrtFlowNodeByNodeId(nodeId);
    }

    @Transactional
    @Override
    public BrtFlowNodeVo insertBrtFlowNode(BrtFlowNodeVo brtFlowNodeVo) {
        saveBefore(brtFlowNodeVo);
        int i = this.baseMapper.insert(brtFlowNodeVo);
        return brtFlowNodeVo;
    }

    @Transactional
    @Override
    public BrtFlowNodeVo updateBrtFlowNode(BrtFlowNodeVo brtFlowNodeVo) {
        saveBefore(brtFlowNodeVo);
        int i = this.baseMapper.updateById(brtFlowNodeVo);
        return brtFlowNodeVo;
    }

    @Transactional
    @Override
    public int deleteBrtFlowNodeByNodeIds(String[] nodeIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(nodeIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param brtFlowNodeVo
     * @return:
     **/
    public void saveBefore(BrtFlowNodeVo brtFlowNodeVo){
        // 保存字段信息
//        flowNodeFieldService.saveOrUpdateBatch(BeanUtil.copyToList(brtFlowNodeVo.getFlowNodeFieldList(), BrtFlowNodeField.class));
    }

    public BrtFlowNodeVo getFlowNode(String templateId,String nodeName,String status){
        return this.baseMapper.queryFlownode(templateId,nodeName,status);
    }

}
