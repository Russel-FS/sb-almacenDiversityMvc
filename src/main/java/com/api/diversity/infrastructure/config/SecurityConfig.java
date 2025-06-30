package com.api.diversity.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableTransactionManagement
@RequiredArgsConstructor
public class SecurityConfig {

        private final DataSource dataSource;
        private final UserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeHttpRequests(auth -> auth
                                                // rutas públicas
                                                .requestMatchers("/auth/login", "/css/**", "/js/**", "/images/**",
                                                                "/static/**")
                                                .permitAll()

                                                // rutas de administración
                                                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")

                                                // rutas de supervisión general
                                                .requestMatchers("/supervisor/**")
                                                .hasAnyRole("ADMINISTRADOR", "SUPERVISOR_GENERAL")

                                                // rutas de piñatería
                                                .requestMatchers("/pinateria/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_PINATERIA",
                                                                "OPERADOR_PINATERIA")

                                                // rutas de librería
                                                .requestMatchers("/libreria/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_LIBRERIA",
                                                                "OPERADOR_LIBRERIA")

                                                // rutas de cámaras
                                                .requestMatchers("/camaras/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_CAMARAS",
                                                                "OPERADOR_CAMARAS")

                                                // rutas de ventas
                                                .requestMatchers("/**/salida/**", "/**/venta/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_PINATERIA",
                                                                "SUPERVISOR_LIBRERIA",
                                                                "SUPERVISOR_CAMARAS",
                                                                "OPERADOR_PINATERIA",
                                                                "OPERADOR_LIBRERIA",
                                                                "OPERADOR_CAMARAS",
                                                                "VENDEDOR")

                                                // rutas de almacén
                                                .requestMatchers("/**/entrada/**", "/**/compra/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_PINATERIA",
                                                                "SUPERVISOR_LIBRERIA",
                                                                "SUPERVISOR_CAMARAS",
                                                                "OPERADOR_PINATERIA",
                                                                "OPERADOR_LIBRERIA",
                                                                "OPERADOR_CAMARAS",
                                                                "ALMACENERO")

                                                // rutas de reportes
                                                .requestMatchers("/**/reporte/**", "/**/reportes/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_PINATERIA",
                                                                "SUPERVISOR_LIBRERIA",
                                                                "SUPERVISOR_CAMARAS",
                                                                "CONTADOR")

                                                // rutas de gestión de usuarios
                                                .requestMatchers("/usuarios/**", "/roles/**", "/usuario-rubros/**")
                                                .hasRole("ADMINISTRADOR")

                                                // rutas de gestión de rubros
                                                .requestMatchers("/rubros/**").hasRole("ADMINISTRADOR")

                                                // rutas de gestión de proveedores y clientes
                                                .requestMatchers("/proveedores/**", "/clientes/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_PINATERIA",
                                                                "SUPERVISOR_LIBRERIA",
                                                                "SUPERVISOR_CAMARAS")

                                                // Página principal y home
                                                .requestMatchers("/", "/home").authenticated()

                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/auth/login")
                                                .usernameParameter("username")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/auth/login?error=true")
                                                .permitAll())
                                .rememberMe(remember -> remember
                                                .tokenRepository(persistentTokenRepository())
                                                .tokenValiditySeconds(86400 * 30)
                                                .rememberMeParameter("remember-me")
                                                .key("diversityKey"))
                                .logout(logout -> logout
                                                .logoutUrl("/auth/logout")
                                                .logoutSuccessUrl("/auth/login")
                                                .deleteCookies("JSESSIONID", "remember-me")
                                                .permitAll())
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
                builder.userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder());
                return builder.build();
        }

        @Bean
        public PersistentTokenRepository persistentTokenRepository() {
                JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
                tokenRepository.setDataSource(dataSource);
                return tokenRepository;
        }

        @Bean
        public static PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}