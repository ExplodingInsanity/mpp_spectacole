import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Client {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:client-spring.xml");
        IService service = (IService)factory.getBean("serviceImpl");
        System.out.println("Obtained a reference to remote chat server");

        List<Spectacol> spectacole = service.getSpectacole();
        int maxlength = spectacole.get(0).lista_locuri_vandute.length()-1;
        Random random = new Random();
        while (true){
            StringBuilder builder = new StringBuilder("");
            int showId = random.nextInt(3)+1;
            int number = random.nextInt(5)+1;
            for(int i = 0; i < number; i++) {
                int placeAttempt = random.nextInt(maxlength)+1;
                while(builder.indexOf((placeAttempt+" "))!=-1) {
                    placeAttempt = random.nextInt(maxlength)+1;
                }
                builder.append((placeAttempt));
                if(i < number-1) builder.append(" ");
            }


            if(service.cumparaLocuri(showId,builder.toString()))
                System.out.println(String.format("True - bought places %s for show %d",builder.toString(),showId));
            else System.out.println(String.format("False - couldn't buy places %s for show %d",builder.toString(),showId));
            Thread.sleep(2000);
        }

    }
}
