package ru.ddc.springws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ddc.springws.dto.ObjectFactory;

@Configuration
public class ObjectFactoryConfig {

    @Bean
    public ObjectFactory getObjectFactory() {
        return new ObjectFactory();
    }
}
