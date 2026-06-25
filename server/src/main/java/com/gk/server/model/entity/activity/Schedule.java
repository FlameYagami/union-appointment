package com.gk.server.model.entity.activity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gk.common.model.base.BaseEntity;
import com.gk.server.model.dto.activity.ScheduleResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 鎺掓湡琛ㄥ疄浣? *
 * @author Codex
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("act_schedule")
public class Schedule extends BaseEntity {

    @TableId
    private Long id;
    private Long venueId;
    private String name;
    private String scheduleDate;
    private String startTime;
    private String endTime;
    private Integer maxCount;
    private Integer bookedCount;
    private String status;
    private String remark;

    public ScheduleResp toResp() {
        return new ScheduleResp()
                .setId(id)
                .setVenueId(venueId)
                .setName(name)
                .setScheduleDate(scheduleDate)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setMaxCount(maxCount)
                .setBookedCount(bookedCount)
                .setStatus(status)
                .setRemark(remark);
    }
}
