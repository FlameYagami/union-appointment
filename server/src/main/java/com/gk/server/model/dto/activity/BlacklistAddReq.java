package com.gk.server.model.dto.activity;

import com.gk.server.enums.BlacklistStatus;
import com.gk.server.model.entity.activity.AppointmentBlacklist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 黑名单 新增请求类
 *
 * @author Codex
 */
@ApiModel(value = "黑名单新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class BlacklistAddReq extends BlacklistBaseReq {

    @ApiModelProperty(value = "违约次数", example = "1")
    private int violateCount = 1;

    @ApiModelProperty(value = "封禁状态：0-解封，1-封禁", example = "1")
    private String status = BlacklistStatus.BLOCK.value;

    /**
     * 转换成数据库操作类
     */
    public AppointmentBlacklist toEntity(String userName, long deptId) {
        return super.toEntity()
                .setUserName(userName)
                .setDeptId(deptId)
                .setViolateCount(violateCount)
                .setStatus(status);
    }
}