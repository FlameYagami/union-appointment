package com.gk.common.enums;

import cn.hutool.core.text.NamingCase;
import com.gk.common.intf.EnumValuable;
import com.gk.common.intf.IResultStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据审核状态
 *
 * @author Flame
 * @since 2023-02-22 16:47
 **/
@Getter
@Slf4j
public enum ReviewStatus implements EnumValuable {

    /**
     * 未提交
     */
    NOT_SUBMIT("1", "未提交"),

    /**
     * 待审核
     */
    PENDING("2", "待审核"),

    /**
     * 通过
     */
    PASS("3", "通过"),

    /**
     * 驳回
     */
    REJECT("4", "驳回"),

    /**
     * 撤回
     */
    WITHDRAW("5", "撤回");

    public final String value;
    public final String name;

    ReviewStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static ReviewStatus getReviewStatus(String reviewStatus) {
        for (ReviewStatus status : ReviewStatus.values()) {
            if (status.value.equals(reviewStatus)) {
                return status;
            }
        }
        throw new SysException(SysStatus.REVIEW_STATUS_EXCEPTION);
    }

    /**
     * 校验是否满 通过 的审核流程
     *
     * @param dbReviewStatus      当前的数据的审核状态
     * @param entity              数据库模型
     * @param excludeReviewStatus 需要排除的状态校验
     */
    public static void validPass(String dbReviewStatus, Object entity, ReviewStatus... excludeReviewStatus) {
        Map<String, IResultStatus> map = new HashMap<>() {{
            put(ReviewStatus.NOT_SUBMIT.value, SysStatus.APPLY_NOT_SUBMIT);
            put(ReviewStatus.PASS.value, SysStatus.APPLY_HAS_PASS);
            put(ReviewStatus.REJECT.value, SysStatus.APPLY_HAS_REJECT);
            put(ReviewStatus.WITHDRAW.value, SysStatus.APPLY_HAS_WITHDRAW);
        }};
        removeReviewStatus(map, excludeReviewStatus);
        validReviewStatus(map, dbReviewStatus, ReviewStatus.PASS, entity);
    }

    /**
     * 校验是否满 驳回 的审核流程
     *
     * @param dbReviewStatus      当前的数据的审核状态
     * @param entity              数据库模型
     * @param excludeReviewStatus 需要排除的状态校验
     */
    public static void validReject(String dbReviewStatus, Object entity, ReviewStatus... excludeReviewStatus) {
        Map<String, IResultStatus> map = new HashMap<>() {{
            put(ReviewStatus.NOT_SUBMIT.value, SysStatus.APPLY_NOT_SUBMIT);
            put(ReviewStatus.PASS.value, SysStatus.APPLY_HAS_PASS);
            put(ReviewStatus.REJECT.value, SysStatus.APPLY_HAS_REJECT);
            put(ReviewStatus.WITHDRAW.value, SysStatus.APPLY_HAS_WITHDRAW);
        }};
        removeReviewStatus(map, excludeReviewStatus);
        validReviewStatus(map, dbReviewStatus, ReviewStatus.REJECT, entity);
    }

    /**
     * 校验是否满 撤回 的审核流程
     *
     * @param dbReviewStatus      当前的数据的审核状态
     * @param entity              数据库模型
     * @param excludeReviewStatus 需要排除的状态校验
     */
    public static void validWithdraw(String dbReviewStatus, Object entity, ReviewStatus... excludeReviewStatus) {
        Map<String, IResultStatus> map = new HashMap<>() {{
            put(ReviewStatus.NOT_SUBMIT.value, SysStatus.APPLY_NOT_SUBMIT);
            put(ReviewStatus.PASS.value, SysStatus.APPLY_HAS_PASS);
            put(ReviewStatus.REJECT.value, SysStatus.APPLY_HAS_REJECT);
            put(ReviewStatus.WITHDRAW.value, SysStatus.APPLY_HAS_WITHDRAW);
        }};
        removeReviewStatus(map, excludeReviewStatus);
        validReviewStatus(map, dbReviewStatus, ReviewStatus.WITHDRAW, entity);
    }

    /**
     * 校验是否满 提交 的审核流程
     *
     * @param dbReviewStatus 当前的数据的审核状态
     * @param entity         数据库模型
     */
    public static void validSubmit(String dbReviewStatus, Object entity) {
        Map<String, IResultStatus> map = new HashMap<>() {{
            put(ReviewStatus.PENDING.value, SysStatus.APPLY_HAS_REVIEW);
            put(ReviewStatus.PASS.value, SysStatus.APPLY_HAS_PASS);
        }};
        validReviewStatus(map, dbReviewStatus, ReviewStatus.NOT_SUBMIT, entity);
    }

    /**
     * 校验是否满 编辑 的审核流程
     *
     * @param dbReviewStatus 当前的数据的审核状态
     * @param entity         数据库模型
     */
    public static void validEdit(String dbReviewStatus, Object entity) {
        Map<String, IResultStatus> map = new HashMap<>() {{
            put(ReviewStatus.PENDING.value, SysStatus.APPLY_HAS_REVIEW);
            put(ReviewStatus.PASS.value, SysStatus.APPLY_HAS_PASS);
        }};
        validReviewStatus(map, dbReviewStatus, "Edit", entity);
    }

    /**
     * 校验是否满 删除 的审核流程
     *
     * @param dbReviewStatus 当前的数据的审核状态
     * @param entity         数据库模型
     */
    public static void validDelete(String dbReviewStatus, Object entity) {
        Map<String, IResultStatus> map = new HashMap<>() {{
            put(ReviewStatus.PENDING.value, SysStatus.APPLY_HAS_SUBMIT);
            put(ReviewStatus.PASS.value, SysStatus.APPLY_HAS_PASS);
        }};
        validReviewStatus(map, dbReviewStatus, "Delete", entity);
    }

    /**
     * 校验数据库的审核状态
     */
    private static void validReviewStatus(Map<String, IResultStatus> map, String dbReviewStatus, ReviewStatus bizReviewStatus, Object entity) {
        if (map.containsKey(dbReviewStatus)) {
            log.error("{} {} Error: {}", NamingCase.toPascalCase(bizReviewStatus.getName()), entity.getClass().getSimpleName(), JsonUtils.toJson(entity));
            throw new SysException(map.get(dbReviewStatus));
        }
    }

    /**
     * 校验数据库的审核状态
     */
    private static void validReviewStatus(Map<String, IResultStatus> map, String dbReviewStatus, String operateType, Object entity) {
        if (map.containsKey(dbReviewStatus)) {
            log.error("{} {} Error: {}", operateType, entity.getClass().getSimpleName(), JsonUtils.toJson(entity));
            throw new SysException(map.get(dbReviewStatus));
        }
    }

    /**
     * 移除需要排除校验的审核状态
     */
    private static void removeReviewStatus(Map<String, IResultStatus> map, ReviewStatus... excludeReviewStatus) {
        if (null != excludeReviewStatus) {
            for (ReviewStatus reviewStatus : excludeReviewStatus) {
                map.remove(reviewStatus.value);
            }
        }
    }

}
