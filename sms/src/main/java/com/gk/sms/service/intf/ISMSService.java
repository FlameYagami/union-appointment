package com.gk.sms.service.intf;

public interface ISMSService {

    boolean sendSMS(String mobile, String message);
}
