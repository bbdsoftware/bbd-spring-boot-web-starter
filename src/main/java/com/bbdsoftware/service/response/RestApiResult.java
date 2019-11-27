package com.bbdsoftware.service.response;

import lombok.*;

import java.util.*;

@Data
public class RestApiResult<T> implements Result<T> {
    /**
     * The default successful {@link ResultMessage#getMessage() message} of {@literal Successful}.
     */
    private static final String DEFAULT_SUCCESS_MESSAGE = "Successful";

    private List<ResultMessage> messages = new ArrayList<>();
    private T                   result;
    private String              businessCode;

    public RestApiResult() {
        /*Basic constructor*/
    }

    /**
     * Set the {@link #getResult() result} and return this instance.
     *
     * @param result the result to set
     * @return this instance
     */
    public RestApiResult<T> withResult(final T result) {
        this.result = result;
        return this;
    }

    /**
     * Set the {@link #getBusinessCode() business code} and return this instance.
     *
     * @param businessCode the business code to set
     * @return this instance
     */
    public RestApiResult<T> withBusinessCode(final String businessCode) {
        this.businessCode = businessCode;
        return this;
    }

    /**
     * Set the {@link #getMessages() messages} and return this instance.
     *
     * @param messages the messages to set
     * @return this instance
     */
    public RestApiResult<T> withMessages(final List<ResultMessage> messages) {
        this.messages = messages;
        return this;
    }

    /**
     * Set the {@link #getMessages() messages} to a list containing the single message provided,
     * and return this instance.
     *
     * @param message the message to set
     * @return this instance
     */
    public RestApiResult<T> withMessage(final ResultMessage message) {
        return this.withMessages(List.of(message));
    }

    /**
     * Set the {@link #getMessages() messages} to a list containing a single message constructed from the provided
     * arguments, and return this instance.
     *
     * @param message the {@link ResultMessage#getMessage() message}
     * @param severity the {@link ResultMessage#getMessageSeverity() message severity}
     * @return this instance
     */
    public RestApiResult<T> withMessage(final String message, final MessageSeverity severity) {
        return this.withMessage(new ResultMessage(message, severity));
    }

    /**
     * Set the {@link #getMessages() messages} to a list containing a single message of
     * {@link MessageSeverity#INFO INFO} severity with the provided message, and return this instance.
     *
     * @param message the {@link ResultMessage#getMessage() message}
     * @return this instance
     */
    public RestApiResult<T> withSuccessMessage(final String message) {
        return withMessage(message, MessageSeverity.INFO);
    }

    /**
     * Set the {@link #getMessages() messages} to a list containing a single message with
     * {@link MessageSeverity#INFO INFO} severity, a default message of {@link #DEFAULT_SUCCESS_MESSAGE},
     * and return this instance.
     *
     * @return this instance
     */
    public RestApiResult<T> withDefaultSuccessMessage() {
        return this.withMessage(DEFAULT_SUCCESS_MESSAGE, MessageSeverity.INFO);
    }

    /**
     * Add the provided messages to the {@link #getMessages() messages} list, and return this instance.
     *
     * @param messages the messages to add
     * @return this instance
     */
    public RestApiResult<T> addMessages(final Collection<ResultMessage> messages) {
        this.messages.addAll(messages);
        return this;
    }

    /**
     * Add the provided message to the {@link #getMessages() messages} list, and return this instance.
     *
     * @param message the message to add
     * @return this instance
     */
    public RestApiResult<T> addMessage(final ResultMessage message) {
        return this.addMessages(Collections.singleton(message));
    }

    /**
     * Add a message to the {@link #getMessages() messages} list constructed from the provided arguments, and return
     * this instance.
     *
     * @param message the {@link ResultMessage#getMessage() message}
     * @param severity the {@link ResultMessage#getMessageSeverity() message severity}
     * @return this instance
     */
    public RestApiResult<T> addMessage(final String message, final MessageSeverity severity) {
        return this.addMessage(new ResultMessage(message, severity));
    }

    /**
     * Add a message to the {@link #getMessages() messages} list of {@link MessageSeverity#INFO INFO}
     * severity with the provided message, and return this instance.
     *
     * @param message the {@link ResultMessage#getMessage() message}
     * @return this instance
     */
    public RestApiResult<T> addSuccessMessage(final String message) {
        return this.addMessage(message, MessageSeverity.INFO);
    }
}



