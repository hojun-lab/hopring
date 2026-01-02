package com.rojojun.hopringframework.bean.factory.support;

import com.rojojun.hopringframework.bean.BeanException;
import com.rojojun.hopringframework.core.io.Resource;
import com.rojojun.hopringframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(String location) throws BeanException;

    void loadBeanDefinitions(Resource resource) throws BeanException;

    void loadBeanDefinitions(String... locations) throws BeanException;
}
