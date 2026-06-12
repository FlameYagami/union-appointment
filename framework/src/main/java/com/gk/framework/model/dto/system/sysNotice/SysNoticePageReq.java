package com.gk.framework.model.dto.system.sysNotice;

import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.YesOrNo;
import com.gk.common.model.dto.PageDateReq;
import com.gk.common.utils.StringExtUtils;
import com.gk.framework.utils.LoginUserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 公告通知 分页请求类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
@ApiModel(value = "公告通知分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNoticePageReq extends PageDateReq {

    @ApiModelProperty(value = "通知标题", example = "通知标题")
    private String noticeTitle;

    @ApiModelProperty(value = "通知类型;(1:公告, 2:通知, 默认:1)", example = "1")
    private String noticeType;

    @ApiModelProperty(value = "通知状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "通知范围;(1:全平台数据, 2:部门及以下数据, 3:本部门数据)", example = "1")
    private String noticeScope;

    @ApiModelProperty(value = "消息置顶;(1:是, 0:否, 默认:0)", example = "0")
    private String noticeTop;

    @ApiModelProperty(value = "操作人", example = "管理员")
    private String creator;

    @ApiModelProperty(value = "用户id集合", hidden = true)
    private String userIdColl;

    @ApiModelProperty(value = "是否为超管", hidden = true)
    private String isSuperAdmin;

    /**
     * 延迟初始化参数
     */
    public void lateInitParams(List<Long> userIds) {
        userIdColl = StringExtUtils.joinComma(userIds);
        isSuperAdmin = CommonConstant.TOP_ID == LoginUserUtils.getUserId() ? YesOrNo.YES.value : YesOrNo.NO.value;
    }

}
