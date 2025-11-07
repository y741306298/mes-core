package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtCheckType;
import com.brt.order.vo.BrtCheckTypeVo;

import java.util.List;

/**
 * 账单类型Service接口
 * 
 * @author Fgn
 * @date 2024-05-15
 */
public interface IBrtCheckTypeService extends IService<BrtCheckType> {

    /**
     * @description: TODO 分页查询账单类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckTypeVo 账单类型
     * @return:
     * @return TableDataInfo<BrtCheckTypeVo>
     **/
    public TableDataInfo<BrtCheckTypeVo> queryBrtCheckTypeList(BrtCheckTypeVo brtCheckTypeVo);

    /**
     * @description: TODO 查询全部账单类型列表
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckTypeVo 账单类型
     * @return:
     * @return java.util.List<BrtCheckTypeVo>
     **/
    public List<BrtCheckTypeVo> queryBrtCheckTypeAll(BrtCheckTypeVo brtCheckTypeVo);

    /**
     * @description: TODO 根据typeId查询账单类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: typeId
     * @return:
     * @return BrtCheckTypeVo
     **/
    public BrtCheckTypeVo queryBrtCheckTypeByTypeId(String typeId);

    /**
     * @description: TODO 新增账单类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckTypeVo 账单类型
     * @return:
     * @return int
     **/
    public BrtCheckTypeVo insertBrtCheckType(BrtCheckTypeVo brtCheckTypeVo);

    /**
     * @description: TODO 修改账单类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param: brtCheckTypeVo 账单类型
     * @return:
     * @return int
     **/
    public BrtCheckTypeVo updateBrtCheckType(BrtCheckTypeVo brtCheckTypeVo);

    /**
     * @description: TODO 批量删除账单类型
     * @author: Fgn
     * @date: 2024-05-15
     * @param:
     * @param:  typeIds 需要删除的账单类型主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtCheckTypeByTypeIds(String[] typeIds);

}
