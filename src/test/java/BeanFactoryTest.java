import com.rojojun.hopringframework.bean.BeanDefinition;
import com.rojojun.hopringframework.bean.MutablePropertyValues;
import com.rojojun.hopringframework.bean.PropertyValue;
import com.rojojun.hopringframework.bean.factory.config.BeanReference;
import com.rojojun.hopringframework.bean.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    @DisplayName("빈 등록이 되는지 확인하는 테스트 코드")
    @Test
    void testBeanFactory() {
        String className = "helloClass";

        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();

        BeanDefinition beanDefinition = new BeanDefinition(HelloClass.class);
        defaultListableBeanFactory.registerBeanDefinition(className, beanDefinition);

        HelloClass helloClass = (HelloClass) defaultListableBeanFactory.getBean(className);

        assertNotNull(helloClass);
        assertEquals("Hello!", helloClass.say());

        HelloClass helloClass2 = (HelloClass) defaultListableBeanFactory.getBean(className);
        assertEquals(helloClass, helloClass2);
    }

    @DisplayName("DI가 되는지 확인하는 테스트 코드")
    @Test
    void testDependencyInjection() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        BeanDefinition daoDef = new BeanDefinition(UserDao.class);
        factory.registerBeanDefinition("userDao", daoDef);

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        BeanDefinition serviceDef = new BeanDefinition(UserService.class, propertyValues);
        factory.registerBeanDefinition("userService", serviceDef);

        UserService userService = (UserService) factory.getBean("userService");

        assertNotNull(userService.getUserDao());
        assertEquals("user_dao", userService.getUserDao().getName());
    }

    static class HelloClass {
        public String say() {
            return "Hello!";
        }
    }

    static class UserDao {
        public String getName() {
            return "user_dao";
        }
    }

    static class UserService {
        private UserDao userDao;

        public UserDao getUserDao() {
            return userDao;
        }
    }
}
