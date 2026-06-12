CREATE TABLE sys_user(
    id NUMBER NOT NULL,
    username VARCHAR2(32) NOT NULL,
    password VARCHAR2(256) NOT NULL,
    password_changed VARCHAR2(1) DEFAULT  '0',
    nickname VARCHAR2(32) DEFAULT  '',
    gender VARCHAR2(1) DEFAULT  '9',
    telephone VARCHAR2(11) DEFAULT  '',
    user_type VARCHAR2(1) DEFAULT  '1' NOT NULL,
    data_status VARCHAR2(1) DEFAULT  '1' NOT NULL,
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_user IS '用户表';
COMMENT ON COLUMN sys_user.id IS '主键';
COMMENT ON COLUMN sys_user.username IS '账号';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.password_changed IS '初始密码是否变更过;(1:是, 0:否, 默认:1, 账号创建以及密码重置都将此值置为0)';
COMMENT ON COLUMN sys_user.nickname IS '姓名';
COMMENT ON COLUMN sys_user.gender IS '性别;(1:男, 2:女, 9:未知)';
COMMENT ON COLUMN sys_user.telephone IS '手机号;敏感信息';
COMMENT ON COLUMN sys_user.user_type IS '用户类型;(1:管理员, 2:普通用户)';
COMMENT ON COLUMN sys_user.data_status IS '账号状态;(1:正常, 0:停用, 默认:1)';
COMMENT ON COLUMN sys_user.create_by IS '创建人';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_by IS '更新人';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_role(
    id NUMBER NOT NULL,
    role_name VARCHAR2(32) NOT NULL,
    role_code VARCHAR2(64) NOT NULL,
    data_scope VARCHAR2(1) NOT NULL,
    cascade_checked VARCHAR2(1) DEFAULT  '1',
    role_level INT DEFAULT  2 NOT NULL,
    role_desc VARCHAR2(256) DEFAULT  '',
    data_status VARCHAR2(1) DEFAULT  '1' NOT NULL,
    role_order INT DEFAULT  1000 NOT NULL,
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_role IS '角色表';
COMMENT ON COLUMN sys_role.id IS '主键';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_code IS '角色编码';
COMMENT ON COLUMN sys_role.data_scope IS '数据权限;(1:全平台数据, 2:部门及以下数据, 3:本部门数据, 4:个人数据)';
COMMENT ON COLUMN sys_role.cascade_checked IS '是否级联勾选;(1:是, 0:否)';
COMMENT ON COLUMN sys_role.role_level IS '角色级别';
COMMENT ON COLUMN sys_role.role_desc IS '角色描述';
COMMENT ON COLUMN sys_role.data_status IS '角色状态;(1:正常, 0:停用, 默认:1)';
COMMENT ON COLUMN sys_role.role_order IS '角色排序';
COMMENT ON COLUMN sys_role.create_by IS '创建人';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_by IS '更新人';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_role.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_dept(
    id NUMBER NOT NULL,
    parent_id NUMBER NOT NULL,
    dept_name VARCHAR2(32) NOT NULL,
    dept_code VARCHAR2(64) DEFAULT  '',
    dept_level INT NOT NULL,
    dept_type VARCHAR2(1) DEFAULT  '1' NOT NULL,
    data_status VARCHAR2(1) DEFAULT  '1' NOT NULL,
    dept_order INT DEFAULT  1000 NOT NULL,
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_dept IS '部门表';
COMMENT ON COLUMN sys_dept.id IS '主键';
COMMENT ON COLUMN sys_dept.parent_id IS '父级id';
COMMENT ON COLUMN sys_dept.dept_name IS '部门名称';
COMMENT ON COLUMN sys_dept.dept_code IS '部门编码';
COMMENT ON COLUMN sys_dept.dept_level IS '部门层级';
COMMENT ON COLUMN sys_dept.dept_type IS '部门类型;(1:部门, 2:科室)';
COMMENT ON COLUMN sys_dept.data_status IS '部门状态;(1:正常, 0:停用, 默认:1)';
COMMENT ON COLUMN sys_dept.dept_order IS '部门排序';
COMMENT ON COLUMN sys_dept.create_by IS '创建人';
COMMENT ON COLUMN sys_dept.create_time IS '创建时间';
COMMENT ON COLUMN sys_dept.update_by IS '更新人';
COMMENT ON COLUMN sys_dept.update_time IS '更新时间';
COMMENT ON COLUMN sys_dept.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_user_role(
    id NUMBER NOT NULL,
    user_id NUMBER NOT NULL,
    role_id NUMBER NOT NULL,
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_user_role IS '用户角色关系表';
COMMENT ON COLUMN sys_user_role.id IS '主键';
COMMENT ON COLUMN sys_user_role.user_id IS '用户表id';
COMMENT ON COLUMN sys_user_role.role_id IS '角色表id';
COMMENT ON COLUMN sys_user_role.create_by IS '创建人';
COMMENT ON COLUMN sys_user_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_role.update_by IS '更新人';
COMMENT ON COLUMN sys_user_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_role.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_menu(
    id NUMBER NOT NULL,
    parent_id NUMBER NOT NULL,
    menu_name VARCHAR2(32) NOT NULL,
    permission_code VARCHAR2(64) DEFAULT '',
    menu_type VARCHAR2(1) NOT NULL,
    menu_path VARCHAR2(64) DEFAULT '',
    menu_component VARCHAR2(500) DEFAULT '',
    menu_icon VARCHAR2(32) DEFAULT '',
    data_status VARCHAR2(1) DEFAULT '1' NOT NULL,
    menu_cached VARCHAR2(1) DEFAULT '0',
    menu_show VARCHAR2(1) DEFAULT '1',
    inner_link VARCHAR2(1) DEFAULT '1',
    menu_open VARCHAR2(1) DEFAULT '1',
    menu_order INT DEFAULT 1000,
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_menu IS '菜单权限表';
COMMENT ON COLUMN sys_menu.id IS '主键';
COMMENT ON COLUMN sys_menu.parent_id IS '父级id';
COMMENT ON COLUMN sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN sys_menu.permission_code IS '权限标识';
COMMENT ON COLUMN sys_menu.menu_type IS '菜单类型;(1:目录, 2:菜单, 3:按钮)';
COMMENT ON COLUMN sys_menu.menu_path IS '菜单地址';
COMMENT ON COLUMN sys_menu.menu_component IS '菜单组件';
COMMENT ON COLUMN sys_menu.menu_icon IS '菜单图标';
COMMENT ON COLUMN sys_menu.data_status IS '菜单状态;(1:正常, 0:停用, 默认:1)';
COMMENT ON COLUMN sys_menu.menu_cached IS '菜单缓存;(1:是, 0:否, 默认:0)';
COMMENT ON COLUMN sys_menu.menu_show IS '菜单显示;(1:是, 0:否, 默认:1)';
COMMENT ON COLUMN sys_menu.inner_link IS '是否为内部链接;(1:是, 0:否, 默认:1)';
COMMENT ON COLUMN sys_menu.menu_open IS '菜单开放;(1:是, 0:否, 默认:1)';
COMMENT ON COLUMN sys_menu.menu_order IS '菜单排序';
COMMENT ON COLUMN sys_menu.create_by IS '创建人';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.update_by IS '更新人';
COMMENT ON COLUMN sys_menu.update_time IS '更新时间';
COMMENT ON COLUMN sys_menu.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_user_info(
    id NUMBER NOT NULL,
    user_id NUMBER NOT NULL,
    avatar_fid NUMBER,
    card_type VARCHAR2(1) DEFAULT  '' NOT NULL,
    card_number VARCHAR2(32) DEFAULT  '' NOT NULL,
    birthday DATE,
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_user_info IS '用户信息表';
COMMENT ON COLUMN sys_user_info.id IS '主键';
COMMENT ON COLUMN sys_user_info.user_id IS '用户表id';
COMMENT ON COLUMN sys_user_info.avatar_fid IS '头像id;(sys_file表id)';
COMMENT ON COLUMN sys_user_info.card_type IS '证件类型;(01:身份证, 03:护照, 99:其他)';
COMMENT ON COLUMN sys_user_info.card_number IS '证件号;敏感信息';
COMMENT ON COLUMN sys_user_info.birthday IS '生日';
COMMENT ON COLUMN sys_user_info.create_by IS '创建人';
COMMENT ON COLUMN sys_user_info.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_info.update_by IS '更新人';
COMMENT ON COLUMN sys_user_info.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_info.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE UNIQUE INDEX SUI_CARD_IDX ON sys_user_info(card_type,card_number);

CREATE TABLE sys_role_menu(
    id NUMBER NOT NULL,
    role_id NUMBER NOT NULL,
    menu_id NUMBER NOT NULL,
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_role_menu IS '角色菜单关系表';
COMMENT ON COLUMN sys_role_menu.id IS '主键';
COMMENT ON COLUMN sys_role_menu.role_id IS '角色表id';
COMMENT ON COLUMN sys_role_menu.menu_id IS '菜单表id';
COMMENT ON COLUMN sys_role_menu.create_by IS '创建人';
COMMENT ON COLUMN sys_role_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_role_menu.update_by IS '更新人';
COMMENT ON COLUMN sys_role_menu.update_time IS '更新时间';
COMMENT ON COLUMN sys_role_menu.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_user_dept(
    id NUMBER NOT NULL,
    user_id NUMBER NOT NULL,
    dept_id NUMBER NOT NULL,
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_user_dept IS '用户部门关系表';
COMMENT ON COLUMN sys_user_dept.id IS '主键';
COMMENT ON COLUMN sys_user_dept.user_id IS '用户表id';
COMMENT ON COLUMN sys_user_dept.dept_id IS '部门表id';
COMMENT ON COLUMN sys_user_dept.create_by IS '创建人';
COMMENT ON COLUMN sys_user_dept.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_dept.update_by IS '更新人';
COMMENT ON COLUMN sys_user_dept.update_time IS '更新时间';
COMMENT ON COLUMN sys_user_dept.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_operate_log(
    id NUMBER NOT NULL,
    dept_id NUMBER NOT NULL,
    title VARCHAR2(64) NOT NULL,
    request_url VARCHAR2(256) NOT NULL,
    request_method VARCHAR2(32) NOT NULL,
    type VARCHAR2(32) NOT NULL,
    ip VARCHAR2(64) DEFAULT  '',
    param VARCHAR2(4000) NOT NULL,
    result VARCHAR2(4000) DEFAULT  '',
    status VARCHAR2(1) DEFAULT  '1',
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_operate_log IS '操作日志表';
COMMENT ON COLUMN sys_operate_log.id IS '主键';
COMMENT ON COLUMN sys_operate_log.dept_id IS '部门表id';
COMMENT ON COLUMN sys_operate_log.title IS '操作标题';
COMMENT ON COLUMN sys_operate_log.request_url IS '操作Url';
COMMENT ON COLUMN sys_operate_log.request_method IS '操作方式';
COMMENT ON COLUMN sys_operate_log.type IS '操作类型';
COMMENT ON COLUMN sys_operate_log.ip IS '操作ip';
COMMENT ON COLUMN sys_operate_log.param IS '操作参数';
COMMENT ON COLUMN sys_operate_log.result IS '操作结果';
COMMENT ON COLUMN sys_operate_log.status IS '操作状态;(1:成功 0:失败; 默认:1)';
COMMENT ON COLUMN sys_operate_log.create_by IS '创建人';
COMMENT ON COLUMN sys_operate_log.create_time IS '创建时间';
COMMENT ON COLUMN sys_operate_log.update_by IS '更新人';
COMMENT ON COLUMN sys_operate_log.update_time IS '更新时间';
COMMENT ON COLUMN sys_operate_log.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_login_log(
    id NUMBER NOT NULL,
    username VARCHAR2(32) NOT NULL,
    dept_id NUMBER NOT NULL,
    login_ip VARCHAR2(32) DEFAULT '',
    browser VARCHAR2(32) DEFAULT '',
    os VARCHAR2(64) DEFAULT '',
    result_msg VARCHAR2(256) DEFAULT '',
    status VARCHAR2(1) DEFAULT '1' NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_login_log IS '登录日志表';
COMMENT ON COLUMN sys_login_log.id IS '主键';
COMMENT ON COLUMN sys_login_log.username IS '账号';
COMMENT ON COLUMN sys_login_log.dept_id IS '部门表id';
COMMENT ON COLUMN sys_login_log.login_ip IS '登录ip';
COMMENT ON COLUMN sys_login_log.browser IS '浏览器';
COMMENT ON COLUMN sys_login_log.os IS '操作系统';
COMMENT ON COLUMN sys_login_log.result_msg IS '返回消息';
COMMENT ON COLUMN sys_login_log.status IS '登录状态;(1:成功 0:失败; 默认:1)';
COMMENT ON COLUMN sys_login_log.create_time IS '创建时间';
COMMENT ON COLUMN sys_login_log.update_time IS '更新时间';
COMMENT ON COLUMN sys_login_log.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_dict(
    id NUMBER NOT NULL,
    dict_name VARCHAR2(32) NOT NULL,
    dict_code VARCHAR2(64) NOT NULL,
    remark VARCHAR2(256) DEFAULT  '',
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_dict IS '字典表';
COMMENT ON COLUMN sys_dict.id IS '主键';
COMMENT ON COLUMN sys_dict.dict_name IS '字典名称';
COMMENT ON COLUMN sys_dict.dict_code IS '字典编码';
COMMENT ON COLUMN sys_dict.remark IS '字典备注';
COMMENT ON COLUMN sys_dict.create_by IS '创建人';
COMMENT ON COLUMN sys_dict.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict.update_by IS '更新人';
COMMENT ON COLUMN sys_dict.update_time IS '更新时间';
COMMENT ON COLUMN sys_dict.enabled IS '可用状态';

CREATE TABLE sys_dict_data(
    id NUMBER NOT NULL,
    dict_code VARCHAR2(64) NOT NULL,
    dict_label VARCHAR2(32) NOT NULL,
    dict_value VARCHAR2(32) NOT NULL,
    color_type VARCHAR2(32) DEFAULT  'default' NOT NULL,
    dict_css VARCHAR2(256) DEFAULT  '',
    data_status VARCHAR2(1) DEFAULT '1' NOT NULL,
    dict_order INT DEFAULT  1 NOT NULL,
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_dict_data IS '字典数据表';
COMMENT ON COLUMN sys_dict_data.id IS '主键';
COMMENT ON COLUMN sys_dict_data.dict_code IS '字典编码';
COMMENT ON COLUMN sys_dict_data.dict_label IS '字典标签';
COMMENT ON COLUMN sys_dict_data.dict_value IS '字典值';
COMMENT ON COLUMN sys_dict_data.color_type IS '颜色类型';
COMMENT ON COLUMN sys_dict_data.dict_css IS '字典样式';
COMMENT ON COLUMN sys_dict_data.data_status IS '字典数据状态;(1:正常, 0:停用, 默认:1)';
COMMENT ON COLUMN sys_dict_data.dict_order IS '字典排序';
COMMENT ON COLUMN sys_dict_data.create_by IS '创建人';
COMMENT ON COLUMN sys_dict_data.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict_data.update_by IS '更新人';
COMMENT ON COLUMN sys_dict_data.update_time IS '更新时间';
COMMENT ON COLUMN sys_dict_data.enabled IS '可用状态';

CREATE TABLE sys_config(
    id NUMBER NOT NULL,
    config_name VARCHAR2(32) NOT NULL,
    config_key VARCHAR2(32) NOT NULL,
    config_value VARCHAR2(500) NOT NULL,
    data_status VARCHAR2(1) DEFAULT '1' NOT NULL,
    remark VARCHAR2(255) DEFAULT '',
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_config IS '配置表';
COMMENT ON COLUMN sys_config.id IS '主键';
COMMENT ON COLUMN sys_config.config_name IS '配置名称';
COMMENT ON COLUMN sys_config.config_key IS '配置键';
COMMENT ON COLUMN sys_config.config_value IS '配置值';
COMMENT ON COLUMN sys_config.data_status IS '配置状态;(1:正常, 0:停用, 默认:1)';
COMMENT ON COLUMN sys_config.remark IS '备注';
COMMENT ON COLUMN sys_config.create_by IS '创建人';
COMMENT ON COLUMN sys_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_config.update_by IS '更新人';
COMMENT ON COLUMN sys_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_config.enabled IS '可用状态';

CREATE TABLE sys_notice(
    id NUMBER NOT NULL,
    notice_title VARCHAR2(64) NOT NULL,
    notice_type VARCHAR2(1) DEFAULT '1',
    dept_id NUMBER NOT NULL,
    data_status VARCHAR2(1) DEFAULT '1' NOT NULL,
    notice_scope VARCHAR2(1) NOT NULL,
    notice_start_time DATE NOT NULL,
    notice_end_time DATE NOT NULL,
    notice_content CLOB NOT NULL,
    notice_top VARCHAR2(1) DEFAULT '0',
    image_fids VARCHAR2(256) DEFAULT '',
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_notice IS '公告通知表';
COMMENT ON COLUMN sys_notice.id IS '主键';
COMMENT ON COLUMN sys_notice.notice_title IS '公告通知标题';
COMMENT ON COLUMN sys_notice.notice_type IS '公告通知类型;(1:公告, 2:通知, 默认:1)';
COMMENT ON COLUMN sys_notice.dept_id IS '部门表id';
COMMENT ON COLUMN sys_notice.data_status IS '公告通知状态;(1:正常, 0:停用, 默认:1)';
COMMENT ON COLUMN sys_notice.notice_scope IS '公告通知范围;(1:全平台数据, 2:部门及以下数据, 3:本部门数据)';
COMMENT ON COLUMN sys_notice.notice_start_time IS '公告通知开始时间';
COMMENT ON COLUMN sys_notice.notice_end_time IS '公告通知结束时间';
COMMENT ON COLUMN sys_notice.notice_content IS '公告通知内容';
COMMENT ON COLUMN sys_notice.notice_top IS '置顶;(1:是, 0:否, 默认:0)';
COMMENT ON COLUMN sys_notice.image_fids IS '图片ids';
COMMENT ON COLUMN sys_notice.create_by IS '创建人';
COMMENT ON COLUMN sys_notice.create_time IS '创建时间';
COMMENT ON COLUMN sys_notice.update_by IS '更新人';
COMMENT ON COLUMN sys_notice.update_time IS '更新时间';
COMMENT ON COLUMN sys_notice.enabled IS '可用状态;(1:是, 0:否, 默认:1)';

CREATE TABLE sys_file(
    id NUMBER NOT NULL,
    biz_type VARCHAR2(2) NOT NULL,
    filename VARCHAR2(32) NOT NULL,
    file_path VARCHAR2(256) NOT NULL,
    file_size VARCHAR2(32) NOT NULL,
    deleted VARCHAR2(1) DEFAULT  '0',
    create_by NUMBER NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_by NUMBER NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '0' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_file IS '文件表';
COMMENT ON COLUMN sys_file.id IS '主键';
COMMENT ON COLUMN sys_file.biz_type IS '业务类型;(1:用户头像)';
COMMENT ON COLUMN sys_file.filename IS '文件名';
COMMENT ON COLUMN sys_file.file_path IS '文件路径';
COMMENT ON COLUMN sys_file.file_size IS '文件大小';
COMMENT ON COLUMN sys_file.deleted IS '是否删除;(1:是 0:否; 默认:0)';
COMMENT ON COLUMN sys_file.create_by IS '创建人';
COMMENT ON COLUMN sys_file.create_time IS '创建时间';
COMMENT ON COLUMN sys_file.update_by IS '更新人';
COMMENT ON COLUMN sys_file.update_time IS '更新时间';
COMMENT ON COLUMN sys_file.enabled IS '可用状态;(1:是, 0:否, 默认:0)';

CREATE TABLE sys_open_config(
    id NUMBER NOT NULL,
    company_name VARCHAR2(32) NOT NULL,
    company_desc VARCHAR2(800) DEFAULT  '',
    dept_id NUMBER NOT NULL,
    role_id NUMBER NOT NULL,
    open_id VARCHAR2(16) NOT NULL,
    aes_key VARCHAR2(16) NOT NULL,
    aes_iv VARCHAR2(16) NOT NULL,
    data_status VARCHAR2(1) DEFAULT  '1' NOT NULL,
    create_time DATE DEFAULT SYSDATE NOT NULL,
    update_time DATE DEFAULT SYSDATE NOT NULL,
    enabled VARCHAR2(1) DEFAULT  '1' NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE sys_open_config IS '第三方对接配置表';
COMMENT ON COLUMN sys_open_config.id IS '主键';
COMMENT ON COLUMN sys_open_config.company_name IS '对接厂商名称';
COMMENT ON COLUMN sys_open_config.company_desc IS '对接厂商描述';
COMMENT ON COLUMN sys_open_config.dept_id IS '部门表id';
COMMENT ON COLUMN sys_open_config.role_id IS '角色表id';
COMMENT ON COLUMN sys_open_config.open_id IS '开放式认证系统id;由系统生成';
COMMENT ON COLUMN sys_open_config.aes_key IS '秘钥;由系统生成';
COMMENT ON COLUMN sys_open_config.aes_iv IS '初始化向量;由系统生成';
COMMENT ON COLUMN sys_open_config.data_status IS '秘钥状态;(1:正常, 0:停用, 默认:1)';
COMMENT ON COLUMN sys_open_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_open_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_open_config.enabled IS '可用状态;(1:是, 0:否, 默认:1)';
