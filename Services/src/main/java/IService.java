import java.util.List;

public interface IService {
    List<Spectacol> getSpectacole();
    boolean cumparaLocuri(int spectacolId, String locuri);
}
