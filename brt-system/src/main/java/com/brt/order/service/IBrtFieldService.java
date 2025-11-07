package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtField;
import com.brt.order.vo.BrtFieldVo;

import java.util.List;

/**
 * 自定义字段Service接口
 * 
 * @author Fgn
 * @date 2024-06-15
 */
public interface IBrtFieldService extends IService<BrtField> {

    /**
     * @description: TODO 分页查询自定义字段列表
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: brtFieldVo 自定义字段
     * @return:
     * @return TableDataInfo<BrtFieldVo>
     **/
    public TableDataInfo<BrtFieldVo> queryBrtFieldList(BrtFieldVo brtFieldVo);

    /**
     * @description: TODO 查询全部自定义字段列表
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: brtFieldVo 自定义字段
     * @return:
     * @return java.util.List<BrtFieldVo>
     **/
    public List<BrtFieldVo> queryBrtFieldAll(BrtFieldVo brtFieldVo);

    /**
     * @description: TODO 根据fieldId查询自定义字段
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: fieldId
     * @return:
     * @return BrtFieldVo
     **/
    public BrtFieldVo queryBrtFieldByFieldId(String fieldId);

    /**
     * @description: TODO 新增自定义字段
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: brtFieldVo 自定义字段
     * @return:
     * @return int
     **/
    public BrtFieldVo insertBrtField(BrtFieldVo brtFieldVo);

    /**
     * @description: TODO 修改自定义字段
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: brtFieldVo 自定义字段
     * @return:
     * @return int
     **/
    public BrtFieldVo updateBrtField(BrtFieldVo brtFieldVo);

    /**
     * @description: TODO 批量删除自定义字段
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param:  fieldIds 需要删除的自定义字段主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtFieldByFieldIds(String[] fieldIds);
}
