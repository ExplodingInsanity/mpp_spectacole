import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {
    void ServerClosed() throws RemoteException;
}
