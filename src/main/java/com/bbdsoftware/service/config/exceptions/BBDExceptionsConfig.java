package com.bbdsoftware.service.config.exceptions;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "com.bbdsoftware.service.config.exceptions")
@Import(BBDErrorAdvice.class)
public class BBDExceptionsConfig {
}
