package com.apgar.techinical_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Expor o relógio da aplicacao como bean para
 * injetar no construtor e facilitar os testes.
 *
 * Fuso fixo em America/Sao_Paulo (-03:00): todas as reservas do
 * contrato da API chegam nesse offset, então "hoje" (usado em
 * "não pode iniciar no passado" e nas estatísticas do dia) precisa
 * ser calculado no mesmo fuso, e não em UTC.
 */
@Configuration
public class RelogioConfig {

    @Bean
    public Clock relogio() {
        return Clock.system(ZoneId.of("America/Sao_Paulo"));
    }
}
