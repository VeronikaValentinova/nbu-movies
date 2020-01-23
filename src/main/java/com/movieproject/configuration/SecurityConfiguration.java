package com.movieproject.configuration;

import javax.sql.DataSource;

import com.movieproject.Service.UserServiceImpl;
import com.movieproject.security.JWTAuthenticationFilter;
import com.movieproject.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

 @Autowired
 private BCryptPasswordEncoder bCryptPasswordEncoder;
 private UserServiceImpl userService;

 public SecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder, UserServiceImpl userSer) {
  this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  this.userService = userSer;
 }

 @Autowired
 private DataSource dataSource;
 
 private final String USERS_QUERY = "select email, password, active from user where email=?";
 private final String ROLES_QUERY = "select u.email, r.role from user u inner join user_role ur on (u.id = ur.user_id) inner join role r on (ur.role_id=r.role_id) where u.email=?";

 @Override
 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  auth.userDetailsService(userService)
//   .usersByUsernameQuery(USERS_QUERY)
//   .authoritiesByUsernameQuery(ROLES_QUERY)
//   .dataSource(dataSource)
   .passwordEncoder(bCryptPasswordEncoder);
 }
 
 @Override
 public void configure(WebSecurity web) throws Exception {
     web
        .ignoring()
        .antMatchers("/css/**", "/js/**");
 }
 
 @Override
 protected void configure(HttpSecurity http) throws Exception{
	 http.authorizeRequests()
   .antMatchers("/").permitAll()
   .antMatchers("/login").permitAll()
   .antMatchers("/signup").permitAll()
   .antMatchers("/home/**").access("hasAuthority('USER') or hasAuthority('ADMIN')")
   .anyRequest()
   .authenticated().and().csrf().disable()
   .formLogin().loginPage("/login").failureUrl("/login?error=true")
   .defaultSuccessUrl("/home")
   .usernameParameter("email")
   .passwordParameter("password")
   .and().logout()
   .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
   .logoutSuccessUrl("/")
   .and().rememberMe()
   .tokenRepository(persistentTokenRepository())
   .tokenValiditySeconds(60*60)
   .and().exceptionHandling().accessDeniedPage("/access_denied")
     .and().addFilter(new JWTAuthenticationFilter(authenticationManager()))
             .addFilter(new JWTAuthorizationFilter(authenticationManager()))
     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);;
 }
 
 @Bean
 public PersistentTokenRepository persistentTokenRepository() {
  JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
  db.setDataSource(dataSource);
  
  return db;
 }
}
