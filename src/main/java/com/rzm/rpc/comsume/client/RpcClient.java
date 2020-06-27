package com.rzm.rpc.comsume.client;


import com.rzm.rpc.comsume.codec.JSONDECODE;
import com.rzm.rpc.comsume.codec.JSONENCODER;
import com.rzm.rpc.comsume.handler.ClientChannelHandler;
import com.rzm.rpc.comsume.param.Request;
import com.rzm.rpc.comsume.param.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class RpcClient {


    @Autowired
    ClientChannelHandler clientChannelHandler;

    static Map<String,Channel> map = new HashMap<String,Channel>();

    Bootstrap bootstrap;

    public RpcClient(){
        bootstrap = new Bootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup(1);

        bootstrap.group(boss)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new JSONENCODER());
                        pipeline.addLast(new JSONDECODE());
                        pipeline.addLast(clientChannelHandler);
                    }
                });
    }
    public Channel connect(String host,int port){
        if (map.containsKey(host+":"+port)){
            Channel channel = map.get(host + ":" + port);
            if (channel.isOpen()){
                System.out.println("该channel已存在，不用重复连接");
                return channel;
            }
        }
        Channel channel = null;
        try {
            ChannelFuture sync = bootstrap.connect(host, port);
            channel = sync.sync().channel();
            map.put(host+":"+port,channel);
            return channel;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }




    public void connectAndSend(String host, int port, Request request, CompletableFuture<Response> objectCompletableFuture) {
        Channel channel = connect(host, port);
        clientChannelHandler.send(channel,request,objectCompletableFuture);


    }
}
