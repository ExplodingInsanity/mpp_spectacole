import java.util.List;
import java.util.concurrent.Future;

public interface IService {
    List<Spectacol> getSpectacole();
    Boolean cumparaLocuri(int spectacolId, String locuri);
}
