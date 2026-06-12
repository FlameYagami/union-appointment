package com.gk.security.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * Created by raodeming on 2020/5/16.
 */
@Data
@AllArgsConstructor
public class CaptchaPoint {
    /**
     * 密钥参数 aesKey + | + aesIv
     */
    private String secretKey;
    /**
     * x轴坐标
     */
    private double x;
    /**
     * y轴坐标
     */
    private double y;

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CaptchaPoint captchaPoint = (CaptchaPoint) o;
        return x == captchaPoint.x && y == captchaPoint.y && Objects.equals(secretKey, captchaPoint.secretKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secretKey, x, y);
    }
}
