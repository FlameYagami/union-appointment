package com.gk.framework.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk.framework.model.entity.system.SysUserDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户部门关系表 Mapper 接口
 *
 * @author Flame
 * @since 2023-01-31 10:22:38
 */

public interface SysUserDeptMapper extends BaseMapper<SysUserDept> {

    List<Long> getEnableDeptIdByUserId(@Param("userId") long userId);

}
