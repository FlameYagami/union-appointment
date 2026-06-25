package com.gk.server.model.entity.activity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gk.common.model.base.BaseEntity;
import com.gk.server.model.dto.activity.BlacklistResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 杩濈害榛戝悕鍗曞疄浣? *
 * @author Codex
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("act_blacklist")
public class AppointmentBlacklist extends BaseEntity {

    @TableId
    private Long id;
    private Long userId;
    private String userName;
    private Long deptId;
    private Integer violateCount;
    private String status;
    private String reason;
    private Date expireTime;
    private String remark;

    public BlacklistResp toResp() {
        return new BlacklistResp()
                .setId(id)
                .setUserId(userId)
                .setUserName(userName)
                .setDeptId(deptId)
                .setViolateCount(violateCount)
                .setStatus(status)
                .setReason(reason)
                .setExpireTime(expireTime)
                .setRemark(remark);
    }
}
