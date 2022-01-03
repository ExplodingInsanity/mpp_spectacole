import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface IService {
    List<Spectacol> getSpectacole();
    void register(IObserver client);
    Boolean cumparaLocuri(int client_id, int spectacolId, String locuri);
}
