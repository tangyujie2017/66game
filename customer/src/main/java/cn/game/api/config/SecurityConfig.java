package cn.game.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.*;
@EnableWebSecurity
public class SecurityConfig {
  @Configuration
  @Order(1)
  public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private javax.sql.DataSource dataSource;

    protected void configure(HttpSecurity http) throws Exception {
     
      http
          .csrf().disable()
          .antMatcher("/api/**")
          .authorizeRequests()
          .antMatchers("/api/v1/*/*").permitAll();
    
    }

    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository(){
      JdbcTokenRepositoryImpl impl = new JdbcTokenRepositoryImpl();
      impl.setDataSource(dataSource);
      return impl;
    }

   

  }

  @Configuration
  @Order(2)
  public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   

    @Override
    public void configure(WebSecurity web) throws Exception {
      web.ignoring().antMatchers(
          "/resources/**",
          "/webjars/**",
          "/js/**",
          "/images/**",
          "/css/**",
          "/h2-console/*",
          "/assets/*"
      );
    }

   

  }
}