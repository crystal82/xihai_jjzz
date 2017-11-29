package com.chipsea.mode.entity;

import java.io.Serializable;

public class HeadInfo implements Serializable {

    /**
     * https请求头信息
     */
    private static final long serialVersionUID = 1L;

    private String token;
    private String token_expirytime;
    private String user_agent;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_expirytime() {
        return token_expirytime;
    }

    public void setToken_expirytime(String token_expirytime) {
        this.token_expirytime = token_expirytime;
    }


    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    @Override
    public String toString() {
        return "HeadInfo [token=" + token
                + ", token_expirytime=" + token_expirytime + ", user_agent=" + user_agent + "]";
    }
}
