package com.rzm.rpc.comsume.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

public class RpcFactoryBean<T> implements FactoryBean<T> {

    Class<T> tClass;

    @Autowired
    ProxyFactory proxyFactory;

    public RpcFactoryBean(){}

    public RpcFactoryBean(Class<T> tClass){
        this.tClass = tClass;
    }


    public T getObject() throws Exception {

        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),new Class[]{tClass},proxyFactory);
    }

    public Class<?> getObjectType() {
        return tClass;
    }

    public boolean isSingleton() {
        return true;
    }
}
