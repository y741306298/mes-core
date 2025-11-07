package com.brt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brt.order.domain.BrtField;
import com.brt.order.vo.BrtFieldVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义字段Mapper接口
 * 
 * @author Fgn
 * @date 2024-06-15
 */
public interface BrtFieldMapper extends BaseMapper<BrtField> {

    /**
     * @description: TODO 分页查询自定义字段列表
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: page
     * @param: brtFieldVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    Page<BrtFieldVo> queryBrtFieldList(Page<?> page, @Param("brtFieldVo") BrtFieldVo brtFieldVo);

    /**
     * @description: TODO 查询自定义字段列表
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @param: brtFieldVo
     * @return:
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<Vo>
     **/
    List<BrtFieldVo> queryBrtFieldList(@Param("brtFieldVo") BrtFieldVo brtFieldVo);

    /**
     * @description: TODO 根据fieldId查询自定义字段
     * @author: Fgn
     * @date: 2024-06-15
     * @param:
     * @return:
     * @return Vo
     **/
    BrtFieldVo queryBrtFieldByFieldId(@Param("FieldId") String fieldId);

}
