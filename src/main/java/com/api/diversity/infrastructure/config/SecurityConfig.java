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
                                                // Rutas públicas
                                                .requestMatchers("/login", "/css/**", "/js/**",
                                                                "/images/**", "/favicon.ico")
                                                .permitAll()
                                                // ruta de errores
                                                .requestMatchers("/error/**", "/error")
                                                .permitAll()

                                                // Rutas de administración
                                                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                                                .requestMatchers("/admin/usuarios/**").hasRole("ADMINISTRADOR")
                                                .requestMatchers("/admin/productos/**").hasRole("ADMINISTRADOR")
                                                .requestMatchers("/admin/categorias/**").hasRole("ADMINISTRADOR")
                                                .requestMatchers("/admin/proveedores/**").hasRole("ADMINISTRADOR")
                                                .requestMatchers("/admin/usuarios-rubros/**").hasRole("ADMINISTRADOR")

                                                // Rutas de supervisión general
                                                .requestMatchers("/supervisor/**")
                                                .hasAnyRole("ADMINISTRADOR", "SUPERVISOR_GENERAL")

                                                // Rutas de piñatería
                                                .requestMatchers("/pinateria/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_PINATERIA",
                                                                "OPERADOR_PINATERIA")

                                                // Rutas de librería
                                                .requestMatchers("/libreria/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_LIBRERIA",
                                                                "OPERADOR_LIBRERIA")

                                                // Rutas de cámaras
                                                .requestMatchers("/camaras/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_CAMARAS",
                                                                "OPERADOR_CAMARAS")

                                                // Rutas de gestión de usuarios solo admin
                                                .requestMatchers("/usuarios/**", "/roles/**", "/usuario-rubros/**")
                                                .hasRole("ADMINISTRADOR")

                                                // Rutas de gestión de rubros solo admin
                                                .requestMatchers("/rubros/**").hasRole("ADMINISTRADOR")

                                                // Rutas de gestión de proveedores y clientes
                                                .requestMatchers("/proveedores/**", "/clientes/**").hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "SUPERVISOR_GENERAL",
                                                                "SUPERVISOR_PINATERIA",
                                                                "SUPERVISOR_LIBRERIA",
                                                                "SUPERVISOR_CAMARAS")

                                                // Página principal y home
                                                .requestMatchers("/", "/home").authenticated()

                                                // Cualquier otra ruta requiere autenticación
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
                                // Manejo de errores de acceso denegaddo
                                .exceptionHandling(exceptions -> exceptions
                                                .accessDeniedPage("/error/access-denied")
                                                .authenticationEntryPoint((request, response, authException) -> {
                                                        response.sendRedirect("/auth/login?error=unauthorized");
                                                }))
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