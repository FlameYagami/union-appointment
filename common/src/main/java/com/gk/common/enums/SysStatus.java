package com.gk.common.enums;

import cn.hutool.http.HttpStatus;
import com.gk.common.intf.IResultStatus;
import lombok.ToString;

/**
 * 系统异常枚举类
 *
 * @author Kevin
 * @since 2020-03-23 15:06
 **/
@ToString
public enum SysStatus implements IResultStatus {

    // ------------------------ 通用结果 ------------------------
    SUCCESS(1, "请求成功"),
    FAILED(0, "请求失败"),
    OPERATE_SUCCESS(1, "操作成功"),
    OPERATE_FAILED(0, "操作失败"),

    IMPORT_SUCCESS(1, "导入成功"),
    IMPORT_FAILED(0, "导入失败"),

    // 框架级失败(使用HttpCode等价替换)
    UNAUTHORIZED_ERROR(HttpStatus.HTTP_UNAUTHORIZED, "认证失败, 无法访问"),
    UNAUTHORIZED_USER_ID(HttpStatus.HTTP_UNAUTHORIZED, "用户ID异常"),
    UNAUTHORIZED_DEPT_ID(HttpStatus.HTTP_UNAUTHORIZED, "部门ID异常"),
    UNAUTHORIZED_USER_INFO(HttpStatus.HTTP_UNAUTHORIZED, "用户信息异常"),
    PERMISSION_ERROR(999, "没有访问权限"),

    // ------------------------ 用户 ------------------------
    LOGIN_SUCCESS(1, "登录成功"),
    LOGOUT_SUCCESS(1, "登出成功"),
    ACCOUNT_OR_PASSWORD_ERROR(10001, "账号或密码错误"),
    ACCOUNT_HAS_NO_DEPT(10003, "用户尚未分配部门"),
    ACCOUNT_HAS_NO_ROLE(10004, "用户尚未分配角色"),
    ACCOUNT_ALREADY_EXIST(10005, "用户已存在"),
    ACCOUNT_DISABLE(10006, "用户已停用"),
    CANT_NOT_DELETE_SELF(10007, "不允许删除自己的账号"),
    ACCOUNT_IN_BLACKLIST(10008, "账号已被屏蔽"),
    PASSWORD_ILLEGAL(10009, "密码必须是8-16位的大小写字母、数字、特殊字符组合"),
    PASSWORD_SAME(10010, "新密码不能设置成与原密码相同"),
    OLD_PASSWORD_ERROR(10011, "原密码错误"),
    LOGIN_FAILED(10012, "登录失败, 密钥解析失败"),
    ACCOUNT_LOCKED(10013, "多次密码错误, 账号被锁定, 请联系管理员或等待24小时后解锁"),
    PASSWORD_HAS_CHANGED(10014, "密码已经变更"),
    NEW_PASSWORD_CAN_NOT_CONTAIN_USERNAME(10015, "密码不允许包含用户名"),

    CAPTCHA_TYPE_INVALID(10101, "验证码类型不合法"),
    CAPTCHA_EXPIRE_ERROR(10102, "验证码已失效"),
    CAPTCHA_IMAGE_ERROR(10103, "验证码生成失败"),
    CAPTCHA_CHECK_ERROR(10104, "验证码校验失败"),
    CAPTCHA_REQ_GET_LIMITED(10105, "获取验证码接口请求次数过多, 请稍后再试"),
    CAPTCHA_REQ_GET_LOCKED(10106, "接口验证失败次数过多, 请稍后再试"),
    CAPTCHA_REQ_CHECK_LIMITED(10107, "校验接口请求次数超限，请稍后再试!"),

    CARD_NUMBER_EXIST(10201, "证件号已经使用"),
    IDCARD_INVALID(10012, "身份证号不合法"),
    REGISTER_PADDING(10203, "您的注册正在审核中"),
    REGISTER_PASS(10204, "您的注册正已经通过了"),
    REGISTER_NOT_PASS(810205, "您的注册没有通过"),
    CAN_NOT_FIND_REGISTER(10206, "没有查询到相关的用户注册信息"),

    // ------------------------ 菜单 ------------------------
    MENU_HAS_CHILD(20000, "存在子级菜单, 不允许删除"),
    MENU_PARENT_NOT_EXITS(20001, "父级菜单不存在"),
    MENU_NOT_ENABLE(20002, "不允许选择停用的菜单"),
    MENU_PARENT_IS_CHILD(20003, "不能设置自己或子级菜单为父级菜单"),
    MENU_OUT_LINK_ILLEGAL(20004, "外链地址的协议头必须是http或https"),
    MENU_PARENT_TYPE_ILLEGAL(20005, "菜单的父级必须是目录类型"),

