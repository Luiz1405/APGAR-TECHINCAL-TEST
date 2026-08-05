package com.apgar.techinical_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Expor o relógio da aplicacao como bean para
 * injetar no construtor e facilitar os testes.
 */
@Configuration
public class RelogioConfig {

    @Bean
    public Clock relogio() {
        return Clock.systemUTC();
    }
}
