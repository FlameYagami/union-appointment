package com.gk.server.service.impl.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.annotation.DataScope;
import com.gk.framework.helper.SysUserHelper;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.server.enums.BlacklistStatus;
import com.gk.server.enums.ServerStatus;
import com.gk.server.mapper.activity.AppointmentBlacklistMapper;
import com.gk.server.model.dto.activity.*;
import com.gk.server.model.entity.activity.AppointmentBlacklist;
import com.gk.server.service.intf.activity.IBlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 黑名单服务实现类
 *
 * @author Codex
 */
@Service
@Slf4j
public class BlacklistServiceImpl extends ServiceImpl<AppointmentBlacklistMapper, AppointmentBlacklist> implements IBlacklistService {

    @DataScope(bizTableAlias = "ab")
    @Override
    public IPage<BlacklistResp> pageList(BlacklistPageReq req) {
        req.handleData();
        IPage<AppointmentBlacklist> page = baseMapper.pageBlacklist(req.createPage(), req);
        IPage<BlacklistResp> resp = page.convert(AppointmentBlacklist::toResp);
        resp.getRecords().forEach(BlacklistResp::handleData);
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long add(BlacklistAddReq req) {
        // 检查是否已在黑名单
        boolean exist = lambdaQuery()
                .eq(AppointmentBlacklist::getUserId, req.getUserId())
                .eq(AppointmentBlacklist::getStatus, BlacklistStatus.BLOCK.value)
                .exists();
        if (exist) {
            throw new SysException(ServerStatus.BLACKLIST_USER_EXIST);
        }

        SysUser sysUser = SysUserHelper.getInstance().findByUserId(req.getUserId());
        if (sysUser == null) {
            throw new SysException(ServerStatus.BLACKLIST_USER_NOT_FOUND);
        }
        String userName = StrUtil.blankToDefault(sysUser.getNickname(), sysUser.getUsername());
        long deptId = RedisCacheManager.getInstance().getRedisDeptId();
        AppointmentBlacklist blacklist = req.toEntity(userName, deptId);
        if (!save(blacklist)) {
            log.error("Add Blacklist Error: add failed, {}", JsonUtils.toJson(blacklist));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return blacklist.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(BlacklistEditReq req) {
        AppointmentBlacklist dbEntity = getById(req.getId());
        if (dbEntity == null) {
            throw new SysException(ServerStatus.BLACKLIST_NOT_FOUND);
        }

        AppointmentBlacklist blacklist = req.toEntity();
        if (!updateById(blacklist)) {
            log.error("Edit Blacklist Error: edit failed, {}", JsonUtils.toJson(blacklist));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unblock(long id) {
        AppointmentBlacklist dbEntity = getById(id);
        if (dbEntity == null) {
            throw new SysException(ServerStatus.BLACKLIST_NOT_FOUND);
        }

        boolean result = lambdaUpdate()
                .eq(AppointmentBlacklist::getId, id)
                .set(AppointmentBlacklist::getStatus, BlacklistStatus.UNBLOCK.value)
                .update();
        if (!result) {
            log.error("Unblock Blacklist Error: unblock failed, id({})", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    @Override
    public BlacklistResp findOne(long id) {
        AppointmentBlacklist blacklist = lambdaQuery()
                .eq(AppointmentBlacklist::getId, id)
                .one();
        if (blacklist == null) {
            return null;
        }
        BlacklistResp resp = blacklist.toResp();
        resp.handleData();
        return resp;
    }
}
