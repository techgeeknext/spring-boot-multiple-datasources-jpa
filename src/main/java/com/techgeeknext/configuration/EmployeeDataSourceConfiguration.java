package com.techgeeknext.configuration;

import com.techgeeknext.entities.employee.Employee;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.techgeeknext.repository.employee",
        entityManagerFactoryRef = "employeeEntityManagerFactory",
        transactionManagerRef= "employeeTransactionManager")
public class EmployeeDataSourceConfiguration {
    /**
     * Here it will get url, username, password and driver-class-name
     * which we have defined in application properties file for employee.
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.employee")
    public DataSourceProperties employeeDatasourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Create the datasource using employeeDatasourceProperties
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.user.configuration")
    public DataSource userDataSource() {
        return employeeDatasourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    /**
     * EntityManager will find Entity classes inside this company package
     * (i.e com.techgeeknext.entities.employee.Employee).
     * @param builder
     * @return
     */
    @Primary
    @Bean(name = "employeeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean employeeEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(userDataSource())
                // for specifying package .packages("com.techgeeknext.entities.employee.type")
                .packages(Employee.class)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager employeeTransactionManager(
            final @Qualifier("employeeEntityManagerFactory") LocalContainerEntityManagerFactoryBean employeeEntityManagerFactory) {
        return new JpaTransactionManager(employeeEntityManagerFactory.getObject());
    }
}
