package com.gk.server.enums;

import com.gk.common.enums.ReviewStatus;
import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 注册审核状态 {@link ReviewStatus}
 *
 * @author Flame
 * @since 2023-02-22 16:47
 **/
@Getter
public enum RegisterReviewStatus implements EnumValuable {

    /**
     * 待审核
     */
    PENDING(ReviewStatus.PENDING),

    /**
     * 通过
     */
    PASS(ReviewStatus.PASS),

    /**
     * 驳回
     */
    REJECT(ReviewStatus.REJECT);

    public final String value;

    RegisterReviewStatus(ReviewStatus reviewStatus) {
        this.value = reviewStatus.value;
    }

}
