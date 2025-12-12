package com.rojojun.hopringframework.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record MutablePropertyValues(List<PropertyValue> propertyValueList) {
    public MutablePropertyValues() {
        this(new ArrayList<>());
    }

    public MutablePropertyValues(List<PropertyValue> propertyValueList) {
        this.propertyValueList = Optional.ofNullable(propertyValueList).orElseGet(ArrayList::new);
    }

    public void addPropertyValue(PropertyValue propertyValue) {
        if (this.propertyValueList.contains(propertyValue)) {
            throw new BeanException("중복된 Bean을 가지고 있습니다");
        }
        this.propertyValueList.add(propertyValue);
    }
}
