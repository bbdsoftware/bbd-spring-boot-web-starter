package com.bbdsoftware.service.config.exceptions;

import com.bbdsoftware.service.config.exceptions.checked.*;
import com.bbdsoftware.service.config.exceptions.runtime.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.flips.exception.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.mock.web.*;
import org.springframework.stereotype.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.*;
import org.springframework.web.servlet.config.annotation.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableWebMvc
@WebAppConfiguration
@ContextConfiguration(classes = {
        BBDErrorAdviceTest.TestController.class,
        BBDErrorAdvice.class
})
public class BBDErrorAdviceTest {

    private MockMvc mockMvc;
    private MockHttpServletRequestBuilder request;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TestController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Controller
    @RequestMapping("/test")
    public static class TestController {
        Callable<Void> handler = () -> null;

        @SuppressWarnings("unused")
        @GetMapping
        public @ResponseBody String handle() throws Exception {
            handler.call();
            return null;
        }
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        request = MockMvcRequestBuilders.get("/test");
    }

    /**
     * Match a response which is expected to be an error with a single message.
     */
    private ResultMatcher errorResult(int httpStatus, String message, String messageSeverity) {
        // Map.of would be nice, but those null values....
        Map<String,String> map = new HashMap<>();
        map.put("message", message);
        map.put("messageSeverity", messageSeverity);

        return errorResult(httpStatus, Collections.singletonList(map));
    }

    /**
     * Match a response which is expected to contain any number of messages.
     */
    private ResultMatcher errorResult(int httpStatus, List<Map<String,String>> messages) {
        return result -> {
            MockHttpServletResponse response = result.getResponse();

            assertNotNull(response.getContentType());
            assertTrue(MediaType.parseMediaType(response.getContentType()).isCompatibleWith(MediaType.APPLICATION_JSON));
            assertEquals(httpStatus, response.getStatus());

            JsonNode json = objectMapper.readTree(response.getContentAsString());

            assertEquals(messages.size(), json.at("/messages").size());
            for (int i = 0; i < messages.size(); i++) {
                Map<String,String> message = messages.get(i);
                assertEquals(message.get("message"), json.at("/messages/" + i + "/message").textValue());
                assertEquals(message.get("messageSeverity"), json.at("/messages/" + i + "/messageSeverity").textValue());
            }
        };
    }

    @Test
    public void testJsonMappingException() throws Exception {
        controller.handler = () -> { throw new JsonMappingException(null, "error"); };

        mockMvc.perform(request)
                .andExpect(errorResult(400, "Invalid Request Payload", "CRITICAL"));
    }


    @Test
    public void testBBDHttpBusinessException() throws Exception {
        controller.handler = () -> {
            throw new BBDHttpBusinessException("BBDHttpBusinessException", HttpStatus.INTERNAL_SERVER_ERROR);
        };

        mockMvc.perform(request)
                .andExpect(errorResult(500, "BBDHttpBusinessException", "ERROR"));
    }

    @Test
    public void testBBDHttpServiceException() throws Exception {
        controller.handler = () -> {
            throw new BBDHttpServiceException("BBDHttpServiceException");
        };

        mockMvc.perform(request)
                .andExpect(errorResult(500, "BBDHttpServiceException", "ERROR"));
    }

    @Test
    public void testBBDHttpInvalidRequestException() throws Exception {
        controller.handler = () -> {
            throw new BBDHttpInvalidRequestException("BBDHttpInvalidRequestException");
        };

        mockMvc.perform(request)
                .andExpect(errorResult(400, "BBDHttpInvalidRequestException", "ERROR"));
    }

    @Test
    public void testMethodArgumentNotValidException_noParameter() throws Exception {
        class ValidationClass {
            @SuppressWarnings({"WeakerAccess", "unused"})
            public void method(int a, int b, int c) {}
        }

        final Method method = ValidationClass.class.getMethod("method", int.class, int.class, int.class);
        final MethodParameter parameter = new MethodParameter(method, -1);
        final BindingResult binding = new BindException(this, "BindingResult");
        controller.handler = () -> {
            throw new MethodArgumentNotValidException(parameter, binding);
        };

        mockMvc.perform(request)
                .andExpect(errorResult(400, Collections.emptyList()));
    }

