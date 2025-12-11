package com.Ospuaye.BackendOspuaye.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // ✅ Registrar módulo para Java 8 Optional, OptionalInt, OptionalLong, etc.
        mapper.registerModule(new Jdk8Module());

        // ✅ Registrar módulo para Java 8 date/time (LocalDate, LocalDateTime, etc.)
        mapper.registerModule(new JavaTimeModule());

        // Desactivar timestamps (usar strings para fechas)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Configurar formato de fecha global
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
        mapper.setDateFormat(dateFormat);

        return mapper;
    }
}