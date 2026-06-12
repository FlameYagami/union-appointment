package com.gk.framework.service.impl.system;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.mapper.system.SysOpenConfigMapper;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeLabelQuery;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigAddReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigEditReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigPageReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigPageResp;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigResp;
import com.gk.framework.model.entity.system.SysOpenConfig;
import com.gk.framework.service.intf.system.ISysDeptService;
import com.gk.framework.service.intf.system.ISysOpenConfigService;
import com.gk.framework.service.intf.system.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 第三方对接配置表 服务实现类
 *
 * @author Flame
 * @since 2023-03-13 16:26:48
 */

@Service
@Slf4j
public class SysOpenConfigService extends ServiceImpl<SysOpenConfigMapper, SysOpenConfig> implements ISysOpenConfigService {

    @Resource
    private ISysDeptService sysDeptService;

    @Resource
    private ISysRoleService sysRoleService;

    /**
     * 分页查询
     */
    @Override
    public IPage<SysOpenConfigPageResp> pageList(SysOpenConfigPageReq req) {
        return baseMapper.pageSysOpenConfig(req.createPage(), req);
    }

    /**
     * 新增
     */
    @Override
    public long add(SysOpenConfigAddReq req) {
        // 生成秘钥
        Triple<String, String, String> key = generateKey();
        SysOpenConfig sysOpenConfig = req.toEntity(key);
        if (!save(sysOpenConfig)) {
            log.error("Add SysOpenConfig Error: {}", JsonUtils.toJson(sysOpenConfig));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
        return sysOpenConfig.getId();
    }

    /**
     * 修改
     */
    @Override
    public void edit(SysOpenConfigEditReq req) {
        SysOpenConfig sysOpenConfig = req.toEntity();
        if (!updateById(sysOpenConfig)) {
            log.error("Edit SysOpenConfig Error: {}", JsonUtils.toJson(sysOpenConfig));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 删除
     */
    @Override
    public void delete(List<Long> idList) {
        boolean result = lambdaUpdate()
                .in(SysOpenConfig::getId, idList)
                .set(SysOpenConfig::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete SysOpenConfig Error: {}", idList);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 单个查询
     */
    @Override
    public SysOpenConfigResp findOne(long id) {
        SysOpenConfig sysOpenConfig = lambdaQuery()
                .eq(SysOpenConfig::getId, id)
                .eq(SysOpenConfig::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (sysOpenConfig == null) {
            return null;
        }
        return sysOpenConfig.toResp();
    }

    /**
     * 修改秘钥状态
     */
    @Override
    public void changeDataStatus(DataStatusChangeReq req) {
        boolean result = lambdaUpdate()
                .set(SysOpenConfig::getDataStatus, req.getDataStatus())
                .eq(SysOpenConfig::getId, req.getId())
                .update();
        if (!result) {
            log.error("Change DataStatus Error in SysOpenConfig: id({})", req.getId());
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 生成秘钥
     */
    @Override
    public void generateKey(long id) {
        Triple<String, String, String> key = generateKey();
        boolean result = lambdaUpdate()
                .set(SysOpenConfig::getOpenId, key.getLeft())
                .set(SysOpenConfig::getAesKey, key.getMiddle())
                .set(SysOpenConfig::getAesIv, key.getRight())
                .eq(SysOpenConfig::getId, id)
                .update();
        if (!result) {
            log.error("Change DataStatus Error: id({})", id);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 部门树形结构标签查询(用于厂商设置中的可选部门勾选)
     */
    @Override
    public List<TreeLabelResp> listMgmtDeptTreeLabel() {
        return sysDeptService.listTreeLabel(new SysDeptTreeLabelQuery());
    }

    /**
     * 角色标签查询(用于厂商设置中的可选角色勾选)
     */
    @Override
    public List<LabelResp> listMgmtRoleLabel() {
        return sysRoleService.listMgmtRoleLabel();
    }

    /**
     * 生成秘钥数据模型
     *
     * @return Triple<String, String, String> 分别对应 openId, aesKey, aesIv
     */
    private Triple<String, String, String> generateKey() {
        String uuid = UuidUtil.getTimeBasedUuid().toString();
        String openId = SecureUtil.md5(uuid).substring(20, 32);
        String aesKey = "open" + uuid.substring(0, 8) + uuid.substring(19, 23);
        String aesIv = SecureUtil.md5(aesKey).substring(16, 32);
        return Triple.of(openId, aesKey, aesIv);
    }

    /**
     * 通过认证id获取第三方对接配置
     */
    @Override
    public SysOpenConfig findByOpenId(String openId) {
        return lambdaQuery()
                .eq(SysOpenConfig::getEnabled, EnabledType.ENABLE.value)
                .eq(SysOpenConfig::getOpenId, openId)
                .one();
    }

}

