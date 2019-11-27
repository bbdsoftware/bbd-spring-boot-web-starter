package com.bbdsoftware.service.config.exceptions.checked;

import com.bbdsoftware.service.response.*;
import org.springframework.http.*;

import java.util.*;

public class BBDHttpInvalidRequestException extends BBDHttpBaseException {

    public  BBDHttpInvalidRequestException(List<ResultMessage> messages) {
        super(messages, HttpStatus.BAD_REQUEST);
    }

    public BBDHttpInvalidRequestException(List<ResultMessage> messages, Throwable e) {
        super(messages, HttpStatus.BAD_REQUEST, e);
    }

    public BBDHttpInvalidRequestException(String message, Throwable e) {
        super(message, HttpStatus.BAD_REQUEST, e);
    }

    public BBDHttpInvalidRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
