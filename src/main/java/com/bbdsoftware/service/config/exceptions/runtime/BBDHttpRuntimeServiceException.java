package com.bbdsoftware.service.config.exceptions.runtime;

import com.bbdsoftware.service.response.*;
import org.springframework.http.*;

import java.util.*;

public class BBDHttpRuntimeServiceException extends BBDHttpBaseRuntimeException {

    public BBDHttpRuntimeServiceException(List<ResultMessage> messages) {
        super(messages, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public BBDHttpRuntimeServiceException(List<ResultMessage> messages, HttpStatus httpStatus, Throwable e) {
        super(messages, HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    public BBDHttpRuntimeServiceException(String message, Throwable e) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    public BBDHttpRuntimeServiceException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
