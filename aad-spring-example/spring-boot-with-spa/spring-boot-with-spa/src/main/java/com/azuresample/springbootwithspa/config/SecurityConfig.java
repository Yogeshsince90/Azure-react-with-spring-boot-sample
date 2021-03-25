package com.azuresample.springbootwithspa.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final GraphUserService graphUserService;

    public SecurityConfig(GraphUserService graphUserService) {
        this.graphUserService = graphUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors()
                .and()
//                .headers().frameOptions().sameOrigin()
//                .and()
                .authorizeRequests()
//                .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(this.oidcUserService());
//                .oidcUserService(oidcUserService)
//                .and()
//                .authorizationEndpoint()
//                .authorizationRequestResolver(
//                        new AADOAuth2AuthorizationRequestResolver(clientRegistrationRepository))
//                .and()
//                .failureHandler(new AADAuthenticationFailureHandler());
    }
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() throws OAuth2AuthorizationException {
        final OidcUserService delegate = new OidcUserService();

        return (oidcUserRequest) -> {
            OidcUser oidcUser = delegate.loadUser(oidcUserRequest);
            //String userPrincipal = "test3@yogeshvasudevanhotmail.onmicrosoft.com";//(String) oidcUser.getAttributes().get("upn");
            String userPrincipal = (String) oidcUser.getAttributes().get("preferred_username");

            Set<String> roles = getGSRRoles(userPrincipal);

            Map<String, Object> claims = new HashMap<>();
            //logic here to retrieve user details like name, email tnumber, gpin, etc.
            claims.put("Email",userPrincipal);
            OidcUserInfo oidcUserInfo = new OidcUserInfo(claims);

            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            roles.forEach(role -> mappedAuthorities.add(new SimpleGrantedAuthority(role)));
            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUserInfo);

            return oidcUser;

        };
    }
    private Set<String> getGSRRoles(String userPrincipal) throws OAuth2AuthorizationException {
        graphUserService.initGraphClient(userPrincipal);
        Set<String> roles = graphUserService.getGsrRoles();

        return roles;
    }

}
