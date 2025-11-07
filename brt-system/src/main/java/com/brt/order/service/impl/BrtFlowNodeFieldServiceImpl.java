package com.brt.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.enums.NodeTypeEnums;
import com.brt.common.utils.PageUtils;
import com.brt.common.utils.StringUtils;
import com.brt.order.domain.BrtFlowNode;
import com.brt.order.domain.BrtFlowNodeField;
import com.brt.order.mapper.BrtFlowNodeMapper;
import com.brt.order.vo.BrtFlowNodeFieldVo;
import com.brt.order.mapper.BrtFlowNodeFieldMapper;
import com.brt.order.service.IBrtFlowNodeFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 节点字段Service业务层处理
 * 
 * @author Fgn
 * @date 2024-04-30
 */
@Service
public class BrtFlowNodeFieldServiceImpl extends ServiceImpl<BrtFlowNodeFieldMapper, BrtFlowNodeField> implements IBrtFlowNodeFieldService {

    @Autowired
    private BrtFlowNodeMapper flowNodeMapper;

    @Override
    public TableDataInfo<BrtFlowNodeFieldVo> queryBrtFlowNodeFieldList(BrtFlowNodeFieldVo brtFlowNodeFieldVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtFlowNodeFieldList(PageUtils.buildPage(), brtFlowNodeFieldVo));
    }

    @Override
    public List<BrtFlowNodeFieldVo> queryBrtFlowNodeFieldAll(BrtFlowNodeFieldVo brtFlowNodeFieldVo) {
        // 查询节点的字段列表
        List<BrtFlowNodeFieldVo> nodeFieldList = this.baseMapper.queryBrtFlowNodeFieldList(brtFlowNodeFieldVo);

        // 判断如果节点的字段列表为空 则查询同一模板下相同节点类型的字段
        if(ObjectUtil.isEmpty(nodeFieldList) && StringUtils.isNotBlank(brtFlowNodeFieldVo.getNodeId())){
            // 查询节点信息
            BrtFlowNode flowNode = flowNodeMapper.selectById(brtFlowNodeFieldVo.getNodeId());

            // 审批节点和自定义记录节点不需要同步字段信息
            if (flowNode.getNodeType().equals(NodeTypeEnums.审批.getCode()) || flowNode.getNodeType().equals(NodeTypeEnums.自定义纪录任务.getCode())){
                return nodeFieldList;
            }

            // 查询上一个同类型的节点
            BrtFlowNode brtFlowNode = flowNodeMapper.selectOne(new LambdaQueryWrapper<BrtFlowNode>().eq(BrtFlowNode::getTemplateId, flowNode.getTemplateId()).eq(BrtFlowNode::getNodeType, flowNode.getNodeType()).lt(BrtFlowNode::getSort, flowNode.getSort()).orderByDesc(BrtFlowNode::getSort).last(" limit 1"));
            if (ObjectUtil.isNotEmpty(brtFlowNode)){
                // 查询上一个节点的字段列表
                brtFlowNodeFieldVo.setNodeId(brtFlowNode.getNodeId());
                nodeFieldList = this.baseMapper.queryBrtFlowNodeFieldList(brtFlowNodeFieldVo);
                nodeFieldList.forEach(item -> {
                    item.setNodeId(flowNode.getNodeId());
                    item.setFieldId(null);
                });
            }

        }
        return nodeFieldList;
    }

    @Override
    public BrtFlowNodeFieldVo queryBrtFlowNodeFieldByFieldId(String fieldId) {
        return this.baseMapper.queryBrtFlowNodeFieldByFieldId(fieldId);
    }

    @Transactional
    @Override
    public BrtFlowNodeFieldVo insertBrtFlowNodeField(BrtFlowNodeFieldVo brtFlowNodeFieldVo) {
        saveBefore(brtFlowNodeFieldVo);
        int i = this.baseMapper.insert(brtFlowNodeFieldVo);
        return brtFlowNodeFieldVo;
    }

    @Transactional
    @Override
    public BrtFlowNodeFieldVo updateBrtFlowNodeField(BrtFlowNodeFieldVo brtFlowNodeFieldVo) {
        saveBefore(brtFlowNodeFieldVo);
        int i = this.baseMapper.updateById(brtFlowNodeFieldVo);
        return brtFlowNodeFieldVo;
    }

    @Transactional
    @Override
    public int deleteBrtFlowNodeFieldByFieldIds(String[] fieldIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(fieldIds));
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param brtFlowNodeFieldVo
     * @return:
     **/
    public void saveBefore(BrtFlowNodeFieldVo brtFlowNodeFieldVo){

    }

}
