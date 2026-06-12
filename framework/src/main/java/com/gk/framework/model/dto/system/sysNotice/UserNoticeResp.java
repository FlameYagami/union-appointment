package com.gk.framework.model.dto.system.sysNotice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户公告通知 分页响应类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
@ApiModel(value = "用户公告通知分页响应")
@Data
@Accessors(chain = true)
public class UserNoticeResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "公告通知标题", example = "公告通知标题")
    private String noticeTitle;

    @ApiModelProperty(value = "公告通知类型;(1:公告, 2:通知, 默认:1)", example = "1")
    private String noticeType;

    @ApiModelProperty(value = "公告通知内容", example = "公告通知内容")
    private String noticeContent;


}
