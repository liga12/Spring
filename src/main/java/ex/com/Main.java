package ex.com;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) throws InterruptedException {

//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(Quoter.class).sayQuote();

//



    }
}
