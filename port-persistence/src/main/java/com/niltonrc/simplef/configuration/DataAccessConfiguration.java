package com.niltonrc.simplef.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories( basePackages = "com.niltonrc.simplef.persistence" )
@EnableTransactionManagement
public class DataAccessConfiguration
{
    @Bean
    FlywayMigrationStrategy getFlyWayMigrationStrategy( final Environment env )
    {
        return flyway -> {
            final String url = env.getRequiredProperty( "simplef.configuration.database.url" );
            final String username = env.getRequiredProperty( "simplef.configuration.database.username" );
            final String password = env.getRequiredProperty( "simplef.configuration.database.password" );
            flyway.configure()
                  .locations( "classpath:db/migration" )
                  .dataSource( url, username, password )
                  .load()
                  .migrate();
        };
    }

    @Bean
    DataSource getDataSource( final Environment env )
    {
        final HikariDataSource ds = new HikariDataSource();
        ds.setAutoCommit( false );
        ds.setAllowPoolSuspension( false );
        ds.setIdleTimeout( env.getRequiredProperty( "simplef.configuration.datasource.hikari.idle-timeout", Integer.class ) );
        ds.setConnectionTestQuery( env.getRequiredProperty( "simplef.configuration.datasource.hikari.connection-test-query" ) );
        ds.setConnectionTimeout( env.getRequiredProperty( "simplef.configuration.datasource.hikari.connection-timeout", Integer.class ) );
        ds.setMinimumIdle( env.getRequiredProperty( "simplef.configuration.datasource.hikari.minimum-idle", Integer.class ) );
        ds.setMaximumPoolSize( env.getRequiredProperty( "simplef.configuration.datasource.hikari.maximum-pool-size", Integer.class ) );
        ds.setPoolName( "SimpleForwarding-ConnectionPool" );
        ds.setDriverClassName( env.getRequiredProperty( "simplef.configuration.database.driverClassName" ) );
        ds.setJdbcUrl( env.getRequiredProperty( "simplef.configuration.database.url" ) );
        ds.setUsername( env.getRequiredProperty( "simplef.configuration.database.username" ) );
        ds.setPassword( env.getRequiredProperty( "simplef.configuration.database.password" ) );
        return ds;
    }

    @Bean( name = "entityManagerFactory", destroyMethod = "destroy" )
    LocalContainerEntityManagerFactoryBean getEntityManagerFactory(
            final DataSource dataSource,
            final Environment env )
    {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource( dataSource );
        entityManagerFactoryBean.setJpaVendorAdapter( new HibernateJpaVendorAdapter() );
        entityManagerFactoryBean.setJpaDialect( new HibernateJpaDialect() );
        entityManagerFactoryBean.setPackagesToScan( "com.niltonrc.simplef.entities", "com.niltonrc.simplef.persistence" );
        entityManagerFactoryBean.setJpaProperties( getDefaultJpaProperties( env ) );
        entityManagerFactoryBean.setPersistenceUnitName( "simple-forwarding-persistence-unit" );
        return entityManagerFactoryBean;
    }

    Properties getDefaultJpaProperties( final Environment env )
    {
        final Properties jpaProperties = new Properties();
        jpaProperties.put( "hibernate.transaction.coordinator_class", "jdbc" );
        jpaProperties.put( "hibernate.connection.autocommit", false );
        jpaProperties.put( "hibernate.connection.release_mode", "after_transaction" );
        jpaProperties.put( "hibernate.dialect", env.getRequiredProperty( "simplef.configuration.jpa.hibernate.dialect" ) );
        jpaProperties.put( "hibernate.physical_naming_strategy", env.getRequiredProperty( "simplef.configuration.jpa.hibernate.naming.physical-strategy" ) );
        jpaProperties.put( "hibernate.show_sql", env.getRequiredProperty( "simplef.configuration.jpa.properties.show-sql" ) );
        jpaProperties.put( "hibernate.use_sql_comments", true );
        jpaProperties.put( "hibernate.format_sql", true );
        jpaProperties.put( "hibernate.hbm2ddl.auto", env.getRequiredProperty( "simplef.configuration.jpa.hibernate.ddl-auto" ) );
        jpaProperties.put( "hibernate.cache.region.factory_class", env.getRequiredProperty( "simplef.configuration.jpa.properties.hibernate.cache.cacheProvider", String.class ) );
        jpaProperties.put( "hibernate.cache.use_second_level_cache", env.getRequiredProperty( "simplef.configuration.jpa.properties.hibernate.cache.use_second_level_cache", String.class ) );
        jpaProperties.put( "hibernate.cache.use_query_cache", env.getRequiredProperty( "simplef.configuration.jpa.properties.hibernate.cache.use_query_cache", String.class ) );
        jpaProperties.put( "hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext" );
        return jpaProperties;
    }

    @Bean( name = "transactionManager" )
    JpaTransactionManager getTransactionManager( EntityManagerFactory emf )
    {
        return new JpaTransactionManager( emf );
    }
}
