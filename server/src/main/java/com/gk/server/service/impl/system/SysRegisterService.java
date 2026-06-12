package com.gk.server.service.impl.system;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.constant.CommonConstant;
import com.gk.common.constant.RegexConstant;
import com.gk.common.enums.CardType;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.ReviewResult;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.AesUtils;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.RsaUtils;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.framework.model.entity.system.SysUserDept;
import com.gk.framework.model.entity.system.SysUserRole;
import com.gk.framework.service.intf.system.ISysUserDeptService;
import com.gk.framework.service.intf.system.ISysUserRoleService;
import com.gk.framework.utils.LoginUserUtils;
import com.gk.server.enums.RegisterReviewStatus;
import com.gk.server.enums.SysUserType;
import com.gk.server.mapper.system.SysRegisterMapper;
import com.gk.server.model.dto.system.sysRegister.*;
import com.gk.server.model.entity.system.SysRegister;
import com.gk.server.model.entity.system.SysUserInfo;
import com.gk.server.service.intf.system.ISysRegisterService;
import com.gk.server.service.intf.system.ISysUserExtService;
import com.gk.server.service.intf.system.ISysUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户注册表 服务实现类
 *
 * @author Flame
 * @since 2024-05-13 09:30:02
 */

@Service
@Slf4j
public class SysRegisterService extends ServiceImpl<SysRegisterMapper, SysRegister> implements ISysRegisterService {

    @Lazy
    @Resource
    private ISysUserExtService sysUserExtService;
    @Resource
    private ISysUserRoleService sysUserRoleService;
    @Resource
    private ISysUserDeptService sysUserDeptService;
    @Lazy
    @Resource
    private ISysUserInfoService sysUserInfoService;

    /**
     * 通关解密后, 可以拆分成 AES KEY 和 AES IV 两部分, 所以长度是2
     */
    private static final int CARNET_SIZE = 2;

    /**
     * 分页查询
     */
    @Override
    public IPage<SysRegisterPageResp> pageList(SysRegisterPageReq req) {
        IPage<SysRegisterPageResp> resp = baseMapper.pageSysRegister(req.createPage(), req);
        resp.getRecords().forEach(SysRegisterPageResp::handleData);
        return resp;
    }

