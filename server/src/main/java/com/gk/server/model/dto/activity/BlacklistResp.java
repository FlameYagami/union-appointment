package com.gk.server.model.dto.activity;

import com.gk.server.enums.BlacklistStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 黑名单 响应类
 *
 * @author Codex
 */
@ApiModel(value = "黑名单响应")
@Data
@Accessors(chain = true)
public class BlacklistResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "用户ID", example = "1000000000000000001")
    private long userId;

    @ApiModelProperty(value = "用户账号", example = "zhangsan")
    private String userName;

    @ApiModelProperty(value = "部门ID", example = "1000000000000000001")
    private long deptId;

    @ApiModelProperty(value = "部门名称", example = "综合部")
    private String deptName;

    @ApiModelProperty(value = "违约次数", example = "1")
    private int violateCount;

    @ApiModelProperty(value = "封禁状态", example = "1")
    private String status;

    @ApiModelProperty(value = "封禁状态名称", example = "封禁")
    private String statusName;

    @ApiModelProperty(value = "封禁原因", example = "多次违约")
    private String reason;

    @ApiModelProperty(value = "封禁截止时间", example = "2026-12-31 23:59:59")
    private Date expireTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "可用状态", example = "1")
    private String enabled;

    @ApiModelProperty(value = "创建时间", example = "2026-01-01 10:00:00")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2026-01-01 10:00:00")
    private Date updateTime;

    public void handleData() {
        BlacklistStatus statusEnum = BlacklistStatus.getInstance(status);
        this.statusName = statusEnum != null ? statusEnum.desc : status;
    }
}
