package com.rzm.rpc.comsume.anno;


import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RpcScannerConfig.class)
public @interface EnableRpcProxy {

    String basePackage() default "";
}
