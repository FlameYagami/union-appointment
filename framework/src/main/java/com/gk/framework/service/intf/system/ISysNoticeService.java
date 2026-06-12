package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeAddReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeEditReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticePageReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticePageResp;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeResp;
import com.gk.framework.model.dto.system.sysNotice.UserNoticePageReq;
import com.gk.framework.model.dto.system.sysNotice.UserNoticePageResp;
import com.gk.framework.model.dto.system.sysNotice.UserNoticeResp;
import com.gk.framework.model.entity.system.SysNotice;

import java.util.List;

/**
 * 公告通知表 服务接口类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
public interface ISysNoticeService extends IService<SysNotice> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysNoticePageResp> pageList(SysNoticePageReq req);

    /**
     * 新增
     *
     * @param req 请求类
     * @return 主键id
     */
    long add(SysNoticeAddReq req, boolean hasAllDept);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(SysNoticeEditReq req, boolean hasAllDept);

    /**
     * 删除
     *
     * @param idList 主键id集合
     */
    void delete(List<Long> idList);

    /**
     * 单个查询
     *
     * @param id 主键id
     * @return 响应类
     */
    SysNoticeResp findOne(long id);

    List<TreeLabelResp> listMgmtAllDeptLabel();

    List<TreeLabelResp> listMgmtChildDeptLabel(boolean hasAllDept);

    void changeDataStatus(DataStatusChangeReq req);

    IPage<UserNoticePageResp> pageUserList(UserNoticePageReq req);

    UserNoticeResp findUserNotice(long id);

}
