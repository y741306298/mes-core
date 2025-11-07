package com.brt.system.service;

import java.util.List;

import com.brt.common.core.page.TableDataInfo;
import com.brt.system.domain.SysNotice;

/**
 * 公告 服务层
 * 
 * @author ruoyi
 */
public interface ISysNoticeService
{
    /**
     * 查询公告信息
     * 
     * @param noticeId 公告ID
     * @return 公告信息
     */
    public SysNotice selectNoticeById(Long noticeId);

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * 新增公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    public int insertNotice(SysNotice notice);

    /**
     * 修改公告
     * 
     * @param notice 公告信息
     * @return 结果
     */
    public int updateNotice(SysNotice notice);

    /**
     * 删除公告信息
     * 
     * @param noticeId 公告ID
     * @return 结果
     */
    public int deleteNoticeById(Long noticeId);
    
    /**
     * 批量删除公告信息
     * 
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    public int deleteNoticeByIds(Long[] noticeIds);

    /**
     * @description: TODO 获取通知公告列表
     * @author: FanGN
     * @date: 14:25 2023/8/7
     * @param:
     * @return:
     * @return com.brt.common.core.page.TableDataInfo<com.brt.system.domain.SysNotice>
     **/
    TableDataInfo<SysNotice> apiGetNoticeList();

    /**
     * @description: TODO 获取最新一条消息公告
     * @author: FanGN
     * @date: 14:33 2023/8/7
     * @param:
     * @return:
     * @return com.brt.system.domain.SysNotice
     **/
    SysNotice apiGetNewNotice();
}
