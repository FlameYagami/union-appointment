package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigAddReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigEditReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigPageReq;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigPageResp;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigResp;
import com.gk.framework.model.entity.system.SysOpenConfig;

import java.util.List;

/**
 * 第三方对接配置表 服务接口类
 *
 * @author Flame
 * @since 2023-03-13 16:26:48
 */
public interface ISysOpenConfigService extends IService<SysOpenConfig> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysOpenConfigPageResp> pageList(SysOpenConfigPageReq req);

    /**
     * 新增
     *
     * @param req 请求类
     * @return 主键id
     */
    long add(SysOpenConfigAddReq req);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(SysOpenConfigEditReq req);

    /**
     * 删除
     *
     * @param idList 主键id集合
     */
    void delete(List<Long> idList);

    /**
     * 单个查询
     *
     * @param id 主键id
     * @return 响应类
     */
    SysOpenConfigResp findOne(long id);

    void changeDataStatus(DataStatusChangeReq req);

    void generateKey(long id);

    List<TreeLabelResp> listMgmtDeptTreeLabel();

    List<LabelResp> listMgmtRoleLabel();

    SysOpenConfig findByOpenId(String openId);

}
