package br.gov.ba.saude.teleconsulta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authz -> authz
				.requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/javax.faces", "/jakarta.faces" +
						".resource/**", "/resources/i18n/**", "/i18n/**", "/error/**").permitAll()
				.requestMatchers("/").permitAll()
				.requestMatchers("/login.faces").permitAll()
				.requestMatchers("/reserva.faces", "/disponibilidade.faces").hasAnyAuthority("ADMINISTRADOR", "RESTRITO")
				.requestMatchers("/usuario.faces", "/paciente.faces", "/unidade.faces", "/sala.faces").hasAuthority("ADMINISTRADOR")
				.anyRequest().authenticated()
			)
			.formLogin(formLogin -> formLogin
				.loginPage("/login.faces")
				.usernameParameter("loginForm:email")
				.passwordParameter("loginForm:password")
				.defaultSuccessUrl("/index.faces", true)
				.failureUrl("/login.faces?error=true")
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login.faces")
		        .invalidateHttpSession(true)
		        .deleteCookies("JSESSIONID")
				.permitAll()
			);
		return http.build();
	}
}
