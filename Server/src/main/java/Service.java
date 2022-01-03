import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class Service implements IService {
    ArrayList<IObserver> clients = new ArrayList<IObserver>();
    boolean isStopped = false;
    int timeoutMs = 30000;
    int checkInterval = 2000;
    HashSet<Integer> ids = new HashSet<>();
    Long startTime = System.currentTimeMillis();


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

    @Override
    public void register(IObserver client) {
        clients.add(client);
    }

    @SneakyThrows
    @Override
    public Boolean cumparaLocuri(int client_id, int spectacolId, String locuri) {
        if(!isStopped && System.currentTimeMillis() - startTime >= timeoutMs){
            System.out.println("STOPPING!");
            isStopped = true;
            for(IObserver client : clients) {
                try{
                    client.ServerClosed();
                }
                catch (Exception e){
                }
            }
        }
        if(isStopped) return null;


        return executor.submit(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return buy(client_id,spectacolId, locuri);
                    }
                }
        ).get();
    }

    public synchronized Boolean buy(int client_id, int spectacolId, String locuri) {


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

    class Checker extends Thread {
        @lombok.SneakyThrows
        @Override
        public void run() {
            while (true) {
                if(isStopped) {
                    Thread.sleep(1000);
                    System.exit(0);
                }
                System.out.println("Time left: "+(timeoutMs - System.currentTimeMillis() - startTime));
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

                Thread.sleep(checkInterval);
            }
        }
    }

}