    /**
     * 注册
     */
    @Override
    public long register(SysRegisterReq req) {
        // 校验注册参数
        checkRegisterParams(req);

        // 通关卡解密
        List<String> carnetList = decryptCarnet(req.getCarnet());
        String aesKey = carnetList.get(0);
        String aesIv = carnetList.get(1);
        // 获取解密的密码
        String decryptPassword = AesUtils.decrypt(req.getPassword(), aesKey, aesIv);
        // 密码规则校验
        if (!ReUtil.isMatch(RegexConstant.PASSWORD, decryptPassword)) {
            throw new SysException(SysStatus.PASSWORD_ILLEGAL);
        }

        // 保存数据
        SysRegister sysRegister = req.toEntity(decryptPassword);
        if (!save(sysRegister)) {
            log.error("Add SysRegister Error: add failed, {}", JsonUtils.toJson(sysRegister));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return sysRegister.getId();
    }

    /**
     * 注册状态查询
     */
    @Override
    public String getRegisterStatus(SysRegisterStatusReq req) {
        List<SysRegister> sysRegisters = lambdaQuery()
                .eq(SysRegister::getEnabled, EnabledType.ENABLE.value)
                .eq(SysRegister::getUsername, req.getUsername())
                .eq(SysRegister::getTelephone, req.getTelephone())
                .eq(SysRegister::getCardNumber, req.getCardNumber())
                .orderByDesc(SysRegister::getId)
                .list();
        // 没有注册信息
        if (sysRegisters.isEmpty()) {
            return SysStatus.CAN_NOT_FIND_REGISTER.getMessage();
        }
        SysRegister sysRegister = sysRegisters.get(0);
        // 注册审核中
        if (RegisterReviewStatus.PENDING.value.equals(sysRegister.getReviewStatus())) {
            return SysStatus.REGISTER_PADDING.getMessage();
        }
        // 注册未通过
        if (RegisterReviewStatus.REJECT.value.equals(sysRegister.getReviewStatus())) {
            return SysStatus.REGISTER_NOT_PASS.getMessage();
        }
        // 注册已通过
        return SysStatus.REGISTER_PASS.getMessage();
    }

    /**
     * 单个查询
     */
    @Override
    public SysRegisterResp findOne(long id) {
        SysRegister sysRegister = lambdaQuery()
                .eq(SysRegister::getEnabled, EnabledType.ENABLE.value)
                .eq(SysRegister::getId, id)
                .one();
        if (sysRegister == null) {
            return null;
        }

        // 查询审核人
        SysUser reviewSysUser = null;
        if (null != sysRegister.getReviewBy()) {
            reviewSysUser = sysUserExtService.lambdaQuery()
                    .eq(SysUser::getId, sysRegister.getReviewBy())
                    .one();
        }

        return sysRegister.toResp(reviewSysUser);
    }

    /**
     * 审核
     */
    @Transactional
    @Override
    public void review(SysRegisterReviewReq req) {
        SysRegister dbEntity = getById(req.getId());
        // 校验参数
        if (!RegisterReviewStatus.PENDING.value.equals(dbEntity.getReviewStatus())) {
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 处理驳回的结果
        if (ReviewResult.REJECT.value.equals(req.getReviewResult())) {
            if (!modifyReviewStatus(req.getId(), RegisterReviewStatus.REJECT, StrUtil.trim(req.getRemark()))) {
                log.error("Reject SysRegister Error: {}", JsonUtils.toJson(req));
                throw new SysException(SysStatus.OPERATE_FAILED);
            }
            return;
        }

        // 处理通过的结果
        if (!modifyReviewStatus(req.getId(), RegisterReviewStatus.PASS, StrUtil.trim(req.getRemark()))) {
            log.error("Pass SysRegister Error in pass: {}", JsonUtils.toJson(req));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户数据
        SysUser sysUser = dbEntity.toSysUser();
        if (!sysUserExtService.save(sysUser)) {
            log.error("Pass SysRegister Error in add SysUser : {}", JsonUtils.toJson(sysUser));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户信息数据
        SysUserInfo sysUserInfo = dbEntity.toSysUserInfo(sysUser.getId());
        if (!sysUserInfoService.save(sysUserInfo)) {
            log.error("Pass SysRegister Error in add SysUserInfo: {}", JsonUtils.toJson(sysUserInfo));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户部门数据
        SysUserDept sysUserDept = dbEntity.toSysUserDept(sysUser.getId());
        if (!sysUserDeptService.save(sysUserDept)) {
            log.error("Pass SysRegister Error in add SysUserDept: {}", JsonUtils.toJson(sysUserDept));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存用户角色数据
        SysUserRole sysUserRole = dbEntity.toSysUserRole(sysUser.getId());
        if (!sysUserRoleService.save(sysUserRole)) {
            log.error("Pass SysRegister Error in add SysUserRole: {}", JsonUtils.toJson(sysUserRole));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // todo 根据用户类型修改不同的用户信息表
        if (SysUserType.isStaff(dbEntity.getUserType())) {
//            StaffInfo staffInfo = dbEntity.toStaffInfo(sysUser.getId());
//            if (!StaffInfoService.save(StaffInfo)) {
//                log.error("Pass SysRegister Error in add StaffInfo: {}", JsonUtils.toJson(staffInfo));
//                throw new SysException(SysStatus.OPERATE_FAILED);
//            }
        }

    }

    /**
     * 校验账号唯一性
     */
    @Override
    public void checkUsernameUnique(String username) {
        boolean existSysRegister = lambdaQuery()
                .eq(SysRegister::getEnabled, EnabledType.ENABLE.value)
                .eq(SysRegister::getReviewStatus, RegisterReviewStatus.PENDING.value)
                .eq(SysRegister::getUsername, username)
                .exists();
        if (existSysRegister) {
            throw new SysException(SysStatus.REGISTER_PADDING);
        }
    }

    /**
     * 校验证件号
     */
    @Override
    public void checkCardNumberUnique(String cardNumber) {
        boolean existSysRegister = lambdaQuery()
                .eq(SysRegister::getEnabled, EnabledType.ENABLE.value)
                .eq(SysRegister::getReviewStatus, RegisterReviewStatus.PENDING.value)
                .eq(SysRegister::getCardNumber, cardNumber)
                .exists();
        if (existSysRegister) {
            throw new SysException(SysStatus.CARD_NUMBER_EXIST);
        }
    }

    /**
     * 更改审核状态
     */
    private boolean modifyReviewStatus(long id, RegisterReviewStatus reviewStatus, String remark) {
        return lambdaUpdate()
                .set(SysRegister::getReviewStatus, reviewStatus.value)
                .set(SysRegister::getReviewBy, LoginUserUtils.getUserId())
                .set(SysRegister::getReviewTime, new Date())
                .set(SysRegister::getRemark, remark)
                .eq(SysRegister::getId, id)
                .update();
    }

    /**
     * 解密通关卡
     *
     * @return AES Key/IV组成的字符串数组
     */
    private List<String> decryptCarnet(String carnet) {
        if (StrUtil.isEmpty(carnet)) {
            log.error("Register Error: carnet is blank");
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        String privateKey = RsaUtils.getPrivateKey();
        String decryptCarnetStr = RsaUtils.decryptBase64(carnet, privateKey);
        List<String> decryptCarnetList = StrUtil.split(decryptCarnetStr, CommonConstant.PIPE);
        if (decryptCarnetList.size() != CARNET_SIZE) {
            log.error("Register Error: carnet split size is not 2");
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return decryptCarnetList;
    }

    /**
     * 校验注册参数
     */
    public void checkRegisterParams(SysRegisterReq req) {
        // 校验账号唯一性
        checkUsernameUnique(req.getUsername());
        sysUserExtService.checkUsernameUnique(req.getUsername());
        // 校验证件号唯一性
        checkCardNumberUnique(req.getCardNumber());
        sysUserInfoService.checkCardNumberUnique(req.getCardNumber(), null);
        // 校验身份证
        if (CardType.ID_CARD.value.equals(req.getCardType()) && !IdcardUtil.isValidCard(req.getCardNumber())) {
            throw new SysException(SysStatus.IDCARD_INVALID);
        }
    }

}

