package com.bbdsoftware.service.config.exceptions.checked;

import com.bbdsoftware.service.response.*;
import lombok.*;
import org.springframework.http.*;

import java.util.*;

@ToString
public abstract class BBDHttpBaseException extends Exception {

    protected List<ResultMessage> messages = new ArrayList<>();
    protected HttpStatus httpStatus;



    public BBDHttpBaseException(
            List<ResultMessage> messages,
            HttpStatus httpStatus) {
        super("Exception Occurred ");
        this.httpStatus = httpStatus;
        this.messages = messages;


    }

    public BBDHttpBaseException(
            List<ResultMessage> messages,
            HttpStatus httpStatus,
            Throwable e) {
        super("Exception Occurred ");
        this.httpStatus = httpStatus;
        this.messages.addAll(messages);

    }

    public BBDHttpBaseException(
            String message,
            HttpStatus httpStatus,
            Throwable e) {
        super("Exception Occurred ", e);
        this.httpStatus = httpStatus;
        this.messages.add(
                ResultMessage.builder()
                        .message(message)
                        .messageSeverity(MessageSeverity.ERROR)
                        .build()
        );

    }

    public BBDHttpBaseException(
            String message,
            HttpStatus httpStatus
            ) {
        super("Exception Occurred ");
        this.httpStatus = httpStatus;
        this.messages.add(
                ResultMessage.builder()
                        .message(message)
                        .messageSeverity(MessageSeverity.ERROR)
                        .build()
        );

    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Result<Void> getServiceResult() {
        return new RestApiResult<Void>().withMessages(messages);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
