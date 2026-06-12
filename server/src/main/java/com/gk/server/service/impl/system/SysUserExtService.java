package com.gk.server.service.impl.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.enums.YesOrNo;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.annotation.DataScope;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.model.entity.system.SysUserDept;
import com.gk.framework.model.entity.system.SysUserRole;
import com.gk.framework.service.intf.system.ISysUserDeptService;
import com.gk.framework.service.intf.system.ISysUserRoleService;
import com.gk.framework.utils.LoginUserUtils;
import com.gk.server.mapper.system.SysUserExtMapper;
import com.gk.server.model.dto.system.sysUser.*;
import com.gk.server.model.entity.system.SysUserInfo;
import com.gk.server.service.intf.system.ISysRegisterService;
import com.gk.server.service.intf.system.ISysUserExtService;
import com.gk.server.service.intf.system.ISysUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表 服务实现类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */

@Service
@Slf4j
public class SysUserExtService extends ServiceImpl<SysUserExtMapper, SysUser> implements ISysUserExtService {

    @Lazy
    @Resource
    private ISysRegisterService sysRegisterService;
    @Resource
    private ISysUserInfoService sysUserInfoService;
    @Resource
    private ISysUserDeptService sysUserDeptService;
    @Resource
    private ISysUserRoleService sysUserRoleService;

    /**
     * 分页查询
     */
    @DataScope(bizTableAlias = "sud", userIdAlias = "id")
    @Override
    public IPage<SysUserPageResp> pageList(SysUserPageReq req) {
        req.handleData();
        IPage<SysUserPageResp> resp = baseMapper.pageSysUser(req.createPage(), req);
        resp.getRecords().forEach(SysUserPageResp::lateInit);
        return resp;
    }

    /**
     * 全表导出
     */
    @DataScope(bizTableAlias = "sud", userIdAlias = "id")
    @Override
    public List<SysUserExportResp> exportList(SysUserExportReq req) {
        req.handleData();
        return baseMapper.exportSysUser(req).stream()
                .peek(SysUserExportResp::lateInit)
                .collect(Collectors.toList());
    }

