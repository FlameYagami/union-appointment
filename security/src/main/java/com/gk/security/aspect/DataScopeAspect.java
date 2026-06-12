package com.gk.security.aspect;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.DataScopeType;
import com.gk.common.model.dto.DataScopeReq;
import com.gk.framework.annotation.DataScope;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.manager.RoleCacheManager;
import com.gk.framework.model.entity.system.SysRole;
import com.gk.framework.utils.LoginUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据过滤
 * {@link com.gk.common.model.dto.DataScopeReq}
 *
 * @author Flame
 * @date 2023-02-02 16:58
 **/

@Component
@Aspect
@Slf4j
public class DataScopeAspect {

    @Value("${spring.datasource.dynamic.primary}")
    private String dataSourceType;

    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint point, DataScope dataScope) {
        handleDataScope(point, dataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope dataScope) {
        // 获取切面参数
        Object[] params = joinPoint.getArgs();
        if (null == params[0] || !(params[0] instanceof DataScopeReq)) {
            return;
        }

        DataScopeReq req = (DataScopeReq) params[0];
        String sqlString = buildDataScopeSql(req, dataScope);
        // 如果上面不包含任何语句, 则不设置切面sql
        if (StrUtil.isEmpty(sqlString)) {
            return;
        }

        // 拼接权限sql前先清空params.dataScope参数防止注入
        req.setDataScopeSql(CommonConstant.EMPTY);
        req.setDataScopeSql(StrUtil.format(" and ({})", sqlString));
    }

    /**
     * 构架数据权限Sql
     */
    public String buildDataScopeSql(DataScopeReq req, DataScope ds) {
        // 获取当前的用户
        long userId = LoginUserUtils.getUserId();
        // 查询当前登录的角色数据权限
        long roleId = RedisCacheManager.getInstance().getRedisRoleId();
        SysRole sysRole = RoleCacheManager.getInstance().findByRoleId(roleId);
        String dataScope = sysRole.getDataScope();

        String businessTableAlias = ds.bizTableAlias();
        String deptIdAlias = ds.deptIdAlias();
        String userIdAlias = ds.userIdAlias();
        Long reqDeptId = req.getDeptId();
        Boolean reqSelDept = req.getSelfDept();

        // 情况一, 如果是 ALL 依照查询条件生成Sql
        if (DataScopeType.ALL.value.equals(dataScope)) {
            // 级联数据构建
            if (null == reqSelDept || !reqSelDept) {
                return null == reqDeptId || CommonConstant.TOP_ID == reqDeptId // 判断是否为非顶级部门的查询
                        ? CommonConstant.EMPTY // 顶级部门直接返回空字符串
                        : buildDeptAndChildSql(reqDeptId, businessTableAlias, deptIdAlias); // 非顶级部门使用请求中的部门id
            }
            // 本级数据构建
            long topDeptId = null == reqDeptId ? CommonConstant.TOP_ID : reqDeptId;
            return buildDeptSql(topDeptId, businessTableAlias, deptIdAlias);
        }

        long deptId = RedisCacheManager.getInstance().getRedisDeptId();

        // 情况二, 如果是 DEPT 查询当前角色所处部门的数据
        if (DataScopeType.DEPT.value.equals(dataScope)) {
            return StrUtil.format("{}.{} = {}", businessTableAlias, deptIdAlias, deptId);
        }

        // 情况三, 如果是 DEPT_AND_CHILD 查询当前角色所处部门以及子部门的数据
        if (DataScopeType.DEPT_AND_CHILD.value.equals(dataScope)) {
            // 级联数据构建
            if (null == reqSelDept || !reqSelDept) {
                return null == reqDeptId || deptId == reqDeptId // 判断是否为非顶级部门的查询
                        ? buildDeptAndChildSql(deptId, businessTableAlias, deptIdAlias) // 顶级部门直接用用户所在部门id
                        : buildDeptAndChildSql(reqDeptId, businessTableAlias, deptIdAlias); // 非顶级部门使用请求中的部门id
            }
            // 本级数据构建
            long topDeptId = null == reqDeptId ? deptId : reqDeptId;
            return buildDeptSql(topDeptId, businessTableAlias, deptIdAlias);
        }

        // 情况四, 如果是 SELF 查询当前角色自己的数据
        if (DataScopeType.SELF.value.equals(dataScope)) {
            return StrUtil.format("{}.{} = {}", businessTableAlias, userIdAlias, userId);
        }

        return CommonConstant.EMPTY;
    }

    /**
     * 创建级联查询的sql
     */
    private String buildDeptAndChildSql(long deptId, String businessTableAlias, String deptIdAlias) {
        String format = "";
        if (DbType.ORACLE.getDb().equals(dataSourceType)) {
            format = "SELECT id FROM sys_dept sd START WITH sd.parent_id = {} CONNECT BY PRIOR sd.id = sd.parent_id";
        } else if (DbType.MYSQL.getDb().equals(dataSourceType)) {
            format = "WITH RECURSIVE temp_sys_dept AS (SELECT psd.id, psd.parent_id FROM sys_dept psd WHERE psd.id = {}" +
                " UNION ALL SELECT csd.id, csd.parent_id FROM sys_dept csd, temp_sys_dept tsd WHERE tsd.id = csd.parent_id)" +
                " SELECT id FROM temp_sys_dept";
        } else {
            return format;
        }
        String deptIds = StrUtil.format(format, deptId);
        return StrUtil.format("{}.{} in ({})", businessTableAlias, deptIdAlias, deptIds);
    }

    /**
     * 创建本级查询的sql
     */
    private String buildDeptSql(long deptId, String businessTableAlias, String deptIdAlias) {
        return StrUtil.format("{}.{} = {}", businessTableAlias, deptIdAlias, deptId);
    }

}
