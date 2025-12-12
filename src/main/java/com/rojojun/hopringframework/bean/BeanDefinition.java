package com.rojojun.hopringframework.bean;

import java.util.Optional;

public class BeanDefinition {
    private Class<?> beanClass;
    private MutablePropertyValues propertyValues;

    public BeanDefinition(Class<?> beanClass) {
        this(beanClass, null);
    }

    public BeanDefinition(Class<?> beanClass, MutablePropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = Optional.ofNullable(propertyValues).orElseGet(MutablePropertyValues::new);
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public MutablePropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = Optional.ofNullable(propertyValues).orElseGet(MutablePropertyValues::new);
    }
}
