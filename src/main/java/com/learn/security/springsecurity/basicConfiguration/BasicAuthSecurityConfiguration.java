package com.learn.security.springsecurity.basicConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class BasicAuthSecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity  http) throws Exception
    {
        http.authorizeHttpRequests(
                auth -> {
                    auth.anyRequest().authenticated();
                });
        http.sessionManagement(
                session ->
                {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
//        http.formLogin();
        http.httpBasic();
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService()
//    {
//        var user = User.withUsername("user")
//                .password("{noop}user")
//                .roles("USER")
//                .build();
//        var admin = User.withUsername("admin")
//                .password("{noop}admin")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user,admin);
//    }


    @Bean
    public DataSource dataSource()
    {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource)
    {
        var user = User.withUsername("user")
//                .password("{noop}user")
                .password("user")
                .passwordEncoder(str->passwordEncoder().encode(str))
                .roles("USER")
                .build();
        var admin = User.withUsername("admin")
//                .password("{noop}admin")  // password without encoding or hashing
                .password("admin")
                .passwordEncoder(str->passwordEncoder().encode(str))  // to hash/encode the password
                .roles("ADMIN")
                .build();

        var jdbcUserDetailManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailManager.createUser(user);
        jdbcUserDetailManager.createUser(admin);
        return jdbcUserDetailManager;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
