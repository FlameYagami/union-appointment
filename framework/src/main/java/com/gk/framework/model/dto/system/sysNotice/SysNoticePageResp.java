package com.gk.framework.model.dto.system.sysNotice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gk.common.constant.CommonConstant;
import com.gk.common.constant.DateConstant;
import com.gk.common.utils.StringExtUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 公告通知 分页响应类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
@ApiModel(value = "公告通知分页响应")
@Data
@Accessors(chain = true)
public class SysNoticePageResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "公告通知标题", example = "公告通知标题")
    private String noticeTitle;

    @ApiModelProperty(value = "公告通知类型;(1:公告, 2:通知, 默认:1)", example = "1")
    private String noticeType;

    @ApiModelProperty(value = "公告通知状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "公告通知开始时间", example = "2000-01-31 23:59:59")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date noticeStartTime;

    @ApiModelProperty(value = "公告通知结束时间", example = "2000-01-31 23:59:59")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date noticeEndTime;

    @ApiModelProperty(value = "公告通知消息置顶;(1:是, 0:否, 默认:0)", example = "0")
    private String noticeTop;

    @ApiModelProperty(value = "创建时间", example = "2022-01-01 00:00:00")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date createTime;

    @ApiModelProperty(value = "账号", example = "admin")
    private String creatorUsername;

    @ApiModelProperty(value = "姓名", example = "管理员")
    private String creatorNickname;

    @ApiModelProperty(value = "是否可以修改;(1:是, 0:否, 默认:0)", example = "1")
    private boolean canModify;

    @ApiModelProperty(value = "创建人", example = "1000000000000000001", hidden = true)
    @JsonIgnore
    private long createBy;

    public void lateInit(long userId) {
        this.canModify = (CommonConstant.TOP_ID == userId || createBy == userId);
        this.creatorUsername = StringExtUtils.hideSuperAdmin(creatorUsername);
    }

}
