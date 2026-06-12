package com.gk.mail.constant;

/**
 * 邮件常量类
 *
 * @author Flame
 * @since 2020-03-18 10:20
 **/
public class MailConstant {

    private MailConstant() {
        throw new IllegalStateException("Constant class");
    }

    public static final String AUTH                = "mail.smtp.auth";
    public static final String HOST                = "mail.smtp.host";
    public static final String PORT                = "mail.smtp.port";
    public static final String FACTORY_PORT        = "mail.smtp.socketFactory.port";
    public static final String FACTORY_CLASS       = "mail.smtp.socketFactory.class";
    public static final String FACTORY_FALLBACK    = "mail.smtp.socketFactory.fallback";
    public static final String FACTORY_CLASS_VALUE = "javax.net.ssl.SSLSocketFactory";
    public static final String USERNAME            = "mail.user";
    public static final String PASSWORD            = "mail.password";
}
