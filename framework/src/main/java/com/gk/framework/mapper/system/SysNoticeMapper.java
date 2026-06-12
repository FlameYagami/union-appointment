package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.framework.model.dto.system.sysNotice.SysNoticePageReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticePageResp;
import com.gk.framework.model.dto.system.sysNotice.UserNoticePageReq;
import com.gk.framework.model.dto.system.sysNotice.UserNoticePageResp;
import com.gk.framework.model.entity.system.SysNotice;
import org.apache.ibatis.annotations.Param;

/**
 * 公告通知表 Mapper 接口
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */

public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    /**
     * 分页查询
     */
    IPage<SysNoticePageResp> pageSysNotice(IPage<?> page, @Param("req") SysNoticePageReq req);

    /**
     * 用户公告通知查询
     */
    IPage<UserNoticePageResp> pageSysUserNotice(IPage<?> page, @Param("req") UserNoticePageReq req);

}
