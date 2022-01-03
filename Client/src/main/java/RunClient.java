import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

public class RunClient {
    public static void main(String[] args) throws ExecutionException, InterruptedException, RemoteException {
        Client c = new Client();
        c.run();
    }
}
