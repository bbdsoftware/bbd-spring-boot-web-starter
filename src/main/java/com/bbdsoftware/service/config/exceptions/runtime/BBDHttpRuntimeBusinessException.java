package com.bbdsoftware.service.config.exceptions.runtime;

import com.bbdsoftware.service.response.*;
import org.springframework.http.*;

import java.util.*;

public class BBDHttpRuntimeBusinessException extends BBDHttpBaseRuntimeException {

    public BBDHttpRuntimeBusinessException(List<ResultMessage> messages, HttpStatus httpStatus) {
        super(messages, httpStatus);
    }

    public BBDHttpRuntimeBusinessException(List<ResultMessage> messages, HttpStatus httpStatus, Throwable e) {
        super(messages, httpStatus, e);
    }

    public BBDHttpRuntimeBusinessException(String message, HttpStatus httpStatus, Throwable e) {
        super(message, httpStatus, e);
    }

    public BBDHttpRuntimeBusinessException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
