package com.bbdsoftware.service.config.exceptions.checked;

import com.bbdsoftware.service.response.*;
import org.springframework.http.*;


import java.util.*;

public class BBDHttpAuthException extends BBDHttpBaseException {


    public  BBDHttpAuthException(
            List<ResultMessage> messages
    ) {
        super(messages, HttpStatus.UNAUTHORIZED);
    }

    public BBDHttpAuthException(
            List<ResultMessage> messages,

            Throwable e) {
        super(messages, HttpStatus.UNAUTHORIZED, e);
    }

    public BBDHttpAuthException(
            String message,

            Throwable e) {
        super(message, HttpStatus.UNAUTHORIZED, e);
    }

    public BBDHttpAuthException(
            String message
    ) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
