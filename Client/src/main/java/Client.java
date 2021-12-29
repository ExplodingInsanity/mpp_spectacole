import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
    public static void main(String[] args) {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:client-spring.xml");
        IService service = (IService)factory.getBean("serviceImpl");
        System.out.println("Obtained a reference to remote chat server");

        service.getSpectacole().forEach(System.out::println);
    }
}
