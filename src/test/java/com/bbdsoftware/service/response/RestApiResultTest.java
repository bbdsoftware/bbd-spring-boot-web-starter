package com.bbdsoftware.service.response;

import org.junit.*;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class RestApiResultTest {

    private RestApiResult<Object> result;

    @Before
    public void before() {
        result = new RestApiResult<>();
    }

    @Test
    public void withResult() {
        // given
        Object payload = new Object();

        // when
        result.withResult(payload);

        // then
        assertThat(result.getResult(), is(payload));
    }

    @Test
    public void withBusinessCode() {
        // given
        String businessCode = UUID.randomUUID().toString();

        // when
        result.withBusinessCode(businessCode);

        // then
        assertThat(result.getBusinessCode(), is(businessCode));;
    }

    @Test
    public void withMessages() {
        // given
        List<ResultMessage> messages = List.of(
            new ResultMessage("message-1", MessageSeverity.INFO),
            new ResultMessage("message-2", MessageSeverity.WARNING)
        );

        // when
        result.withMessages(messages);

        // then
        assertThat(result.getMessages(), is(messages));
    }

    @Test
    public void withMessage() {
        // given
        ResultMessage message = new ResultMessage("message-1", MessageSeverity.INFO);

        // when
        result.withMessage(message);

        // then
        assertThat(result.getMessages(), contains(message));
    }

    @Test
    public void withMessage2() {
        // given
        String message = "message-1";
        MessageSeverity severity = MessageSeverity.INFO;

        // when
        result.withMessage(message, severity);

        // then
        assertThat(result.getMessages(), contains(new ResultMessage(message, severity)));
    }

    @Test
    public void withSuccessMessage() {
        // given
        String message = UUID.randomUUID().toString();

        // when
        result.withSuccessMessage(message);

        // then
        assertThat(result.getMessages(), contains(new ResultMessage(message, MessageSeverity.INFO)));
    }

    @Test
    public void withDefaultSuccessMessage() {
        // when
        result.withDefaultSuccessMessage();

        // then
        assertThat(result.getMessages(), contains(new ResultMessage("Successful", MessageSeverity.INFO)));
    }

    @Test
    public void addMessages() {
        // given
        ResultMessage message1 = new ResultMessage("message-1", MessageSeverity.INFO);
        ResultMessage message2 = new ResultMessage("message-2", MessageSeverity.WARNING);

        // when
        result.addMessages(List.of(message1, message2));

        // then
        assertThat(result.getMessages(), contains(message1, message2));
    }

    @Test
    public void addMessage() {
        // given
        ResultMessage message1 = new ResultMessage("message-1", MessageSeverity.INFO);
        ResultMessage message2 = new ResultMessage("message-2", MessageSeverity.WARNING);

        // when
        result.addMessage(message1)
              .addMessage(message2);

        // then
        assertThat(result.getMessages(), contains(message1, message2));
    }

    @Test
    public void AddMessage2() {
        // given
        String message = "message-1";
        MessageSeverity severity = MessageSeverity.INFO;

        // when
        result.addMessage(message, severity);

        // then
        assertThat(result.getMessages(), contains(new ResultMessage(message, severity)));
    }

    @Test
    public void addSuccessMessage() {
        // given
        String message = "message-1";

        // when
        result.addSuccessMessage(message);

        // then
        assertThat(result.getMessages(), contains(new ResultMessage(message, MessageSeverity.INFO)));
    }
}