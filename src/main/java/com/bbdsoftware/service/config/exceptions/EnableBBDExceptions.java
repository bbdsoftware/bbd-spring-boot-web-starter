package com.bbdsoftware.service.config.exceptions;

import org.springframework.context.annotation.*;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@Import({BBDExceptionsConfig.class})
public @interface EnableBBDExceptions {

}
