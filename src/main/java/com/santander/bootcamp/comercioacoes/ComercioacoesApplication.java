package com.santander.bootcamp.comercioacoes;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ComercioacoesApplication {

    public static void main(String[] args) {
        System.out.println( System.getenv( "JDBC_DATABASE_URL" ) );

        SpringApplication.run( ComercioacoesApplication.class, args );
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title( "Comércio de Ações API" )
                        .version( "1.0" )
                        .termsOfService( "https://swagger.io/terms" )
                        .license( new License().name( "Apache 2.0" ) )
        );
    }

}
