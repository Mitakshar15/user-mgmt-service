package org.ainkai.usermgmt.api.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({"org.ainkai.usermgmt", "org.ainkai.digimart.lib"})
@EnableMongoAuditing
@EnableTransactionManagement
@EnableConfigurationProperties
public class UserServiceconfig {

  @Bean
  GroupedOpenApi apiV1() {
    return GroupedOpenApi.builder().group("v1").pathsToMatch("/v1/**").build();
  }


  @Bean
  OpenAPI rideShareUserApiV1() {
    return new OpenAPI().info(new Info().title("User Management Service API")
        .description("User Management service to support User related operations")
        .version("0.0.1"));
  }

}
