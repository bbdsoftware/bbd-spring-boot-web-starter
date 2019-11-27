package com.bbdsoftware.service.config.exceptions.runtime;

import com.bbdsoftware.service.response.*;
import org.springframework.http.*;


import java.util.*;

public class BBDHttpRuntimeInvalidRequestException extends BBDHttpBaseRuntimeException {


    public BBDHttpRuntimeInvalidRequestException(
            List<ResultMessage> messages
    ) {
        super(messages, HttpStatus.BAD_REQUEST);
    }

    public BBDHttpRuntimeInvalidRequestException(
            List<ResultMessage> messages,

            Throwable e) {
        super(messages, HttpStatus.BAD_REQUEST, e);
    }

    public BBDHttpRuntimeInvalidRequestException(
            String message,
            Throwable e) {
        super(message, HttpStatus.BAD_REQUEST, e);
    }

    public BBDHttpRuntimeInvalidRequestException(
            String message
    ) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
