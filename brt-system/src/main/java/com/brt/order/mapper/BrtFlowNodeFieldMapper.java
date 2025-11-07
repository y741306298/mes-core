package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtFlowNodeField;
import com.brt.order.vo.BrtFlowNodeFieldVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点字段Mapper接口
 * 
 * @author Fgn
 * @date 2024-04-30
 */
public interface BrtFlowNodeFieldMapper extends BaseMapper<BrtFlowNodeField> {

    /**
     * @description: TODO 分页查询节点字段列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: page
     * @param: brtFlowNodeFieldVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtFlowNodeFieldVo> queryBrtFlowNodeFieldList(Page<?> page, @Param("brtFlowNodeFieldVo") BrtFlowNodeFieldVo brtFlowNodeFieldVo);

    /**
     * @description: TODO 查询节点字段列表
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @param: brtFlowNodeFieldVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtFlowNodeFieldVo> queryBrtFlowNodeFieldList(@Param("brtFlowNodeFieldVo") BrtFlowNodeFieldVo brtFlowNodeFieldVo);

    /**
     * @description: TODO 根据fieldId查询节点字段
     * @author: Fgn
     * @date: 2024-04-30
     * @param:
     * @return:
     * @return Vo
     **/
    BrtFlowNodeFieldVo queryBrtFlowNodeFieldByFieldId(@Param("FieldId") String fieldId);

}
