package com.bbdsoftware.service.config.exceptions.checked;

import com.bbdsoftware.service.response.*;
import org.springframework.http.*;

import java.util.*;

public class BBDHttpBusinessException extends BBDHttpBaseException {

    public BBDHttpBusinessException(List<ResultMessage> messages, HttpStatus httpStatus) {
        super(messages, httpStatus);
    }

    public BBDHttpBusinessException(List<ResultMessage> messages, HttpStatus httpStatus, Throwable e) {
        super(messages, httpStatus, e);
    }

    public BBDHttpBusinessException(String message, HttpStatus httpStatus, Throwable e) {
        super(message, httpStatus, e);
    }

    public BBDHttpBusinessException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}


