package net.thumbtack.school.onlineshop.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.cdi.LocalTransactionInterceptor;
import org.mybatis.cdi.SessionFactoryProvider;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.apache.ibatis.io.Resources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.Reader;

@Configuration
@MapperScan("net.thumbtack.school.onlineshop.mybatis.mapper")
@ComponentScan("net.thumbtack.school.onlineshop")
@EnableTransactionManagement
@Slf4j
public class AppConfig {

    @Bean
    public org.apache.ibatis.session.Configuration configuration() {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            return new SqlSessionFactoryBuilder().build(reader).getConfiguration();
        } catch (Exception e) {
            log.error("Error loading mybatis-config.xml", e);
            return null;
        }
    }

    @Bean
    public DataSource dataSource() {
        return configuration().getEnvironment().getDataSource();
    }

    @Bean
    @SessionFactoryProvider
    public SqlSessionFactory sqlSessionFactory() {
        try {
            SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
            sqlSessionFactory.setConfiguration(configuration());
            sqlSessionFactory.setDataSource(configuration().getEnvironment().getDataSource());
            return sqlSessionFactory.getObject();
        } catch (Exception e) {
            log.error("Error creating Session Factory", e);
            return null;
        }
    }
}
