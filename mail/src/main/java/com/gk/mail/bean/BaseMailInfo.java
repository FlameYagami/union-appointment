package com.gk.mail.bean;

import lombok.Data;

/**
 * Created by Flame on 2020/03/18.
 **/
@Data
public class BaseMailInfo {
    private String host;
    private int port;
    private String username;
    private String password;
    private String fromAddress;
    private String toAddress;
    private boolean auth;
}
