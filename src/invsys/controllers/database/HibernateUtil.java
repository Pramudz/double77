package invsys.controllers.database;

import java.io.FileInputStream;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;
	

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Properties prop = new Properties();
				prop.load(new FileInputStream("resources/dbConfig.properties"));
				
				String user = prop.getProperty("user");
				String password = prop.getProperty("password");
				String dbName = prop.getProperty("databaseName");
				String dbIpAddr = prop.getProperty("databaseIpAddress");
								
				Configuration config = new Configuration();
			
				config.setProperty("hibernate.connection.url", "jdbc:mysql://"+dbIpAddr+":3306/"+dbName+"?createDatabaseIfNotExist=true");
			    config.setProperty("hibernate.connection.username", user);
			    config.setProperty("hibernate.connection.password", password);
				//config.configure("hibernate.cfg.xml");
				//File config = new File("resources/hibernate.cfg.xml");
				
				// Create registry
				registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).configure().build();

				// Create MetadataSources
				MetadataSources sources = new MetadataSources(registry);

				// Create Metadata
				Metadata metadata = sources.getMetadataBuilder().build();

				// Create SessionFactory
				sessionFactory = metadata.getSessionFactoryBuilder().build();

			} catch (Exception e) {
				e.printStackTrace();
				if (registry != null) {
					StandardServiceRegistryBuilder.destroy(registry);
				}
			}
		}
		return sessionFactory;
	}

	public static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}