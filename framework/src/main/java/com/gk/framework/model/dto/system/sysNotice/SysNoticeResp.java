package com.gk.framework.model.dto.system.sysNotice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import com.gk.framework.model.dto.system.sysFile.SysFileResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 公告通知 响应类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
@ApiModel(value = "公告通知响应")
@Data
@Accessors(chain = true)
public class SysNoticeResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "公告通知标题", example = "公告通知标题")
    private String noticeTitle;

    @ApiModelProperty(value = "公告通知类型;(1:公告, 2:通知, 默认:1)", example = "1")
    private String noticeType;

    @ApiModelProperty(value = "部门表id", example = "1000000000000000001")
    private long deptId;

    @ApiModelProperty(value = "公告通知状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "公告通知范围;(1:全平台数据, 2:部门及以下数据, 3:本部门数据)", example = "1")
    private String noticeScope;

    @ApiModelProperty(value = "公告通知开始时间", example = "2000-01-31 23:59:59")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date noticeStartTime;

    @ApiModelProperty(value = "公告通知结束时间", example = "2000-01-31 23:59:59")
    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private Date noticeEndTime;

    @ApiModelProperty(value = "公告通知内容", example = "公告通知内容")
    private String noticeContent;

    @ApiModelProperty(value = "公告通知置顶;(1:是, 0:否, 默认:0)", example = "0")
    private String noticeTop;

    /**
     * 文件信息
     */
    private List<SysFileResp> sysFiles;

}
