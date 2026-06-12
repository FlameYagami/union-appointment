package com.gk.server.service.intf.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.dto.system.sysFile.SysFileResp;
import com.gk.server.model.dto.system.sysUserInfo.SimpleSysUserInfoResp;
import com.gk.server.model.dto.system.sysUserInfo.SysUserInfoEditReq;
import com.gk.server.model.dto.system.sysUserInfo.SysUserInfoResp;
import com.gk.server.model.entity.system.SysUserInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户信息表 服务接口类
 *
 * @author Flame
 * @since 2022-12-16 17:46:27
 */
public interface ISysUserInfoService extends IService<SysUserInfo> {

    SimpleSysUserInfoResp getSimpleSysUserInfo();

    SysUserInfoResp getSysUserInfo(Long userId);

    void editSysUserInfo(SysUserInfoEditReq req);

    SysFileResp uploadSysAvatar(MultipartFile file, String uploadDir, String bizType);

    void deleteByUserId(long userId);

    SysUserInfo findByUserId(long userId);

    void checkCardNumberUnique(String idcard, Long userInfoId);
}
