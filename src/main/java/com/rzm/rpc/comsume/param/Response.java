package com.rzm.rpc.comsume.param;


import lombok.Data;

@Data
public class Response {



    private String requestId;

    private String code;

    private String msg;

    private Object data;

}
