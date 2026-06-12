package com.gk.security.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gk.common.constant.CommonConstant;
import com.gk.common.model.base.BaseCreateEntity;
import com.gk.common.model.base.BaseEntity;
import com.gk.framework.utils.LoginUserUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;
import java.util.Objects;

/**
 * 自动填充字段处理类
 * 如果没有显式的对数据库默认字段进行赋值，这里会对默认字段进行自动填充、赋值
 *
 * @author GuoYu
 * @since 2022-12-28 10:35
 */
public class DatabaseFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();

            Date current = new Date();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(baseEntity.getCreateTime())) {
                baseEntity.setCreateTime(current);
            }
            // 更新时间为空，则以当前时间为更新时间
            if (Objects.isNull(baseEntity.getUpdateTime())) {
                baseEntity.setUpdateTime(current);
            }

            Long userId = LoginUserUtils.getUserIdOrNull();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if (Objects.isNull(baseEntity.getCreateBy())) {
                if (Objects.nonNull(userId)) {
                    baseEntity.setCreateBy(userId);
                } else {
                    baseEntity.setCreateBy(CommonConstant.ANONYMOUS_ID);
                }
            }
            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            if ( Objects.isNull(baseEntity.getUpdateBy())) {
                if (Objects.nonNull(userId)) {
                    baseEntity.setUpdateBy(userId);
                } else {
                    baseEntity.setUpdateBy(CommonConstant.ANONYMOUS_ID);
                }
            }
            return;
        }

        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseCreateEntity) {
            BaseCreateEntity baseCreateEntity = (BaseCreateEntity) metaObject.getOriginalObject();

            Date current = new Date();
            // 创建时间为空，则以当前时间为插入时间
            if (Objects.isNull(baseCreateEntity.getCreateTime())) {
                baseCreateEntity.setCreateTime(current);
            }

            Long userId = LoginUserUtils.getUserIdOrNull();
            // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
            if (Objects.isNull(baseCreateEntity.getCreateBy())) {
                if (Objects.nonNull(userId)) {
                    baseCreateEntity.setCreateBy(userId);
                } else {
                    baseCreateEntity.setCreateBy(CommonConstant.ANONYMOUS_ID);
                }
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object modifyTime = getFieldValByName("updateTime", metaObject);
        if (Objects.isNull(modifyTime)) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }

        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        Object modifier = getFieldValByName("updateBy", metaObject);
        Long userId = LoginUserUtils.getUserIdOrNull();
        if (Objects.isNull(modifier)) {
            if (Objects.nonNull(userId)) {
                setFieldValByName("updateBy", userId, metaObject);
            } else {
                setFieldValByName("updateBy", CommonConstant.ANONYMOUS_ID, metaObject);
            }
        }
    }
}
