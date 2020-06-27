package com.rzm.rpc.provide.param;


import lombok.Data;

@Data
public class Response {



    private String requestId;

    private String code;

    private String msg;

    private Object data;

}
