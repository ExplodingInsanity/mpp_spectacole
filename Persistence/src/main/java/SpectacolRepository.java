import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class SpectacolRepository {
    Connection connection;
    public SpectacolRepository(Properties jdbcProps) {
        connection = dbUtils.getInstance().getConnection(jdbcProps);
    }

    public Spectacol Add(Spectacol entity) {
        if(entity.lista_locuri_vandute == null || entity.lista_locuri_vandute.isBlank()) entity.setLista_locuri_vandute("0000000000000000000000000");
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            Transaction tr = session.beginTransaction();
            session.save(entity);
            tr.commit();
            return entity;
        }
    }

    public Spectacol Update(Spectacol entity) {
        if(entity.lista_locuri_vandute == null || entity.lista_locuri_vandute.isBlank()) entity.setLista_locuri_vandute("0000000000000000000000000");
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

    public void DeleteAll() {
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            Transaction tr = session.beginTransaction();
            session.createQuery("Delete from Spectacol").executeUpdate();
            tr.commit();
        }
    }
}