import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class VanzareRepository {
    Connection connection;
    public VanzareRepository(Properties jdbcProps) {
        connection = dbUtils.getInstance().getConnection(jdbcProps);
    }

    public synchronized Vanzare Add(Vanzare entity) {
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            Transaction tr = session.beginTransaction();
            session.save(entity);
            tr.commit();
            return entity;
        }
    }

    public List<Vanzare> FindAll() {
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            return session.createQuery("From Vanzare", Vanzare.class).stream().collect(Collectors.toList());
        }
    }

    public List<Vanzare> FindBySpectacolId(Integer id) {
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            return session.createQuery("From Vanzare where id_spectacol="+id, Vanzare.class).stream().collect(Collectors.toList());
        }
    }

    public synchronized void DeleteAll() {
        try(Session session = dbUtils.getInstance().getSessionFactory().openSession()) {
            Transaction tr = session.beginTransaction();
            session.createQuery("Delete from Vanzare").executeUpdate();
            tr.commit();
        }
        catch(Exception e){
            System.out.println("EROARE "+e.getMessage());
        }
    }

}
