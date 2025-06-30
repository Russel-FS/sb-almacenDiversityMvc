package com.api.diversity.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sunat")
@Data
public class SunatConfig {
    private String ruc;
    private String claveSol;
    private String certificadoPath;
    private String certificadoPassword;
    private String ambiente;
    private String razonSocial;
    private String direccion;
    private String urbanizacion;
    private String distrito;
    private String provincia;
    private String departamento;
    private String ubigeo;
}