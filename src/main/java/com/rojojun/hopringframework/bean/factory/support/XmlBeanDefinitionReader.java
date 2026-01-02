package com.rojojun.hopringframework.bean.factory.support;

import com.rojojun.hopringframework.bean.BeanDefinition;
import com.rojojun.hopringframework.bean.BeanException;
import com.rojojun.hopringframework.bean.PropertyValue;
import com.rojojun.hopringframework.bean.factory.config.BeanReference;
import com.rojojun.hopringframework.core.io.Resource;
import com.rojojun.hopringframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader{
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeanException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeanException {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinitions(inputStream);
        } catch (IOException e) {
            throw new BeanException("IOException parsing XML document from " + resource, e);
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeanException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    private void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        registerBeanDefinition(document);
    }

    private void registerBeanDefinition(Document document) {
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if ("bean".equals(element.getTagName())) {
                    processBeanDefinition(element);
                }
            }
        }
    }

    private void processBeanDefinition(Element element) {
        String id = element.getAttribute("id");
        String className = element.getAttribute("class");

        if (id == null || id.trim().isEmpty()) {
            throw new BeanException("Bean id attribute is required");
        }
        if (className == null || className.trim().isEmpty()) {
            throw new BeanException("Bean class attribute is required");
        }

        if (getRegistry().containsBeanDefinition(id)) {
            throw new BeanException("Duplicate beanName[" + id + "] is not allowed");
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeanException("Cannot find class [" + className + "] for bean with name '" + id + "'", e);
        }

        BeanDefinition beanDefinition = new BeanDefinition(clazz);

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element && "property".equals(((Element) node).getTagName())) {
                Element propertyElement = (Element) node;
                String name = propertyElement.getAttribute("name");
                String value = propertyElement.getAttribute("value");
                String ref = propertyElement.getAttribute("ref");

                Object finalValue;
                if (ref != null && !ref.trim().isEmpty()) {
                    finalValue = new BeanReference(ref);
                } else if (value != null && !value.trim().isEmpty()) {
                    finalValue = value;
                } else {
                    throw new BeanException("Property '" + name + "' in bean '" + id + "' must have either 'value' or 'ref' attribute");
                }

                beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, finalValue));
            }
        }

        if (getRegistry().containsBeanDefinition(id)) {
            throw new BeanException("Duplicate beanName[" + id + "] is not allowed");
        }
        getRegistry().registerBeanDefinition(id, beanDefinition);
    }
}
