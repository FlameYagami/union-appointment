package com.gk.mail.service.impl;

import com.gk.mail.bean.BaseMailInfo;
import com.gk.mail.bean.FeedbackMailInfo;
import com.gk.mail.bean.VerifyMailInfo;
import com.gk.mail.constant.MailConstant;
import com.gk.mail.service.intf.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
public class MailService implements IMailService {

    @Resource
    private FeedbackMailInfo feedbackMailInfo;
    @Resource
    private VerifyMailInfo verifyMailInfo;

    /**
     * 发送注册邮件
     *
     * @param address 邮件地址
     * @param title   邮件标题
     * @param message 邮件内容
     * @return 邮件发送结果
     */
    @Override
    public boolean sendVerificationCodeMail(String address, String title, String message) {
        verifyMailInfo.setToAddress(address);
        return sendMail(verifyMailInfo, title, message);
    }

    /**
     * 发送反馈邮件
     *
     * @param address 邮件地址
     * @param title   邮件标题
     * @param message 邮件内容
     * @return 邮件发送结果
     */
    @Override
    public boolean sendFeedbackMail(String address, String title, String message) {
        feedbackMailInfo.setToAddress(address);
        return sendMail(feedbackMailInfo, title, message);
    }

    private Properties getProperties(BaseMailInfo baseMailInfo) {
        // 因为阿里云的25端口被禁用, 所以使用第三方的465端口进行发送
        Properties properties = new Properties();
        properties.put(MailConstant.AUTH, String.valueOf(true));
        properties.put(MailConstant.HOST, baseMailInfo.getHost());
        properties.put(MailConstant.PORT, baseMailInfo.getPort());
        properties.put(MailConstant.FACTORY_PORT, baseMailInfo.getPort());
        properties.put(MailConstant.FACTORY_CLASS, MailConstant.FACTORY_CLASS_VALUE);
        properties.put(MailConstant.FACTORY_FALLBACK, String.valueOf(false));
        properties.put(MailConstant.USERNAME, baseMailInfo.getUsername());
        properties.put(MailConstant.PASSWORD, baseMailInfo.getPassword());
        return properties;
    }

    /**
     * 发送邮件
     *
     * @param baseMailInfo 邮件的基础配置
     * @param title        邮件标题
     * @param message      邮件内容
     * @return 邮件发送结果
     */
    @Override
    public boolean sendMail(BaseMailInfo baseMailInfo, String title, String message) {
        Properties properties = getProperties(baseMailInfo);
        Authenticator authenticator = null;
        if (baseMailInfo.isAuth()) {
            authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(baseMailInfo.getUsername(), baseMailInfo.getPassword());
                }
            };
        }
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(properties, authenticator);
        // 创建邮件消息
        MimeMessage mimeMessage = new MimeMessage(mailSession);
        try {
            // 设置发件人
            InternetAddress from = new InternetAddress(baseMailInfo.getFromAddress());
            mimeMessage.setFrom(from);
            Address[] addresses = new Address[]{new InternetAddress(baseMailInfo.getFromAddress())};
            mimeMessage.setReplyTo(addresses);
            // 设置收件人
            InternetAddress toAddress = new InternetAddress(baseMailInfo.getToAddress());
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, toAddress);
            // 设置邮件标题
            mimeMessage.setSubject(title);
            // 设置邮件的内容体
            mimeMessage.setContent(message, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Send mail error: {}", e.getMessage());
            return false;
        }
    }
}
