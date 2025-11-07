package com.brt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.brt.common.core.page.TableDataInfo;
import com.brt.common.utils.PageUtils;
import com.brt.order.domain.BrtFlowNode;
import com.brt.order.domain.BrtFlowTemplate;
import com.brt.order.service.IBrtFlowNodeService;
import com.brt.order.vo.BrtFlowNodeVo;
import com.brt.order.vo.BrtFlowTemplateVo;
import com.brt.order.mapper.BrtFlowTemplateMapper;
import com.brt.order.service.IBrtFlowTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;

/**
 * 流程模板Service业务层处理
 * 
 * @author Fgn
 * @date 2024-04-30
 */
@Service
public class BrtFlowTemplateServiceImpl extends ServiceImpl<BrtFlowTemplateMapper, BrtFlowTemplate> implements IBrtFlowTemplateService {

    @Autowired
    private IBrtFlowNodeService flowNodeService;

    @Override
    public TableDataInfo<BrtFlowTemplateVo> queryBrtFlowTemplateList(BrtFlowTemplateVo brtFlowTemplateVo) {
        return PageUtils.buildDataInfo(this.baseMapper.queryBrtFlowTemplateList(PageUtils.buildPage(), brtFlowTemplateVo));
    }

    @Override
    public List<BrtFlowTemplateVo> queryBrtFlowTemplateAll(BrtFlowTemplateVo brtFlowTemplateVo) {
        return this.baseMapper.queryBrtFlowTemplateList(brtFlowTemplateVo);
    }

    @Override
    public BrtFlowTemplateVo queryBrtFlowTemplateByTemplateId(String templateId) {
        return this.baseMapper.queryBrtFlowTemplateByTemplateId(templateId);
    }

    @Transactional
    @Override
    public BrtFlowTemplateVo insertBrtFlowTemplate(BrtFlowTemplateVo brtFlowTemplateVo) {
        saveBefore(brtFlowTemplateVo);
        int i = this.baseMapper.insert(brtFlowTemplateVo);

        //获取节点列表 保存节点
        List<BrtFlowNodeVo> flowNodeList = brtFlowTemplateVo.getFlowNodeList();
        for (int j = 0; j < flowNodeList.size(); j++) {
            BrtFlowNodeVo flowNodeVo = flowNodeList.get(j);
            flowNodeVo.setSort(j);
            flowNodeVo.setTemplateId(brtFlowTemplateVo.getTemplateId());
            flowNodeService.save(flowNodeVo);
        }
        return brtFlowTemplateVo;
    }

    @Transactional
    @Override
    public BrtFlowTemplateVo updateBrtFlowTemplate(BrtFlowTemplateVo brtFlowTemplateVo) {
        saveBefore(brtFlowTemplateVo);
        int i = this.baseMapper.updateById(brtFlowTemplateVo);

        //获取节点列表 保存节点
        List<BrtFlowNodeVo> flowNodeList = brtFlowTemplateVo.getFlowNodeList();
        for (int j = 0; j < flowNodeList.size(); j++) {
            BrtFlowNodeVo flowNodeVo = flowNodeList.get(j);
            flowNodeVo.setSort(j);
            flowNodeVo.setTemplateId(brtFlowTemplateVo.getTemplateId());
            flowNodeService.saveOrUpdate(flowNodeVo);
        }
        return brtFlowTemplateVo;
    }

    @Transactional
    @Override
    public int deleteBrtFlowTemplateByTemplateIds(String[] templateIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(templateIds));
    }

    @Override
    public int copyTemplate(String templateId) {
        //查询模板信息
        BrtFlowTemplate flowTemplate = this.baseMapper.selectById(templateId);
        flowTemplate.setTemplateId(null);
        int insert = this.baseMapper.insert(flowTemplate);

        // 查询节点列表
        List<BrtFlowNode> flowNodeList = flowNodeService.list(new LambdaQueryWrapper<BrtFlowNode>().eq(BrtFlowNode::getTemplateId, templateId).orderByAsc(BrtFlowNode::getSort));
        flowNodeList.forEach(item -> {
            item.setNodeId(null);
            item.setTemplateId(flowTemplate.getTemplateId());
            BrtFlowNode flowNode = BeanUtil.copyProperties(item, BrtFlowNode.class);
            flowNodeService.save(flowNode);
        });

        return insert;
    }

    /**
     * @description: TODO 保存之前操作
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param brtFlowTemplateVo
     * @return:
     **/
    public void saveBefore(BrtFlowTemplateVo brtFlowTemplateVo){

    }

}
