package com.bbdsoftware.service.config.swagger;

import org.springframework.context.annotation.*;
import springfox.documentation.swagger2.annotations.*;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@EnableSwagger2
@Import(BBDSwaggerConfig.class)
public @interface EnableBBDSwagger {

}
