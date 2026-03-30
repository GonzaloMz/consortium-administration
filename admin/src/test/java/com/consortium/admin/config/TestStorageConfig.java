package com.consortium.admin.config;

import com.consortium.admin.service.StorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestStorageConfig {

    @Bean
    @ConditionalOnMissingBean(StorageService.class)
    public StorageService storageService() {
        return file -> "mock/" + file.getOriginalFilename();
    }
}
