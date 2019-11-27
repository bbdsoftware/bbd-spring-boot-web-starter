package com.bbdsoftware.service.config.exceptions.checked;

import com.bbdsoftware.service.response.*;
import org.springframework.http.*;

import java.util.*;

public class BBDHttpServiceException extends BBDHttpBaseException {

    public BBDHttpServiceException(List<ResultMessage> messages) {
        super(messages, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public BBDHttpServiceException(List<ResultMessage> messages, HttpStatus httpStatus, Throwable e) {
        super(messages, HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    public BBDHttpServiceException(String message, Throwable e) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    public BBDHttpServiceException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
