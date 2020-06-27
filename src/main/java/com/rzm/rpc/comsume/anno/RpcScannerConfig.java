package com.rzm.rpc.comsume.anno;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Set;

public class RpcScannerConfig implements ImportBeanDefinitionRegistrar {


    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<String, Object> annotationAttributes =
                annotationMetadata.getAnnotationAttributes(EnableRpcProxy.class.getName());
        String basekPackage = String.valueOf(annotationAttributes.get("basePackage"));
        ClassPathRpcScanner classPathRpcScanner = new ClassPathRpcScanner(beanDefinitionRegistry);
        Set<BeanDefinitionHolder> beanDefinitionHolders =
                classPathRpcScanner.doScan(new String[]{basekPackage});
    }

}
