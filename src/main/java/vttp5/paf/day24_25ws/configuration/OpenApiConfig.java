package vttp5.paf.day24_25ws.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig 
{
    @Bean
    public OpenAPI openAPI()
    {
        return new OpenAPI().info(
            new Info()
            .title("PAF Day 24-25 Workshop")
            .description("Workshop Practice for Day 24-25 of PAF")
            .version("1.0")
        );
    }    
}
