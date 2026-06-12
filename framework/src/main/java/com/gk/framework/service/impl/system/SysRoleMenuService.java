package com.gk.framework.service.impl.system;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.enums.EnabledType;
import com.gk.common.utils.EntityUtils;
import com.gk.framework.mapper.system.SysRoleMenuMapper;
import com.gk.framework.model.entity.system.SysRoleMenu;
import com.gk.framework.service.intf.system.ISysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色菜单关系表 服务实现类
 *
 * @author Flame
 * @since 2022-12-29 09:43:13
 */

@Service
@Slf4j
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public boolean modify(long roleId, List<SysRoleMenu> sysRoleMenus){
        // 查询主表id关联的所有数据
        List<SysRoleMenu> dbEntities = lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId)
                .list();
        // 获取最终需要保存的数据
        List<SysRoleMenu> saveEntities = EntityUtils.generateRelEntity(sysRoleMenus, dbEntities, SysRoleMenu::getMenuId);
        if (CollUtil.isEmpty(saveEntities)) {
            return true;
        }
        // 保存数据
        return saveOrUpdateBatch(saveEntities);
    }

    /**
     * 通过菜单id删除数据
     */
    @Override
    public void deleteByMenuId(long menuId) {
        boolean existRoleUse = lambdaQuery()
                .eq(SysRoleMenu::getEnabled, EnabledType.ENABLE.value)
                .eq(SysRoleMenu::getMenuId, menuId)
                .exists();
        if (!existRoleUse) {
            return;
        }
        boolean result = lambdaUpdate()
                .set(SysRoleMenu::getEnabled, EnabledType.DISABLE.value)
                .eq(SysRoleMenu::getEnabled, EnabledType.ENABLE.value)
                .eq(SysRoleMenu::getMenuId, menuId)
                .update();
        if (!result) {
            log.error("Delete menuId({}) error", menuId);
        }
    }

}

