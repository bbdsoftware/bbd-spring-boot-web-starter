package com.bbdsoftware.service.config.exceptions;

import com.bbdsoftware.service.config.exceptions.checked.*;
import com.bbdsoftware.service.config.exceptions.runtime.*;
import com.bbdsoftware.service.response.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.*;
import lombok.extern.slf4j.*;
import org.flips.exception.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.util.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;
import java.util.stream.*;

@ControllerAdvice
@Slf4j
public class BBDErrorAdvice {

    private static final String UNKNOWN_ERROR_MESSAGE = "Unknown Error";

    private ResponseEntity<Result<Void>> getResponse(BBDHttpBaseException exception) {
        Result<Void> result = exception.getServiceResult();

        return new ResponseEntity<>(result, exception.getHttpStatus());
    }

    private ResponseEntity<Result<Void>> getResponse(BBDHttpBaseRuntimeException exception) {
        Result<Void> result = exception.getServiceResult();
        log.error("Exception Occurred  ", exception);
        return new ResponseEntity<>(result, exception.getHttpStatus());
    }

    @ExceptionHandler(value = {BBDHttpBusinessException.class})
    public ResponseEntity<Result<Void>> defaultBBDBusinessExceptionErrorHandler(Exception e) {
        if (log.isDebugEnabled())
            log.error("Exception Occurred  ", e);
        return getResponse((BBDHttpBaseException) e);
    }

    @ExceptionHandler(value = {BBDHttpServiceException.class})
    public ResponseEntity<Result<Void>> defaultBBDServiceExceptionErrorHandler(Exception e) {
        log.error("Exception Occurred  ", e);
        return getResponse((BBDHttpBaseException) e);
    }

    @ExceptionHandler(value = {BBDHttpInvalidRequestException.class})
    public ResponseEntity<Result<Void>> defaultBBDTechnicalExceptionHandler(Exception e) {
        log.error("Exception Occurred  ", e);
        return getResponse((BBDHttpBaseException) e);
    }

