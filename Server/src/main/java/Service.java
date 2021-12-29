import com.example.domain.Spectacol;

import java.util.List;

public class Service implements IService{
    @Override
    public List<Spectacol> getSpectacole() {
        return List.of(
                Spectacol.builder().id(1).pret_bilet(50f).build(),
                Spectacol.builder().id(2).pret_bilet(100f).build()
        );
    }
}