    @Test
    public void testMethodArgumentNotValidException_singleError() throws Exception {
        @SuppressWarnings("unused")
        class ValidationClass {
            private int a;
            public void setA(int a) { this.a = a; }
            public int getA() { return a; }
            @SuppressWarnings("WeakerAccess")
            public void method(int a, int b, int c) {}
        }
        ValidationClass validation = new ValidationClass();

        final Method method = ValidationClass.class.getMethod("method", int.class, int.class, int.class);
        final MethodParameter parameter = new MethodParameter(method, 0);
        final BindingResult binding = new BindException(validation, "BindingResult");
        binding.rejectValue("a", "ignored", "binding-message");

        controller.handler = () -> {
            throw new MethodArgumentNotValidException(parameter, binding);
        };

        mockMvc.perform(request)
                .andExpect(errorResult(400, "binding-message", "ERROR"));
    }

    @Test
    public void testMethodArgumentNotValidException_multipleError() throws Exception {
        @SuppressWarnings("unused")
        class ValidationClass {
            private int a;
            public void setA(int a) {}
            public int getA() { return 0; }
            @SuppressWarnings("WeakerAccess")
            public void method(int a, int b, int c) {}
        }
        ValidationClass validation = new ValidationClass();

        final Method method = ValidationClass.class.getMethod("method", int.class, int.class, int.class);
        final MethodParameter parameter = new MethodParameter(method, 0);
        final BindingResult binding = new BindException(validation, "BindingResult");
        binding.rejectValue("a", "ignored", "binding-message");
        binding.rejectValue("a", "ignored", "another-binding-message");

        controller.handler = () -> {
            throw new MethodArgumentNotValidException(parameter, binding);
        };

        mockMvc.perform(request)
                .andExpect(errorResult(400, List.of(
                        Map.of("message", "binding-message", "messageSeverity", "ERROR"),
                        Map.of("message", "another-binding-message", "messageSeverity", "ERROR")
                )));
    }

    @Test
    @Ignore
    public void testConstraintViolationException() {
    }

    @Test
    public void testBBDHttpRuntimeBusinessException() throws Exception {
        controller.handler = () -> {
            throw new BBDHttpRuntimeBusinessException("BBDHttpRuntimeBusinessException", HttpStatus.INTERNAL_SERVER_ERROR);
        };

        mockMvc.perform(request)
                .andExpect(errorResult(500, "BBDHttpRuntimeBusinessException", "ERROR"));
    }

    @Test
    public void testBBDHttpRuntimeInvalidRequestException() throws Exception {
        controller.handler = () -> {
            throw new BBDHttpRuntimeInvalidRequestException("BBDHttpRuntimeInvalidRequestException");
        };

        mockMvc.perform(request)
                .andExpect(errorResult(400, "BBDHttpRuntimeInvalidRequestException", "ERROR"));
    }

    @Test
    public void testBBDHttpRuntimeServiceException() throws Exception {
        controller.handler = () -> {
            throw new BBDHttpRuntimeServiceException("BBDHttpRuntimeServiceException");
        };

        mockMvc.perform(request)
                .andExpect(errorResult(500, "BBDHttpRuntimeServiceException", "ERROR"));
    }

    @Test
    public void testFeatureNotEnabledException() throws Exception {
        class FeatureClass {
            @SuppressWarnings("WeakerAccess")
            public void method() {}
        }
        final Method method = FeatureClass.class.getMethod("method");

        controller.handler = () -> {
            throw new FeatureNotEnabledException("NotEnabled", method);
        };

        mockMvc.perform(request)
                .andExpect(errorResult(501, "method", "WARNING"));
    }

    @Test
    public void testException() throws Exception {
        controller.handler = () -> {
            throw new Exception("Exception");
        };

        mockMvc.perform(request)
                .andExpect(errorResult(500, "Unknown Error", "CRITICAL"));
    }

    @Test
    public void testRuntimeException() throws Exception {
        controller.handler = () -> {
            throw new RuntimeException("RuntimeException");
        };

        mockMvc.perform(request)
                .andExpect(errorResult(500, "Unknown Error", "CRITICAL"));
    }

    @Test
    public void testRuntimeException_JsonParseException() throws Exception {
        controller.handler = () -> {
            throw new RuntimeException("RuntimeException", new JsonParseException(null, ""));
        };

        mockMvc.perform(request)
                .andExpect(errorResult(400, "Invalid Request Payload", "CRITICAL"));
    }
}