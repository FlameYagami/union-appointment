package com.gk.server.service.intf.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.entity.system.SysUser;
import com.gk.server.model.dto.system.sysUser.*;

import java.util.List;

/**
 * 用户表 服务接口类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
public interface ISysUserExtService extends IService<SysUser> {

    /**
     * 分页查询
     *
     * @param req 查询条件
     * @return 分页列表
     */
    IPage<SysUserPageResp> pageList(SysUserPageReq req);

    /**
     * 全表导出
     *
     * @param req 查询条件
     * @return 全表数据
     */
    List<SysUserExportResp> exportList(SysUserExportReq req);

    /**
     * 新增
     *
     * @param req 请求类
     * @return 主键id
     */
    long add(SysUserAddReq req);

    /**
     * 修改
     *
     * @param req 请求类
     */
    void edit(SysUserEditReq req);

    /**
     * 删除
     *
     * @param id 主键id
     */
    void delete(long id);

    SysUserResp findOne(long id);

    void checkUsernameUnique(String username);

}
