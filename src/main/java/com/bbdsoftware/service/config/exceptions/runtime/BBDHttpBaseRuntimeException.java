package com.bbdsoftware.service.config.exceptions.runtime;

import com.bbdsoftware.service.response.*;
import lombok.*;
import org.springframework.http.*;

import java.util.*;

@ToString
public abstract class BBDHttpBaseRuntimeException extends RuntimeException {

    protected List<ResultMessage> messages = new ArrayList<>();
    protected HttpStatus httpStatus;

    public BBDHttpBaseRuntimeException(List<ResultMessage> messages, HttpStatus httpStatus) {
        super("BBD Exception Occurred ");
        this.httpStatus = httpStatus;
        this.messages = messages;

    }

    public BBDHttpBaseRuntimeException(List<ResultMessage> messages, HttpStatus httpStatus, Throwable e) {
        super("BBD Exception Occurred ");
        this.httpStatus = httpStatus;
        this.messages.addAll(messages);

    }

    public BBDHttpBaseRuntimeException(String message, HttpStatus httpStatus, Throwable e) {
        super("BBD Exception Occurred ", e);
        this.httpStatus = httpStatus;
        this.messages.add(
                ResultMessage.builder()
                        .message(message)
                        .messageSeverity(MessageSeverity.ERROR)
                        .build()
        );

    }

    public BBDHttpBaseRuntimeException(String message, HttpStatus httpStatus) {
        super("BBD Exception Occurred ");
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
