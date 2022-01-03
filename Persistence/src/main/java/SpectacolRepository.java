import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.stream.Collectors;

public class SpectacolRepository {
//    private static final Logger logger = LogManager.getLogger(SpectacolRepository.class);
    Connection connection;
    public SpectacolRepository(Properties jdbcProps) {
//        logger.error("SALUT");
        connection = dbUtils.getInstance().getConnection(jdbcProps);
    }

    public synchronized Spectacol Add(Spectacol entity) {
        if(entity.lista_locuri_vandute == null || entity.lista_locuri_vandute.isBlank()) entity.setLista_locuri_vandute(domainUtils.locuriSpectacol);
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            Transaction tr = session.beginTransaction();
            session.save(entity);
            tr.commit();
            return entity;
        }
    }

    public synchronized Spectacol Update(Spectacol entity) {
        if(entity.lista_locuri_vandute == null || entity.lista_locuri_vandute.isBlank()) entity.setLista_locuri_vandute(domainUtils.locuriSpectacol);
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            Transaction tr = session.beginTransaction();
            session.update(entity);
            tr.commit();
            return entity;
        }
    }


    public List<Spectacol> FindAll() {
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            return session.createQuery("From Spectacol", Spectacol.class).stream().collect(Collectors.toList());
        }
    }

    public Spectacol FindById(Integer id) {
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            System.out.println("looking for id = "+id);
            return session.createQuery("From Spectacol where id = "+id, Spectacol.class).getSingleResult();
        }
    }

    public synchronized void DeleteAll() {
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            Transaction tr = session.beginTransaction();
            session.createQuery("Delete from Spectacol").executeUpdate();
            tr.commit();
        }
    }
}