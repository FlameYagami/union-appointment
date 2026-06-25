package com.gk.server.service.impl.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.annotation.DataScope;
import com.gk.server.enums.ServerStatus;
import com.gk.server.mapper.activity.ScheduleMapper;
import com.gk.server.mapper.activity.VenueMapper;
import com.gk.server.model.dto.activity.*;
import com.gk.server.model.entity.activity.Schedule;
import com.gk.server.model.entity.activity.Venue;
import com.gk.server.service.intf.activity.IVenueService;
import com.gk.server.service.intf.activity.IScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 排期服务实现类
 *
 * @author Codex
 */
@Service
@Slf4j
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements IScheduleService {

    @Lazy
    @Resource
    private IVenueService venueService;

    @Resource
    private VenueMapper venueMapper;

    @DataScope(bizTableAlias = "sch")
    @Override
    public IPage<ScheduleResp> pageList(SchedulePageReq req) {
        req.handleData();
        IPage<Schedule> page = baseMapper.pageSchedule(req.createPage(), req);
        IPage<ScheduleResp> resp = page.convert(Schedule::toResp);
        resp.getRecords().forEach(ScheduleResp::handleData);
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long add(ScheduleAddReq req) {
        Venue venue = venueMapper.selectById(req.getVenueId());
        if (venue == null) {
            throw new SysException(ServerStatus.VENUE_NOT_FOUND);
        }
        if (!EnabledType.ENABLE.value.equals(venue.getEnabled())) {
            throw new SysException(ServerStatus.VENUE_NOT_ENABLED);
        }

        Schedule schedule = req.toEntity();
        if (!save(schedule)) {
            log.error("Add Schedule Error: add failed, {}", JsonUtils.toJson(schedule));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return schedule.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(ScheduleEditReq req) {
        Schedule dbEntity = getById(req.getId());
        if (dbEntity == null) {
            throw new SysException(ServerStatus.SCHEDULE_NOT_FOUND);
        }

        Venue venue = venueMapper.selectById(req.getVenueId());
        if (venue == null) {
            throw new SysException(ServerStatus.VENUE_NOT_FOUND);
        }

        if (req.getMaxCount() < dbEntity.getBookedCount()) {
            throw new SysException(ServerStatus.SCHEDULE_MAX_COUNT_LESS_THAN_BOOKED, "当前已预约：" + dbEntity.getBookedCount());
        }

        Schedule schedule = req.toEntity();
        if (!updateById(schedule)) {
            log.error("Edit Schedule Error: edit failed, {}", JsonUtils.toJson(schedule));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(long id) {
        Schedule dbEntity = getById(id);
        if (dbEntity == null) {
            throw new SysException(ServerStatus.SCHEDULE_NOT_FOUND);
        }

        boolean hasAppointment = lambdaQuery()
                .eq(Schedule::getId, id)
                .exists();
        if (hasAppointment) {
            throw new SysException(ServerStatus.SCHEDULE_HAS_APPOINTMENT);
        }

        boolean result = lambdaUpdate()
                .eq(Schedule::getId, id)
                .set(Schedule::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete Schedule Error: delete failed, id({})", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    @Override
    public ScheduleResp findOne(long id) {
        Schedule schedule = lambdaQuery()
                .eq(Schedule::getEnabled, EnabledType.ENABLE.value)
                .eq(Schedule::getId, id)
                .one();
        if (schedule == null) {
            return null;
        }
        ScheduleResp resp = schedule.toResp();
        resp.handleData();
        return resp;
    }
}
