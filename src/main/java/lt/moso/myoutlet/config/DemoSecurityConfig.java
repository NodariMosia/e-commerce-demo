package lt.moso.myoutlet.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Qualifier("securityDataSource")
	private DataSource securityDataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(securityDataSource);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/employees/**").hasAnyRole("MANAGER", "ADMIN")
			.antMatchers("/admins/**").hasRole("ADMIN") /* TODO: add admins page where admin will be able to add employees */
			.antMatchers("/users/**").hasRole("USER")
			.antMatchers("/landing/**").permitAll()
			.antMatchers("/resources/**").permitAll()
			.and()
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/authenticateTheUser")
				.permitAll()
			.and()
			.logout().permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/access-denied"); /* IF employee tries to go to admin page */
		
	}
	
}
