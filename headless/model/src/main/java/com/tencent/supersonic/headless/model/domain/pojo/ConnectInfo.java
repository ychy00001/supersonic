package com.tencent.supersonic.headless.model.domain.pojo;


import lombok.Data;

@Data
public class ConnectInfo {


    private String url;

    private String userName;

    private String password;

    private String database;

}
