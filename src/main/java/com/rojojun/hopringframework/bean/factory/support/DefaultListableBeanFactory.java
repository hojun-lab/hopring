package com.rojojun.hopringframework.bean.factory.support;

import com.rojojun.hopringframework.bean.BeanDefinition;
import com.rojojun.hopringframework.bean.BeanException;
import com.rojojun.hopringframework.bean.PropertyValue;
import com.rojojun.hopringframework.bean.factory.BeanFactory;
import com.rojojun.hopringframework.bean.factory.config.BeanReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

            Object bean = constructor.newInstance();
            populateBean(name, beanDefinition, bean);
            return bean;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, Object beanWrapper) {
        for (PropertyValue propertyValue: beanDefinition.getPropertyValues().propertyValueList()) {
            String name = propertyValue.name();
            Object value = propertyValue.value();

            try {
                if (value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.beanName());
                }

                Field field = beanDefinition.getBeanClass().getDeclaredField(name);
                field.setAccessible(true);

                field.set(beanWrapper, value);
            } catch (NoSuchFieldException e) {
                throw new BeanException("Field '" + name + "' not found in bean '" + beanName + "'");
            } catch (IllegalAccessException e) {
                throw new BeanException("Failed to inject property '" + name + "' into bean '" + beanName + "'", e);            }
        }
    }
}
