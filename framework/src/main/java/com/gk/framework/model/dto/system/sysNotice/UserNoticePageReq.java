package com.gk.framework.model.dto.system.sysNotice;

import cn.hutool.core.date.DateUtil;
import com.gk.common.model.dto.PageDateReq;
import com.gk.common.utils.StringExtUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户公告通知 分页请求类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
@ApiModel(value = "用户公告通知分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserNoticePageReq extends PageDateReq {

    @ApiModelProperty(value = "通知标题", example = "通知标题")
    private String noticeTitle;

    @ApiModelProperty(value = "通知类型;(1:公告, 2:通知, 默认:1)", example = "1")
    private String noticeType;

    @ApiModelProperty(value = "当前部门id", hidden = true)
    private long currentDeptId;

    @ApiModelProperty(value = "父级部门id集合", hidden = true)
    private String parentDeptIdColl;

    @ApiModelProperty(value = "当前时间", hidden = true)
    private String currentTime;

    /**
     * 延迟初始化参数
     */
    public void lateInitParams(long currentDeptId, List<Long> parentDeptIds) {
        this.currentDeptId = currentDeptId;
        this.parentDeptIdColl = StringExtUtils.joinComma(parentDeptIds);
        this.currentTime = DateUtil.now();
    }

}
