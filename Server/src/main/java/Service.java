import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

public class Service implements IService {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    SpectacolRepository spectacolRepository;
    VanzareRepository vanzareRepository;
    final Logger logger = LogManager.getLogger(SpectacolRepository.class);

    public Service(SpectacolRepository spectacolRepo, VanzareRepository vanzareRepo) {
        this.spectacolRepository = spectacolRepo;
        this.vanzareRepository = vanzareRepo;
        vanzareRepo.DeleteAll();
        spectacolRepo.DeleteAll();
        mockValues();
        Checker checker = new Checker();
        checker.start();
    }

    public void mockValues() {
        spectacolRepository.Add(Spectacol.builder().id(1).pret_bilet(100f).titlu("Spectacol 1").sold(0f).build());
        spectacolRepository.Add(Spectacol.builder().id(2).pret_bilet(200f).titlu("Spectacol 2").sold(0f).build());
        spectacolRepository.Add(Spectacol.builder().id(3).pret_bilet(150f).titlu("Spectacol 3").sold(0f).build());
    }

    @Override
    public List<Spectacol> getSpectacole() {
        return spectacolRepository.FindAll();
    }

    @SneakyThrows
    @Override
    public Boolean cumparaLocuri(int spectacolId, String locuri) {
        Thread.sleep(1000);
        return executor.submit(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return buy(spectacolId, locuri);
                    }
                }
        ).get();
    }

    public boolean buy(int spectacolId, String locuri) {
        Spectacol spectacol = spectacolRepository.FindById(spectacolId);
//        if(spectacol.lista_locuri_vandute.isBlank()){
//            String gol = "";
//            for(int i = 0 ; i <= spectacol.getCapacitate(); i++)  gol += "0";
//            spectacol.setLista_locuri_vandute(gol);
//        }
        StringBuilder newListaLocuriVandute = new StringBuilder(spectacol.lista_locuri_vandute);
        Integer nrLoc = 0;
        for (String loc : locuri.split(" ")) {
            nrLoc = Integer.valueOf(loc);
            if (spectacol.lista_locuri_vandute.charAt(nrLoc) == '1') {
                return false;
            }
            newListaLocuriVandute.setCharAt(nrLoc, '1');
        }
        spectacol.setLista_locuri_vandute(newListaLocuriVandute.toString());
        spectacol.setSold(spectacol.getSold() + domainUtils.getNrLocuriSpatiu(locuri) * spectacol.getPret_bilet());
        spectacolRepository.Update(spectacol);
        vanzareRepository.Add(
                Vanzare.builder().data_vanzare(LocalDateTime.now())
                        .lista_locuri_vandute(locuri)
                        .nr_bilete_vandute(domainUtils.getNrLocuri(locuri))
                        .pret_bilet(spectacol.getPret_bilet())
                        .suma(domainUtils.getPretTotal(locuri, spectacol.getPret_bilet()))
                        .id_spectacol(spectacol.getId())
                        .build()
        );
            return true;

    }

//    class Buyer extends Thread {
//        int spectacolId;
//        String locuri;
//        private volatile Boolean result = null;
//        public Buyer(int spectacolId, String locuri) {
//        }
//
//        @SneakyThrows
//        @Override
//        public void run() {
//            Spectacol spectacol = spectacolRepository.FindById(spectacolId);
////        if(spectacol.lista_locuri_vandute.isBlank()){
////            String gol = "";
////            for(int i = 0 ; i <= spectacol.getCapacitate(); i++)  gol += "0";
////            spectacol.setLista_locuri_vandute(gol);
////        }
//            StringBuilder newListaLocuriVandute = new StringBuilder(spectacol.lista_locuri_vandute);
//            Integer nrLoc = 0;
//            for (String loc : locuri.split(" ")) {
//                nrLoc = Integer.valueOf(loc);
//                if (spectacol.lista_locuri_vandute.charAt(nrLoc) == '1') {
//                    result = false;
//                    waitForConsumption();
//                    return ;//false;
//                }
//                newListaLocuriVandute.setCharAt(nrLoc, '1');
//            }
//            spectacol.setLista_locuri_vandute(newListaLocuriVandute.toString());
//            spectacol.setSold(spectacol.getSold() + domainUtils.getNrLocuriSpatiu(locuri) * spectacol.getPret_bilet());
//            spectacolRepository.Update(spectacol);
//            vanzareRepository.Add(
//                    Vanzare.builder().data_vanzare(LocalDateTime.now())
//                            .lista_locuri_vandute(locuri)
//                            .nr_bilete_vandute(domainUtils.getNrLocuri(locuri))
//                            .pret_bilet(spectacol.getPret_bilet())
//                            .suma(domainUtils.getPretTotal(locuri, spectacol.getPret_bilet()))
//                            .id_spectacol(spectacol.getId())
//                            .build()
//            );
//            result = true;
//            waitForConsumption();
////            return true;
//
//        }
//
//        public boolean getResult(){
//            while(result == null) {
//
//            }
//            boolean retVal = result;
//            result = null;
//            return retVal;
//        }
//
//        public void waitForConsumption(){
//            while(result!=null){
//
//            }
//        }
//
//        }

    class Checker extends Thread {
        @lombok.SneakyThrows
        @Override
        public void run() {
            while (true) {
                List<Spectacol> spectacole = spectacolRepository.FindAll();
                for (Spectacol spectacol : spectacole) {
                    float realSold = 0f;
                    StringBuilder output = new StringBuilder();
                    output.append(spectacol.getSold().toString() + "\n");
                    List<Vanzare> vanzari = vanzareRepository.FindBySpectacolId(spectacol.getId());
                    StringBuilder toateLocurile = new StringBuilder();

                    for (Vanzare vanzare : vanzari) {
                        output.append(vanzare + "\n");
                        realSold += spectacol.getPret_bilet() * domainUtils.getNrLocuriSpatiu(vanzare.getLista_locuri_vandute());
                        toateLocurile.append(vanzare.getLista_locuri_vandute()+" ");
                    }
                    Integer nrLoc = 0;
                    StringBuilder newLocuri = new StringBuilder(domainUtils.locuriSpectacol);
                    for (String loc : toateLocurile.toString().split(" ")) {
                        if (loc.isBlank()) continue;
                        nrLoc = Integer.valueOf(loc);
                        newLocuri.setCharAt(nrLoc, '1');
                    }


                    output.append((spectacol.getSold().equals(realSold) && newLocuri.toString().equals(spectacol.lista_locuri_vandute)) ? "corect" : "incorect");
                    if(!spectacol.getSold().equals(realSold) || !newLocuri.toString().equals(spectacol.lista_locuri_vandute)) {
                        output.append(String.format(",%f!=%f\n%s!=%s",spectacol.getSold(),realSold,spectacol.lista_locuri_vandute,newLocuri));
                    }
                    logger.error(output.toString());
                }
                logger.error("\n\n\n");

                Thread.sleep(2000);
            }
        }
    }

}
