package org.flowable.ui.common.security;

import org.flowable.idm.api.IdmIdentityService;
import org.flowable.spring.boot.FlowableSecurityAutoConfiguration;
import org.flowable.spring.boot.idm.IdmEngineServicesAutoConfiguration;
import org.flowable.ui.common.properties.FlowableCommonAppProperties;
import org.flowable.ui.common.properties.FlowableRestAppProperties;
import org.flowable.ui.common.service.idm.RemoteIdmService;
import org.flowable.ui.common.service.idm.RemoteIdmServiceImpl;
import org.flowable.ui.common.service.idm.cache.RemoteIdmUserCache;
import org.flowable.ui.common.service.idm.cache.UserCache;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 说明：重构FlowableUiSecurity自动配置
 * From：www.fhadmin.org
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({
        IdmEngineServicesAutoConfiguration.class,
})
@AutoConfigureBefore({
        FlowableSecurityAutoConfiguration.class,
        OAuth2ClientAutoConfiguration.class,
})
public class FlowableUiSecurityAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowableUiSecurityAutoConfiguration.class);

    public static final String REST_ENDPOINTS_PREFIX = "/app/rest";

    @Autowired
    protected RemoteIdmAuthenticationProvider authenticationProvider;

//    @Bean
//    public FlowableCookieFilterRegistrationBean flowableCookieFilterRegistrationBean(RemoteIdmService remoteIdmService, FlowableCommonAppProperties properties) {
//        FlowableCookieFilterRegistrationBean filter = new FlowableCookieFilterRegistrationBean(remoteIdmService, properties);
//        filter.addUrlPatterns("/app/*");
//        filter.setRequiredPrivileges(Collections.singletonList(DefaultPrivileges.ACCESS_MODELER));
//        return filter;
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {

        // Default auth (database backed)
        try {
            auth.authenticationProvider(authenticationProvider);
        } catch (Exception e) {
            LOGGER.error("Could not configure authentication mechanism:", e);
        }
    }

//    @Configuration
//    @Order(10)
//    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//
////        @Autowired
////        protected FlowableCookieFilterRegistrationBean flowableCookieFilterRegistrationBean;
//
//        @Autowired
//        protected AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
////                    .addFilterBefore(flowableCookieFilterRegistrationBean.getFilter(), UsernamePasswordAuthenticationFilter.class)
//                    .logout()
//                    .logoutUrl("/app/logout")
//                    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
//                    .addLogoutHandler(new ClearFlowableCookieLogoutHandler())
//                    .and()
//                    .csrf()
//                    .disable() // Disabled, cause enabling it will cause sessions
//                    .headers()
//                    .frameOptions()
//                    .sameOrigin()
//                    .addHeaderWriter(new XXssProtectionHeaderWriter())
//                    .and()
//                    .authorizeRequests()
////                    .antMatchers(REST_ENDPOINTS_PREFIX + "/**").hasAuthority(DefaultPrivileges.ACCESS_MODELER);
//                    .antMatchers(REST_ENDPOINTS_PREFIX + "/**").permitAll();
//        }
//    }

    //
    // BASIC AUTH
    //

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        protected final FlowableRestAppProperties restAppProperties;
        protected final FlowableModelerAppProperties modelerAppProperties;

        public ApiWebSecurityConfigurationAdapter(FlowableRestAppProperties restAppProperties,
                                                  FlowableModelerAppProperties modelerAppProperties) {
            this.restAppProperties = restAppProperties;
            this.modelerAppProperties = modelerAppProperties;
        }

        protected void configure(HttpSecurity http) throws Exception {

            http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf()
                    .disable();

            http.antMatcher("/api/**").authorizeRequests().antMatchers("/api/**").permitAll();

//            if (modelerAppProperties.isRestEnabled()) {
//
//                if (restAppProperties.isVerifyRestApiPrivilege()) {
//                    http.antMatcher("/api/**").authorizeRequests().antMatchers("/api/**").hasAuthority(DefaultPrivileges.ACCESS_REST_API).and().httpBasic();
//                } else {
//                    http.antMatcher("/api/**").authorizeRequests().antMatchers("/api/**").authenticated().and().httpBasic();
//
//                }
//
//            } else {
//                http.antMatcher("/api/**").authorizeRequests().antMatchers("/api/**").denyAll();
//
//            }

        }
    }

    //
    // Actuator
    //

    @ConditionalOnClass(EndpointRequest.class)
    @Configuration
    @Order(5) // Actuator configuration should kick in before the Form Login there should always be http basic for the endpoints
    public static class ActuatorWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        protected void configure(HttpSecurity http) throws Exception {

            http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf()
                    .disable();

            http
                    .requestMatcher(new ActuatorRequestMatcher())
                    .authorizeRequests()
                    .requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class)).authenticated()
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyAuthority(DefaultPrivileges.ACCESS_ADMIN)
                    .and().httpBasic();
        }
    }



    @Configuration(
            proxyBeanMethods = false
    )
    @ConditionalOnClass(
            name = {"org.flowable.ui.idm.service.GroupServiceImpl"}
    )
    public static class LocalIdmConfiguration {
        public LocalIdmConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean
        public PersistentTokenService flowableUiPersistentTokenService(IdmIdentityService idmIdentityService) {
            return new IdmEnginePersistentTokenService(idmIdentityService);
        }
    }

    @Configuration(
            proxyBeanMethods = false
    )
    @ConditionalOnMissingClass({"org.flowable.ui.idm.service.GroupServiceImpl"})
    public static class RemoteIdmConfiguration {
        public RemoteIdmConfiguration() {
        }

        @Bean
        public RemoteIdmService remoteIdmService(FlowableCommonAppProperties properties) {
            return new RemoteIdmServiceImpl(properties);
        }

        @Bean
        public UserCache remoteIdmUserCache(FlowableCommonAppProperties properties, RemoteIdmService remoteIdmService) {
            return new RemoteIdmUserCache(properties, remoteIdmService);
        }

        @Bean
        @ConditionalOnMissingBean
        public UserDetailsService flowableUiUserDetailsService(RemoteIdmService remoteIdmService) {
            return new RemoteIdmUserDetailsService(remoteIdmService);
        }

        @Bean
        @ConditionalOnMissingBean
        public PersistentTokenService flowableUiPersistentTokenService(RemoteIdmService remoteIdmService) {
            return new RemoteIdmPersistentTokenService(remoteIdmService);
        }

        @Bean
        public RemoteIdmAuthenticationProvider remoteIdmAuthenticationProvider(RemoteIdmService remoteIdmService) {
            return new RemoteIdmAuthenticationProvider(remoteIdmService);
        }
    }



    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "flowable.common.app.security",
            name = {"type"},
            havingValue = "idm",
            matchIfMissing = true
    )
    public ApiHttpSecurityCustomizer defaultApiHttpSecurityCustomizer() {
        return new DefaultApiHttpSecurityCustomizer();
    }
}