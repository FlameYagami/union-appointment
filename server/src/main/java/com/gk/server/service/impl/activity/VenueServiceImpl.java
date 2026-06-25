package com.gk.server.service.impl.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.annotation.DataScope;
import com.gk.server.enums.ServerStatus;
import com.gk.server.mapper.activity.VenueMapper;
import com.gk.server.model.dto.activity.*;
import com.gk.server.model.entity.activity.Venue;
import com.gk.server.service.intf.activity.IVenueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 场地服务实现类
 *
 * @author Codex
 */
@Service
@Slf4j
public class VenueServiceImpl extends ServiceImpl<VenueMapper, Venue> implements IVenueService {

    @DataScope(bizTableAlias = "av")
    @Override
    public IPage<VenueResp> pageList(VenuePageReq req) {
        req.handleData();
        IPage<Venue> page = baseMapper.pageVenue(req.createPage(), req);
        IPage<VenueResp> resp = page.convert(Venue::toResp);
        resp.getRecords().forEach(VenueResp::handleData);
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long add(VenueAddReq req) {
        Venue venue = req.toEntity();
        if (!save(venue)) {
            log.error("Add Venue Error: add failed, {}", JsonUtils.toJson(venue));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return venue.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(VenueEditReq req) {
        Venue dbEntity = getById(req.getId());
        if (dbEntity == null) {
            throw new SysException(ServerStatus.VENUE_NOT_FOUND);
        }
        Venue venue = req.toEntity();
        if (!updateById(venue)) {
            log.error("Edit Venue Error: edit failed, {}", JsonUtils.toJson(venue));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(long id) {
        Venue dbEntity = getById(id);
        if (dbEntity == null) {
            throw new SysException(ServerStatus.VENUE_NOT_FOUND);
        }
        boolean result = lambdaUpdate()
                .eq(Venue::getId, id)
                .set(Venue::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete Venue Error: delete failed, id({})", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    @Override
    public VenueResp findOne(long id) {
        Venue venue = lambdaQuery()
                .eq(Venue::getEnabled, EnabledType.ENABLE.value)
                .eq(Venue::getId, id)
                .one();
        if (venue == null) {
            return null;
        }
        VenueResp resp = venue.toResp();
        resp.handleData();
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeStatus(long id, String enabled) {
        Venue dbEntity = getById(id);
        if (dbEntity == null) {
            throw new SysException(ServerStatus.VENUE_NOT_FOUND);
        }
        boolean result = lambdaUpdate()
                .eq(Venue::getId, id)
                .set(Venue::getEnabled, enabled)
                .update();
        if (!result) {
            log.error("Change Venue Status Error: change failed, id({}), enabled({})", id, enabled);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

}
