package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import com.gk.framework.helper.SysFileHelper;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeResp;
import com.gk.framework.model.dto.system.sysNotice.UserNoticeResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 公告通知表 数据库模型
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysNotice extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 通知标题
     */
    private String noticeTitle;

    /**
     * 通知类型;(1:公告, 2:通知, 默认:1)
     */
    private String noticeType;

    /**
     * 部门表id
     */
    private Long deptId;

    /**
     * 通知状态(1:正常, 0:停用, 默认:1)
     */
    private String dataStatus;

    /**
     * 通知范围;(1:全平台数据, 2:部门及以下数据, 3:本部门数据)
     */
    private String noticeScope;

    /**
     * 通知开始时间
     */
    private Date noticeStartTime;

    /**
     * 通知结束时间
     */
    private Date noticeEndTime;

    /**
     * 通知内容
     */
    private String noticeContent;

    /**
     * 消息置顶;(1:是, 0:否, 默认:0)
     */
    private String noticeTop;

    /**
     * 图片ids
     */
    private String imageFids;


    public SysNoticeResp toResp() {
        return new SysNoticeResp()
                .setId(id)
                .setNoticeTitle(noticeTitle)
                .setNoticeType(noticeType)
                .setDeptId(deptId)
                .setDataStatus(dataStatus)
                .setNoticeScope(noticeScope)
                .setNoticeStartTime(noticeStartTime)
                .setNoticeEndTime(noticeEndTime)
                .setNoticeContent(noticeContent)
                .setNoticeTop(noticeTop)
                .setSysFiles(SysFileHelper.getInstance().listSysFile(imageFids));
    }

    public UserNoticeResp toUserNoticeResp() {
        return new UserNoticeResp()
                .setId(id)
                .setNoticeTitle(noticeTitle)
                .setNoticeType(noticeType)
                .setNoticeContent(noticeContent);
    }

}
