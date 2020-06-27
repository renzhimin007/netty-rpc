package com.rzm.rpc.comsume.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.rzm.rpc.comsume.client.RpcClient;
import com.rzm.rpc.comsume.param.Request;
import com.rzm.rpc.comsume.param.Response;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Component
public class ProxyFactory<T> implements InvocationHandler {


    @Autowired
    RpcClient rpcClient;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理方法执行");
        Request request = new Request();
        request.setId(UUID.randomUUID().toString());
        Class<?> declaringClass = method.getDeclaringClass();
        request.setClassName(declaringClass.getSimpleName());
        request.setMethodName(method.getName());
        request.setParams(args);
        request.setParamsType(method.getParameterTypes());
        CompletableFuture<Response> objectCompletableFuture = new CompletableFuture<>();
        rpcClient.connectAndSend("localhost", 9000,request,objectCompletableFuture);
        Response response = objectCompletableFuture.get();
//        System.out.println(JSON.toJSONString(response));
        Object data = response.getData();
        Class<?> returnType = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();
        if (returnType.isPrimitive()||String.class.isAssignableFrom(returnType)){
            return data;
        }else if (Collection.class.isAssignableFrom(returnType)){
            ParameterizedType genericReturn = (ParameterizedType) genericReturnType;
            Type type = genericReturn.getActualTypeArguments()[0];
            String[] split = type.toString().split(" ");
            Class<?> aClass = Class.forName(split[1]);
            return JSONArray.parseArray(data.toString(),aClass);
        }
        return null;
    }
}
