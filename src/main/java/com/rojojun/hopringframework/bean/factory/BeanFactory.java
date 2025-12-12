package com.rojojun.hopringframework.bean.factory;

import com.rojojun.hopringframework.bean.BeanException;

public interface BeanFactory {
    Object getBean(String name) throws BeanException;
}