    // ------------------------ 角色 ------------------------
    ROLE_HAS_USER_BY_USER_CAN_NOT_OPERATE(30000, "有用户在使用当前角色, 不允许操作"),
    ROLE_HAS_USER_BY_GROUP_CAN_NOT_OPERATE(30000, "有用户组在使用当前角色, 不允许操作"),
    ROLE_HAS_ONLY_ONE(30001, "不允许删除只有一个角色的用户"),
    ROLE_EXCEEDS_UPPER_LIMIT(30002, "用户持有的角色已达到上限"),
    ROLE_CODE_EXITS(30003, "角色编码已经存在"),
    SYSTEM_ROLE_CODE_CAN_NOT_EDIT(30004, "不允许修改系统角色编码"),
    SYSTEM_ROLE_LEVEL_CAN_NOT_EDIT(30005, "不允许修改系统角色等级"),

    // ------------------------ 部门 ------------------------
    DEPT_PARENT_NOT_EXITS(40001, "父级部门不存在"),
    DEPT_EXITS_CHILD(40003, "存在子级部门, 不允许删除"),
    DEPT_EXISTS_USER(40005, "部门中存在用户, 不允许删除"),
    DEPT_NOT_ENABLE(40006, "不允许选择停用的部门"),
    DEPT_PARENT_IS_CHILD(40007, "不能设置自己或子级部门为父级部门"),
    DEPT_CODE_EXITS(40008, "部门编码已经存在"),
    DEPT_OUT_OF_RANGE(40009, "部门超出选择范围"),
    TOP_DEPT_CAN_NOT_OPERATE(40010, "顶级部门不允许操作"),

    // ------------------------ 字典 ------------------------
    DICT_CODE_EXIST(80001, "字典编码已存在"),
    DICT_DATA_LABEL_VALUE_EXIST(80002, "字典标签或字典值已存在"),
    DICT_DATA_CODE_CAN_NOT_EDIT(80003, "修改字典项时, 不可修改字典编码"),

    // ------------------------ 配置 ------------------------
    CONFIG_KEY_ALREADY_EXIST(80101, "配置键已存在"),
    SYSTEM_CONFIG_KEY_CAN_NOT_EDIT(80102, "不允许修改系统配置键"),
    SYSTEM_CONFIG_VALUE_ILLEGAL(80103, "系统配置值不合法"),

    // ------------------------ 通用异常 ------------------------
    INVALID_PARAM(90000, "参数校验异常: "),
    MISSING_PARAM(90001, "缺少必填参数: %s"),
    READ_PARAM_FAIL(90002, "请求参数异常: %s"),
    INVALID_PARAM_TYPE(90003, "请求参数类型错误: %s"),
    ILLEGAL_PARAM(90004, "非法的请求参数"),
    ID_NOT_FOUND(90005, "找不到对应的数据"),
    SYSTEM_DATA_CAN_NOT_DELETE(90006, "不允许删除系统数据"),
    DATA_OUT_OF_OPERATE_RANGE(90007, "不允许操作他人的数据"),
    DATA_EXCEPTION_AND_REFRESH_PAGE(90008, "数据异常, 请刷新页面"),
    UPLOAD_FILE_MAX_LIMIT(90100, "上传文件大小超出最大限制"),
    FILE_UPLOAD_ERROR(90101, "上传文件失败, 请稍后再试"),

    FONT_LOAD_FAIL(90200, "字体加载失败"),
    EXCEL_IMPORT_TEMPLATE_ERROR(90201, "请使用正确的Excel模板进行导入"),
    EXCEL_EXPORT_ERROR(90202, "导出Excel文件失败"),
    FILE_IMPORT_TYPE_ERROR(90203, "导入的文件格式不支持"),
    FILE_ANALYSIS_ERROR(90204, "文件解析失败"),
    FILE_READ_ERROR(90205, "文件读取失败"),
    EXCEL_IMPORT_ERROR(90210, "Excel文件导入失败"),

    FRONT_CODE_GEN_ERROR(90301, "前端代码生成失败"),
    FRONT_CODE_DOWNLOAD_ERROR(90302, "生成前端代码下载失败"),

    NO_AUTHORIZE_ACCESS(91000,"访问未被授权"),
    DATA_ANALYZE_ERROR(91001,"数据解析错误"),
    DATA_FORMAT_ERROR(91002,"数据格式错误"),
    DATA_ENCRYPT_ERROR(91003, "数据加密失败"),
    DATA_DECRYPT_ERROR(91004, "数据解密失败"),
    DATA_SIGN_ERROR(91005, "数据签名错误"),
    AES_KEY_IV_ERROR(91006,"数据密钥错误"),

    // ------------------------ 审核 ------------------------
    REVIEW_STATUS_EXCEPTION(92006, "审核状态异常"),
    APPLY_HAS_REVIEW(92001, "申请正在审核中"),
    APPLY_NOT_SUBMIT(92002, "申请未提交"),
    APPLY_HAS_SUBMIT(92003, "申请已提交"),
    APPLY_HAS_PASS(92004, "申请已通过"),
    APPLY_HAS_REJECT(92005, "申请已驳回"),
    APPLY_HAS_WITHDRAW(92006, "申请已撤回");

    /**
     * 返回码
     */
    private final int code;

    /**
     * 返回结果描述
     */
    private final String message;

    SysStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
