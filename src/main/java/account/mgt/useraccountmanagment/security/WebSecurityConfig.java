package account.mgt.useraccountmanagment.security;

import jakarta.persistence.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig{
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Override
//    protected void configure(HttpSecurity http) throws  Exception{
//        try{
//            http
//                    .sessionManagement()
//                    .maximumSessions(1)
//                    .maxSessionsPreventsLogin(true)
//                    .expiredUrl("/login?expired")
//                    .and()
//                    .and()
//                    .authorizeRequests()
////                    .antMatchers("/customer","/save-order").authenticated()
////                    .antMatchers("/customer").hasAnyAuthority("CUSTOMER")
//                    .anyRequest().permitAll()
//                    .and()
//                    .formLogin()
//                    .loginPage("/login")
//                    .usernameParameter("username")
//                    .successHandler(loginSuccessHandler)
//                    .permitAll()
//                    .and()
//                    .logout().permitAll()
//                    .and()
//                    .rememberMe()
//                    .key("123456789_abcdefghijkLMnopQRstuvWxYZ")
//                    .tokenValiditySeconds(14 * 24 * 60 *60)
//            ;
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests((requests) -> requests
//                        .requestMatchers("/user/signup", "/user/**", "/static/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin((form) -> form
//                        .loginPage("/user/login")
//                        .successHandler(loginSuccessHandler)
//                        .permitAll()
//                )
//                .logout((logout) -> logout.permitAll());
//
//        return http.build();
//    }
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

//    @Autowired
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**", "/static/**");
//    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserCustomDetailsService();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
