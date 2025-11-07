package com.brt.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.brt.common.core.page.TableDataInfo;
import com.brt.order.domain.BrtMateriel;
import com.brt.order.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 物料信息Service接口
 *
 * @author Fgn
 * @date 2024-05-07
 */
public interface IBrtMaterielService extends IService<BrtMateriel> {

    /**
     * @description: TODO 分页查询物料信息列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielVo 物料信息
     * @return:
     * @return TableDataInfo<BrtMaterielVo>
     **/
    public TableDataInfo<BrtMaterielVo> queryBrtMaterielList(BrtMaterielVo brtMaterielVo);

    /**
     * @description: TODO 查询全部物料信息列表
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielVo 物料信息
     * @return:
     * @return java.util.List<BrtMaterielVo>
     **/
    public List<BrtMaterielVo> queryBrtMaterielAll(BrtMaterielVo brtMaterielVo);

    /**
     * @description: TODO 根据materielId查询物料信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: materielId
     * @return:
     * @return BrtMaterielVo
     **/
    public BrtMaterielVo queryBrtMaterielByMaterielId(String materielId);

    /**
     * @description: TODO 新增物料信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielVo 物料信息
     * @return:
     * @return int
     **/
    public BrtMaterielVo insertBrtMateriel(BrtMaterielVo brtMaterielVo);

    /**
     * @description: TODO 修改物料信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param: brtMaterielVo 物料信息
     * @return:
     * @return int
     **/
    public BrtMaterielVo updateBrtMateriel(BrtMaterielVo brtMaterielVo);

    /**
     * @description: TODO 批量删除物料信息
     * @author: Fgn
     * @date: 2024-05-07
     * @param:
     * @param:  materielIds 需要删除的物料信息主键集合
     * @return:
     * @return int
     **/
    public int deleteBrtMaterielByMaterielIds(String[] materielIds);

    /**
     * @description: TODO 导入物料信息
     * @author: FanGN
     * @date: 01:04 2024/5/8
     * @param:
     * @param materielVoList
     * @param operName
     * @return:
     * @return java.lang.String
     **/
    String importData(List<BrtMaterielVo> materielVoList, String operName);

    /**
     * @description: TODO 设置库存预警数量
     * @author: FanGN
     * @date: 22:33 2024/5/18
     * @param:
     * @param materielIds
     * @param warningNum
     * @return:
     * @return int
     **/
    int warning(String materielIds, String warningNum);

    /**
     * @description: TODO 锁定库存
     * @author: FanGN
     * @date: 16:53 2024/6/26
     * @param:
     * @param brtSalesOrderVo
     * @return:
     * @return int
     **/
    int lockStock(BrtSalesOrderVo brtSalesOrderVo);


    /**
     * @description: TODO 锁定库存
     * @author: FanGN
     * @date: 16:53 2024/6/26
     * @param:
     * @param brtSalesOrderVo
     * @return:
     * @return int
     **/
    int lockStockNew(BrtSalesOrderVo brtSalesOrderVo);


    /**
     * @description: TODO 扣除锁定库存
     * @author: FanGN
     * @date: 16:46 2024/6/27
     * @param:
     * @return:
     * @return int
     **/
    int deductionSock(List<BrtOrderBoomVo> orderBoomVoList);

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    BrtMaterielVo queryByName(String name);

    /**
     * 修改开库存数量
     * @param inInventoryMaterielVo
     */
    void addNum(BrtInInventoryMaterielVo inInventoryMaterielVo);

    /**
     * 修改开库存数量
     * @param outInventoryMaterielVo
     */
    void reduceNum(BrtOutInventoryMaterielVo outInventoryMaterielVo);

    /**
     * 查询出入库记录
     * @param materielRecordVo
     * @return
     */
    TableDataInfo<BrtMaterielRecordVo> selectRecord(BrtMaterielRecordVo materielRecordVo);
}
