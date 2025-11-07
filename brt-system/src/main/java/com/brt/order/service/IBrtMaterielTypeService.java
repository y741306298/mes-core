package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtMaterielType;
import com.brt.order.vo.BrtMaterielTypeVo;

import java.util.List;

/**
 * 物料类型Service接口
 *
 * @author Fgn
 * @date 2024-05-07
 */
public interface IBrtMaterielTypeService extends IService<BrtMaterielType> {

    /**
     * @description: TODO 分页查询物料类型列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielTypeVo 物料类型
     * @return:
     * @return TableDataInfo<BrtMaterielTypeVo>
     **/
    public TableDataInfo<BrtMaterielTypeVo> queryBrtMaterielTypeList(BrtMaterielTypeVo brtMaterielTypeVo);

    /**
     * @description: TODO 查询全部物料类型列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielTypeVo 物料类型
     * @return:
     * @return java.util.List<BrtMaterielTypeVo>
     **/
    public List<BrtMaterielTypeVo> queryBrtMaterielTypeAll(BrtMaterielTypeVo brtMaterielTypeVo);

    /**
     * @description: TODO 根据typeId查询物料类型
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: typeId
     * @return:
     * @return BrtMaterielTypeVo
     **/
    public BrtMaterielTypeVo queryBrtMaterielTypeByTypeId(String typeId);

    /**
     * @description: TODO 新增物料类型
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielTypeVo 物料类型
     * @return:
     * @return int
     **/
    public BrtMaterielTypeVo insertBrtMaterielType(BrtMaterielTypeVo brtMaterielTypeVo);

    /**
     * @description: TODO 修改物料类型
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielTypeVo 物料类型
     * @return:
     * @return int
     **/
    public BrtMaterielTypeVo updateBrtMaterielType(BrtMaterielTypeVo brtMaterielTypeVo);

    /**
     * @description: TODO 批量删除物料类型
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param:  typeIds 需要删除的物料类型主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtMaterielTypeByTypeIds(String[] typeIds);

    /**
     * 根据名称查询
     * @param typeName
     * @return
     */
    BrtMaterielTypeVo queryByName(String typeName);
}
