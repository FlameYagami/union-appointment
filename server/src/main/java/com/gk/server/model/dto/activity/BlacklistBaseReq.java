package com.gk.server.model.dto.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gk.common.constant.DateConstant;
import com.gk.server.model.entity.activity.AppointmentBlacklist;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.util.Date;

/**
 * 黑名单 基础请求类
 *
 * @author Codex
 */
@Data
public class BlacklistBaseReq {

    @Min(value = 1, message = "用户ID不合法")
    @ApiModelProperty(value = "用户ID", required = true, example = "1000000000000000001")
    private long userId;

    @Length(max = 500, message = "封禁原因长度不能超过500")
    @ApiModelProperty(value = "封禁原因", example = "多次违约")
    private String reason;

    @JsonFormat(pattern = DateConstant.DEFAULT_PATTERN)
    @ApiModelProperty(value = "封禁截止时间", example = "2026-12-31 23:59:59")
    private Date expireTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 转换成数据库操作类
     */
    protected AppointmentBlacklist toEntity() {
        return new AppointmentBlacklist()
                .setUserId(userId)
                .setReason(reason)
                .setExpireTime(expireTime)
                .setRemark(remark);
    }
}