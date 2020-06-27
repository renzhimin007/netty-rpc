package com.rzm.rpc.comsume.anno;

import com.rzm.rpc.comsume.factory.RpcFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

public class ClassPathRpcScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathRpcScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }


    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        processBeanDefinition(beanDefinitionHolders);
        return beanDefinitionHolders;
    }

    private void processBeanDefinition(Set<BeanDefinitionHolder> beanDefinitionHolders) {

        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClass(RpcFactoryBean.class);
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        return true;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }
}
