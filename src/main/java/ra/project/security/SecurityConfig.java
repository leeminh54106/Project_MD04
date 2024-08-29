package ra.project.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ra.project.constants.RoleName;
import ra.project.security.exception.AccessDenied;
import ra.project.security.exception.JwtEntryPoint;
import ra.project.security.jwt.JwtTokenFilter;
import ra.project.security.principle.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyUserDetailsService userDetailsService;
    private final JwtEntryPoint jwtEntryPoint;
    private final AccessDenied accessDenied;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    // nơi cấu hình cả ứng dụng HTTP
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //tắt tấn công của hacker đến sever
                .csrf(AbstractHttpConfigurer::disable)

                //Cấu hình quyền truy cập cho các URL cụ thể.
                .authorizeHttpRequests(
                        url -> url
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/api/v1/admin/**").hasAuthority(RoleName.ROLE_ADMIN.toString())
                                .requestMatchers("/api/v1/user/**").hasAuthority(RoleName.ROLE_USER.toString())
                                .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint(jwtEntryPoint)
                                .accessDeniedHandler(accessDenied)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    //được Spring Security sử dụng để thực hiện xác thực người dùng.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

}