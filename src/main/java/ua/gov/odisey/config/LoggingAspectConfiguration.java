package ua.gov.odisey.config;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import tech.jhipster.config.JHipsterConstants;
import ua.gov.odisey.aop.logging.LoggingAspect;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }
}
