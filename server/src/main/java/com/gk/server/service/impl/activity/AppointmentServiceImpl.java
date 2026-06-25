package com.gk.server.service.impl.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.annotation.DataScope;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.model.bo.security.LoginUser;
import com.gk.framework.utils.LoginUserUtils;
import com.gk.server.enums.AppointmentStatus;
import com.gk.server.enums.ServerStatus;
import com.gk.server.mapper.activity.AppointmentBlacklistMapper;
import com.gk.server.mapper.activity.AppointmentMapper;
import com.gk.server.mapper.activity.ScheduleMapper;
import com.gk.server.mapper.activity.VenueMapper;
import com.gk.server.model.dto.activity.*;
import com.gk.server.model.entity.activity.Appointment;
import com.gk.server.model.entity.activity.Schedule;
import com.gk.server.model.entity.activity.Venue;
import com.gk.server.service.intf.activity.IAppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 预约服务实现类
 *
 * @author Codex
 */
@Service
@Slf4j
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

    @Resource
    private VenueMapper venueMapper;

    @Resource
    private ScheduleMapper scheduleMapper;

    @Resource
    private AppointmentBlacklistMapper blacklistMapper;

    @DataScope(bizTableAlias = "aa")
    @Override
    public IPage<AppointmentResp> pageList(AppointmentPageReq req) {
        req.handleData();
        IPage<Appointment> page = baseMapper.pageAppointment(req.createPage(), req);
        IPage<AppointmentResp> resp = page.convert(Appointment::toResp);
        resp.getRecords().forEach(AppointmentResp::handleData);
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long add(AppointmentAddReq req) {
        LoginUser loginUser = LoginUserUtils.getLoginUser();

        // 检查黑名单
        if (blacklistMapper.selectExists(loginUser.getUserId())) {
            throw new SysException(ServerStatus.APPOINTMENT_BLACKLISTED);
        }

        // 校验场地
        Venue venue = venueMapper.selectById(req.getVenueId());
        if (venue == null) {
            throw new SysException(ServerStatus.VENUE_NOT_FOUND);
        }
        if (!EnabledType.ENABLE.value.equals(venue.getEnabled())) {
            throw new SysException(ServerStatus.VENUE_NOT_ENABLED);
        }

        // 校验排期
        Schedule schedule = scheduleMapper.selectById(req.getScheduleId());
        if (schedule == null) {
            throw new SysException(ServerStatus.SCHEDULE_NOT_FOUND);
        }
        if (!EnabledType.ENABLE.value.equals(schedule.getEnabled())) {
            throw new SysException(ServerStatus.SCHEDULE_NOT_ENABLED);
        }
        if (!EnabledType.ENABLE.value.equals(schedule.getStatus())) {
            throw new SysException(ServerStatus.SCHEDULE_NOT_OPEN);
        }

        // 校验名额
        if (schedule.getBookedCount() + req.getPersonCount() > schedule.getMaxCount()) {
            throw new SysException(ServerStatus.APPOINTMENT_QUOTA_EXCEED, "当前剩余：" + (schedule.getMaxCount() - schedule.getBookedCount()));
        }

        // 判断是否需要审批
        String status = EnabledType.ENABLE.value.equals(venue.getRequireApproval())
                ? AppointmentStatus.PENDING.value
                : AppointmentStatus.APPROVED.value;

        long deptId = RedisCacheManager.getInstance().getRedisDeptId();
        Appointment appointment = req.toEntity(loginUser.getUserId(), loginUser.getUsername(), deptId);
        appointment.setStatus(status);

        if (!save(appointment)) {
            log.error("Add Appointment Error: add failed, {}", JsonUtils.toJson(appointment));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 如果不需要审批，直接更新排期已预约人数
        if (AppointmentStatus.APPROVED.value.equals(status)) {
            scheduleMapper.updateBookedCount(schedule.getId(), req.getPersonCount());
        }

        return appointment.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(long id) {
        Appointment dbEntity = getById(id);
        if (dbEntity == null) {
            throw new SysException(ServerStatus.APPOINTMENT_NOT_FOUND);
        }

        String status = dbEntity.getStatus();
        if (!AppointmentStatus.PENDING.value.equals(status) && !AppointmentStatus.APPROVED.value.equals(status)) {
            throw new SysException(ServerStatus.APPOINTMENT_STATUS_NOT_SUPPORT);
        }

        boolean result = lambdaUpdate()
                .eq(Appointment::getId, id)
                .set(Appointment::getStatus, AppointmentStatus.CANCELLED.value)
                .update();
        if (!result) {
            log.error("Cancel Appointment Error: cancel failed, id({})", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        if (AppointmentStatus.APPROVED.value.equals(status)) {
            scheduleMapper.updateBookedCount(dbEntity.getScheduleId(), -dbEntity.getPersonCount());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approve(AppointmentApproveReq req) {
        Appointment dbEntity = getById(req.getId());
        if (dbEntity == null) {
            throw new SysException(ServerStatus.APPOINTMENT_NOT_FOUND);
        }

        if (!AppointmentStatus.PENDING.value.equals(dbEntity.getStatus())) {
            throw new SysException(ServerStatus.APPOINTMENT_STATUS_NOT_SUPPORT);
        }

        LoginUser loginUser = LoginUserUtils.getLoginUser();

        boolean result = lambdaUpdate()
                .eq(Appointment::getId, req.getId())
                .set(Appointment::getStatus, req.getResult())
                .set(Appointment::getApproveBy, loginUser.getUserId())
                .set(Appointment::getApproveTime, new Date())
                .set(Appointment::getApproveRemark, req.getApproveRemark())
                .update();
        if (!result) {
            log.error("Approve Appointment Error: approve failed, id({})", req.getId());
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        if (AppointmentStatus.APPROVED.value.equals(req.getResult())) {
            scheduleMapper.updateBookedCount(dbEntity.getScheduleId(), dbEntity.getPersonCount());
        }
    }

    @Override
    public AppointmentResp findOne(long id) {
        Appointment appointment = lambdaQuery()
                .eq(Appointment::getEnabled, EnabledType.ENABLE.value)
                .eq(Appointment::getId, id)
                .one();
        if (appointment == null) {
            return null;
        }
        AppointmentResp resp = appointment.toResp();
        resp.handleData();
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void markViolated(long id) {
        Appointment dbEntity = getById(id);
        if (dbEntity == null) {
            throw new SysException(ServerStatus.APPOINTMENT_NOT_FOUND);
        }

        String status = dbEntity.getStatus();
        if (AppointmentStatus.CANCELLED.value.equals(status) || AppointmentStatus.VIOLATED.value.equals(status)) {
            throw new SysException(ServerStatus.APPOINTMENT_STATUS_NOT_SUPPORT);
        }

        boolean result = lambdaUpdate()
                .eq(Appointment::getId, id)
                .set(Appointment::getStatus, AppointmentStatus.VIOLATED.value)
                .update();
        if (!result) {
            log.error("Mark Violated Error: mark failed, id({})", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        if (AppointmentStatus.APPROVED.value.equals(status)) {
            scheduleMapper.updateBookedCount(dbEntity.getScheduleId(), -dbEntity.getPersonCount());
        }
    }
}
