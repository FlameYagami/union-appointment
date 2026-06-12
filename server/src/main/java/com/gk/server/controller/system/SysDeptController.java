package com.gk.server.controller.system;

import com.gk.common.enums.OperateType;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.annotation.OperateLog;
import com.gk.framework.model.dto.system.sysDept.SysDeptAddReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptEditReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptResp;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeLabelQuery;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeReq;
import com.gk.framework.model.dto.system.sysDept.SysDeptTreeResp;
import com.gk.framework.service.intf.system.ISysDeptService;
import com.gk.common.model.others.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 部门表 控制类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */

@RestController
@RequestMapping("/api/sys-dept")
@Api(tags = "部门管理")
@Validated
public class SysDeptController {

    @Resource
    private ISysDeptService sysDeptService;

    @GetMapping("/tree")
    @PreAuthorize("@ss.hasPermission('server:sys-dept:list')")
    @ApiOperation(value = "部门树形结构查询", notes = "server:sys-dept:list")
    public ApiResult<List<SysDeptTreeResp>> listTree(SysDeptTreeReq req) {
        return ApiResult.ok(sysDeptService.listTree(req));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dept:add')")
    @ApiOperation(value = "新增部门", notes = "server:sys-dept:add")
    @OperateLog(title = "新增部门", operateType = OperateType.ADD)
    public ApiResult<Long> add(@RequestBody @Valid SysDeptAddReq req) {
        long id = sysDeptService.add(req);
        return ApiResult.ok(id);
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dept:edit')")
    @ApiOperation(value = "修改部门", notes = "server:sys-dept:edit")
    @OperateLog(title = "修改部门", operateType = OperateType.UPDATE)
    public ApiResult<?> edit(@RequestBody @Valid SysDeptEditReq req) {
        sysDeptService.edit(req);
        return ApiResult.ok();
    }

    @DeleteMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dept:delete')")
    @ApiOperation(value = "删除部门", notes = "server:sys-dept:delete")
    @OperateLog(title = "删除部门", operateType = OperateType.DELETE)
    public ApiResult<?> delete(@RequestBody @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        sysDeptService.delete(id);
        return ApiResult.ok();
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:sys-dept:info')")
    @ApiOperation(value = "部门查询详情", notes = "server:sys-dept:info")
    @ApiImplicitParam(name = "id", value = "部门id", required = true, dataTypeClass = Long.class, example = "1000000000000000001")
    public ApiResult<SysDeptResp> findOne(@RequestParam("id") @Min(value = 1000000000000000001L, message = "id不合法") long id) {
        return ApiResult.ok(sysDeptService.findOne(id));
    }

    @GetMapping("/tree-label")
    @PreAuthorize("@ss.hasPermission('server:sys-dept:list')")
    @ApiOperation(value = "部门树形结构标签查询", notes = "server:sys-dept:list")
    public ApiResult<List<TreeLabelResp>> listTreeLabel() {
        return ApiResult.ok(sysDeptService.listTreeLabel(new SysDeptTreeLabelQuery()));
    }

}
