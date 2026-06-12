package com.gk.common.constant;

/**
 * 正则常量类
 *
 * @author Kevin
 * @date 2020-03-12 10:26
 **/

public class RegexConstant {

    private RegexConstant() {
        throw new IllegalStateException("Constant class");
    }

    /**
     * 密码正则
     */
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$~#^!%*?&|+=_,.-])[A-Za-z\\d@$~#^!%*?&|+=_,.-]{8,16}$";

    /**
     * 手机号正则
     */
    public static final String TELEPHONE = "^[1][3,4,5,6,7,8,9][0-9]{9}$";

}