    /**
     * 新增
     */
    @Transactional
    @Override
    public long add(SysUserAddReq req) {
        // 账号唯一性校验
        checkUsernameUnique(req.getUsername());
        sysRegisterService.checkUsernameUnique(req.getUsername());

        // 保存用户数据
        SysUser sysUser = req.toEntity();
        if (!save(sysUser)) {
            log.error("Add SysUser Error: {}", JsonUtils.toJson(sysUser));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户信息数据
        SysUserInfo sysUserInfo = req.toSysUserInfo(sysUser.getId());
        if (!sysUserInfoService.save(sysUserInfo)) {
            log.error("Add SysUser Error in add SysUserInfo: {}", JsonUtils.toJson(sysUserInfo));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户部门数据
        List<SysUserDept> sysUserDepts = req.toSysUserDepts(sysUser.getId());
        if (!sysUserDeptService.saveBatch(sysUserDepts)) {
            log.error("Add SysUser Error in add SysUserDept: {}", JsonUtils.toJson(sysUserDepts));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户角色数据
        List<SysUserRole> sysUserRoles = req.toSysUserRoles(sysUser.getId());
        if (!sysUserRoleService.saveBatch(sysUserRoles)) {
            log.error("Add SysUser Error in add SysUserRole: {}", JsonUtils.toJson(sysUserRoles));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // todo 根据用户类型, 转化成不同类型的用户信息并保存
//        if (SysUserType.isStaff(req.getUserType())) {
//            StaffInfo staffInfo = req.toStaffInfo(sysUser.getId());
//            if (!StaffInfoService.save(StaffInfo)) {
//                log.error("Add SysUser Error in add StaffInfo: {}", JsonUtils.toJson(staffInfo));
//                throw new SysException(SysStatus.OPERATE_FAILED);
//            }
//        }

        return sysUser.getId();
    }

    /**
     * 修改
     */
    @Transactional
    @Override
    public void edit(SysUserEditReq req) {
        // 保存用户数据
        SysUser sysUser = req.toEntity();
        if (!updateById(sysUser)) {
            log.error("Edit SysUser Error: {}", JsonUtils.toJson(sysUser));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户信息数据
        SysUserInfo dbSysUserInfo = sysUserInfoService.findByUserId(req.getId());
        SysUserInfo sysUserInfo = req.toSysUserInfo(dbSysUserInfo.getId());
        if (!sysUserInfoService.updateById(sysUserInfo)) {
            log.error("Edit SysUser Error in edit SysUserInfo: {}", JsonUtils.toJson(sysUserInfo));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户部门数据
        List<SysUserDept> sysUserDepts = req.toSysUserDepts();
        if (!sysUserDeptService.modify(sysUser.getId(), sysUserDepts)) {
            log.error("Edit SysUser Error in modify SysUserDept: {}", JsonUtils.toJson(sysUserDepts));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户角色数据
        List<SysUserRole> sysUserRoles = req.toSysUserRoles();
        if (!sysUserRoleService.modify(sysUser.getId(), sysUserRoles)) {
            log.error("Edit SysUser Error in modify SysUserRole: {}", JsonUtils.toJson(sysUserRoles));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 删除
     */
    @Transactional
    @Override
    public void delete(long id) {
        // 判断是否为自身账号
        long userId = LoginUserUtils.getUserId();
        if (userId == id) {
            throw new SysException(SysStatus.CANT_NOT_DELETE_SELF);
        }

        // 判断是否为系统数据
        SysUser dbEntity = getById(id);
        if (YesOrNo.YES.value.equals(dbEntity.getSystemData())) {
            throw new SysException(SysStatus.SYSTEM_DATA_CAN_NOT_DELETE);
        }

        boolean result = lambdaUpdate()
                .set(SysUser::getEnabled, EnabledType.DISABLE.value)
                .eq(SysUser::getId, id)
                .update();
        if (!result) {
            log.error("Delete SysUser Error: {}", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        // 删除用户信息数据
        sysUserInfoService.deleteByUserId(id);
        // 删除用户部门数据
        sysUserDeptService.delete(id);
        // 删除用户角色数据
        sysUserRoleService.delete(id);
    }

    /**
     * 用户信息查询
     */
    @Override
    public SysUserResp findOne(long id) {
        SysUser sysUser = getById(id);
        List<Long> deptIds = sysUserDeptService.getDeptIds(id); // 管理界面要显示停用的部门
        List<Long> roleIds = sysUserRoleService.getRoleIds(id); // 管理界面要显示停用的角色

        SysUserResp sysUserResp = new SysUserResp()
                .setId(id)
                .setNickname(sysUser.getNickname())
                .setSystemData(sysUser.getSystemData())
                .setDataStatus(sysUser.getDataStatus())
                .setDeptIds(deptIds)
                .setRoleIds(roleIds);

        // 填充基本信息
        SysUserInfo sysUserInfo = sysUserInfoService.findByUserId(id);
        sysUserResp.setUserType(sysUserInfo.getUserType())
                .setTelephone(sysUserInfo.getTelephone())
                .setCardType(sysUserInfo.getCardType())
                .setCardNumber(sysUserInfo.getCardNumber())
                .setBirthday(sysUserInfo.getBirthday());

        // todo 根据用户类型, 从不同类型的用户信息表提取信息并填充到响应类中
//        if (SysUserType.isStaff(sysUserInfo.getUserType())) {
//            StaffInfo staffInfo = StaffInfoService.findByUserId(id);
//            sysUserResp.setCompany(StaffInfo.getCompany())
//                    .setCreditCode(StaffInfo.getCreditCode());
//        }

        return sysUserResp;
    }

    /**
     * 账号唯一性校验
     */
    @Override
    public void checkUsernameUnique(String username) {
        boolean existSysUser = lambdaQuery()
                .eq(SysUser::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUser::getUsername, username)
                .exists();
        if (existSysUser) {
            throw new SysException(SysStatus.ACCOUNT_ALREADY_EXIST);
        }
    }

}

