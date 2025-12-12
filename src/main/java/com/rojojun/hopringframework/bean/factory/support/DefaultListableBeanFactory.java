package com.rojojun.hopringframework.bean.factory.support;

import com.rojojun.hopringframework.bean.BeanDefinition;
import com.rojojun.hopringframework.bean.BeanException;
import com.rojojun.hopringframework.bean.factory.BeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String name) throws BeanException {
        Object sharedInstance = singletonObjects.get(name);
        if (sharedInstance != null) {
            return sharedInstance;
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean = createBean(name, beanDefinition);
        singletonObjects.put(name, bean);
        return bean;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeanException {
        return Optional.ofNullable(beanDefinitionMap.get(beanName))
                .orElseThrow(() -> new BeanException("빈을 찾을 수 없습니다 : " + beanName));
    }

    private Object createBean(String name, BeanDefinition beanDefinition) {
        try {
             Constructor<?> constructor = beanDefinition.getBeanClass()
                    .getDeclaredConstructor();
             constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
