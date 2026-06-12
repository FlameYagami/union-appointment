package com.gk.framework.model.dto.system.sysDept;

import com.gk.common.model.dto.DataScopeReq;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 部门 树形结构请求类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */

@Data
@Accessors(chain = true)
public class SysDeptTreeLabelQuery extends DataScopeReq {

}
