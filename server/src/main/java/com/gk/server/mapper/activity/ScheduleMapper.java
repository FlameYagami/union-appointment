package com.gk.server.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.server.model.dto.activity.SchedulePageReq;
import com.gk.server.model.entity.activity.Schedule;
import org.apache.ibatis.annotations.Param;

/**
 * 排期 Mapper
 *
 * @author Codex
 */
public interface ScheduleMapper extends BaseMapper<Schedule> {

    IPage<Schedule> pageSchedule(IPage<Schedule> page, @Param("req") SchedulePageReq req);

    void updateBookedCount(@Param("scheduleId") long scheduleId, @Param("count") int count);
}