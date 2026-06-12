package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.framework.model.entity.system.SysUserDept;

import java.util.List;

/**
 * 用户部门关系表 服务接口类
 *
 * @author Flame
 * @since 2023-01-31 10:22:38
 */
public interface ISysUserDeptService extends IService<SysUserDept> {

    List<Long> getDeptIds(long userId);

    List<Long> getEnableDeptIds(long userId);

    boolean modify(long userId, List<SysUserDept> sysUserDepts);

    void delete(long userId);

    List<Long> getUserIdByDeptId(long deptId);

    boolean hasUseDept(long deptId);

}
