package com.bbdsoftware.service.config.toggles;

import org.springframework.context.annotation.*;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({FeatureFlipsConfig.class})
public @interface EnableBBDFlipToggles {


}
