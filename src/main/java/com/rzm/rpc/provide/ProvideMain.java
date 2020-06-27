package com.rzm.rpc.provide;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class ProvideMain {



    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ProvideConfig.class);
        System.out.println("服务提供者启动完毕");
    }

}
