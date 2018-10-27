package com.ptit.edu.store.security;


import com.ptit.edu.store.response_model.RoleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {
    @Value("${security.oauth2.resource.id}")
    private String resourceID;

    @Autowired
    private DefaultTokenServices tokenService;
    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/customer/register").permitAll()
                .antMatchers("/api/customer/**").hasRole(RoleConstants.CUSTOMER)
                .antMatchers("/api/products/**").permitAll()
                .antMatchers("/api/commons/**").permitAll()
                .antMatchers("/api/users/**").authenticated()
                .antMatchers("/api/auth/newPassword").authenticated()
                .antMatchers("/api/auth/**",
                        "/api/**",
                        "/v2/api-docs",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/swagger-ui.html").permitAll()
                .anyRequest().authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceID)
                .tokenServices(tokenService)
                .tokenStore(tokenStore);
    }
}
