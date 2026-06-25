package com.gk.wxstaff.controller.system;

import com.gk.common.model.others.ApiResult;
import com.gk.framework.model.dto.system.CachedDictData;
import com.gk.framework.service.intf.system.IRedisCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 字典数据表 控制类
 *
 * @author Flame
 * @since 2025-07-03 14:11:51
 */

@RestController
@RequestMapping("/api/wx-staff/sys-dict-data")
@Api(tags = "Worker端-字典项管理")
@Validated
public class WxSysDictDataController {

    @Resource
    private IRedisCacheService redisCacheService;

    @GetMapping("/list-dict")
    @ApiOperation(value = "根据字典编码查询所有对应的字典项")
    @ApiImplicitParam(name = "dictCode", value = "字典编码", required = true, dataTypeClass = String.class, example = "dict_code")
    public ApiResult<List<CachedDictData>> listCacheDictData(@RequestParam("dictCode") @NotBlank(message = "字典编码缺失") String dictCode) {
        return ApiResult.ok(redisCacheService.getDictData(dictCode));
    }

}
