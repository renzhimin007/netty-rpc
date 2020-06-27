package com.rzm.rpc.provide.handler;

import com.alibaba.fastjson.JSON;
import com.rzm.rpc.provide.channe.ChannelGroup;
import com.rzm.rpc.provide.param.Request;
import com.rzm.rpc.provide.param.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.IvParameterSpec;
import java.lang.reflect.Method;
import java.util.Map;


@Component
@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String,Object> serviceMap;



    @Autowired
    ChannelGroup channelGroup;

    public ServerHandler(Map<String, Object> map) {
        this.serviceMap = map;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = JSON.parseObject(msg.toString(), Request.class);
        String methodName = request.getMethodName();
        if ("heartBeat".equals(methodName)){
            System.out.println("心跳");
        }else {
            System.out.println(JSON.toJSONString(request));
            String className = request.getClassName();
            Object o = serviceMap.get(className);
            Response response;
            if (null==o){
                response = new Response();
                response.setRequestId(request.getId());
                response.setCode("调用失败");
                response.setCode("0");
                response.setData(null);
            }else {
                Class<?>[] paramsType = request.getParamsType();
                Object[] params = request.getParams();
                Class<?> aClass = o.getClass();
                Method method = aClass.getMethod(methodName, paramsType);
                Object invoke = method.invoke(o, getParams(paramsType,params));
                response = new Response();
                response.setRequestId(request.getId());
                response.setData(invoke);
                response.setCode("200");
                response.setMsg("success");
                ctx.writeAndFlush(response);
                System.out.println("处理客户端请求完成");
            }
        }
    }

    private Object[] getParams(Class<?>[] paramsType, Object[] params) {

        if (params==null||paramsType==null){
            return null;
        }
        Object[] objects = new Object[params.length];
        for (int i=0;i<params.length;i++){
            if (paramsType[i].isAssignableFrom(String.class)){
                objects[i] = params[i];
            }else {
                objects[i] = JSON.parseObject(params[i].toString(),paramsType[i]);
            }
        }
        return objects;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//        super.channelRegistered(ctx);
        log.info("客户端注册 {}",ctx.channel().id().asLongText());
        System.out.println("客户端注册");
//        channelGroup.add(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//        super.channelUnregistered(ctx);
        log.info("客户端断开连接");
        System.out.println("客户端断开连接");
//        channelGroup.del(ctx.channel());

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        log.info("客户端激活 {}",ctx.channel().id().asLongText());
        System.out.println("客户端激活");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
        log.info("客户端未激活 {}",ctx.channel().id().asLongText());
        System.out.println("客户端未激活");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        log.info("数据读取完毕 {}" ,ctx.channel().id().asLongText());
        System.out.println("数据读取完毕");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        log.info("客户端发生异常 {}",ctx.channel().id().asLongText());
        System.out.println("客户端发生异常");
    }
}
