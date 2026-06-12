package com.gk.server.service.impl.system;

import cn.hutool.core.util.IdcardUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.CardType;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.helper.SysFileHelper;
import com.gk.framework.model.bo.system.sysUser.SysUserExt;
import com.gk.framework.model.dto.system.sysFile.SysFileResp;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.service.intf.system.ISysFileService;
import com.gk.framework.service.intf.system.ISysUserService;
import com.gk.framework.utils.LoginUserUtils;
import com.gk.server.mapper.system.SysUserInfoMapper;
import com.gk.server.model.dto.system.sysUserInfo.SimpleSysUserInfoResp;
import com.gk.server.model.dto.system.sysUserInfo.SysUserInfoEditReq;
import com.gk.server.model.dto.system.sysUserInfo.SysUserInfoResp;
import com.gk.server.model.entity.system.SysUserInfo;
import com.gk.server.service.intf.system.ISysRegisterService;
import com.gk.server.service.intf.system.ISysUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 用户信息表 服务实现类
 *
 * @author Flame
 * @since 2022-12-16 17:46:27
 */

@Service
@Slf4j
public class SysUserInfoService extends ServiceImpl<SysUserInfoMapper, SysUserInfo> implements ISysUserInfoService {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysFileService sysFileService;
    @Resource
    private ISysRegisterService sysRegisterService;

    /**
     * 简易用户信息详情查询
     */
    @Override
    public SimpleSysUserInfoResp getSimpleSysUserInfo(){
        long userId = LoginUserUtils.getUserId();
        return findByUserId(userId).toSysUserInfoSimpleResp();
    }

    /**
     * 用户信息详情查询
     */
    @Override
    public SysUserInfoResp getSysUserInfo(Long userId) {
        if (null == userId) {
            userId = LoginUserUtils.getUserId();
        }
        SysUserExt sysUserExt = sysUserService.findSysUserExt(userId);
        return findByUserId(userId).toResp(sysUserExt);
    }

    /**
     * 修改系统用户信息
     */
    @Transactional
    @Override
    public void editSysUserInfo(SysUserInfoEditReq req) {
        // 校验用户信息参数
        checkUserInfoParams(req);

        // 保存系统用户信息数据
        SysUserInfo sysUserInfo = req.toEntity();
        if (!updateById(sysUserInfo)) {
            log.error("Edit SysUserInfo Error: SysUserInfo({})", JsonUtils.toJson(req));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存系统用户数据
        SysUserInfo dbEntity = getById(req.getId());
        SysUser sysUser = req.toSysUser(dbEntity.getUserId());
        if (!sysUserService.updateById(sysUser)) {
            log.error("Edit SysUserInfo Error in edit SysUser: SysUser({})", JsonUtils.toJson(sysUser));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 系统头像上传
     */
    @Transactional
    @Override
    public SysFileResp uploadSysAvatar(MultipartFile file, String uploadDir, String bizType) {
        SysFileResp sysFileResp = sysFileService.upload(file, uploadDir, bizType, EnabledType.ENABLE);
        long userId = LoginUserUtils.getUserId();
        SysUserInfo sysUserInfo = findByUserId(userId);
        // 把以前的头像标记为删除
        if (null != sysUserInfo.getAvatarFid()) {
            if (!SysFileHelper.getInstance().updateEnabled(sysUserInfo.getAvatarFid(), EnabledType.DISABLE)) {
                log.error("Upload SysAvatar error in delete old fileId({})", sysUserInfo.getAvatarFid());
            }
        }
        // 填充新的头像id
        sysUserInfo.setAvatarFid(sysFileResp.getId());
        if (!updateById(sysUserInfo)) {
            log.error("Upload SysAvatar error in save SysUserInfo: {}", JsonUtils.toJson(sysUserInfo));
            throw new SysException(SysStatus.FILE_UPLOAD_ERROR);
        }
        return sysFileResp;
    }

    /**
     * 删除
     */
    @Override
    public void deleteByUserId(long userId) {
        SysUserInfo sysUserInfo = findByUserId(userId);

        boolean result = lambdaUpdate()
                .set(SysUserInfo::getEnabled, EnabledType.DISABLE.value)
                .eq(SysUserInfo::getId, sysUserInfo.getId())
                .update();
        if (!result) {
            log.error("Delete SysUserInfo Error: userId({})", userId);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // todo 扩展逻辑, 如果对应的userType包含扩展信息表, 在这里做删除逻辑
//        if (SysUserType.isStaff(sysUserInfo.getUserType())) {
//            StaffInfoService.deleteByUserId(userId);
//            return;
//        }
    }

    /**
     * 通过userId查询单个用户信息
     */
    @Override
    public SysUserInfo findByUserId(long userId) {
        return lambdaQuery()
                .eq(SysUserInfo::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUserInfo::getUserId, userId)
                .one();
    }

    /**
     * 证件号唯一性校验
     */
    @Override
    public void checkCardNumberUnique(String cardNumber, Long userInfoId) {
        boolean existSysRegister = lambdaQuery()
                .eq(SysUserInfo::getEnabled, EnabledType.ENABLE.value)
                .eq(SysUserInfo::getCardNumber, cardNumber)
                .ne(null != userInfoId, SysUserInfo::getId, userInfoId)
                .exists();
        if (existSysRegister) {
            throw new SysException(SysStatus.CARD_NUMBER_EXIST);
        }
    }

    /**
     * 校验用户信息参数
     */
    public void checkUserInfoParams(SysUserInfoEditReq req) {
        // 校验证件号唯一性
        checkCardNumberUnique(req.getCardNumber(), req.getId());
        sysRegisterService.checkCardNumberUnique(req.getCardNumber());
        // 校验身份证
        if (CardType.ID_CARD.value.equals(req.getCardType()) && !IdcardUtil.isValidCard(req.getCardNumber())) {
            throw new SysException(SysStatus.IDCARD_INVALID);
        }
    }

}

