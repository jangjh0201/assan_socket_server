package org.assansocketserver.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
//@EnableJpaRepositories(basePackages = "org.asansocketserver.domain.*.mongorepository")
@Configuration
public class JpaConfig {
}