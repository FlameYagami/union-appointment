package com.gk.framework.service.impl.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.StringExtUtils;
import com.gk.framework.helper.SysFileHelper;
import com.gk.framework.mapper.system.SysNoticeMapper;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeLabelQuery;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeAddReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeEditReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticePageReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticePageResp;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeResp;
import com.gk.framework.model.dto.system.sysNotice.UserNoticePageReq;
import com.gk.framework.model.dto.system.sysNotice.UserNoticePageResp;
import com.gk.framework.model.dto.system.sysNotice.UserNoticeResp;
import com.gk.framework.model.entity.system.SysDept;
import com.gk.framework.model.entity.system.SysNotice;
import com.gk.framework.manager.RedisCacheManager;
import com.gk.framework.service.intf.system.ISysDeptService;
import com.gk.framework.service.intf.system.ISysNoticeService;
import com.gk.framework.service.intf.system.ISysUserDeptService;
import com.gk.framework.utils.LoginUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 公告通知表 服务实现类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */

@Service
@Slf4j
public class SysNoticeService extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    @Resource
    private ISysDeptService sysDeptService;

    @Resource
    private ISysUserDeptService sysUserDeptService;

    /**
     * 分页查询
     */
    @Override
    public IPage<SysNoticePageResp> pageList(SysNoticePageReq req) {
        long currentDeptId = RedisCacheManager.getInstance().getRedisDeptId();
        // 查询用户当前部门的所有用户
        Set<Long> userIds = new HashSet<>(sysUserDeptService.getUserIdByDeptId(currentDeptId));
        long userId = LoginUserUtils.getUserId();
        userIds.add(userId);
        // 设置查询的参数
        req.lateInitParams(new ArrayList<>(userIds));
        IPage<SysNoticePageResp> resp = baseMapper.pageSysNotice(req.createPage(), req);
        resp.getRecords().forEach(it -> it.lateInit(userId));
        return resp;
    }

    /**
     * 新增
     */
    @Override
    public long add(SysNoticeAddReq req, boolean hasAllDept) {
        long currentDeptId = RedisCacheManager.getInstance().getRedisDeptId();
        // 校验父级部门id合法性
        checkDeptIdLegal(req.getDeptId(), currentDeptId, hasAllDept);

        SysNotice sysNotice = req.toEntity();
        if (!save(sysNotice)) {
            log.error("Add SysNotice Error: {}", JsonUtils.toJson(sysNotice));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存图片
        if (!SysFileHelper.getInstance().updateBatchEnabled(req.getImageFids(), EnabledType.ENABLE)) {
            log.error("Enable image error in add SysNotice, fileIds({})", JsonUtils.toJson(req.getImageFids()));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        return sysNotice.getId();
    }

    /**
     * 修改
     */
    @Override
    public void edit(SysNoticeEditReq req, boolean hasAllDept) {
        long currentDeptId = RedisCacheManager.getInstance().getRedisDeptId();
        // 校验父级部门id合法性
        checkDeptIdLegal(req.getDeptId(), currentDeptId, hasAllDept);
        // 校验操作权限
        checkOperatePermission(List.of(req.getId()));

        SysNotice sysNotice = req.toEntity();
        if (!updateById(sysNotice)) {
            log.error("Edit SysNotice Error: {}", JsonUtils.toJson(sysNotice));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 保存图片
        SysNotice dbEntity = getById(req.getId());
        List<Long> dbImageIds = StringExtUtils.splitComma(dbEntity.getImageFids());
        if (!SysFileHelper.getInstance().updateBatchCompare(req.getImageFids(), dbImageIds)) {
            log.error("Update image error in edit SysNotice, fileIds({})", JsonUtils.toJson(req.getImageFids()));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

    }

    /**
     * 删除
     */
    @Override
    public void delete(List<Long> idList) {
        // 校验操作权限
        checkOperatePermission(idList);

        boolean result = lambdaUpdate()
                .in(SysNotice::getId, idList)
                .set(SysNotice::getEnabled, EnabledType.DISABLE.value)
                .update();
        if (!result) {
            log.error("Delete SysNotice Error: {}", idList);
            throw new SysException(SysStatus.OPERATE_FAILED);
        }

        // 删除图片
        List<Long> deleteImageIds = lambdaQuery()
                .select(SysNotice::getImageFids)
                .in(SysNotice::getId, idList)
                .list().stream()
                .flatMap(it -> StringExtUtils.splitComma(it.getImageFids()).stream())
                .collect(Collectors.toList());
        if (!SysFileHelper.getInstance().updateBatchCompare(new ArrayList<>(), deleteImageIds)) {
            log.error("Delete image error in delete SysNotice, fileIds({})", JsonUtils.toJson(deleteImageIds));
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 单个查询
     */
    @Override
    public SysNoticeResp findOne(long id) {
        SysNotice sysNotice = lambdaQuery()
                .eq(SysNotice::getId, id)
                .eq(SysNotice::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (sysNotice == null) {
            return null;
        }
        return sysNotice.toResp();
    }

    /**
     * 全部门通知类型获取部门(直接获取顶级部门)
     */
    @Override
    public List<TreeLabelResp> listMgmtAllDeptLabel() {
        SysDept sysDept = sysDeptService.lambdaQuery()
                .eq(SysDept::getDeptLevel, CommonConstant.TOP_DEPT_LEVEL)
                .one();
        return List.of(sysDept.toTreeLabelResp());
    }

    /**
     * 子部门通知类型获取部门
     * <p>
     * 是否可以查询所有部门
     * 通知类型的设计模式中, 如果用户拥有全部门通知权限, 则调用此接口直接获取全部部门的树状结构数据
     * 否则根据数据权限获取用户所在部门以及以下的所有子部门的树状结构数据
     */
    @Override
    public List<TreeLabelResp> listMgmtChildDeptLabel(boolean hasAllDept) {
        return hasAllDept ? sysDeptService.listTreeLabelByDeptId(CommonConstant.TOP_ID)
                : sysDeptService.listTreeLabel(new SysDeptTreeLabelQuery());
    }

    /**
     * 修改公告通知状态
     */
    @Override
    public void changeDataStatus(DataStatusChangeReq req) {
        boolean result = lambdaUpdate()
                .set(SysNotice::getDataStatus, req.getDataStatus())
                .eq(SysNotice::getId, req.getId())
                .update();
        if (!result) {
            log.error("Change DataStatus Error in SysNotice: id({})", req.getId());
            throw new SysException(SysStatus.OPERATE_FAILED);
        }
    }

    /**
     * 校验部门id合法性(新增或是修改)
     */
    private void checkDeptIdLegal(long deptId, long currentDeptId, boolean hasAllDept) {
        // 部门不存在
        SysDept sysDept = sysDeptService.getById(deptId);
        if (null == sysDept) {
            throw new SysException(SysStatus.DEPT_PARENT_NOT_EXITS);
        }

        // 有全部门通知权限直接放行
        if (hasAllDept) {
            return;
        }

        // 没有全部门通知权限则需要判定当前部门是否处于用户的部门之下
        List<Long> childDeptIds = sysDeptService.getEnableChildDeptIds(currentDeptId, true);
        if (!childDeptIds.contains(deptId)) {
            throw new SysException(SysStatus.DEPT_OUT_OF_RANGE);
        }
    }

    /**
     * 校验操作权限
     */
    private void checkOperatePermission(List<Long> ids) {
        long userId = LoginUserUtils.getUserId();
        // 超管直接放行
        if (CommonConstant.TOP_ID == userId) {
            return;
        }

        // 非超管校验
        long selfCreateCount = lambdaQuery()
                .in(SysNotice::getId, ids)
                .eq(SysNotice::getCreateBy, userId)
                .count();
        if (ids.size() != selfCreateCount) {
            throw new SysException(SysStatus.DATA_OUT_OF_OPERATE_RANGE);
        }
    }

    /**
     * 用户公告通知分页查询
     */
    @Override
    public IPage<UserNoticePageResp> pageUserList(UserNoticePageReq req) {
        long currentDeptId = RedisCacheManager.getInstance().getRedisDeptId();
        // 查询用户当前部门的所有父级部门id集合
        List<Long> parentDeptIds = sysDeptService.getParentDeptIds(currentDeptId, true);
        // 设置查询的参数
        req.lateInitParams(currentDeptId, parentDeptIds);
        IPage<UserNoticePageResp> resp = baseMapper.pageSysUserNotice(req.createPage(), req);
        resp.getRecords().forEach(UserNoticePageResp::handleData);
        return resp;
    }

    /**
     * 用户公告通知查询详情
     */
    @Override
    public UserNoticeResp findUserNotice(long id) {
        SysNotice sysNotice = lambdaQuery()
                .eq(SysNotice::getId, id)
                .eq(SysNotice::getEnabled, EnabledType.ENABLE.value)
                .one();
        if (sysNotice == null) {
            return null;
        }
        return sysNotice.toUserNoticeResp();
    }

}

