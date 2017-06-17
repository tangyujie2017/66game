package cn.tz.www.admin.config;

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


/**
 *
 */

@EnableWebSecurity
public class SecurityConfig {
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  //
  @Autowired
  private UserDetailsService securityUserDetailsService;
/**
 * 
 * 验证登录用户
 * */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(securityUserDetailsService).passwordEncoder(passwordEncoder);
    //给内存加载一个用户
    auth
    .inMemoryAuthentication()
        .withUser("admin").password("123456").roles("ADMIN");
    
    
  }
/**
 * 
 * 接口Url请求过滤定义
 * 
 * */
  @Configuration
  @Order(1)
  public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private javax.sql.DataSource dataSource;

    protected void configure(HttpSecurity http) throws Exception {
      http.rememberMe().rememberMeServices(rememberMeServices())
          .userDetailsService(securityUserDetailsService)
      .alwaysRemember(true);
      http
          .csrf().disable()
          .antMatcher("/api/**")
          .authorizeRequests()
          .antMatchers("/api/customer/check").permitAll()
          .antMatchers("/api/customer/login").permitAll()
          .antMatchers("/api/customer/regist").permitAll()
          //公共资源不需要认证
          .antMatchers("/api/customer/public/*").permitAll()
          .antMatchers("/api/customer/*").authenticated()
          .antMatchers("/api/customer/vip/*").hasAnyRole("VIP")
         
      ;
      ;
    }

    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository(){
      JdbcTokenRepositoryImpl impl = new JdbcTokenRepositoryImpl();
      impl.setDataSource(dataSource);
      return impl;
    }

    @Bean
    public AbstractRememberMeServices rememberMeServices() {
      PersistentTokenBasedRememberMeServices rememberMeServices =
          new PersistentTokenBasedRememberMeServices("jkai8892",
              securityUserDetailsService,jdbcTokenRepository());
      rememberMeServices.setAlwaysRemember(true);
      rememberMeServices.setCookieName("remember-me");
      rememberMeServices.setTokenValiditySeconds(1209600);
      return rememberMeServices;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	//关闭X-Frame-Options
    	 http.headers().frameOptions().disable();
    	http
        .authorizeRequests()
        .antMatchers("/", "/home").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/")
        .failureUrl("/login?error")
        .permitAll()
        .and()
        .logout()
        .permitAll();
    }

  }
}