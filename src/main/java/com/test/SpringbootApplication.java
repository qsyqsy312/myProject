package com.test;

import com.test.repository.base.BaseDaoImpl;
import org.flowable.ui.common.security.FlowableUiSecurityAutoConfiguration;
import org.flowable.ui.modeler.conf.ModelerSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = {"com.test"})
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = BaseDaoImpl.class)
@EnableJpaAuditing
public class SpringbootApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootApplication.class, args);
    }
}
