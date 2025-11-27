package myy803.TraineeshipApp.TraineeshipApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import myy803.TraineeshipApp.TraineeshipApp.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomerSecuritySuccessHandler customSecuritySuccessHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.authorizeHttpRequests(
                    (authz) -> authz
                            .requestMatchers("/","/login","/register","/save").permitAll()
                            .requestMatchers("/student/**").hasAnyAuthority("STUDENT")
                            .requestMatchers("/professor/**").hasAnyAuthority("PROFESSOR")
                            .requestMatchers("company/**").hasAnyAuthority("COMPANY")
                            .requestMatchers("/committee/**").hasAnyAuthority("COMMITTEE")
                            .anyRequest().authenticated()
                            );

            http.formLogin(fL -> fL.loginPage("/login")
                    .failureUrl("/login?error=true")
                    .successHandler((AuthenticationSuccessHandler) customSecuritySuccessHandler)
                    .usernameParameter("username")
                    .passwordParameter("password"));

            http.logout(logout -> logout.logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    );
            http.authenticationProvider(authenticationProvider());

            return http.build();
    }
}
