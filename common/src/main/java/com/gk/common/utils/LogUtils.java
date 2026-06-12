package com.gk.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log工具类
 *
 * @author GuoYu
 * @since 2022-11-03 16:16
 **/
public class LogUtils {

    private LogUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Logger logCallApi() {
        return LoggerFactory.getLogger("CallApiLog");
    }

}