    /**
     * Consumes a {@link BindException} and returns a
     * {@code ResponseEntity} indicating a {@link HttpStatus bad request}
     * and containing a {@link RestApiResult} with a List of messages indicating why the Http
     * message was not readable.
     *
     * Exception argument {@link BindException getField() Field} is an
     * {@link String } then this method will attempt to indicate in the response
     * list of messages which fields was in error.
     *
     * @param ex the exception
     * @return the ResponseEntity containing the RestApiResult, with a detail message
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> processValidationError(BindException ex) {
        List<ResultMessage> errorMessages = ex.getBindingResult().getAllErrors().stream().map(BBDErrorAdvice::populateInvalidRequestMessages).collect(Collectors.toList());
        return new ResponseEntity<>(new RestApiResult().withMessages(errorMessages), HttpStatus.BAD_REQUEST);
    }

    private static ResultMessage populateInvalidRequestMessages(ObjectError error) {
        String message = "Could not parse request";
        String fieldName = ((FieldError) error).getField();
        String fieldValue = ((FieldError) error).getRejectedValue().toString();
        if (!StringUtils.isEmpty(fieldName) && !StringUtils.isEmpty(fieldValue)) {
            message = "Could not parse value " + fieldValue + " for field " + fieldName;
        }
        return ResultMessage.builder().message(message).messageSeverity(MessageSeverity.CRITICAL).build();
    }

    /**
     * Consumes a {@link HttpMessageNotReadableException} and returns a
     * {@code ResponseEntity} indicating a {@link HttpStatus bad request}
     * and containing a {@link RestApiResult} with a message indicating why the Http
     * message was not readable.
     *
     * If the exception argument {@link Exception getCause() cause} is an
     * {@link InvalidFormatException} then this method will attempt to indicate in the response
     * message which field was in error.
     *
     * @param ex the exception
     * @return the ResponseEntity containing the RestApiResult, with a detail message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> processValidationError(HttpMessageNotReadableException ex) {
        return new ResponseEntity(new RestApiResult().withMessage(ResultMessage.builder()
                .message(buildNotReadableResponseMessage(ex))
                .messageSeverity(MessageSeverity.CRITICAL)
                .build()),
                HttpStatus.BAD_REQUEST);
    }

    private String buildNotReadableResponseMessage(HttpMessageNotReadableException e) {
        String message = "Could not parse request";
        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidEx = (InvalidFormatException) e.getCause();
            String path = invalidEx.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
            if (!StringUtils.isEmpty(invalidEx.getValue()) && !StringUtils.isEmpty(path))
                message = "Could not parse value " + invalidEx.getValue() + " for field " + path;
        }
        return message;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> processValidationError(MethodArgumentNotValidException ex) {
        log.error("Exception Occurred  ", ex);

        List<String> ListErrors;
        List<ResultMessage> resultMessages;
        ex.getBindingResult().getFieldErrors();
        ListErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        resultMessages = ListErrors.stream()
                .map(message -> new ResultMessage(message, MessageSeverity.ERROR))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new RestApiResult<Void>().withMessages(resultMessages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Void>> processValidationError(ConstraintViolationException ex) {
        log.error("Exception Occurred  ", ex);
        List<String> ListErrors;
        List<ResultMessage> resultMessages;

        if (Objects.nonNull(ex.getConstraintViolations())) {
            ListErrors = ex.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            resultMessages = ListErrors.stream()
                    .map(message -> new ResultMessage(message, MessageSeverity.ERROR))
                    .collect(Collectors.toList());
        }
        else {
            resultMessages = Collections.singletonList(new ResultMessage(ex.getMessage(), MessageSeverity.ERROR));
        }

        return new ResponseEntity<>(new RestApiResult<Void>().withMessages(resultMessages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BBDHttpRuntimeBusinessException.class})
    public ResponseEntity<Result<Void>> defaultBBDRuntimeBusinessExceptionErrorHandler(RuntimeException e) {
        if (log.isDebugEnabled())
            log.error("Exception Occurred  ", e);
        return getResponse((BBDHttpBaseRuntimeException) e);
    }

    @ExceptionHandler(value = {BBDHttpRuntimeInvalidRequestException.class})
    public ResponseEntity<Result<Void>> defaultBBDRuntimeTechnicalExceptionErrorHandler(RuntimeException e) {
        log.error("Exception Occurred  ", e);
        return getResponse((BBDHttpBaseRuntimeException) e);
    }

    @ExceptionHandler(value = {BBDHttpRuntimeServiceException.class})
    public ResponseEntity<Result<Void>> defaultBBDRuntimeServiceExceptionErrorHandler(RuntimeException e) {
        log.error("Exception Occurred  ", e);
        return getResponse((BBDHttpBaseRuntimeException) e);
    }

    @ExceptionHandler(value = {FeatureNotEnabledException.class})
    public ResponseEntity<Result<Void>> defaultFeatureNotEnabledExceptionHandler(FeatureNotEnabledException e) {
        log.error("Exception Occurred ", e);
        Result<Void> result = new RestApiResult<Void>()
                .withMessage("Feature Disabled : " + e.getFeature().getFeatureName(), MessageSeverity.WARNING);
        return new ResponseEntity<>(result, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(value = {JsonMappingException.class})
    public ResponseEntity<Result<Void>> defaultJsonMappingExceptionHandler(JsonMappingException e) {
        log.error("Exception Occurred  ", e);
        Result<Void> result = new RestApiResult<Void>()
                .withMessage("Invalid Request Payload", MessageSeverity.CRITICAL);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Result<Void>> defaultExceptionHandler(Exception e) {
        log.error("Exception Occurred  ", e);
        Result<Void> result = new RestApiResult<Void>()
                .withMessage(UNKNOWN_ERROR_MESSAGE, MessageSeverity.CRITICAL);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Result<Void>> defaultExceptionHandler(RuntimeException e) {
        log.error("Exception Occurred  ", e);
        if (e.getCause() != null && e.getCause() instanceof JsonParseException)
            return new ResponseEntity<>(
                    new RestApiResult<Void>()
                            .withMessage("Invalid Request Payload", MessageSeverity.CRITICAL),
                    HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(
                    new RestApiResult<Void>()
                            .withMessage(UNKNOWN_ERROR_MESSAGE, MessageSeverity.CRITICAL),
                    HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
