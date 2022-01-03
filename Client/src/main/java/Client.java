import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Client extends UnicastRemoteObject implements Serializable,IObserver {
    Random random = new Random();
    Integer my_id = random.nextInt();

    protected Client() throws RemoteException {
    }

    Thread t;

    public void run() throws InterruptedException, ExecutionException {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:client-spring.xml");
        IService service = (IService)factory.getBean("serviceImpl");
        System.out.println("Obtained a reference to remote chat server");
        service.register(this);

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


            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Boolean response = service.cumparaLocuri(my_id,showId,builder.toString());
                    if(response == null){
                        System.out.println("SERVER STOPPED!!!");
                        System.exit(0);
                    }
                    if(response)
                        System.out.println(String.format("True - bought places %s for show %d",builder.toString(),showId));
                    else System.out.println(String.format("False - couldn't buy places %s for show %d",builder.toString(),showId));
                }
            });
            t.start();
            Thread.sleep(2000);
//            t.join();
        }

    }

    @Override
    public void ServerClosed() throws RemoteException {
        System.out.println("Server closed, goodbye!");
        UnicastRemoteObject.unexportObject(this, true);

//        t.interrupt();
        System.exit(0);
    }
}
