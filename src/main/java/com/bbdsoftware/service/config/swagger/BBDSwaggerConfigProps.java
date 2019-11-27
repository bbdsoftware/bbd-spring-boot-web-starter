package com.bbdsoftware.service.config.swagger;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.stereotype.*;

import javax.annotation.*;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Component
@ConfigurationProperties(prefix = "bbd.core.swagger")
    public class BBDSwaggerConfigProps {

    String description;
    String contact;
    Boolean enableadmin;
    Boolean useDefaultResponseMessages = false;
    String packages;

    @PostConstruct
    public void postConstruct()
    {
        if(this.getEnableadmin() == null)
            this.setEnableadmin(false);
    }
}
