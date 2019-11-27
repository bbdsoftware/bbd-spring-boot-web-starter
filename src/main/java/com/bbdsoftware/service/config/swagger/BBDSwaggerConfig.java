package com.bbdsoftware.service.config.swagger;

import org.joda.time.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;

import java.time.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Configuration of Swagger for the service
 *
 */
@Configuration
@ComponentScan(value = "com.bbdsoftware.service.config.swagger")
@Import(BBDSwaggerConfigProps.class)
public class BBDSwaggerConfig {

    @Autowired
    BBDSwaggerConfigProps bbdSwaggerConfigProps;

    @Value("${spring.application.name}")
    private String name;

    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(bbdSwaggerConfigProps.getPackages()))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
                .genericModelSubstitutes(ResponseEntity.class)

                .useDefaultResponseMessages(bbdSwaggerConfigProps.getUseDefaultResponseMessages())
                .enableUrlTemplating(false)
                .forCodeGeneration(true)
                .produces(new HashSet<String>(Arrays.asList("application/json")))
                .groupName("Api")
                .ignoredParameterTypes(DateTime.class, DateTimeZone.class, Chronology.class)
                .directModelSubstitute(LocalDate.class, String.class)
                ;
    }

    public ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title(name)
                .contact(new Contact(bbdSwaggerConfigProps.getContact(), "", ""))
                .build();
    }


}
