package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtFlowNodeField;
import com.brt.order.vo.BrtFlowNodeFieldVo;

import java.util.List;

/**
 * 节点字段Service接口
 * 
 * @author Fgn
 * @date 2024-04-30
 */
public interface IBrtFlowNodeFieldService extends IService<BrtFlowNodeField> {

    /**
     * @description: TODO 分页查询节点字段列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeFieldVo 节点字段
     * @return:
     * @return TableDataInfo<BrtFlowNodeFieldVo>
     **/
    public TableDataInfo<BrtFlowNodeFieldVo> queryBrtFlowNodeFieldList(BrtFlowNodeFieldVo brtFlowNodeFieldVo);

    /**
     * @description: TODO 查询全部节点字段列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeFieldVo 节点字段
     * @return:
     * @return java.util.List<BrtFlowNodeFieldVo>
     **/
    public List<BrtFlowNodeFieldVo> queryBrtFlowNodeFieldAll(BrtFlowNodeFieldVo brtFlowNodeFieldVo);

    /**
     * @description: TODO 根据fieldId查询节点字段
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: fieldId
     * @return:
     * @return BrtFlowNodeFieldVo
     **/
    public BrtFlowNodeFieldVo queryBrtFlowNodeFieldByFieldId(String fieldId);

    /**
     * @description: TODO 新增节点字段
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeFieldVo 节点字段
     * @return:
     * @return int
     **/
    public BrtFlowNodeFieldVo insertBrtFlowNodeField(BrtFlowNodeFieldVo brtFlowNodeFieldVo);

    /**
     * @description: TODO 修改节点字段
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeFieldVo 节点字段
     * @return:
     * @return int
     **/
    public BrtFlowNodeFieldVo updateBrtFlowNodeField(BrtFlowNodeFieldVo brtFlowNodeFieldVo);

    /**
     * @description: TODO 批量删除节点字段
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param:  fieldIds 需要删除的节点字段主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtFlowNodeFieldByFieldIds(String[] fieldIds);

}
