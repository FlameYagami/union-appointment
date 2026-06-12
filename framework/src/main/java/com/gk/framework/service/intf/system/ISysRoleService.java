package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.model.bo.role.RoleMenu;
import com.gk.framework.model.bo.role.RolePermission;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysRole.*;
import com.gk.framework.model.entity.system.SysRole;

import java.util.List;

/**
 * 角色表 服务接口类
 *
 * @author Flame
 * @since 2022-12-29 09:34:30
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysRolePageResp> pageList(SysRolePageReq req);

    /**
     * 新增
     *
     * @param req 请求类
     * @return 主键id
     */
    long add(SysRoleAddReq req);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(SysRoleEditReq req);

    /**
     * 删除
     *
     * @param idList 主键id集合
     */
    void delete(long idList);

    /**
     * 单个查询
     *
     * @param id 主键id
     * @return 响应类
     */
    SysRoleResp findOne(long id);

    List<LabelResp> listMgmtRoleLabel();

    List<TreeLabelResp> listMgmtMenuTreeLabel();

    void changeDataStatus(DataStatusChangeReq req);

    List<RolePermission> listRolePermission();

    List<RoleMenu> listRoleMenu();

}
