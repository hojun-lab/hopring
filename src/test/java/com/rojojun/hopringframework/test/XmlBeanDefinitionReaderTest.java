package com.rojojun.hopringframework.test;

import com.rojojun.hopringframework.bean.factory.support.DefaultListableBeanFactory;
import com.rojojun.hopringframework.bean.factory.support.XmlBeanDefinitionReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XmlBeanDefinitionReaderTest {

    @DisplayName("XML 파일을 읽어서 빈을 등록하고 의존성을 주입한다")
    @Test
    void testBeanDefinitionReader() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinitions("classpath:hopring.xml");

        UserService userService = (UserService) factory.getBean("userService");

        assertNotNull(userService);
        assertNotNull(userService.getUserDao());
        assertEquals("XML_DAO", userService.getUserDao().getName());
    }

    public static class UserDao {
        public String getName() {
            return "XML_DAO";
        }
    }

    public static class UserService {
        private UserDao userDao;

        public void setUserDao(UserDao userDao) {
            this.userDao = userDao;
        }

        public UserDao getUserDao() {
            return this.userDao;
        }
    }
}
