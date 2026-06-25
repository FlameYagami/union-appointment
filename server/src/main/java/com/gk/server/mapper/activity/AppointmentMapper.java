package com.gk.server.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.server.model.dto.activity.AppointmentPageReq;
import com.gk.server.model.entity.activity.Appointment;
import org.apache.ibatis.annotations.Param;

/**
 * 预约 Mapper
 *
 * @author Codex
 */
public interface AppointmentMapper extends BaseMapper<Appointment> {

    IPage<Appointment> pageAppointment(IPage<Appointment> page, @Param("req") AppointmentPageReq req);
}