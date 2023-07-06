package ru.practicum.shareit.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.practicum.shareit.logging.LoggingAspect;

import java.sql.SQLException;

@Configuration
public class BeanConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile("test")
    public Server inMemoryH2DatabaseaServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
