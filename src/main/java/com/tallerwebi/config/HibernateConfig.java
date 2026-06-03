package com.tallerwebi.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class HibernateConfig {

  @Value("${db.type}")
  private String dbType;
  @Value("${db.host}")
  private String dbHost;
  @Value("${db.port}")
  private String dbPort;
  @Value("${db.name}")
  private String dbName;
  @Value("${db.username}")
  private String dbUser;
  @Value("${db.password}")
  private String dbPassword;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();

    String jdbcUrl;
    String driverClassName;

    if ("postgres".equalsIgnoreCase(dbType)) {
      jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);
      driverClassName = "org.postgresql.Driver";
    } else {
      // Default to MySQL configuration
      jdbcUrl = String.format(
        "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8",
        dbHost,
        dbPort,
        dbName
      );
      driverClassName = "com.mysql.cj.jdbc.Driver";
    }

    dataSource.setUrl(jdbcUrl);
    dataSource.setUsername(dbUser);
    dataSource.setPassword(dbPassword);
    dataSource.setDriverClassName(driverClassName);
    return dataSource;
  }

  @Bean
  public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setPackagesToScan("com.tallerwebi.dominio", "com.tallerwebi.presentacion.dto");
    sessionFactory.setHibernateProperties(hibernateProperties());
    return sessionFactory;
  }

  @Bean
  public HibernateTransactionManager transactionManager() {
    return new HibernateTransactionManager(sessionFactory(dataSource()).getObject());
  }

  private Properties hibernateProperties() {
    Properties properties = new Properties();

    if ("postgres".equalsIgnoreCase(dbType)) {
      properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    } else {
      properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
    }
    properties.setProperty("hibernate.show_sql", "true");
    properties.setProperty("hibernate.format_sql", "true");
    properties.setProperty("hibernate.hbm2ddl.auto", "create");
    return properties;
  }
}
