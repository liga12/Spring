package ex.com;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuoterTest {

    @Test
    public void sayQuoter() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        context.getBean(Quoter.class).sayQuote();
    }
}