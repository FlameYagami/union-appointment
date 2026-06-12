package com.gk.mail.service.intf;

import com.gk.mail.bean.BaseMailInfo;

public interface IMailService {

    boolean sendVerificationCodeMail(String address, String title, String message);

    boolean sendFeedbackMail(String address, String title, String message);

    boolean sendMail(BaseMailInfo baseMailInfo, String title, String message);
}
