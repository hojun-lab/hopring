import com.rojojun.hopringframework.bean.BeanDefinition;
import com.rojojun.hopringframework.bean.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
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

    static class HelloClass {
        public String say() {
            return "Hello!";
        }
    }
}
