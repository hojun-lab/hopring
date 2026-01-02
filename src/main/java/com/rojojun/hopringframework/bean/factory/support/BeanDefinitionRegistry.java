package com.rojojun.hopringframework.bean.factory.support;

import com.rojojun.hopringframework.bean.BeanDefinition;
import com.rojojun.hopringframework.bean.BeanException;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanName) throws BeanException;

    boolean containsBeanDefinition(String beanName);
}
