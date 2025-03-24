package com.crm.swagger;
 
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupportTicketSwagger {
        @Bean
        public OpenAPI customOpenAPI() {
            return new OpenAPI()
                    .info(new Info().title("Customer Relation Rest API")
                            .description("API Documentation")
                            .version("v1.0")
                    );
        }
}