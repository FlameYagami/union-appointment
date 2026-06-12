package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.common.enums.MenuType;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.model.dto.system.sysMenu.SysMenuAddReq;
import com.gk.framework.model.dto.system.sysMenu.SysMenuEditReq;
import com.gk.framework.model.dto.system.sysMenu.SysMenuResp;
import com.gk.framework.model.dto.system.sysMenu.SysMenuTreeReq;
import com.gk.framework.model.dto.system.sysMenu.SysMenuTreeResp;
import com.gk.framework.model.entity.system.SysMenu;

import java.util.List;

/**
 * 菜单权限表 服务接口类
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenuTreeResp> listTree(SysMenuTreeReq req);

    /**
     * 新增
     *
     * @param req 请求类
     */
    void add(SysMenuAddReq req);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(SysMenuEditReq req);

    /**
     * 删除
     *
     * @param id 主键id
     */
    void delete(long id);

    /**
     * 单个查询
     *
     * @param id 主键id
     * @return 响应类
     */
    SysMenuResp findOne(long id);

    List<TreeLabelResp> listTreeLabel();

}
