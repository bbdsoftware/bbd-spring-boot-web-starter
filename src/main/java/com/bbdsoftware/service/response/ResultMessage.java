package com.bbdsoftware.service.response;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultMessage {

    private String message;
    private MessageSeverity messageSeverity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private I18nDetails i18n;

    public ResultMessage(final String message, final MessageSeverity messageSeverity) {
        this(message, messageSeverity, null);
    }

    public ResultMessage withMessage(final String message) {
        this.message = message;
        return this;
    }

    public ResultMessage withMessageSeverity(final MessageSeverity messageSeverity) {
        this.messageSeverity = messageSeverity;
        return this;
    }

    public ResultMessage withI18nDetails(final String i18nCode, final Map<String,Object> i18nParams) {
        this.i18n = new I18nDetails(i18nCode, i18nParams);
        return this;
    }

    @Data
    @AllArgsConstructor
    public static class I18nDetails {
        /**
         * The internationalisation message code.
         */
        private String code;

        /**
         * Parameters relating to the message code.
         */
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private Map<String,Object> params;
    }
}
