package me.manaki.deepup.config;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.security.oauth.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final OAuth2Service oAuth2Service;

    private final Environment environment;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/css/*",
                        "/script/*",
                        "/icon/*",

                        "/user/**",

                        "/noi-bat",
                        "/topic/**",
                        "/api/**",
                        "/topic-images/**",
                        "/" + environment.getProperty("storage.topic-images") + "/**",
                        "/" + environment.getProperty("storage.avatars") + "/**",
                        "/" + environment.getProperty("storage.post-images") + "/**",
                        "/" + environment.getProperty("storage.wallpapers") + "/**",
                        "/main-page/**",
                        "/html-templates/**",
                        "/post/**")
                .permitAll()

                .antMatchers(
                        "/login*",
                        "/register*"
                )
                .not()
                .hasAnyAuthority("MEMBER", "ADMIN")

                .antMatchers(
                        "/logout*",
                        "/profile*",
                        "/password*",
                        "/create-post*",
                        "/vote**")
                .hasAnyAuthority("MEMBER", "ADMIN")

                .anyRequest()
                .authenticated()

                // Login
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error=true")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                .and()
                    .rememberMe()
                    .key(this.passwordEncoder().encode("VERY_SECRET_KEY"))

                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login?error=true")
                    .userInfoEndpoint()
                    .userService(oAuth2Service)

                .and()
                .and()
                .exceptionHandling().accessDeniedPage("/")

                .and()
                .httpBasic();
    }
}
