package com.rzm.rpc.comsume;


import com.rzm.rpc.comsume.anno.EnableRpcProxy;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

@Configurable
@ComponentScan("com.rzm.rpc.comsume")
@EnableRpcProxy(basePackage = "com.rzm.rpc.comsume.service")
public class ComsumeConfig {
}
