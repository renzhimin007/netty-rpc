package com.rzm.rpc.provide.server;


import com.rzm.rpc.comsume.codec.JSONDECODE;
import com.rzm.rpc.comsume.codec.JSONENCODER;
import com.rzm.rpc.provide.anno.RpcService;
import com.rzm.rpc.provide.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class RpcServer implements InitializingBean,ApplicationContextAware {





    static Map<String,Object> map = new HashMap<String, Object>(10);



    public void start(int port){
        ServerHandler serverHandler = new ServerHandler(map);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup(1);

        NioEventLoopGroup work = new NioEventLoopGroup(2);

        serverBootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        pipeline.addLast(new IdleStateHandler(0,0,30));
                        pipeline.addLast(new JSONENCODER());
                        pipeline.addLast(new JSONDECODE());
                        pipeline.addLast(serverHandler);
                    }
                });

        try {
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            System.out.println("服务端启动成功  port = " + port);
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    ApplicationContext applicationContext;

    public void afterPropertiesSet() throws Exception {

        start(9000);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (CollectionUtils.isEmpty(beansWithAnnotation)){
            System.out.println("无可加载的service");
            return;
        }
        Collection<Object> values = beansWithAnnotation.values();
        for (Object value : values) {
            Class<?>[] interfaces = value.getClass().getInterfaces();
            for (Class<?> anInterface : interfaces) {
                String simpleName = anInterface.getSimpleName();
                map.put(simpleName,value);
                System.out.println("加载服务service "+simpleName);
            }

        }
    }
}
