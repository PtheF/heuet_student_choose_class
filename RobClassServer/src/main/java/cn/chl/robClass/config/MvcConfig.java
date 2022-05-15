package cn.chl.robClass.config;

import cn.chl.robClass.utils.WebInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig {

    @Bean
    public WebMvcConfigurer getConfigurer(){
        return new WebMvcConfigurer() {

            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new WebInterceptor())
                        .addPathPatterns("/robClass/*")
                        .addPathPatterns("/class/*");
            }
        };
    }
}
