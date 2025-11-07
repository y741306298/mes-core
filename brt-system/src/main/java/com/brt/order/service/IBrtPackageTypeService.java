package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtPackageType;
import com.brt.order.vo.BrtPackageTypeVo;

import java.util.List;

/**
 * 封装类型管理Service接口
 * 
 * @author Fgn
 * @date 2024-05-09
 */
public interface IBrtPackageTypeService extends IService<BrtPackageType> {

    /**
     * @description: TODO 分页查询封装类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPackageTypeVo 封装类型管理
     * @return:
     * @return TableDataInfo<BrtPackageTypeVo>
     **/
    public TableDataInfo<BrtPackageTypeVo> queryBrtPackageTypeList(BrtPackageTypeVo brtPackageTypeVo);

    /**
     * @description: TODO 查询全部封装类型管理列表
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPackageTypeVo 封装类型管理
     * @return:
     * @return java.util.List<BrtPackageTypeVo>
     **/
    public List<BrtPackageTypeVo> queryBrtPackageTypeAll(BrtPackageTypeVo brtPackageTypeVo);

    /**
     * @description: TODO 根据typeId查询封装类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: typeId
     * @return:
     * @return BrtPackageTypeVo
     **/
    public BrtPackageTypeVo queryBrtPackageTypeByTypeId(String typeId);

    /**
     * @description: TODO 新增封装类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPackageTypeVo 封装类型管理
     * @return:
     * @return int
     **/
    public BrtPackageTypeVo insertBrtPackageType(BrtPackageTypeVo brtPackageTypeVo);

    /**
     * @description: TODO 修改封装类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param: brtPackageTypeVo 封装类型管理
     * @return:
     * @return int
     **/
    public BrtPackageTypeVo updateBrtPackageType(BrtPackageTypeVo brtPackageTypeVo);

    /**
     * @description: TODO 批量删除封装类型管理
     * @author: Fgn
     * @date: 2024-05-09
     * @param:
     * @param:  typeIds 需要删除的封装类型管理主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtPackageTypeByTypeIds(String[] typeIds);

}
