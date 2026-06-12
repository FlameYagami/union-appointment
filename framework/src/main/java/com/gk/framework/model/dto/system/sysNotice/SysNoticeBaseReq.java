package com.gk.framework.model.dto.system.sysNotice;

import com.gk.common.constant.DateConstant;
import com.gk.common.enums.DataStatus;
import com.gk.common.enums.YesOrNo;
import com.gk.common.utils.StringExtUtils;
import com.gk.framework.constant.DictConstant;
import com.gk.framework.model.entity.system.SysNotice;
import com.gk.framework.validate.InDict;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公告通知 基础请求类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
@Data
public class SysNoticeBaseReq {

    @ApiModelProperty(value = "公告通知标题[64]", required = true, example = "通知标题")
    @NotBlank(message = "公告通知标题不能为空")
    @Length(max = 64, message = "公告通知标题最多输入64个字符")
    private String noticeTitle;

    @ApiModelProperty(value = "公告通知类型;(1:公告, 2:通知, 默认:1)", required = true, example = "1")
    @NotBlank(message = "公告通知类型不能为空")
    @InDict(DictConstant.CODE_NOTICE_TYPE)
    private String noticeType;

    @ApiModelProperty(value = "部门表id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long deptId;

    @ApiModelProperty(value = "公告通知状态;(1:正常, 0:停用)", required = true, example = "1")
    @NotBlank(message = "公告通知状态不能为空")
    @InEnum(DataStatus.class)
    private String dataStatus;

    @ApiModelProperty(value = "公告通知范围;(1:全平台数据, 2:部门及以下数据, 3:本部门数据)", required = true, example = "1")
    @NotBlank(message = "公告通知范围不能为空")
    @InDict(DictConstant.CODE_NOTICE_SCOPE)
    private String noticeScope;

    @ApiModelProperty(value = "公告通知开始时间", required = true, example = "2000-01-31 23:59:59")
    @DateTimeFormat(pattern = DateConstant.DEFAULT_PATTERN)
    @NotNull(message = "公告通知开始时间不能为空")
    private Date noticeStartTime;

    @ApiModelProperty(value = "公告通知结束时间", required = true, example = "2000-01-31 23:59:59")
    @DateTimeFormat(pattern = DateConstant.DEFAULT_PATTERN)
    @NotNull(message = "公告通知结束时间不能为空")
    private Date noticeEndTime;

    @ApiModelProperty(value = "公告通知内容", required = true, example = "公告通知内容")
    @NotBlank(message = "公告通知内容不能为空")
    private String noticeContent;

    @ApiModelProperty(value = "公告通知置顶;(1:是, 0:否, 默认:0)", required = true, example = "0")
    @NotBlank(message = "公告通知置顶不能为空")
    @InEnum(YesOrNo.class)
    private String noticeTop;

    @ApiModelProperty(value = "图片ids", example = "[1000000000000000001,1000000000000000002]")
    @Size(max = 5, message = "图片不能超过5张")
    private List<@Min(value = 1000000000000000001L, message = "id不合法") Long> imageFids;

    public List<Long> getImageFids() {
        return null == imageFids ? new ArrayList<>() : imageFids;
    }

    /**
     * 转换成数据库操作类
     */
    protected SysNotice toEntity() {
        return new SysNotice()
                .setNoticeTitle(noticeTitle)
                .setNoticeType(noticeType)
                .setDeptId(deptId)
                .setDataStatus(dataStatus)
                .setNoticeScope(noticeScope)
                .setNoticeStartTime(noticeStartTime)
                .setNoticeEndTime(noticeEndTime)
                .setNoticeContent(noticeContent)
                .setNoticeTop(noticeTop)
                .setImageFids(StringExtUtils.joinComma(imageFids));
    }

}
