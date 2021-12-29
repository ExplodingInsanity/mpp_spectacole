import java.time.LocalDateTime;
import java.util.List;

public class Service implements IService{
    SpectacolRepository spectacolRepository;
    VanzareRepository vanzareRepository;

    public Service(SpectacolRepository spectacolRepo, VanzareRepository vanzareRepo) {
        this.spectacolRepository = spectacolRepo;
        this.vanzareRepository = vanzareRepo;
        vanzareRepo.DeleteAll();
        spectacolRepo.DeleteAll();
        mockValues();
    }

    public void mockValues(){
        spectacolRepository.Add(Spectacol.builder().id(1).pret_bilet(100f).titlu("Spectacol 1").build());
        spectacolRepository.Add(Spectacol.builder().id(2).pret_bilet(200f).titlu("Spectacol 2").build());
        spectacolRepository.Add(Spectacol.builder().id(3).pret_bilet(150f).titlu("Spectacol 3").build());
    }

    @Override
    public List<Spectacol> getSpectacole() {
        return spectacolRepository.FindAll();
//        return List.of(
//                Spectacol.builder().id(1).pret_bilet(50f).build(),
//                Spectacol.builder().id(2).pret_bilet(100f).build()
//        );
    }

    @Override
    public boolean cumparaLocuri(int spectacolId, String locuri) {
        Spectacol spectacol = spectacolRepository.FindById(spectacolId);
//        if(spectacol.lista_locuri_vandute.isBlank()){
//            String gol = "";
//            for(int i = 0 ; i <= spectacol.getCapacitate(); i++)  gol += "0";
//            spectacol.setLista_locuri_vandute(gol);
//        }
        StringBuilder newListaLocuriVandute = new StringBuilder(spectacol.lista_locuri_vandute);
        for (String loc : locuri.split(" ")){
            Integer nrLoc = Integer.valueOf(loc);
            if(spectacol.lista_locuri_vandute.charAt(nrLoc) == '1') return false;
            newListaLocuriVandute.setCharAt(nrLoc,'1');
        }
        spectacol.setLista_locuri_vandute(newListaLocuriVandute.toString());
        spectacolRepository.Update(spectacol);
        vanzareRepository.Add(
                Vanzare.builder().data_vanzare(LocalDateTime.now())
                        .lista_locuri_vandute(locuri)
                        .nr_bilete_vandute(domainUtils.getNrLocuri(locuri))
                        .pret_bilet(spectacol.getPret_bilet())
                        .suma(domainUtils.getPretTotal(locuri,spectacol.getPret_bilet()))
                        .build()
        );
        return true;
    }
}
