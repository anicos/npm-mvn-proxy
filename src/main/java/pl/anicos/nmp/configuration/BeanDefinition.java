package pl.anicos.nmp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import java.util.Base64;

@EnableWebMvc
@Configuration
public class BeanDefinition {
    @Bean
    public Base64.Encoder encoder() {
        return Base64.getEncoder();
    }

    @Bean
    public Base64.Decoder decoder() {
        return Base64.getDecoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new NpmErrorAttributes();
    }

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        return new ContentNegotiationConfig();
    }

    @Bean
    public FilterRegistrationBean requestDumperFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter requestDumperFilter = new RequestDumperFilter();
        registration.setFilter(requestDumperFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }
}