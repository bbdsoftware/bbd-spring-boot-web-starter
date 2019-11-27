package com.bbdsoftware.service.config.toggles;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;


/**
 * Created by kanzi on 12/07/17.
 */
@Configuration
@PropertySource("classpath:org/flips/application.properties")
@ComponentScan(value = "org.flips", excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = {org.flips.config.FlipContextConfiguration.class, org.flips.describe.config.FlipWebContextConfiguration.class})
)
@Component
public class FeatureFlipsConfig {

}
