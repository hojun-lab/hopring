package com.rojojun.hopringframework.bean.factory.support;

import com.rojojun.hopringframework.bean.BeanException;
import com.rojojun.hopringframework.core.io.DefaultResourceLoader;
import com.rojojun.hopringframework.core.io.Resource;
import com.rojojun.hopringframework.core.io.ResourceLoader;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{
    private final BeanDefinitionRegistry registry;
    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.resourceLoader = new DefaultResourceLoader();
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
