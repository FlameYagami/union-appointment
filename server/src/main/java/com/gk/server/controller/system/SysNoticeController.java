package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.helper.SysFileHelper;
import com.gk.framework.model.dto.system.base.DataStatusChangeReq;
import com.gk.framework.model.dto.system.sysFile.SysFileResp;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeAddReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeEditReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticePageReq;
import com.gk.framework.model.dto.system.sysNotice.SysNoticePageResp;
import com.gk.framework.model.dto.system.sysNotice.SysNoticeResp;
import com.gk.framework.model.dto.system.sysNotice.UserNoticePageReq;
import com.gk.framework.model.dto.system.sysNotice.UserNoticePageResp;
import com.gk.framework.model.dto.system.sysNotice.UserNoticeResp;
import com.gk.framework.service.intf.system.ISysNoticeService;
import com.gk.security.service.intf.IAuthPermissionService;
import com.gk.common.model.others.ApiResult;
import com.gk.common.model.others.PageResult;
import com.gk.server.constant.PathConstant;
import com.gk.server.enums.BizType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 公告通知表 控制类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */

@RestController
@RequestMapping("/api/sys-notice")
@Api(tags = "公告通知管理")
@Validated
public class SysNoticeController {

    @Resource
    private ISysNoticeService sysNoticeService;

    @Resource
    private IAuthPermissionService authPermissionService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('server:sys-notice:list')")
    @ApiOperation(value = "公告通知分页查询", notes = "server:sys-notice:list")
    public PageResult<SysNoticePageResp> pageList(SysNoticePageReq req) {
        return PageResult.ok(sysNoticeService.pageList(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:sys-notice:add')")
    @ApiOperation(value = "新增公告通知", notes = "server:sys-notice:add")
    @OperateLog(title = "新增公告通知", operateType = OperateType.ADD)
    public ApiResult<Long> add(@RequestBody @Valid SysNoticeAddReq req) {
        // 判断当前的用户是否拥有调取全部部门的权限
        boolean hasAllDept = authPermissionService.hasPermission("server:sys-notice:all-dept");
        long id = sysNoticeService.add(req, hasAllDept);
        return ApiResult.ok(id);
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:sys-notice:edit')")
    @ApiOperation(value = "修改公告通知", notes = "server:sys-notice:edit")
    @OperateLog(title = "修改公告通知", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid SysNoticeEditReq req) {
        // 判断当前的用户是否拥有调取全部部门的权限
        boolean hasAllDept = authPermissionService.hasPermission("server:sys-notice:all-dept");
        sysNoticeService.edit(req, hasAllDept);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:sys-notice:delete')")
    @ApiOperation(value = "删除公告通知", notes = "server:sys-notice:delete")
    @OperateLog(title = "删除公告通知", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @NotEmpty(message = "公告通知id缺失") List<@Min(value = 1000000000000000001L, message = "id不合法") Long> idList) {
        sysNoticeService.delete(idList);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:sys-notice:info')")
    @ApiOperation(value = "公告通知查询详情", notes = "server:sys-notice:info")
    @ApiImplicitParam(name = "id", value = "公告通知详情id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysNoticeResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysNoticeService.findOne(id));
    }

    @GetMapping("/mgmt/dept/all-label")
    @PreAuthorize("@ss.hasPermission('server:sys-notice:all-dept')")
    @ApiOperation(value = "全部门通知类型获取部门", notes = "server:sys-notice:all-dept")
    public ApiResult<List<TreeLabelResp>> listMgmtAllDeptLabel() {
        return ApiResult.ok(sysNoticeService.listMgmtAllDeptLabel());
    }

    @GetMapping("/mgmt/dept/child-label")
    @PreAuthorize("@ss.hasPermission('server:sys-notice:child-dept')")
    @ApiOperation(value = "子部门通知类型获取部门", notes = "server:sys-notice:child-dept")
    public ApiResult<List<TreeLabelResp>> listMgmtChildDeptLabel() {
        // 判断当前的用户是否拥有调取全部部门的权限
        boolean hasAllDept = authPermissionService.hasPermission("server:sys-notice:all-dept");
        return ApiResult.ok(sysNoticeService.listMgmtChildDeptLabel(hasAllDept));
    }

    @PostMapping("/status/change")
    @PreAuthorize("@ss.hasPermission('server:sys-notice:edit')")
    @ApiOperation(value = "修改公告通知状态", notes = "server:sys-notice:edit")
    @OperateLog(title = "修改公告通知状态", operateType = OperateType.UPDATE)
    public ApiResult<?> changeDataStatus(@RequestBody @Valid DataStatusChangeReq req) {
        sysNoticeService.changeDataStatus(req);
        return ApiResult.ok();
    }

    @PostMapping("/upload/notice")
    @PreAuthorize("@ss.hasAnyPermissions('server:sys-notice:add', 'server:sys-notice:edit')")
    @ApiOperation(value = "公告通知图片上传", notes = "server:sys-notice:upload")
    public ApiResult<SysFileResp> uploadNotice(@ApiParam(value = "file") @RequestPart("file") MultipartFile file) {
        return ApiResult.ok(SysFileHelper.getInstance().upload(file, PathConstant.NOTICE, BizType.NOTICE.value));
    }

    @GetMapping("/user/page")
    @PreAuthorize("@ss.hasPermission('server:user-notice:list')")
    @ApiOperation(value = "用户公告通知分页查询", notes = "server:user-notice:list")
    public PageResult<UserNoticePageResp> pageList(UserNoticePageReq req) {
        return PageResult.ok(sysNoticeService.pageUserList(req));
    }

    @GetMapping("/user")
    @PreAuthorize("@ss.hasPermission('server:user-notice:info')")
    @ApiOperation(value = "用户公告通知查询详情", notes = "server:user-notice:info")
    @ApiImplicitParam(name = "id", value = "公告通知id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<UserNoticeResp> findUserNotice(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysNoticeService.findUserNotice(id));
    }

}
