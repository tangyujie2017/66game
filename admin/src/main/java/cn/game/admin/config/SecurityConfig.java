package cn.game.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.game.core.tools.BaseResponse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zzc on 11/11/2016.
 */

@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private PasswordEncoder passwordEncoder;
	// 调用cn.gaiasys.retail.core.service.SecurityUserDetailsService(实现了UserDetailsService接口)
	@Autowired
	private UserDetailsService securityUserDetailsService;

	@Autowired
	private ApiAuthenticationEntryPoint unauthorizedHandler;
	@Autowired
	private ApiAccessDeniedHandler apiAccessDeniedHandler;

	/**
	 * 
	 * 验证登录用户
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(securityUserDetailsService).passwordEncoder(passwordEncoder);

	}

	/**
	 * 
	 * 接口Url请求过滤定义
	 * 
	 */
	@Configuration
	@Order(1)
	public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private javax.sql.DataSource dataSource;

		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**");
			http.rememberMe().rememberMeServices(rememberMeServices()).userDetailsService(securityUserDetailsService)
					.alwaysRemember(true);
			http.authorizeRequests().antMatchers("/api/v1/inner/cardNumber").permitAll();
			http.authorizeRequests().antMatchers("/api/provider/user/admin/*").permitAll();
			http.authorizeRequests().antMatchers("/api/provider/logistics/list").permitAll();
			
			http.csrf().disable().antMatcher("/api/**")

					.authorizeRequests()

					.antMatchers("/api/**").hasAnyRole("COMMON_USER");
			http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
					.accessDeniedHandler(apiAccessDeniedHandler);
		}

		@Bean
		public JdbcTokenRepositoryImpl jdbcTokenRepository() {
			JdbcTokenRepositoryImpl impl = new JdbcTokenRepositoryImpl();
			impl.setDataSource(dataSource);
			return impl;
		}

		@Bean
		public AbstractRememberMeServices rememberMeServices() {
			PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
					"jkai8892", securityUserDetailsService, jdbcTokenRepository());
			rememberMeServices.setAlwaysRemember(true);
			rememberMeServices.setCookieName("remember-me");
			rememberMeServices.setTokenValiditySeconds(1209600);
			return rememberMeServices;
		}

	}

	@Component
	public class ApiAccessDeniedHandler implements AccessDeniedHandler {
		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response,
				AccessDeniedException accessDeniedException) throws IOException, ServletException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			String str = mapper.writeValueAsString(new BaseResponse(BaseResponse.STATUS_FORBIDDEN, "未授权"));
			response.reset();
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.getOutputStream().write(str.getBytes("utf-8"));
			response.getOutputStream().close();
		}
	}

	@Component
	public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {
		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			String str = mapper.writeValueAsString(new BaseResponse(BaseResponse.STATUS_UNAUTH, "未登录"));
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.getOutputStream().write(str.getBytes("utf-8"));
			response.getOutputStream().close();
		}
	}

	@Configuration
	@Order(2)
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Value("${retail.login.url}")
		private String loginUrl;

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**", "/webjars/**", "/js/**", "/images/**", "/css/**",
					"/h2-console/*", "/assets/*");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();

			http.authorizeRequests().antMatchers("/login*").permitAll().antMatchers("/**").hasRole("COMMON_USER").and()
					.formLogin().loginPage(loginUrl).and().exceptionHandling()
					.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(loginUrl));

			;
			// http.authorizeRequests()
			//
			// .antMatchers("/api/**").permitAll()
			// .antMatchers("/provider/cardNumber").permitAll()
			// .antMatchers("/util/**").permitAll();
		}

	}
}