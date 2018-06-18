package ex.com;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Component;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Class> map = new HashMap<>();

    public ProfilingHandlerBeanPostProcessor() throws Exception {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        platformMBeanServer.registerMBean(controller, new ObjectName("profiling", "name", "controller"));
    }

    private ProfilingController controller = new ProfilingController();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beenClass = bean.getClass();
        if (beenClass.isAnnotationPresent(Profiling.class))
            map.put(beanName, beenClass);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        Class beenClass = map.get(beanName);
        if (beenClass != null) {
            return Proxy.newProxyInstance(beenClass.getClassLoader(), beenClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (controller.isEnabled()) {
                        System.out.println("Profiling");
                        long before = System.nanoTime();
                        Object reVal = method.invoke(bean, args);
                        long after = System.nanoTime();
                        System.out.println(after - before);
                        System.out.println("End");
                        return reVal;
                    } else {
                        return method.invoke(bean, args);
                    }
                }
            });
        }


        return bean;
    }
}
