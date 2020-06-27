package com.rzm.rpc.comsume.handler;

import com.alibaba.fastjson.JSON;
import com.rzm.rpc.comsume.param.Request;
import com.rzm.rpc.comsume.param.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Component
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {



    static Map<String,CompletableFuture> maps = new HashMap<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 注册");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel激活");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = JSON.parseObject(msg.toString(), Response.class);
//        System.out.println("读取服务器数据为"+ JSON.toJSONString(response));
        CompletableFuture completableFuture = maps.get(response.getRequestId());
        completableFuture.complete(response);
        maps.remove(response.getRequestId());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客戶端未注册册");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端未激活");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端读取数据完毕");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端异常");
        cause.printStackTrace();
    }

    public void send(Channel channel, Request request, CompletableFuture<Response> objectCompletableFuture) {

        maps.put(request.getId(),objectCompletableFuture);
        channel.writeAndFlush(request);
    }
}
