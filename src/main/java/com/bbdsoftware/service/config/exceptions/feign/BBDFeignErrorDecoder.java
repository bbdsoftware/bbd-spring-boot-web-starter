package com.bbdsoftware.service.config.exceptions.feign;

import com.bbdsoftware.service.config.exceptions.checked.*;
import com.bbdsoftware.service.response.*;
import com.fasterxml.jackson.databind.*;
import feign.*;
import feign.codec.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.*;
import org.springframework.http.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import static feign.FeignException.*;

public class BBDFeignErrorDecoder implements feign.codec.ErrorDecoder {

    private static final String DECODE_FAILED_MESSAGE = "Unable to decode result from response:";
    private final Map<String, Class<?>> decoderMap = new HashMap<>();

    @Autowired
    Decoder decoder;

    @Override
    public final Exception decode(
            String methodKey,
            Response response) {


        return Stream.of(
                    DecodeToBBDBBDHttpAuthException(methodKey, response),
                    DecodeToBBDHttpAuthExceptionException(methodKey, response),
                    DecodeToBBDHttpTechnicalException(methodKey, response),
                    DecodeToBBDHttpBusinessException(methodKey, response),
                    DecodeToBBDHttpServiceException(methodKey, response))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst().orElse(errorStatus(methodKey, response));
    }

    public Optional<Exception> DecodeToBBDHttpAuthExceptionException(String methodKey, Response response) {
        if (response.status() == 401) {

            Result<Void> result = getResultFromResponse(response, methodKey);
            return Optional.of(new BBDHttpAuthException(
                    result.getMessages()
            ));
        }

        return Optional.empty();
    }


    public Optional<Exception> DecodeToBBDBBDHttpAuthException(String methodKey, Response response) {
        if (response.status() == 401) {

            Result<Void> result = getResultFromResponse(response, methodKey);

            return Optional.of(new BBDHttpAuthException(
                    result.getMessages()

            ));
        }

        return Optional.empty();
    }

    public Optional<Exception> DecodeToBBDHttpTechnicalException(String methodKey, Response response) {
        if (response.status() == 400) {

            Result<Void> result = getResultFromResponse(response, methodKey);

            return Optional.of(new BBDHttpInvalidRequestException(result.getMessages()));
        }

        return Optional.empty();
    }

    public Optional<Exception> DecodeToBBDHttpBusinessException(String methodKey, Response response) {

        if (response.status() > 400 && response.status() <= 499) {

            Result<Void> result = getResultFromResponse(response, methodKey);
            return Optional.of(new BBDHttpBusinessException(
                    result.getMessages(),
                    HttpStatus.valueOf(response.status())) {
            });
        }

        return Optional.empty();

    }

    public Optional<Exception> DecodeToBBDHttpServiceException(String methodKey, Response response) {

        if (response.status() >= 500 && response.status() <= 599) {

            Result<Void> result = getResultFromResponse(response, methodKey);

            return Optional.of(new BBDHttpServiceException(

                    result.getMessages()
            ));

        }
        return Optional.empty();

    }

    private Result<Void> getResultFromResponse(Response response, String methodKey) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            if (response.body() != null) {

                Type overrideType = new ParameterizedTypeReference<Result<Void>>() {
                }.getType();
                return (RestApiResult<Void>) decoder.decode(response, overrideType);
            }

        } catch (Exception e) {
            return new RestApiResult<Void>()
                    .withMessages(List.of(ResultMessage.builder()
                            .message(methodKey + " " + e.getMessage())
                            .messageSeverity(MessageSeverity.ERROR)
                            .build()));

        }

        return new RestApiResult<>();
    }


}
