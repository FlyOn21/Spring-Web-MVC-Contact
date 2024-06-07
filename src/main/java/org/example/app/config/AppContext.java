package org.example.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan("org.example.app")
@PropertySource("classpath:db/db.properties")
public class AppContext {

    @Autowired
    Environment environment;

    @Bean
    DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource =
                new DriverManagerDataSource();
        driverManagerDataSource.setUrl(getDbUrl());
        driverManagerDataSource.setUsername(environment.getProperty("MYSQL_USER"));
        driverManagerDataSource.setPassword(environment.getProperty("MYSQL_PASSWORD"));
        driverManagerDataSource.setDriverClassName(
                Objects.requireNonNull(environment.getProperty("MYSQL_JDBC_DRIVER")));
        return driverManagerDataSource;
    }

    private String getDbUrl() {
        return String.format("%s%s:%s/%s", environment.getProperty("MYSQL_JDBC_URL_PREFIX"), environment.getProperty("MYSQL_HOST"), environment.getProperty("MYSQL_HOST_PORT"), environment.getProperty("MYSQL_DATABASE"));
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("org.example.app.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setAnnotatedClasses(
                org.example.app.domain.entity.customer.Customer.class
        );
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", environment.getProperty("MYSQL_JDBC_DRIVER"));
        properties.setProperty("hibernate.connection.username", environment.getProperty("MYSQL_USER"));
        properties.setProperty("hibernate.connection.password", environment.getProperty("MYSQL_PASSWORD"));
        properties.setProperty("hibernate.dialect", environment.getProperty("HIBERNATE_DIALECT"));
        properties.setProperty("hibernate.show_sql", environment.getProperty("HIBERNATE_SHOW_SQL"));
        properties.setProperty("hibernate.format_sql", environment.getProperty("HIBERNATE_FORMAT_SQL"));
        properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("HIBERNATE_HBM2DDL_AUTO"));
        properties.setProperty("hibernate.current_session_context_class", environment.getProperty("HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS"));
        properties.setProperty("hibernate.connection.pool_size", environment.getProperty("HIBERNATE_CONNECTION_POOL_SIZE"));
        properties.setProperty("hibernate.generate_statistics", environment.getProperty("HIBERNATE_GENERATE_STATISTICS"));
        return properties;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
