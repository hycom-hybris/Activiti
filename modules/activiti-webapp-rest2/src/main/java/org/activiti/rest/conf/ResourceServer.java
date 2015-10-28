package org.activiti.rest.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {


    protected static final Logger LOGGER = LoggerFactory.getLogger(ResourceServer.class);

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("rAnDoMhYbRiSsIgNiNgKeY");
//        try {
//            converter.afterPropertiesSet();
//        } catch (Exception e) {
//            LOGGER.warn("Could not set tokenEnchancer", e);
//        }
        return converter;

    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @Bean
    public ResourceServerTokenServices defaultTokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenEnhancer(tokenEnhancer());
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and().requestMatchers().antMatchers("/service/**").and()
                .authorizeRequests()
                .antMatchers("/service/**").access("#oauth2.hasScope('trust') and hasAuthority('ROLE_ACTIVITIADMIN')");
//                .antMatchers("/service/**").hasAuthority("ROLE_ACTIVITIADMIN");
        // @formatter:on
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore()).resourceId("oauth2test/client").tokenServices(defaultTokenServices());
    }

}
