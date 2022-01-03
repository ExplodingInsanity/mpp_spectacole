import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class dbUtils {
    private static dbUtils instance = null;

    private Connection connection;
    public Connection getConnection(Properties props) {
        try {
            return DriverManager.getConnection(props.getProperty("jdbc.url"),
                    props.getProperty("jdbc.user"),
                    props.getProperty("jdbc.password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SessionFactory getSessionFactory(){
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            return new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
        System.out.println("RETURNEZ NULL");
        return null;
    }

    public static dbUtils getInstance()
    {
        if(instance == null) instance = new dbUtils();
        return instance;
    }
}