package ua.test.mega.MegaTester.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import ua.test.mega.MegaTester.core.api.UserAdapter;
import ua.test.mega.MegaTester.core.api.model.User;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests().anyRequest().authenticated()
				.and()
				.httpBasic();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, UserAdapter userAdapter) throws Exception {
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authenticationManagerBuilderInMemoryUserDetailsManagerConfigurer =
				auth.inMemoryAuthentication()
						.passwordEncoder(NoOpPasswordEncoder.getInstance());

		for (User user : userAdapter.getAllUsers()) {
			authenticationManagerBuilderInMemoryUserDetailsManagerConfigurer
					.withUser(user.getUsername())
					.password(user.getPassword())
					.roles((String[])user.getRoles().toArray());
		}
	}
}