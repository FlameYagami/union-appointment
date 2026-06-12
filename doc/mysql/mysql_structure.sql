-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `config_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '配置名称',
  `config_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '配置键',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '配置值',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '备注',
  `data_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '配置状态(1:正常, 0:停用, 默认:1)',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='配置表';

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `parent_id` bigint unsigned NOT NULL COMMENT '父级id',
  `dept_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '部门名称',
  `dept_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '部门编码',
  `dept_level` int NOT NULL COMMENT '部门层级',
  `dept_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '部门类型',
  `dept_order` int NOT NULL DEFAULT '1000' COMMENT '部门排序',
  `data_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '数据状态(1:正常, 0:停用)',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='部门表';

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '字典名称',
  `dict_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '字典编码',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '字典备注',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1773267182920986626 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='字典表';

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '字典编码',
  `dict_label` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '字典标签',
  `dict_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '字典值',
  `color_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT 'default' COMMENT '颜色类型',
  `dict_css` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '字典样式',
  `dict_order` int NOT NULL DEFAULT '1' COMMENT '字典排序',
  `data_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '字典数据状态(1:正常, 0:停用, 默认:1)',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1656210454849085444 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='字典数据表';

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `biz_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '业务类型;(1:用户头像)',
  `filename` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '文件名',
  `file_path` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '文件路径',
  `file_size` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '文件大小',
  `deleted` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '0' COMMENT '是否删除(1:是 0:否; 默认:0)',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态;(1:是, 0:否)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='文件表';

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '账号',
  `dept_id` bigint unsigned NOT NULL COMMENT '部门表id',
  `login_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '' COMMENT '登录ip',
  `browser` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '' COMMENT '浏览器',
  `os` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '' COMMENT '操作系统',
  `result_msg` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '返回消息',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '1' COMMENT '登录状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1776514850621730818 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='登录日志表';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `parent_id` bigint unsigned NOT NULL COMMENT '父级id',
  `menu_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '权限名称',
  `permission_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '权限标识',
  `menu_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '权限类型',
  `menu_path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '菜单地址',
  `menu_component` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '菜单组件',
  `menu_icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '菜单图标',
  `menu_cached` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '菜单缓存',
  `menu_show` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '菜单显示',
  `menu_open` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '1' COMMENT '菜单开放(1:是, 0:否, 默认:1)',
  `inner_link` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '1' COMMENT '是否为内部链接(1:是, 0:否, 默认:1)',
  `active_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '' COMMENT '激活名称',
  `menu_order` int NOT NULL DEFAULT 1000 COMMENT '菜单排序',
  `data_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '菜单状态(1:正常, 0:停用, 默认:1)',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='菜单权限表';

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `notice_title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '通知标题',
  `notice_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '1' COMMENT '通知类型;(1:公告, 2:通知, 默认:1)',
  `dept_id` bigint unsigned NOT NULL COMMENT '部门表id',
  `notice_scope` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '通知范围;(1:全平台数据, 2:部门及以下数据, 3:本部门数据)',
  `notice_start_time` datetime NOT NULL COMMENT '通知开始时间',
  `notice_end_time` datetime NOT NULL COMMENT '通知结束时间',
  `notice_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '通知内容',
  `notice_top` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '0' COMMENT '消息置顶;(1:是, 0:否, 默认:0)',
  `image_fids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '图片ids',
  `data_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '通知状态;(1:正常, 0:停用, 默认:1)',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态;(1:是, 0:否, 默认:1)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='公告通知表';

-- ----------------------------
-- Table structure for sys_open_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_open_config`;
CREATE TABLE `sys_open_config` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `company_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '对接厂商名称',
  `company_desc` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '对接厂商描述',
  `dept_id` bigint unsigned NOT NULL COMMENT '部门表id',
  `role_id` bigint unsigned NOT NULL COMMENT '角色表id',
  `open_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '开放式认证系统id;由系统生成',
  `aes_key` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '秘钥;由系统生成',
  `aes_iv` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '初始化向量;由系统生成',
  `data_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '秘钥状态;(1:正常, 0:停用, 默认:1)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态;(1:是, 0:否, 默认:1)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='第三方对接配置表';

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint unsigned NOT NULL COMMENT '部门表id',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '操作标题',
  `request_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '操作Url',
  `request_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '操作方式',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '操作类型',
  `param` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '操作参数',
  `result` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '操作结果',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '' COMMENT '操作ip',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '1' COMMENT '操作状态',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1774677203487596547 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='操作日志表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '角色编码',
  `cascade_checked` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '1' COMMENT '级联勾选(1:是, 0:否)',
  `data_scope` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '数据权限',
  `role_level` int NOT NULL DEFAULT '2' COMMENT '角色级别',
  `role_desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '角色描述',
  `data_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '角色状态(1:正常, 0:停用, 默认:1)',
  `role_order` int NOT NULL DEFAULT '1000' COMMENT '角色排序',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='角色表';

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `role_id` bigint unsigned NOT NULL COMMENT '角色表id',
  `menu_id` bigint unsigned NOT NULL COMMENT '菜单表id',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_role_menu_enabled_index` (`enabled`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='角色菜单关系表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '账号',
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL COMMENT '密码',
  `password_changed` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '0' COMMENT '初始密码是否变更过(账号创建以及密码重置都将此值置为0)',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '姓名',
  `gender` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '9' COMMENT '性别',
  `telephone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin DEFAULT '' COMMENT '手机号;敏感信息',
  `user_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '用户类型',
  `data_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '账号状态(1:正常, 0:停用, 默认:1)',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_user_create_time_index` (`create_time`) USING BTREE,
  KEY `sys_user_update_time_index` (`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='用户表';

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `user_id` bigint unsigned NOT NULL COMMENT '用户表id',
  `dept_id` bigint unsigned NOT NULL COMMENT '部门表id',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_user_dept_dept_id_index` (`dept_id`) USING BTREE,
  KEY `sys_user_dept_user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='用户部门关系表';

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info` (
  `id` bigint unsigned NOT NULL COMMENT '主键id',
  `user_id` bigint unsigned NOT NULL COMMENT '用户id',
  `avatar_fid` bigint unsigned DEFAULT NULL COMMENT '头像id',
  `card_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '' COMMENT '证件类型',
  `card_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '' COMMENT '证件号;敏感信息',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='用户信息表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `user_id` bigint unsigned NOT NULL COMMENT '用户表id',
  `role_id` bigint unsigned NOT NULL COMMENT '角色表id',
  `create_by` bigint unsigned NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint unsigned NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enabled` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_bin NOT NULL DEFAULT '1' COMMENT '可用状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_bin COMMENT='用户角色关系表';
