package com.buerc.permission.config;

import com.buerc.common.utils.JwtTokenUtil;
import com.buerc.common.utils.RsaUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Validator;
import java.util.List;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Bean
    public JwtTokenUtil jwtTokenUtils(){
        return new JwtTokenUtil();
    }

    @Bean
    public RsaUtil rsaUtil(){
        return new RsaUtil();
    }

    @Bean
    public Validator validator(){
        return new LocalValidatorFactoryBean();
    };

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
//        exceptionResolvers.add(new DefaultExceptionResolver());
    }
}
