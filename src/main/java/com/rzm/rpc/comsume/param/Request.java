package com.rzm.rpc.comsume.param;

import lombok.Data;

public class Request {



    private String id;

    private Object[] params;

    private Class<?>[] paramsType;

    private String methodName;

    private String className;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class<?>[] getParamsType() {
        return paramsType;
    }

    public void setParamsType(Class<?>[] paramsType) {
        this.paramsType = paramsType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
